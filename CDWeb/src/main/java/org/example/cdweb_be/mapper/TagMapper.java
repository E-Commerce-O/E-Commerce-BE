package org.example.cdweb_be.mapper;

import org.example.cdweb_be.dto.request.TagCreateRequest;
import org.example.cdweb_be.entity.Tag;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TagMapper {
    Tag toTag(TagCreateRequest request);
}
