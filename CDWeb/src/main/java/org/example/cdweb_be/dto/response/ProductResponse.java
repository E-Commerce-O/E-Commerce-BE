package org.example.cdweb_be.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.example.cdweb_be.entity.*;

import java.sql.Timestamp;
import java.util.List;

@Data
// annotation giúp khởi tại đối tượng
@Builder
// annotation tạo constructor
@NoArgsConstructor
@AllArgsConstructor
// annotation định nghĩa field mặc định của biến
@FieldDefaults(level = AccessLevel.PRIVATE) // mặc định là private nếu k tự định nghĩa
public class ProductResponse {
    long id;
    String name;
    String slug;
    double defaultPrice;
    int defaultDiscount;
    boolean published;
    Category category;
    String description;
    List<ProductImageResponse> images;
    List<ProductColorResponse> colors;
    List<ProductSizeResponse> sizes;
    List<String> tags;
    int totalSale;
    int quantity;
    double avgRating;
    int numReviews;
    String brand;
//    List<ProductDetailRespone> details;
    Timestamp createdAt;
    Timestamp updatedAt;
}
