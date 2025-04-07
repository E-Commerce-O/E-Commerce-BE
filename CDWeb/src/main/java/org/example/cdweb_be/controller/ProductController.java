package org.example.cdweb_be.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.cdweb_be.dto.request.AddProductImageRequest;
import org.example.cdweb_be.dto.request.ProductCreateRequest;
import org.example.cdweb_be.dto.request.ProductUpdateRequest;
import org.example.cdweb_be.dto.response.ApiResponse;
import org.example.cdweb_be.service.ProductService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductController {
    ProductService productService;
    @GetMapping("/getALl")
    public ApiResponse getAllProduct() {
        return new ApiResponse(productService.getAll());
    }
    @GetMapping("/getById/{productId}")
    public ApiResponse getProductById(@PathVariable long productId){
        return new ApiResponse(productService.getByProductId(productId));
    }
    @GetMapping("/getByName/{productName}")
    public ApiResponse getByName(@PathVariable String productName){
        return new ApiResponse(productService.getByName(productName));
    }

    @PostMapping("/add")
    public ApiResponse addProduct(@RequestBody ProductCreateRequest request){
        return new ApiResponse(productService.addProduct(request));
    }
    @PostMapping("/addImage")
    public ApiResponse addImage(@RequestBody AddProductImageRequest request) {
        return new ApiResponse(productService.addProductImage(request));
    }
    @PutMapping("update")
    public ApiResponse updateProduct(@RequestBody ProductUpdateRequest request){
        return new ApiResponse(productService.updateProduct(request));
    }
}
