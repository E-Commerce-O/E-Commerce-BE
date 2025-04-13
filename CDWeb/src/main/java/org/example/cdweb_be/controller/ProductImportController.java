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
@RequestMapping("/importProduct")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ProductImportController {
    ProductImportService productImportService;
    @GetMapping("/getAll")
    public ApiResponse getAll(){
        return new ApiResponse(productImportService.getAllProductImports());
    }
    @GetMapping("/getByProduct/{productId}")
    public ApiResponse getByProduct(@PathVariable long productId) {
        return new ApiResponse(productImportService.getProductImportByProduct(productId));
    }
    @PostMapping("/add")
    public ApiResponse add(@RequestHeader("Authorization") String token, @RequestBody ProductImportCreateRequest request){
        return new ApiResponse(productImportService.add(token, request));
    }
    @PutMapping("/update")
    public ApiResponse update(@RequestHeader("Authorization") String token, @RequestBody ProductImportUpdateRequest request){
        return new ApiResponse(productImportService.update(token, request));
    }
    @DeleteMapping("/delete/{importId}")
    public ApiResponse delete(@PathVariable long importId){
        return new ApiResponse(productImportService.delete(importId));
    }
}
