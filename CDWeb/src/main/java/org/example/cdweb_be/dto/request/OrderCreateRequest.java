package org.example.cdweb_be.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.cdweb_be.utils.responseUtilsAPI.DeliveryMethodUtil;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

public class OrderCreateRequest {
    List<Long> cartItemIds;
    long addressId;
    DeliveryMethodUtil deliveryMethod;
    long freeshipVcId;
    long productVcId;

}
