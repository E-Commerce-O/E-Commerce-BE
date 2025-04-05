package org.example.cdweb_be.mapper;

import org.example.cdweb_be.dto.request.UserCreateRequest;
import org.example.cdweb_be.dto.response.UserResponse;
import org.example.cdweb_be.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreateRequest request);
    UserResponse toUserResponse(User user);
}
