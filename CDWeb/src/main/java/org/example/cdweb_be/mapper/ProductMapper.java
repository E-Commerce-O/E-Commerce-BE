package org.example.cdweb_be.mapper;

import org.example.cdweb_be.dto.request.ColorRequest;
import org.example.cdweb_be.dto.request.ProductCreateRequest;
import org.example.cdweb_be.dto.response.*;
import org.example.cdweb_be.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mappings({
//            @Mapping(target = "colors", ignore = true),
//            @Mapping(target = "images", ignore = true),
//            @Mapping(target = "sizes", ignore = true),
            @Mapping(source = "slug", target = "slug"),
            @Mapping(target = "category", ignore = true)
    })

    Product toProduct(ProductCreateRequest request);
    @Mappings({
            @Mapping(target = "images", ignore = true),
            @Mapping(target = "colors", ignore = true)
    })
    ProductResponse toProductResponse(Product product);
    @Mappings({
            @Mapping(target = "size", ignore = true),
            @Mapping(target = "color", ignore = true)
    })
    ProductDetailRespone toProductDetailResponse(ProductDetail productDetail);
    ProductColor toProductColor(ColorRequest request);
    ProductColorResponse toProductColorResponse(ProductColor productColor);

    ProductImageResponse toProductImageResponse(ProductImage productImage);
    ProductSizeResponse toProductSizeResponse(ProductSize productSize);
}
