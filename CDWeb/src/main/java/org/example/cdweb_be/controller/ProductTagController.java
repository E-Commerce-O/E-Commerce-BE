package org.example.cdweb_be.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.cdweb_be.dto.response.ApiResponse;
import org.example.cdweb_be.entity.ProductTag;
import org.example.cdweb_be.service.ProductTagService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public List<ProductTag> getAll(){
        return productTagService.getAll();
    }

    @GetMapping("/product/{productId}")
    public ApiResponse getByProductId(@PathVariable long productId){
        return new ApiResponse( productTagService.getByProductId(productId));
    }

}
