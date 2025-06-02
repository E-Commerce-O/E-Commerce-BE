package org.example.cdweb_be.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.cdweb_be.dto.request.ProductImportCreateRequest;
import org.example.cdweb_be.dto.request.ProductImportUpdateRequest;
import org.example.cdweb_be.dto.response.ApiResponse;
import org.example.cdweb_be.entity.ProductImport;
import org.example.cdweb_be.service.ProductImportService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/productImports")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ProductImportController {
    ProductImportService productImportService;
    @GetMapping
    public ApiResponse getAll(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "9") int size){
        return new ApiResponse(productImportService.getAllProductImports(page, size));
    }
    @GetMapping("product/{productId}")
    public ApiResponse getByProduct(@PathVariable long productId, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "9") int size) {
        return new ApiResponse(productImportService.getProductImportByProduct(productId, page, size));
    }
    @PostMapping
    public ApiResponse add(@RequestHeader("Authorization") String token, @RequestBody ProductImportCreateRequest request){
        return new ApiResponse(productImportService.add(token, request));
    }
    @PutMapping
    public ApiResponse update(@RequestHeader("Authorization") String token, @RequestBody ProductImportUpdateRequest request){
        return new ApiResponse(productImportService.update(token, request));
    }
    @DeleteMapping("/{importId}")
    public ApiResponse delete(@PathVariable long importId){
        return new ApiResponse(productImportService.delete(importId));
    }
}
