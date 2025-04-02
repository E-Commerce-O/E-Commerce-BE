package org.example.cdweb_be.entity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Date;
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
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    String name;
    String imagePath;
    String description;
}
