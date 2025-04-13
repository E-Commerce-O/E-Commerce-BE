package org.example.cdweb_be.mapper;

import org.example.cdweb_be.dto.request.SizeCreateRequest;
import org.example.cdweb_be.entity.Size;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductSizeMapper {
//    @Mapping(target = "", ignore = true)
    Size toProductSize(SizeCreateRequest request);
}
