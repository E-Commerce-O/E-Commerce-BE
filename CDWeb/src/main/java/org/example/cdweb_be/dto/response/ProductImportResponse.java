package org.example.cdweb_be.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.example.cdweb_be.entity.Color;
import org.example.cdweb_be.entity.Size;

import java.sql.Date;
import java.sql.Timestamp;

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
    Color productColor;
    Size productSize;
    int quantityImport;
    double priceImport;
    Date importedAt;
}
