package org.example.cdweb_be.dto.response;

import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.example.cdweb_be.entity.Address;
import org.example.cdweb_be.entity.DeliveryMethod;
import org.example.cdweb_be.entity.User;
import org.example.cdweb_be.enums.OrderStatus;

import java.sql.Timestamp;
import java.util.List;

@Data
// annotation giúp khởi tại đối tượng
@Builder
// annotation tạo constructor
@NoArgsConstructor
@AllArgsConstructor
// annotation định nghĩa field mặc định của biến
@FieldDefaults(level = AccessLevel.PRIVATE) // mặc định là private nếu k tự định nghĩa
public class OrderResponse {
    long orderId;
    OrderUser orderUser;
    Address receiverAddress;
    DeliveryMethod deliveryMethod;
    List<OrderItemResponse> orderItems;
    OrderStatusResponse status;
    double productDecrease;
    double shipDecrease;
    double totalPrice;
    double totalPayment;
    Timestamp createdAt;
    Timestamp updatedAt;
}
