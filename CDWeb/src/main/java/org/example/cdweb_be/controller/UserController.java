package org.example.cdweb_be.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.cdweb_be.dto.request.*;
import org.example.cdweb_be.dto.response.ApiResponse;
import org.example.cdweb_be.service.UserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j // annotation để sử dụng log
//@Validated
public class UserController {
    UserService userService;
    @GetMapping("/validEmail/{email}")
    ApiResponse validEmail(@PathVariable String email){
        return new ApiResponse(userService.validEmail(email));
    }
    @GetMapping("/myInfo")
    ApiResponse getMyInfo(@RequestHeader("Authorization") String token){
        return new ApiResponse(userService.getMyInfo(token));
    }
    @GetMapping
    ApiResponse getAllUsers(){
        return new ApiResponse(userService.getAllUsers());
    }
    @PostMapping("/validToken")
    ApiResponse validToken(@RequestBody ValidTokenRequest accessToken){
        return new ApiResponse(userService.validToken(accessToken));
    }
    @PostMapping("/register")
    ApiResponse registerUser(@Valid @RequestBody UserCreateRequest request){
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

    @PostMapping("/sendOTP/{usernameOrEmail}")
    public ApiResponse sendMail(@PathVariable String usernameOrEmail){
        return new ApiResponse(userService.sendOTP(usernameOrEmail));
    }
    @PostMapping("/verifyOTP")
    public ApiResponse verifyOTP(@RequestBody VerifyOtpRequest request){
        return new ApiResponse(userService.verifyOTP(request));
    }
    @PutMapping("/changeInfo")
    public ApiResponse changeInfo(@RequestHeader("Authorization") String token,@Valid @RequestBody UserUpdateRequest request){
        return new ApiResponse(userService.updateUser(token, request));
    }
    @PutMapping("/resetPassword")
    public ApiResponse resetPassword(@RequestBody ResetPasswordRequest request){
        return new ApiResponse(userService.resetPassword(request));
    }
}
