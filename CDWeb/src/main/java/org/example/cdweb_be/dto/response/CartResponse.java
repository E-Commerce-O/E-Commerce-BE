package org.example.cdweb_be.dto.response;

import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.example.cdweb_be.entity.CartItem;
import org.example.cdweb_be.entity.User;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

@Data
// annotation giúp khởi tại đối tượng
@Builder
// annotation tạo constructor
@NoArgsConstructor
@AllArgsConstructor
// annotation định nghĩa field mặc định của biến
@FieldDefaults(level = AccessLevel.PRIVATE) // mặc định là private nếu k tự định nghĩa
public class CartResponse {
    long id;
    long userId;
    List<CartItemResponse> cartItems;
    Timestamp createdAt;
    Timestamp updatedAt;
}
