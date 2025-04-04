package org.example.cdweb_be.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.cdweb_be.dto.request.CategoryCreateRequest;
import org.example.cdweb_be.dto.request.TagCreateRequest;
import org.example.cdweb_be.dto.response.ApiResponse;
import org.example.cdweb_be.entity.Category;
import org.example.cdweb_be.service.CategoryService;
import org.example.cdweb_be.service.TagService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j // annotation để sử dụng log
public class CategoryController {
    CategoryService categoryService;
    @PostMapping("/add")
    ApiResponse addCategory(CategoryCreateRequest request){
        return new ApiResponse(categoryService.addCategory(request));
    }
//    @GetMapping("/{name}")
//    ApiResponse getTag(@PathVariable String name) {
//        return new ApiResponse(tagService.getTagByName(name));
//    }
    @GetMapping("/getAll")
    ApiResponse getAllTags(){
        return new ApiResponse(categoryService.getAll());
    }
//    @GetMapping("/getAllByNames")
//    ApiResponse getAllTagsById(@RequestBody List<String> tagNames){
//        return new ApiResponse(tagService.getAllByid(tagNames));
//    }
    @PutMapping("/update")
    ApiResponse updateTag(@RequestBody Category request){

        return new ApiResponse(categoryService.updateCategory(request));
    }
    @DeleteMapping("/delete/{id}")
    ApiResponse deleteTag(@PathVariable long id){
        categoryService.deleteCategory(id);
        return new ApiResponse<>("Category deleted successfully");
    }

}
