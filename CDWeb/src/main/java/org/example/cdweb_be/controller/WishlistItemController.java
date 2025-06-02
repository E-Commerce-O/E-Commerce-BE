package org.example.cdweb_be.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.cdweb_be.dto.response.ApiResponse;
import org.example.cdweb_be.service.WishlistItemService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wishlists")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WishlistItemController {
    WishlistItemService wishlistItemService;
    @GetMapping("/myWishlist")
    public ApiResponse getMyWishlist(@RequestHeader("Authorization") String token, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "9") int size){
        return new ApiResponse(wishlistItemService.getMyWishlist(token, page, size));
    }
    @PostMapping("/{productId}")
    public ApiResponse addWishlist(@RequestHeader("Authorization") String token, @PathVariable long productId){
        return new ApiResponse(wishlistItemService.addWishlist(token, productId));
    }
    @DeleteMapping("/{productId}")
    public ApiResponse deleteWishlist(@RequestHeader("Authorization") String token, @PathVariable long productId){
        return new ApiResponse(wishlistItemService.deleteWishlist(token, productId));
    }
}
