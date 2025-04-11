package org.example.cdweb_be.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.example.cdweb_be.utils.responseUtilsAPI.ProvinceUtil;

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
public class Province {
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    String name;
    public Province(ProvinceUtil provinceUtil){
        this.id = Long.parseLong(provinceUtil.getPROVINCE_ID());
        this.name = provinceUtil.getPROVINCE_NAME();
    }
}
