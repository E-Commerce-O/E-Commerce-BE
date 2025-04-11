package org.example.cdweb_be.utils.responseUtilsAPI;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
// annotation giúp khởi tại đối tượng
@Builder
// annotation tạo constructor
@NoArgsConstructor
@AllArgsConstructor
// annotation định nghĩa field mặc định của biến
@FieldDefaults(level = AccessLevel.PRIVATE) // mặc định là private nếu k tự định nghĩa
public class DistrictUtil {
    String DISTRICT_ID;
    String DISTRICT_VALUE;
    String DISTRICT_NAME;
    String PROVINCE_ID;
}
