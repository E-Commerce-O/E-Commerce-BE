package org.example.cdweb_be.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.cdweb_be.component.MessageProvider;
import org.example.cdweb_be.dto.request.AddressRequest;
import org.example.cdweb_be.dto.request.ApplyVoucherRequest;
import org.example.cdweb_be.dto.request.OrderCreateByAddressRequest;
import org.example.cdweb_be.dto.request.OrderCreateRequest;
import org.example.cdweb_be.dto.response.*;
import org.example.cdweb_be.entity.*;
import org.example.cdweb_be.enums.OrderStatus;
import org.example.cdweb_be.enums.PaymentMethodEnum;
import org.example.cdweb_be.enums.VoucherType;
import org.example.cdweb_be.exception.AppException;
import org.example.cdweb_be.exception.ErrorCode;
import org.example.cdweb_be.respository.*;
import org.example.cdweb_be.utils.responseUtilsAPI.DeliveryMethodUtil;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
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
    PaymentMethodRepository paymentMethodRepository;
    MessageProvider messageProvider;
    @PreAuthorize("isAuthenticated()")
    public OrderResponse add(String token, OrderCreateRequest request) {
        long userId = authenticationService.getUserId(token);
        User user = userRepository.findById(userId).get();
        Cart cart = cartService.getByUser(userId);
        Set<CartItem> cartItems = new HashSet<>();
        for(long cartItemId: request.getCartItemIds()){
            CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(() ->
                    new AppException(messageProvider,ErrorCode.CART_ITEM_NOT_EXISTS));
            if(cartItem.getCart().getId() != cart.getId()) throw new AppException(messageProvider,ErrorCode.CART_ITEM_UNAUTH);
            cartItems.add(cartItem);
        }
        if(cartItems.size() ==0) throw new AppException(messageProvider,ErrorCode.CART_ITEM_REQUIRED);
        if(cartItems.size() != request.getCartItemIds().size()) throw new AppException(messageProvider,ErrorCode.CART_ITEM_DUPLICATED);
        Address address = addressRepository.findById(request.getAddressId()).orElseThrow(() ->
                new AppException(messageProvider,ErrorCode.ADDRESS_NOT_EXISTS));
        PaymentMethod paymentMethod = paymentMethodRepository.findById(request.getPaymentMethodId()).orElseThrow(() ->
                new AppException(messageProvider,ErrorCode.PAYMENT_METHOD_INVALID));
        if (address.getUser().getId() != userId) throw new AppException(messageProvider,ErrorCode.ADDRESS_UNAUTHORIZED);
        List<DeliveryMethodUtil> deliveryMethods = addressService.getInfoShip(address.getId());
        if (!deliveryMethods.contains(request.getDeliveryMethod()))
            throw new AppException(messageProvider,ErrorCode.DELIVERY_METHOD_INVALID);
        Voucher shipVC = null;
        ApplyVoucherRequest applyVoucherRequest = ApplyVoucherRequest.builder()
                .cartItemIds(request.getCartItemIds())
                .shippingCost(Double.parseDouble(request.getDeliveryMethod().getGia_cuoc()))
                .build();
        if(request.getFreeshipVcId()>0){
            shipVC = voucherRepository.findById(request.getFreeshipVcId()).orElseThrow( () ->
                    new AppException(messageProvider,ErrorCode.VOUCHER_NOT_EXISTS));
            if(shipVC.getType() == VoucherType.PRODUCT.getType()) throw new AppException(messageProvider,ErrorCode.VOUCHER_SHIP_INVALID);
            applyVoucherRequest.setVoucherId(shipVC.getId());
        }

        double shipDecrease = (shipVC == null)?0:voucherService.applyVouhcer(applyVoucherRequest);
        Voucher productVC = null;
        if(request.getProductVcId() >0){
            productVC = voucherRepository.findById(request.getProductVcId()).orElseThrow(() ->
                    new AppException(messageProvider,ErrorCode.VOUCHER_NOT_EXISTS));
            if(productVC.getType() == VoucherType.FREESHIP.getType()) throw new AppException(messageProvider,ErrorCode.VOUCHER_SHOP_INVALID);
            applyVoucherRequest.setVoucherId(productVC.getId());

        }
        double productDecrease = (productVC == null)?0:voucherService.applyVouhcer(applyVoucherRequest);
        int orderStatus = OrderStatus.ST_DAT_HANG_TC;
        if(paymentMethod.getId()!= PaymentMethodEnum.CASH_ON_DELIVERY) orderStatus = OrderStatus.ST_CHO_THANH_TOAN;
        Order order = Order.builder()
                .user(user)
                .status(orderStatus)
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .build();
        order = orderRepository.save(order);
        List<OrderItem> orderItems = new ArrayList<>();
        for(CartItem cartItem : cartItems){
            int remainingQuantity = productService.getRemainingQuantity(cartItem.getProduct(), cartItem.getSize(), cartItem.getColor());
            if(remainingQuantity < cartItem.getQuantity()) throw new AppException(messageProvider,ErrorCode.PRODUCT_QUANTITY_NOT_ENOUGH);
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
                .paymentMethod(paymentMethod)
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
    @PreAuthorize("isAuthenticated()")
    public  OrderResponse addByAddress(String token, OrderCreateByAddressRequest request) {
        long userId = authenticationService.getUserId(token);
        Address address = addressService.getAddress(userId, request.getAddress());
        OrderCreateRequest orderCreateRequest = OrderCreateRequest.builder()
                .addressId(address.getId())
                .freeshipVcId(request.getFreeshipVcId())
                .productVcId(request.getProductVcId())
                .paymentMethodId(request.getPaymentMethodId())
                .cartItemIds(request.getCartItemIds())
                .deliveryMethod(request.getDeliveryMethod()).build();
        return add(token, orderCreateRequest);

    }
    @PreAuthorize("isAuthenticated()")
    public PagingResponse getMyOrders(String token, int page, int size){
        long userId = authenticationService.getUserId(token);
        List<OrderResponse> orderResponses = orderRepository.findByUserId(userId, PageRequest.of(page-1, size)).stream()
                .map(order -> convertToOrderResponse(order)).collect(Collectors.toList());
        return PagingResponse.<OrderResponse>builder()
                .page(page)
                .size(size)
                .totalItem(orderRepository.countByUserId(userId))
                .data(orderResponses)
                .build();
    }
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public PagingResponse getAll(int page, int size){
        List<OrderResponse> orderResponses = orderRepository.findAll(PageRequest.of(page-1, size)).stream()
                .map(order -> convertToOrderResponse(order)).collect(Collectors.toList());
         return PagingResponse.<OrderResponse>builder()
                .page(page)
                .size(size)
                .totalItem(orderRepository.count())
                .data(orderResponses)
                .build();
    }
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public PagingResponse getAllByStatus(int status, int page, int size){
        List<OrderResponse> orderResponses = orderRepository.findByStatus(status, PageRequest.of(page-1, size)).stream()
                .map(order -> convertToOrderResponse(order)).collect(Collectors.toList());
        return PagingResponse.<OrderResponse>builder()
                .page(page)
                .size(size)
                .totalItem(orderRepository.countByStatus(status))
                .data(orderResponses)
                .build();
    }
    @PreAuthorize("isAuthenticated()")
    public String cancelOrder(String token, long orderId){
        long userId = authenticationService.getUserId(token);
        Order order = orderRepository.findById(orderId).orElseThrow(() ->
                new AppException(messageProvider,ErrorCode.ORDER_NOT_EXISTS));
        if(order.getUser().getId() != userId) throw new AppException(messageProvider,ErrorCode.ORDER_UPDATE_UNAUTH);
        if(order.getStatus() > OrderStatus.ST_DANG_CBI_HANG){
            throw new AppException(messageProvider,ErrorCode.ORDER_CANT_CANCEL);
        }
        updateStatus(order, OrderStatus.ST_DA_HUY);
        return messageProvider.getMessage("order.cancel");
    }

    @PreAuthorize("isAuthenticated()")
    public String returnOrder(String token, long orderId){
        long userId = authenticationService.getUserId(token);
        Order order = orderRepository.findById(orderId).orElseThrow(() ->
                new AppException(messageProvider,ErrorCode.ORDER_NOT_EXISTS));
        if(order.getUser().getId() != userId) throw new AppException(messageProvider,ErrorCode.ORDER_UPDATE_UNAUTH);
        if(order.getStatus() != OrderStatus.ST_GIAO_THANH_CONG){
            throw new AppException(messageProvider,ErrorCode.ORDER_CANT_CANCEL);
        }
        updateStatus(order, OrderStatus.ST_YC_TRA_HANG);
        return messageProvider.getMessage("order.return");
    }
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public String updateStatus(long orderId, int status){
        Order order = orderRepository.findById(orderId).orElseThrow(() ->
                new AppException(messageProvider,ErrorCode.ORDER_NOT_EXISTS));
        if(OrderStatus.getByStatusCode(status) == null) throw new AppException(messageProvider,ErrorCode.ORDER_STATUS_INVALID);
        if(order.getStatus() == status) throw new AppException(messageProvider,ErrorCode.ORDER_CANT_UPDATE);
        switch (order.getStatus()){
            case OrderStatus.ST_CHO_THANH_TOAN:{
                if(status == OrderStatus.ST_DAT_HANG_TC || status == OrderStatus.ST_DA_HUY){
                    updateStatus(order, status);
                }else{
                    break;
                }
                return messageProvider.getMessage("order.update.status");
            }
            case OrderStatus.ST_DAT_HANG_TC:{
                if(status == OrderStatus.ST_DANG_CBI_HANG || status == OrderStatus.ST_DA_HUY){
                    updateStatus(order, status);
                }else{
                    break;
                }
                return messageProvider.getMessage("order.update.status");
            }
            case OrderStatus.ST_DANG_CBI_HANG:{
                if(status == OrderStatus.ST_DVVC_LAY_HANG || status == OrderStatus.ST_DA_HUY){
                    updateStatus(order, status);
                }else{
                    break;
                }
                return messageProvider.getMessage("order.update.status");
            }
            case OrderStatus.ST_DVVC_LAY_HANG:{
                if(status == OrderStatus.ST_DANG_VAN_CHUYEN){
                    updateStatus(order, status);
                }else{
                    break;
                }
                return messageProvider.getMessage("order.update.status");
            }
            case OrderStatus.ST_DANG_VAN_CHUYEN:{
                if(status == OrderStatus.ST_DANG_GIAO){
                    updateStatus(order, status);
                }else{
                    break;
                }
                return messageProvider.getMessage("order.update.status");
            }
            case OrderStatus.ST_DANG_GIAO:{
                if(status == OrderStatus.ST_GIAO_THANH_CONG){
                    updateStatus(order, status);
                }else{
                    break;
                }
                return messageProvider.getMessage("order.update.status");
            }
            case OrderStatus.ST_YC_TRA_HANG:{
                if(status == OrderStatus.ST_DA_TRA_HANG){
                    updateStatus(order, status);
                }else{
                    break;
                }
                return messageProvider.getMessage("order.update.status");
            }

        }
       throw new AppException(messageProvider,ErrorCode.ORDER_CANT_UPDATE);
    }
    @PreAuthorize("isAuthenticated()")
    public OrderResponse getById(long orderId){
        Order order = orderRepository.findById(orderId).orElseThrow(() ->
                new AppException(messageProvider,ErrorCode.ORDER_NOT_EXISTS));
        return convertToOrderResponse(order);
    }
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
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
                .paymentMethod(orderDetail.getPaymentMethod())
                .productDecrease(orderDetail.getProductDecrease())
                .orderItems(itemResponses)
                .shipDecrease(orderDetail.getShipDecrease())
                .status(new OrderStatusResponse(OrderStatus.getByStatusCode(order.getStatus())))
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
        totalPrice -= orderResponse.getProductDecrease();
        totalPrice -= orderResponse.getShipDecrease();
        double totalPayment = totalPrice;
        if(orderDetail.getPaymentMethod().getId() != PaymentMethodEnum.CASH_ON_DELIVERY && order.getStatus() != OrderStatus.ST_CHO_THANH_TOAN)
            totalPayment =0;
        orderResponse.setTotalPrice(totalPrice);
        orderResponse.setTotalPayment(totalPayment);
        return orderResponse;
    }
}
