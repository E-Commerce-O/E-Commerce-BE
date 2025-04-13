package org.example.cdweb_be.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductImportCreateRequest {
    long userImported;
    long productId;
    long colorId;
    long sizeId;
    int quantity;
    double price;
    Date importedAt;
}
