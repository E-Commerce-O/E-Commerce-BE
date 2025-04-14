package org.example.cdweb_be.dto.response;

import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.example.cdweb_be.entity.Product;
@Data
// annotation giúp khởi tại đối tượng
@Builder
// annotation tạo constructor
@NoArgsConstructor
@AllArgsConstructor
// annotation định nghĩa field mặc định của biến
@FieldDefaults(level = AccessLevel.PRIVATE) // mặc định là private nếu k tự định nghĩa
public class ProductSizeResponse {
    long id;
    String size;
    String description;
}
