package org.example.cdweb_be.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.cdweb_be.dto.request.ApplyVoucherRequest;
import org.example.cdweb_be.dto.request.OrderCreateRequest;
import org.example.cdweb_be.dto.response.OrderItemResponse;
import org.example.cdweb_be.dto.response.OrderResponse;
import org.example.cdweb_be.dto.response.OrderStatusResponse;
import org.example.cdweb_be.dto.response.OrderUser;
import org.example.cdweb_be.entity.*;
import org.example.cdweb_be.enums.OrderStatus;
import org.example.cdweb_be.enums.VoucherType;
import org.example.cdweb_be.exception.AppException;
import org.example.cdweb_be.exception.ErrorCode;
import org.example.cdweb_be.respository.*;
import org.example.cdweb_be.utils.responseUtilsAPI.DeliveryMethodUtil;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderService {
    ProductImageRepository productImageRepository;
    CartItemRepository cartItemRepository;
    CartService cartService;
    DeliveryMethodRepository deliveryMethodRepository;
    UserRepository userRepository;
    VoucherRepository voucherRepository;
    OrderRepository orderRepository;
    OrderItemRepository orderItemRepository;
    OrderDetailRepository orderDetailRepository;
    AddressService addressService;
    AddressRepository addressRepository;
    VoucherService voucherService;
    ProductService productService;
    AuthenticationService authenticationService;

    public OrderResponse add(String token, OrderCreateRequest request) {
        long userId = authenticationService.getUserId(token);
        User user = userRepository.findById(userId).get();
        Cart cart = cartService.getByUser(userId);
        Set<CartItem> cartItems = new HashSet<>();
        for(long cartItemId: request.getCartItemIds()){
            CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(() ->
                    new AppException(ErrorCode.CART_ITEM_NOT_EXISTS));
            if(cartItem.getCart().getId() != cart.getId()) throw new AppException(ErrorCode.CART_ITEM_UNAUTH);
            cartItems.add(cartItem);
        }
        if(cartItems.size() ==0) throw new AppException(ErrorCode.CART_ITEM_REQUIRED);
        if(cartItems.size() != request.getCartItemIds().size()) throw new AppException(ErrorCode.CART_ITEM_DUPLICATED);
        Address address = addressRepository.findById(request.getAddressId()).orElseThrow(() ->
                new AppException(ErrorCode.ADDRESS_NOT_EXISTS));
        if (address.getUser().getId() != userId) throw new AppException(ErrorCode.ADDRESS_UNAUTHORIZED);
        List<DeliveryMethodUtil> deliveryMethods = addressService.getInfoShip(address.getId());
        if (!deliveryMethods.contains(request.getDeliveryMethod()))
            throw new AppException(ErrorCode.DELIVERY_METHOD_INVALID);
        Voucher shipVC = null;
        ApplyVoucherRequest applyVoucherRequest = ApplyVoucherRequest.builder()
                .cartItemIds(request.getCartItemIds())
                .shippingCost(Double.parseDouble(request.getDeliveryMethod().getGia_cuoc()))
                .build();
        if(request.getFreeshipVcId()>0){
            shipVC = voucherRepository.findById(request.getFreeshipVcId()).orElseThrow( () ->
                    new AppException(ErrorCode.VOUCHER_NOT_EXISTS));
            if(shipVC.getType() == VoucherType.PRODUCT.getType()) throw new AppException(ErrorCode.VOUCHER_SHIP_INVALID);
            applyVoucherRequest.setVoucherId(shipVC.getId());
        }

        double shipDecrease = (shipVC == null)?0:voucherService.applyVouhcer(applyVoucherRequest);
        Voucher productVC = null;
        if(request.getProductVcId() >0){
            productVC = voucherRepository.findById(request.getProductVcId()).orElseThrow(() ->
                    new AppException(ErrorCode.VOUCHER_NOT_EXISTS));
            if(productVC.getType() == VoucherType.FREESHIP.getType()) throw new AppException(ErrorCode.VOUCHER_SHOP_INVALID);
            applyVoucherRequest.setVoucherId(productVC.getId());

        }
        double productDecrease = (productVC == null)?0:voucherService.applyVouhcer(applyVoucherRequest);
        Order order = Order.builder()
                .user(user)
                .status(OrderStatus.ST_DAT_HANG_TC)
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .build();
        order = orderRepository.save(order);
        List<OrderItem> orderItems = new ArrayList<>();
        for(CartItem cartItem : cartItems){
            int remainingQuantity = productService.getRemainingQuantity(cartItem.getProduct(), cartItem.getSize(), cartItem.getColor());
            if(remainingQuantity < cartItem.getQuantity()) throw new AppException(ErrorCode.PRODUCT_QUANTITY_NOT_ENOUGH.setMessage(cartItem.getErrorCodeMessage()));
            OrderItem orderItem = new OrderItem(cartItem);
            orderItem.setOrder(order);
            orderItem.setDiscount(productService.getDiscount(cartItem.getProduct(), cartItem.getSize(), cartItem.getColor()));
            orderItem.setOriginalPrice(productService.getPrice(cartItem.getProduct(), cartItem.getSize(), cartItem.getColor()));
            orderItems.add(orderItem);
        }
        orderItems = orderItemRepository.saveAll(orderItems);
        cartItemRepository.deleteAll(cartItems);
        DeliveryMethodUtil methodUtil = request.getDeliveryMethod();
        DeliveryMethod deliveryMethod = DeliveryMethod.builder()
                .order(order)
                .ten_dichvu(methodUtil.getTen_dichvu())
                .thoi_gian(methodUtil.getThoi_gian())
                .gia_cuoc(Double.parseDouble(methodUtil.getGia_cuoc()))
                .build();
        deliveryMethod = deliveryMethodRepository.save(deliveryMethod);
        OrderDetail orderDetail = OrderDetail.builder()
                .order(order)
                .address(address)
                .productVoucher(productVC)
                .productDecrease(productDecrease)
                .shipVoucher(shipVC)
                .shipDecrease(shipDecrease)
                .build();
        orderDetail =orderDetailRepository.save(orderDetail);
        List<OrderItemResponse> itemResponses = new ArrayList<>();
        for(OrderItem orderItem: orderItems){
            OrderItemResponse itemResponse = new OrderItemResponse(orderItem);
            itemResponse.setProductImages(productImageRepository.findImagePathByProduct(orderItem.getProduct().getId()));
            itemResponses.add(itemResponse);
        }
        OrderResponse orderResponse = OrderResponse.builder()
                .orderId(order.getId())
                .receiverAddress(address)
                .orderUser(new OrderUser(user))
                .deliveryMethod(deliveryMethod)
                .productDecrease(productDecrease)
                .orderItems(itemResponses)
                .shipDecrease(shipDecrease)
                .status(new OrderStatusResponse(OrderStatus.getByStatusCode(order.getStatus())))
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();

        return orderResponse;
    }
    public List<OrderResponse> getMyOrders(String token){
        long userId = authenticationService.getUserId(token);
        List<OrderResponse> result = orderRepository.findByUserId(userId).stream()
                .map(order -> convertToOrderResponse(order)).collect(Collectors.toList());
        return result;
    }
    public List<OrderResponse> getAll(){
        List<OrderResponse> result = orderRepository.findAll().stream()
                .map(order -> convertToOrderResponse(order)).collect(Collectors.toList());
        return result;
    }
    public List<OrderResponse> getAllByStatus(int status){
        List<OrderResponse> result = orderRepository.findByStatus(status).stream()
                .map(order -> convertToOrderResponse(order)).collect(Collectors.toList());
        return result;
    }
    public String cancelOrder(String token, long orderId){
        long userId = authenticationService.getUserId(token);
        Order order = orderRepository.findById(orderId).orElseThrow(() ->
                new AppException(ErrorCode.ORDER_NOT_EXISTS));
        if(order.getUser().getId() != userId) throw new AppException(ErrorCode.ORDER_UPDATE_UNAUTH);
        if(order.getStatus() > OrderStatus.ST_DANG_CBI_HANG){
            throw new AppException(ErrorCode.ORDER_CANT_CANCEL.setMessage("Cannot cancel this order because this order is "+OrderStatus.getByStatusCode(order.getStatus()).getStatusName()));
        }
        updateStatus(order, OrderStatus.ST_DA_HUY);
        return "Cancel order: "+orderId+" successful";
    }

    public String returnOrder(String token, long orderId){
        long userId = authenticationService.getUserId(token);
        Order order = orderRepository.findById(orderId).orElseThrow(() ->
                new AppException(ErrorCode.ORDER_NOT_EXISTS));
        if(order.getUser().getId() != userId) throw new AppException(ErrorCode.ORDER_UPDATE_UNAUTH);
        if(order.getStatus() != OrderStatus.ST_GIAO_THANH_CONG){
            throw new AppException(ErrorCode.ORDER_CANT_CANCEL.setMessage("Cannot return this order because this order is "+OrderStatus.getByStatusCode(order.getStatus()).getStatusName()));
        }
        updateStatus(order, OrderStatus.ST_YC_TRA_HANG);
        return "Return order: "+orderId+" successful. The carrier will pick up the goods soon and the money will be refunded afterwards.";
    }
    public String updateStatus(long orderId, int status){
        Order order = orderRepository.findById(orderId).orElseThrow(() ->
                new AppException(ErrorCode.ORDER_NOT_EXISTS));
        if(OrderStatus.getByStatusCode(status) == null) throw new AppException(ErrorCode.ORDER_STATUS_INVALID);
        if(order.getStatus() == status) throw new AppException(ErrorCode.ORDER_CANT_UPDATE.setMessage("Cannot update because new status duplicates old status"));
        switch (order.getStatus()){
            case OrderStatus.ST_DAT_HANG_TC:{
                if(status == OrderStatus.ST_DANG_CBI_HANG || status == OrderStatus.ST_DA_HUY){
                    updateStatus(order, status);
                }else{
                    break;
                }
                return "Update status of order: "+orderId+" successful!";
            }
            case OrderStatus.ST_DANG_CBI_HANG:{
                if(status == OrderStatus.ST_DVVC_LAY_HANG || status == OrderStatus.ST_DA_HUY){
                    updateStatus(order, status);
                }else{
                    break;
                }
                return "Update status of order: "+orderId+" successful!";
            }
            case OrderStatus.ST_DVVC_LAY_HANG:{
                if(status == OrderStatus.ST_DANG_VAN_CHUYEN){
                    updateStatus(order, status);
                }else{
                    break;
                }
                return "Update status of order: "+orderId+" successful!";
            }
            case OrderStatus.ST_DANG_VAN_CHUYEN:{
                if(status == OrderStatus.ST_DANG_GIAO){
                    updateStatus(order, status);
                }else{
                    break;
                }
                return "Update status of order: "+orderId+" successful!";
            }
            case OrderStatus.ST_DANG_GIAO:{
                if(status == OrderStatus.ST_GIAO_THANH_CONG){
                    updateStatus(order, status);
                }else{
                    break;
                }
                return "Update status of order: "+orderId+" successful!";
            }
            case OrderStatus.ST_YC_TRA_HANG:{
                if(status == OrderStatus.ST_DA_TRA_HANG){
                    updateStatus(order, status);
                }else{
                    break;
                }
                return "Update status of order: "+orderId+" successful!";
            }

        }
        String errorMessage = "Cannot update status of order: "+orderId+" to '"+OrderStatus.getByStatusCode(status).getStatusName()+
                "' because current status is '"+OrderStatus.getByStatusCode(order.getStatus()).getStatusName()+"'";
       throw new AppException(ErrorCode.ORDER_CANT_UPDATE.setMessage(errorMessage));
    }
    public OrderResponse getById(long orderId){
        Order order = orderRepository.findById(orderId).orElseThrow(() ->
                new AppException(ErrorCode.ORDER_NOT_EXISTS));
        return convertToOrderResponse(order);
    }
    public void updateStatus(Order order, int status){
        order.setStatus(status);
        order.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        orderRepository.save(order);
    }

    public OrderResponse convertToOrderResponse(Order order){
        if(order == null) return null;
        OrderDetail orderDetail = orderDetailRepository.findByOrderId(order.getId()).get();
        User user = order.getUser();
        Address address = orderDetail.getAddress();
        DeliveryMethod deliveryMethod = deliveryMethodRepository.findByOrderId(order.getId()).get();
        double totalPrice =0;
        List<OrderItem> orderItems = orderItemRepository.findByOrderId(order.getId());
        List<OrderItemResponse> itemResponses = new ArrayList<>();
        for(OrderItem orderItem: orderItems){
            OrderItemResponse itemResponse = new OrderItemResponse(orderItem);
            itemResponse.setProductImages(productImageRepository.findImagePathByProduct(orderItem.getProduct().getId()));
            itemResponses.add(itemResponse);
            totalPrice += orderItem.getQuantity() * orderItem.getOriginalPrice() * (1 - (double)orderItem.getDiscount()/100);
        }
        totalPrice += deliveryMethod.getGia_cuoc();

        OrderResponse orderResponse = OrderResponse.builder()
                .orderId(order.getId())
                .receiverAddress(address)
                .orderUser(new OrderUser(user))
                .deliveryMethod(deliveryMethod)
                .productDecrease(orderDetail.getProductDecrease())
                .orderItems(itemResponses)
                .shipDecrease(orderDetail.getShipDecrease())
                .status(new OrderStatusResponse(OrderStatus.getByStatusCode(order.getStatus())))
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
        totalPrice -= orderResponse.getProductDecrease();

        totalPrice -= orderResponse.getShipDecrease();
        orderResponse.setTotalPrice(totalPrice);
        orderResponse.setTotalPayment(totalPrice);
        return orderResponse;
    }
}
