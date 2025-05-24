package org.example.cdweb_be.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.example.cdweb_be.dto.request.*;
import org.example.cdweb_be.dto.response.ApiResponse;
import org.example.cdweb_be.service.ProductService;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductController {
    ProductService productService;

    @GetMapping
    public ApiResponse getAllProduct() {
        return new ApiResponse(productService.getAll());
    }
    @GetMapping("/{productId}")
    public ApiResponse getProductById(@PathVariable long productId, HttpServletRequest request){
        return new ApiResponse(productService.getByProductId(productId, request));
    }
    @GetMapping("/name/{productName}")
    public ApiResponse getByName(@PathVariable String productName){
        return new ApiResponse(productService.getByName(productName));
    }
    @GetMapping("/category/{categoryId}")
    public ApiResponse getByCategory(@PathVariable long categoryId){
        return new ApiResponse(productService.getByCategory(categoryId));

    }
    @GetMapping("/similar/{productId}")
    public ApiResponse getSimilar(@PathVariable long productId){
        return new ApiResponse(productService.getSimilar(productId));
    }
    @GetMapping("/history")
    public ApiResponse getHistory(HttpServletRequest request){
        return new ApiResponse(productService.getHistory(request));
    }
    @PostMapping()
    public ApiResponse addProduct(@RequestBody ProductCreateRequest request){
        return new ApiResponse(productService.addProduct(request));
    }
    @PostMapping("/image")
    public ApiResponse addImage(@RequestBody AddProductImageRequest request) {
        return new ApiResponse(productService.addProductImages(request));
    }
    @PostMapping("colors/{productId}")
    public ApiResponse addColors(@PathVariable long productId, @RequestBody List<ColorRequest> requests){
        return new ApiResponse(productService.addColors(productId, requests));
    }
    @PostMapping("/sizes/{productId}")
    public ApiResponse addSizes(@PathVariable long productId, @RequestBody List<SizeCreateRequest> requests){
        return new ApiResponse(productService.addSizes(productId, requests));
    }
    @PutMapping()
    public ApiResponse updateProduct(@RequestBody ProductUpdateRequest request){
        return new ApiResponse(productService.updateProduct(request));
    }
    @PutMapping("/tags")
    public ApiResponse addTags(@RequestBody ProductTagRequest request){
        return new ApiResponse(productService.addTags(request));
    }
    @DeleteMapping("/image")
    public ApiResponse deleteImage(@RequestParam long productId, @RequestParam long imageId){
        return new ApiResponse(productService.deleteImage(productId, imageId));
    }
    @DeleteMapping("/tags")
    public ApiResponse deleteTags(@RequestBody ProductTagRequest request){
        return new ApiResponse(productService.deleteTags(request));
    }
    @DeleteMapping("/{productId}")
    public ApiResponse deleteProduct(@PathVariable long productId){
        return new ApiResponse(productService.deleteProduct(productId));
    }
//    @GetMapping("/get-ip")
//    public String search(HttpServletRequest request) {
//        String ipAddress = getIP(request);
//        // Lưu lịch sử tìm kiếm cùng với ipAddress và query vào cơ sở dữ liệu
//        // Ví dụ: saveSearchHistory(ipAddress, query);
//
//        return " from IP: " + ipAddress;
//    }

}
