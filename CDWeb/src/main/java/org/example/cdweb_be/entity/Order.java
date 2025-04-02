package org.example.cdweb_be.entity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

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
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    @ManyToOne
    User user;
    int status;
    String deliveryMethod;
    double deliveryPrice;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<OrderItem> orderItems;
    @OneToOne
    OrderDetail orderDetail;
    Timestamp createdAt;
    Timestamp updatedAt;
}
