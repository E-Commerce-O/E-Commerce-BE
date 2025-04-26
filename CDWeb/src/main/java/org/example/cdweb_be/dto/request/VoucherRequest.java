package org.example.cdweb_be.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VoucherRequest {
    String name;
    @Size(min = 6, message="VOUCHER_CODE_INVALD")
    String code;
    int type;
    List<Long> categoryIds;
    String imagePath;
    String description;
    int quantity;
    int percentDecrease;
    int maxDecrease;
    int minPrice;
    Timestamp startAt;
    Timestamp endAt;
}
