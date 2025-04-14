package org.example.cdweb_be.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.cdweb_be.dto.request.CartItemRequest;
import org.example.cdweb_be.dto.response.CartItemResponse;
import org.example.cdweb_be.dto.response.CartResponse;
import org.example.cdweb_be.entity.*;
import org.example.cdweb_be.exception.AppException;
import org.example.cdweb_be.exception.ErrorCode;
import org.example.cdweb_be.mapper.CartMapper;
import org.example.cdweb_be.mapper.ProductMapper;
import org.example.cdweb_be.respository.*;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
@Slf4j
public class CartService {
    private final ProductSizeRepository productSizeRepository;
    private final ProductColorRepository productColorRepository;
    private final ProductMapper productMapper;
    private final ProductImageRepository productImageRepository;
    CartRepository cartRepository;
    AuthenticationService authenticationService;
    UserRepository userRepository;
    CartMapper cartMapper;
    ProductRepository productRepository;
    CartItemRepository cartItemRepository;
    ProductService productService;

    public CartResponse getMyCart(String token) {
        long userId = authenticationService.getUserId(token);
        User user = userRepository.findById(userId).get();
        Optional<Cart> cartOptional = cartRepository.findByUserId(userId);
        if (cartOptional.isEmpty()) {
            Cart cart = Cart.builder()
                    .user(user)
                    .createdAt(new Timestamp(System.currentTimeMillis()))
                    .build();
            return cartMapper.toCartResponse(cartRepository.save(cart));
        } else {
            List<CartItem> cartItems = cartItemRepository.findByCartId(cartOptional.get().getId());
            List<CartItemResponse> cartItemResponses = new ArrayList<>();
            for(CartItem ci : cartItems){
                CartItemResponse cartItemResponse = new CartItemResponse(ci);
                cartItemResponse.setPrice(productService.getPrice(ci.getProduct(), ci.getSize(), ci.getColor()));
                cartItemResponse.setDiscount(productService.getDiscount(ci.getProduct(), ci.getSize(), ci.getColor()));
                cartItemResponse.setImages(productImageRepository.findByProductId(ci.getProduct().getId()).stream().map(productImage -> productImage.getImagePath()).collect(Collectors.toList()));
                cartItemResponses.add(cartItemResponse);

            }
            CartResponse cartResponse = cartMapper.toCartResponse(cartOptional.get());
            cartResponse.setCartItems(cartItemResponses);
            return cartResponse;
        }
    }

    public CartItemResponse addItem(String token, CartItemRequest request) {
        long userId = authenticationService.getUserId(token);
        User user = userRepository.findById(userId).get();
        Cart cart = cartRepository.findByUserId(userId).orElseGet(() -> Cart.builder()
                .user(user)
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .build());
        cart = cartRepository.save(cart);
        Product product = productRepository.findById(request.getProductId()).orElseThrow(() ->
                new AppException(ErrorCode.PRODUCT_NOT_EXISTS));
        Optional<ProductColor> color = productColorRepository.findByIdAndProductId(request.getProductColor(), request.getProductId());
        Optional<ProductSize> size = productSizeRepository.findByIdAndProductId(request.getProductSize(), request.getProductId());
        if (!productSizeRepository.findByProductId(request.getProductId()).isEmpty() && size.isEmpty()) {
            throw new AppException(ErrorCode.CARTITEM_INVAID_SIZE);
        }
        if (!productColorRepository.findByProductId(request.getProductId()).isEmpty() && color.isEmpty()) {
            throw new AppException(ErrorCode.CARTITEM_INVAID_COLOR);
        }
        Optional<CartItem> cartItemOptional = null;
        if (color.isPresent() && size.isPresent()) {
            cartItemOptional = cartItemRepository.findByCartAndProductAndColorAndSize(cart.getId(), request.getProductId(), request.getProductColor(), request.getProductSize());
        } else {
            if (color.isPresent() && size.isEmpty()) {
                cartItemOptional = cartItemRepository.findByCartAndProductAndColor(cart.getId(), request.getProductId(), request.getProductColor());
            } else if (color.isEmpty() && size.isPresent()) {
                cartItemOptional = cartItemRepository.findByCartAndProductAndSize(cart.getId(), request.getProductId(), request.getProductSize());
            } else {
                cartItemOptional = cartItemRepository.findByCartAndProduct(cart.getId(), request.getProductId());

            }
        }
        CartItem cartItem = null;
        if(cartItemOptional.isPresent()){
            cartItem = cartItemOptional.get();
            cartItem.setQuantity(cartItem.getQuantity() + request.getQuantity());
            cartItem.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        }else{
            cartItem = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .color(color.get())
                    .size(size.get())
                    .quantity(request.getQuantity())
                    .createdAt(new Timestamp(System.currentTimeMillis()))
                    .build();
        }
        cartItem = cartItemRepository.save(cartItem);
        CartItemResponse cartItemResponse = new CartItemResponse(cartItem);
        cartItemResponse.setPrice(productService.getPrice(cartItem.getProduct(), cartItem.getSize(), cartItem.getColor()));
        cartItemResponse.setDiscount(productService.getDiscount(cartItem.getProduct(), cartItem.getSize(), cartItem.getColor()));
        cartItemResponse.setImages(productImageRepository.findByProductId(cartItem.getProduct().getId()).stream().map(productImage -> productImage.getImagePath()).collect(Collectors.toList()));
        return cartItemResponse;


    }

}
