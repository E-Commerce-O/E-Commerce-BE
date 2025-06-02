package org.example.cdweb_be.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.cdweb_be.component.MessageProvider;
import org.example.cdweb_be.dto.request.CategoryCreateRequest;
import org.example.cdweb_be.dto.response.PagingResponse;
import org.example.cdweb_be.entity.Category;
import org.example.cdweb_be.exception.AppException;
import org.example.cdweb_be.exception.ErrorCode;
import org.example.cdweb_be.mapper.CategoryMapper;
import org.example.cdweb_be.respository.CategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CategoryService {
    CategoryRepository categoryRepository;
    CategoryMapper categoryMapper;
    MessageProvider messageProvider;
    public Category addCategory(CategoryCreateRequest request){
        Optional<Category> categoryOptional = categoryRepository.findByName(request.getName());
        if(categoryOptional.isPresent()){
            throw new AppException(messageProvider,ErrorCode.CATEGORY_EXISTED);
        }else{
            Category category = categoryMapper.toCategory(request);
            return categoryRepository.save(category);
        }
    }
    public PagingResponse getAll(int page, int size){
        return PagingResponse.<Category>builder()
                .page(page)
                .size(size)
                .totalItem(categoryRepository.count())
                .data(categoryRepository.findAll(PageRequest.of(page-1, size)).stream().toList())
                .build();
    }
    public String deleteCategory(long id){
        Category category = categoryRepository.findById(id).orElseThrow(() ->
                new AppException(messageProvider, ErrorCode.CATEGORY_NOT_EXISTS));
        categoryRepository.deleteById(id);
        return messageProvider.getMessage("category.delete");
    }
    public Category updateCategory(Category request){
        Optional<Category> categoryOptional = categoryRepository.findById(request.getId());
        if(categoryOptional.isPresent()){
            Category curCategory = categoryOptional.get();
            curCategory.setName(request.getName());
            curCategory.setDescription(request.getDescription());
            curCategory.setImagePath(request.getImagePath());
            return categoryRepository.save(curCategory);
        }else{
            throw new AppException(messageProvider,ErrorCode.CATEGORY_NOT_EXISTS);
        }
    }

}
