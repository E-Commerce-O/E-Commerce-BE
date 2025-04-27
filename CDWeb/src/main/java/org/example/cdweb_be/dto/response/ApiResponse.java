package org.example.cdweb_be.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;
import lombok.experimental.FieldDefaults;

// annotation tạo getter và setter cho các field private, tự tạo hàm toString, equals ...
@Data
// annotation giúp khởi tại đối tượng
@Builder
// annotation tạo constructor
@NoArgsConstructor
@AllArgsConstructor
// annotation định nghĩa field mặc định của biến
@FieldDefaults(level = AccessLevel.PRIVATE) // mặc định là private nếu k tự định nghĩa
// annotation thể hiện là 1 bảng trong db
//@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "code", "success", "data" }) // Đặt thứ tự các trường
public class ApiResponse<T> {
    int code = 200;
    boolean isSuccess = true;
    T data;

    public ApiResponse(T data) {
        this.data = data;
    }
}
