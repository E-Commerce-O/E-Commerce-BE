package org.example.cdweb_be.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.cdweb_be.dto.request.ProductReviewCreateRequest;
import org.example.cdweb_be.dto.response.ApiResponse;
import org.example.cdweb_be.entity.ProductReview;
import org.example.cdweb_be.service.ProductReviewService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/productReview")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductReviewController {
    ProductReviewService productReviewService;
    @GetMapping("/product/{productId}")
    public ApiResponse getByProduct(@PathVariable long productId){
        return new ApiResponse(productReviewService.getByProductId(productId));
    }
    @PostMapping
    public ApiResponse add(@RequestBody ProductReviewCreateRequest request){
        return new ApiResponse();
    }
}
