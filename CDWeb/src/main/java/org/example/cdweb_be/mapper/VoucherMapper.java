package org.example.cdweb_be.mapper;

import org.example.cdweb_be.dto.request.VoucherRequest;
import org.example.cdweb_be.dto.response.VoucherResponse;
import org.example.cdweb_be.entity.Voucher;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")

public interface VoucherMapper {
    Voucher toVoucber(VoucherRequest request);
    @Mapping(target = "categoriesApply", ignore = true)
    VoucherResponse toVoucherResponse(Voucher voucher);
}
