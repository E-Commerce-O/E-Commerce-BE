package org.example.cdweb_be.controller;

import org.example.cdweb_be.entity.Cart;
import org.example.cdweb_be.entity.CartItem;
import org.example.cdweb_be.entity.User;
import org.example.cdweb_be.entity.WishlistItem;
import org.example.cdweb_be.respository.CartRepository;
import org.example.cdweb_be.respository.UserRepository;
import org.example.cdweb_be.respository.WishlistItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    WishlistItemRepository wishlistItemRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CartRepository cartRepository;
@PostMapping
    public WishlistItem add(){
    WishlistItem wishlistItem = new WishlistItem();
    User user = User.builder()
            .userName("admin")
            .fullName("Át Văn Min").build();
    user = userRepository.save(user);
    Cart cart = Cart.builder()
            .user(user).build();
    cart = cartRepository.save(cart);
    CartItem cartItem1 = CartItem.builder()
            .quantity(10)
            .build();
    CartItem cartItem2 = CartItem.builder()
            .quantity(20)
            .build();
    cart.setCartItems(new HashSet<>());
    cart.getCartItems().add(cartItem1);
    cart.getCartItems().add(cartItem2);
    cartRepository.save(cart);
    return wishlistItemRepository.save(wishlistItem);

}
}
