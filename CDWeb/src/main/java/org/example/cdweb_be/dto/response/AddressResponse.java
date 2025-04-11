package org.example.cdweb_be.dto.response;

import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.example.cdweb_be.entity.District;
import org.example.cdweb_be.entity.Province;
import org.example.cdweb_be.entity.User;
import org.example.cdweb_be.entity.Ward;

// annotation tạo getter và setter cho các field private
@Data
// annotation giúp khởi tại đối tượng
@Builder
// annotation tạo constructor
@NoArgsConstructor
@AllArgsConstructor
// annotation định nghĩa field mặc định của biến
@FieldDefaults(level = AccessLevel.PRIVATE) // mặc định là private nếu k tự định nghĩa
public class AddressResponse {
    long id;
    Province province;
    District district;
    Ward ward;
    String houseNumber;
}
