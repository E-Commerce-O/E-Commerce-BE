package org.example.cdweb_be.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.cdweb_be.dto.response.CartResponse;
import org.example.cdweb_be.entity.Cart;
import org.example.cdweb_be.entity.User;
import org.example.cdweb_be.mapper.CartMapper;
import org.example.cdweb_be.respository.CartRepository;
import org.example.cdweb_be.respository.UserRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Optional;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class CartService {
    CartRepository cartRepository;
    AuthenticationService authenticationService;
    UserRepository userRepository;
    CartMapper cartMapper;
    public CartResponse getMyCart(String token){
        long userId = authenticationService.getUserId(token);
        User user = userRepository.findById(userId).get();
        Optional<Cart> cartOptional = cartRepository.findByUserId(userId);
        if(cartOptional.isEmpty()){
            Cart cart = Cart.builder()
                    .user(user)
                    .cartItems(new HashSet<>())
                    .createdAt(new Timestamp(System.currentTimeMillis()))
                    .build();
            return cartMapper.toCartResponse(cartRepository.save(cart));
        }else{
            return cartMapper.toCartResponse(cartOptional.get());
        }
    }

}
