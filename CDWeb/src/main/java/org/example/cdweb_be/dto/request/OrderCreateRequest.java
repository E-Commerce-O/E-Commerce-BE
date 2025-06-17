package org.example.cdweb_be.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.example.cdweb_be.utils.responseUtilsAPI.DeliveryMethodUtil;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED)
@Builder
public class OrderCreateRequest {
    List<Long> cartItemIds;
    long addressId;
    DeliveryMethodUtil deliveryMethod;
    long paymentMethodId;
    long freeshipVcId;
    long productVcId;

}
