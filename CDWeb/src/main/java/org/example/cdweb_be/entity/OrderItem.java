package org.example.cdweb_be.entity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;
// annotation tạo getter và setter cho các field private
@Data
// annotation giúp khởi tại đối tượng
@Builder
// annotation tạo constructor
@NoArgsConstructor
@AllArgsConstructor
// annotation định nghĩa field mặc định của biến
@FieldDefaults(level = AccessLevel.PRIVATE) // mặc định là private nếu k tự định nghĩa
// annotation thể hiện là 1 bảng trong db
@Entity
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    @ManyToOne
    Order order;
    @ManyToOne
    Product product;
    @ManyToOne
    ProductSize size;
    @ManyToOne
    ProductColor color;
    int quantity;
    double originalPrice;
    int discount;
    Timestamp createdAt;
    Timestamp updatedAt;

    public OrderItem(CartItem cartItem){
        this.product = cartItem.getProduct();
        this.size = cartItem.getSize();
        this.color = cartItem.getColor();
        this.quantity = cartItem.getQuantity();
        this.createdAt = new Timestamp(System.currentTimeMillis());
    }
}
