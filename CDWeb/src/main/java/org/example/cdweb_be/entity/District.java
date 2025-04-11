package org.example.cdweb_be.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.example.cdweb_be.utils.ConvertStringUtils;
import org.example.cdweb_be.utils.responseUtilsAPI.DistrictUtil;

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
public class District {
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    String name;
    long provinceId;

    public District(DistrictUtil districtUtil) {
        this.id = Long.parseLong(districtUtil.getDISTRICT_ID());
        this.name = ConvertStringUtils.convertGivenName(districtUtil.getDISTRICT_NAME());
        this.provinceId = Long.parseLong(districtUtil.getPROVINCE_ID());
    }
}
