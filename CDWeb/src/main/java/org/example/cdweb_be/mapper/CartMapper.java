package org.example.cdweb_be.mapper;

import org.example.cdweb_be.dto.response.CartResponse;
import org.example.cdweb_be.entity.Cart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartMapper {
    @Mapping(source = "user.id", target = "userId")
    CartResponse toCartResponse(Cart cart);
}
