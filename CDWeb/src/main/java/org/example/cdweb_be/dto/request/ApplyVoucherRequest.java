package org.example.cdweb_be.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApplyVoucherRequest {
    long voucherId;
    List<Long> cartItemIds;
    double shippingCost;
}
