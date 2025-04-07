package org.example.cdweb_be.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.cdweb_be.entity.Category;
import org.example.cdweb_be.entity.ProductColor;
import org.example.cdweb_be.entity.ProductSize;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductSizeCreateRequest {
    String size;
    String description;
}
