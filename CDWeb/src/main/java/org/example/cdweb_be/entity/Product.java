package org.example.cdweb_be.entity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

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
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    String name;
    String slug;
    double defaultPrice;
    int defaultDiscount;
    boolean published;
    @ManyToOne
    Category category;
//    @OneToMany
//    List<Color> colors;
//    @OneToMany
//    List<ProductSize> sizes;
//    @OneToMany
//    List<Image> images;
    String description;
    String brand;
    Timestamp createdAt;
    Timestamp updatedAt;
//    public boolean isExistedColor(String colorName){
//        return colors.stream().anyMatch(color -> color.getColorName().equalsIgnoreCase(colorName));
//    }
//    public boolean isExistedSize(String sizeName){
//        return sizes.stream().anyMatch(size -> size.getSize().equalsIgnoreCase(sizeName));
//    }
//    public Color getColorById(long colorId){
//        return colors.stream().filter(color -> color.getId() == colorId).findFirst().orElse(null);
//    }
//    public ProductSize getSizeById(long sizeId){
//        return sizes.stream().filter(size -> size.getId() == sizeId).findFirst().orElse(null);
//    }
}
