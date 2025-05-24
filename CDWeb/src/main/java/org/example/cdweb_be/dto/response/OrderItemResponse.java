package org.example.cdweb_be.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.cdweb_be.entity.OrderItem;
import org.example.cdweb_be.entity.ProductColor;
import org.example.cdweb_be.entity.ProductSize;

import java.sql.Timestamp;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemResponse {
    long id;
    long productId;
    String productName;
    List<String> productImages;
    ProductColor color;
    ProductSize size;
    double originalPrice;
    int discount;
    int quantity;
    Timestamp createdAt;
    Timestamp updatedAt;
    public OrderItemResponse(OrderItem orderItem){
        this.id = orderItem.getId();
        this.productId = orderItem.getProduct().getId();
        this.productName = orderItem.getProduct().getName();
       this.color = orderItem.getColor();
       this.size = orderItem.getSize();
       this.originalPrice = orderItem.getOriginalPrice();
       this.discount = orderItem.getDiscount();
       this.quantity = orderItem.getQuantity();
       this.createdAt = orderItem.getCreatedAt();
       this.updatedAt = orderItem.getUpdatedAt();
    }

}
