package org.example.cdweb_be.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.cdweb_be.dto.response.ProductResponse;
import org.example.cdweb_be.entity.Product;
import org.example.cdweb_be.entity.User;
import org.example.cdweb_be.entity.WishlistItem;
import org.example.cdweb_be.exception.AppException;
import org.example.cdweb_be.exception.ErrorCode;
import org.example.cdweb_be.respository.ProductRepository;
import org.example.cdweb_be.respository.UserRepository;
import org.example.cdweb_be.respository.WishlistItemRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class WishlistItemService {
    WishlistItemRepository wishlistItemRepository;
    AuthenticationService authenticationService;
    ProductRepository productRepository;
    UserRepository userRepository;
    ProductService productService;
    public String addWishlist(String token, long productId){
        long userId = authenticationService.getUserId(token);
        User user = userRepository.findById(userId).get();
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new AppException(ErrorCode.PRODUCT_NOT_EXISTS)
        );
        Optional<WishlistItem> wishlistItemOptional = wishlistItemRepository.findByUserIdAndProductId(userId, productId);
        if(wishlistItemOptional.isPresent()){
            throw new AppException(ErrorCode.WHISTLIST_EXISTED);
        }
        WishlistItem wishlistItem = WishlistItem.builder()
                .user(user)
                .product(product)
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .build();
        wishlistItemRepository.save(wishlistItem);
        return "Add productId: "+productId+" in your wishlist successfully";


    }
    public String deleteWishlist(String token, long productId){
        long userId = authenticationService.getUserId(token);
        User user = userRepository.findById(userId).get();
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new AppException(ErrorCode.PRODUCT_NOT_EXISTS)
        );
        WishlistItem wishlistItem = wishlistItemRepository.findByUserIdAndProductId(userId, productId)
                .orElseThrow(() -> new AppException(ErrorCode.WHISTLIST_NOT_EXISTS));
        wishlistItemRepository.delete(wishlistItem);
        return "Delete productId: "+productId+" from your wishlist successfully";
    }
    public List<ProductResponse> getMyWishlist(String token){
        long userId = authenticationService.getUserId(token);
        List<WishlistItem> wishlistItems = wishlistItemRepository.findByUserId(userId);
        List<ProductResponse> productResponses = wishlistItems.stream()
                .map(wishlistItem -> productService.converToProductResponse(wishlistItem.getProduct()))
                .collect(Collectors.toList());
        return productResponses;

    }
}
