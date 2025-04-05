package org.example.cdweb_be.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.cdweb_be.dto.request.LoginRequest;
import org.example.cdweb_be.dto.request.RefreshTokenRequest;
import org.example.cdweb_be.dto.request.UserCreateRequest;
import org.example.cdweb_be.dto.request.UserUpdateRequest;
import org.example.cdweb_be.dto.response.ApiResponse;
import org.example.cdweb_be.entity.Cart;
import org.example.cdweb_be.entity.CartItem;
import org.example.cdweb_be.entity.User;
import org.example.cdweb_be.entity.WishlistItem;
import org.example.cdweb_be.respository.CartRepository;
import org.example.cdweb_be.respository.UserRepository;
import org.example.cdweb_be.respository.WishlistItemRepository;
import org.example.cdweb_be.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j // annotation để sử dụng log
public class UserController {
    UserService userService;
    @GetMapping("/validEmail/{email}")
    ApiResponse validEmail(@PathVariable String email){
        return new ApiResponse(userService.validEmail(email));
    }


    @PostMapping("/add")
    ApiResponse registerUser(@RequestBody UserCreateRequest request){
        return new ApiResponse(userService.addUser(request));
    }
    @PostMapping("/login")
    ApiResponse login(@RequestBody LoginRequest request){
        return new ApiResponse(userService.login(request));
    }
    @PostMapping("/refreshToken")
    ApiResponse refreshToken( @RequestBody RefreshTokenRequest request){
        return new ApiResponse(userService.refreshToken( request));
    }
    @PutMapping("/changeInfo")
    ApiResponse changeInfo(@RequestHeader("Authorization") String token,@RequestBody UserUpdateRequest request){
        return new ApiResponse(userService.updateUser(token, request));
    }
}
