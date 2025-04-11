package org.example.cdweb_be.mapper;

import org.example.cdweb_be.dto.response.AddressResponse;
import org.example.cdweb_be.entity.Address;
import org.example.cdweb_be.entity.Province;
import org.example.cdweb_be.utils.responseUtilsAPI.ProvinceUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    AddressResponse toAddressResponse(Address address);

}
