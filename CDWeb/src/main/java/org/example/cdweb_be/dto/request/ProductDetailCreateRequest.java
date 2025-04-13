package org.example.cdweb_be.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetailCreateRequest {
    long productId;
    long colorId;
    long sizeId;
    int discount;
    double price;

}
