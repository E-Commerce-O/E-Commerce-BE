package org.example.cdweb_be.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.cdweb_be.dto.request.TagCreateRequest;
import org.example.cdweb_be.dto.response.ApiResponse;
import org.example.cdweb_be.entity.Tag;
import org.example.cdweb_be.service.TagService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tag")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j // annotation để sử dụng log
public class TagController {
    TagService tagService;
    @PostMapping
    ApiResponse addTag(TagCreateRequest request){
        return new ApiResponse(tagService.addTag(request));
    }
    @GetMapping("/{name}")
    ApiResponse getTag(@PathVariable String name) {
        return new ApiResponse(tagService.getTagByName(name));
    }
    @GetMapping
    ApiResponse getAllTags(){
        return new ApiResponse(tagService.getAll());
    }
//    @GetMapping("/getAllByNames")
//    ApiResponse getAllTagsById(@RequestBody List<String> tagNames){
//        return new ApiResponse(tagService.getAllByid(tagNames));
//    }
    @PutMapping
    ApiResponse updateTag(@RequestBody TagCreateRequest request){
        return new ApiResponse(tagService.updateTag(request));
    }
    @DeleteMapping("/{tagName}")
    ApiResponse deleteTag(@PathVariable String tagName){
        tagService.deleteTag(tagName);
        return new ApiResponse<>("Tag deleted successfully");
    }

}
