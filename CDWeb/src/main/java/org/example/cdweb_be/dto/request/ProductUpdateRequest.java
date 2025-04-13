package org.example.cdweb_be.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductUpdateRequest {
    long id;
    String name;
    String slug;
    long categoryId;
    double defaultPrice;
    int defaultDiscount;
    boolean published;
    String description;
    String brand;
}
