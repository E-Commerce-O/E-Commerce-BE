package org.example.cdweb_be.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

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
@Slf4j
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    @OneToOne
    User user;

    Timestamp createdAt;
    Timestamp updatedAt;

//    public CartItem getByProductId(long productId) {
//        CartItem cartItem = null;
//        for (CartItem ci : getCartItems()) {
//            if (ci.getProduct().getId() == productId) {
//                cartItem = ci;
//                break;
//            }
//        }
//        return cartItem;
//    }
//
//    public CartItem getByProductAndSize(long productId, long sizeId) {
//        CartItem cartItem = null;
//        for (CartItem ci : getCartItems()) {
//            if (ci.getProduct().getId() == productId && ci.getSize().getId() == sizeId) {
//                cartItem = ci;
//                break;
//            }
//        }
//        return cartItem;
//    }
//    public CartItem getByProductAndColor(long productId, long colorId) {
//        CartItem cartItem = null;
//        for (CartItem ci : getCartItems()) {
//            if (ci.getProduct().getId() == productId && ci.getColor().getId() == colorId) {
//                cartItem = ci;
//                break;
//            }
//        }
//        return cartItem;
//    }
//    public CartItem getByProductAndColorAndSize(long productId, long colorId, long sizeId) {
//        CartItem cartItem = null;
//        for (CartItem ci : getCartItems()) {
//            log.info("running ...");
//            if (ci.getProduct().getId() == productId && ci.getColor().getId() == colorId && ci.getSize().getId() == sizeId) {
//                cartItem = ci;
//                break;
//            }
//        }
//        return cartItem;
//    }

}
