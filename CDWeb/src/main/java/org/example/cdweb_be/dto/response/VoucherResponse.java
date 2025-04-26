package org.example.cdweb_be.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VoucherResponse {
    long id;
    String name;
    String code;
    List<CategoryVoucher> categoriesApply;
    String imagePath;
    String description;
    int quantity;
    int percentDecrease;
    int maxDecrease;
    int minPrice;
    int type;
    Timestamp createdAt;
    Timestamp updatedAt;
    Timestamp startAt;
    Timestamp endAt;
}
