package org.example.cdweb_be.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.cdweb_be.dto.request.ProductDetailUpdateRequest;
import org.example.cdweb_be.dto.response.ApiResponse;
import org.example.cdweb_be.service.ProductDetailService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/productDetails")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductDetailController {
    ProductDetailService productDetailService;

    @GetMapping("/product/{productId}")
    public ApiResponse getDetailsByProduct(@PathVariable long productId){
        return new ApiResponse(productDetailService.getDetailsByProduct(productId));
    }
    @GetMapping("/product/{productId}/color/{colorId}")
    public ApiResponse getDetailsByProductAndColor(@PathVariable long productId, @PathVariable long colorId){
        return new ApiResponse(productDetailService.getDetailsByProductAndColor(productId, colorId));
    }
    @GetMapping("/product/{productId}/color/{colorId}/size/{sizeId}")
    public ApiResponse getDetailsByProductAndColorAndSize(@PathVariable long productId, @PathVariable long colorId, @PathVariable long sizeId){
        return new ApiResponse(productDetailService.getDetailsByProductAndColorAndSize(productId, colorId, sizeId));
    }
    @GetMapping("/product/{productId}/size/{sizeId}")
    public ApiResponse getDetailsByProductAndSize(@PathVariable long productId, @PathVariable long sizeId){
        return new ApiResponse(productDetailService.getDetailsByProductAndSize(productId, sizeId));
    }
    @PutMapping
    public ApiResponse update(@RequestBody ProductDetailUpdateRequest request){
        return new ApiResponse(productDetailService.update(request));
    }
}
