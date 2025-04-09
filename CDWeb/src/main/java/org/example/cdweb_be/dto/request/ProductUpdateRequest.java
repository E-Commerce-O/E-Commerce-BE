package org.example.cdweb_be.dto.request;

import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.cdweb_be.entity.Category;
import org.example.cdweb_be.entity.ProductColor;
import org.example.cdweb_be.entity.ProductImage;
import org.example.cdweb_be.entity.ProductSize;

import java.sql.Timestamp;
import java.util.List;
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
