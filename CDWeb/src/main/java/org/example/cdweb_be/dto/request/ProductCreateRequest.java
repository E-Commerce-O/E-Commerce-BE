package org.example.cdweb_be.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductCreateRequest {
    String name;
    String slug;
    double defaultPrice;
    int defaultDiscount;
    long categoryId;
    String description;
    String brand;
    List<ColorRequest> colors;
    List<SizeCreateRequest> sizes;
    List<String> images;
    List<String> tags;

}
