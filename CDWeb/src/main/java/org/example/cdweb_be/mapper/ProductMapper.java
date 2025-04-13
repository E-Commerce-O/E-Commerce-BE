package org.example.cdweb_be.mapper;

import org.example.cdweb_be.dto.request.ColorRequest;
import org.example.cdweb_be.dto.request.ProductCreateRequest;
import org.example.cdweb_be.dto.response.ProductDetailRespone;
import org.example.cdweb_be.dto.response.ProductResponse;
import org.example.cdweb_be.entity.Product;
import org.example.cdweb_be.entity.Color;
import org.example.cdweb_be.entity.ProductDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mappings({
            @Mapping(target = "colors", ignore = true),
            @Mapping(target = "images", ignore = true),
            @Mapping(target = "sizes", ignore = true),
            @Mapping(source = "slug", target = "slug"),
            @Mapping(target = "category", ignore = true)
    })

    Product toProduct(ProductCreateRequest request);
    ProductResponse toProductResponse(Product product);
    @Mappings({
            @Mapping(source = "size" , target = "size"),
            @Mapping(source = "color", target = "color")
    })
    ProductDetailRespone toProductDetailResponse(ProductDetail productDetail);
    Color toProductColor(ColorRequest request);
}
