package org.example.cdweb_be.dto.request;

import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.cdweb_be.entity.Category;
import org.example.cdweb_be.entity.ProductColor;
import org.example.cdweb_be.entity.ProductImage;
import org.example.cdweb_be.entity.ProductSize;

import java.util.List;
import java.util.Set;

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
    List<ProductColorRequest> colors;
    List<ProductSizeCreateRequest> sizes;
    List<String> images;
    List<String> tags;

}
