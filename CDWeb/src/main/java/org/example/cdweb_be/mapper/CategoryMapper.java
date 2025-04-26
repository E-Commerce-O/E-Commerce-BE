package org.example.cdweb_be.mapper;

import org.example.cdweb_be.dto.request.CategoryCreateRequest;
import org.example.cdweb_be.dto.request.TagCreateRequest;
import org.example.cdweb_be.dto.response.CategoryVoucher;
import org.example.cdweb_be.entity.Category;
import org.example.cdweb_be.entity.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category toCategory(CategoryCreateRequest request);
    CategoryVoucher toCategoryVoucher(Category category);
}
