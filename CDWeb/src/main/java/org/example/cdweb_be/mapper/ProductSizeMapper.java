package org.example.cdweb_be.mapper;

import org.example.cdweb_be.dto.request.ProductSizeCreateRequest;
import org.example.cdweb_be.entity.ProductSize;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductSizeMapper {
//    @Mapping(target = "", ignore = true)
    ProductSize toProductSize(ProductSizeCreateRequest request);
}
