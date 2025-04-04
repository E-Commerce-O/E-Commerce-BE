package org.example.cdweb_be.mapper;

import org.example.cdweb_be.dto.request.CategoryCreateRequest;
import org.example.cdweb_be.dto.request.TagCreateRequest;
import org.example.cdweb_be.entity.Category;
import org.example.cdweb_be.entity.Tag;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category toCategory(CategoryCreateRequest request);
}
