package org.example.cdweb_be.dto.response;

import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.example.cdweb_be.entity.Product;
import org.example.cdweb_be.entity.ProductColor;
import org.example.cdweb_be.entity.ProductSize;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level =  AccessLevel.PRIVATE)
public class ProductDetailRespone {
    long id;
    ProductColor productColor;
    ProductSize productSize;
    int discount;
    double price;
    int quantity;
}
