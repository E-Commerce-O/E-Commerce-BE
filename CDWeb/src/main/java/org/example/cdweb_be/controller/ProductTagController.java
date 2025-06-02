package org.example.cdweb_be.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.cdweb_be.dto.response.ApiResponse;
import org.example.cdweb_be.entity.ProductTag;
import org.example.cdweb_be.service.ProductTagService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
//Mapping tất cả api trong controller này thành users
//@RequestMapping("/users")
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j // annotation để sử dụng log
public class ProductTagController {
    ProductTagService productTagService;
    @GetMapping
    public ApiResponse getAll(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "9") int size){
        return new ApiResponse(productTagService.getAll(page, size));
    }

    @GetMapping("/product/{productId}")
    public ApiResponse getByProductId(@PathVariable long productId, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "9") int size){
        return new ApiResponse( productTagService.getByProductId(productId, page, size));
    }

}
