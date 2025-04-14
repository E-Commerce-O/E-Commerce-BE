package org.example.cdweb_be.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.cdweb_be.dto.request.CartItemRequest;
import org.example.cdweb_be.dto.response.ApiResponse;
import org.example.cdweb_be.service.CartService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartController {
    CartService cartService;
    @GetMapping("/getMyCart")
    public ApiResponse getMyCart(@RequestHeader("Authorization") String token){
        return new ApiResponse(cartService.getMyCart(token));
    }
    @PostMapping("/addItem")
    public ApiResponse addItem(@RequestHeader("Authorization") String token, @RequestBody CartItemRequest request){
        return new ApiResponse(cartService.addItem(token, request));
    }
}
