package org.example.cdweb_be.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.example.cdweb_be.entity.ProductColor;
import org.example.cdweb_be.entity.ProductSize;

import java.sql.Date;

@Data

@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductImportResponse {
    long id;
    UserImportProduct userImported;
    long productId;
    String productName;
    ProductColor color;
    ProductSize productSize;
    int quantityImport;
    double priceImport;
    Date importedAt;
}
