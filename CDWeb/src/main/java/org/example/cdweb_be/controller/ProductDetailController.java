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
@RequestMapping("/productDetail")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductDetailController {
    ProductDetailService productDetailService;

    @GetMapping("/getByProduct/{productId}")
    public ApiResponse getDetailsByProduct(@PathVariable long productId){
        return new ApiResponse(productDetailService.getDetailsByProduct(productId));
    }
    @GetMapping("/getByProductAndColor")
    public ApiResponse getDetailsByProductAndColor(@RequestParam long productId, @RequestParam long colorId){
        return new ApiResponse(productDetailService.getDetailsByProductAndColor(productId, colorId));
    }
    @GetMapping("/getByProductAndColorAndSize")
    public ApiResponse getDetailsByProductAndColorAndSize(@RequestParam long productId, @RequestParam long colorId, @RequestParam long sizeId){
        return new ApiResponse(productDetailService.getDetailsByProductAndColorAndSize(productId, colorId, sizeId));
    }
    @GetMapping("/getByProductAndSize")
    public ApiResponse getDetailsByProductAndSize(@RequestParam long productId, @RequestParam long sizeId){
        return new ApiResponse(productDetailService.getDetailsByProductAndSize(productId, sizeId));
    }
    @PutMapping("/update")
    public ApiResponse update(@RequestBody ProductDetailUpdateRequest request){
        return new ApiResponse(productDetailService.update(request));
    }
}
