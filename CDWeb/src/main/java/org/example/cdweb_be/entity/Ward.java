package org.example.cdweb_be.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.example.cdweb_be.utils.ConvertStringUtils;
import org.example.cdweb_be.utils.responseUtilsAPI.WardUtil;

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
public class Ward {
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    String name;
    long districtId;

    public Ward(WardUtil wardUtil) {
        this.id = Long.parseLong(wardUtil.getWARDS_ID());
        this.name = ConvertStringUtils.convertGivenName(wardUtil.getWARDS_NAME());
        this.districtId = Long.parseLong(wardUtil.getDISTRICT_ID());
    }
}
