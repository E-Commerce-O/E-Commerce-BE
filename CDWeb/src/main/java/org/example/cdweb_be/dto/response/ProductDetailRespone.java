package org.example.cdweb_be.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.example.cdweb_be.entity.Color;
import org.example.cdweb_be.entity.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level =  AccessLevel.PRIVATE)
public class ProductDetailRespone {
    long id;
    Color color;
    Size size;
    int discount;
    double price;
    int quantity;
}
