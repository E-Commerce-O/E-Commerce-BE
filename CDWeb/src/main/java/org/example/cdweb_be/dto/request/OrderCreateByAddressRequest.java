package org.example.cdweb_be.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.cdweb_be.utils.responseUtilsAPI.DeliveryMethodUtil;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderCreateByAddressRequest {
    List<Long> cartItemIds;
    AddressRequest address;
    DeliveryMethodUtil deliveryMethod;
    long paymentMethodId;
    long freeshipVcId;
    long productVcId;
}
