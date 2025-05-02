package org.example.cdweb_be.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductReviewCreateRequest {
    long orderId;
    long productId;
    int ratingScore;
    String content;
    List<String> images;
}
