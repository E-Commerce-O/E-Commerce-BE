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
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    @ManyToOne
    Cart cart;
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    Product product;
    @ManyToOne
    @JoinColumn(name = "size_id")
    ProductSize size;
    @ManyToOne
    @JoinColumn(name = "color_id")
    ProductColor color;
    int quantity;
    Timestamp createdAt;
    Timestamp updatedAt;
    public String getErrorCodeMessage(){
        String result = "Remaining quantity of product "+this.getProduct().getName();
        if(this.getSize() != null || this.getColor() != null) result += " with ";
        if(this.getSize() != null) result +="Size: "+ this.getSize().getSize()+" ";
        if(this.getColor() != null) result += "Color: "+this.getColor().getColorName()+" ";
        result += "not enought!";
        return result;
    }
}
