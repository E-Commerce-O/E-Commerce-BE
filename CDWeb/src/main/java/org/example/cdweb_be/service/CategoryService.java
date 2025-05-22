package org.example.cdweb_be.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.cdweb_be.dto.request.CategoryCreateRequest;
import org.example.cdweb_be.entity.Category;
import org.example.cdweb_be.exception.AppException;
import org.example.cdweb_be.exception.ErrorCode;
import org.example.cdweb_be.mapper.CategoryMapper;
import org.example.cdweb_be.respository.CategoryRepository;
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
    public Category addCategory(CategoryCreateRequest request){
        Optional<Category> categoryOptional = categoryRepository.findByName(request.getName());
        if(categoryOptional.isPresent()){
            throw new AppException(ErrorCode.CATEGORY_EXISTED);
        }else{
            Category category = categoryMapper.toCategory(request);
            return categoryRepository.save(category);
        }
    }
    public List<Category> getAll(){
        return categoryRepository.findAll();
    }
    public String deleteCategory(long id){
        categoryRepository.deleteById(id);
        return "Delete category successful!";
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
            throw new AppException(ErrorCode.CATEGORY_NOT_EXISTS);
        }
    }

}
