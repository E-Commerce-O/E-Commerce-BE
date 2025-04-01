package org.example.cdweb_be.controller;

import org.example.cdweb_be.entity.User;
import org.example.cdweb_be.entity.WishlistItem;
import org.example.cdweb_be.respository.WishlistItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    WishlistItemRepository wishlistItemRepository;
@PostMapping
    public WishlistItem add(){
    WishlistItem wishlistItem = new WishlistItem();
    return wishlistItemRepository.save(wishlistItem);

}
}
