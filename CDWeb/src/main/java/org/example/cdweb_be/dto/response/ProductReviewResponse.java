package org.example.cdweb_be.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.cdweb_be.entity.ProductColor;
import org.example.cdweb_be.entity.ProductSize;

import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
@Builder
public class ProductReviewResponse {
    long id;
    long productId;
    String productName;
    OrderUser orderUser;
    List<String> images;
    int ratingScore;
    String content;
    Timestamp createdAt;
    Timestamp updatedAt;


}
