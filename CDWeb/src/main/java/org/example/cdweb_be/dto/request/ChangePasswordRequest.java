package org.example.cdweb_be.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChangePasswordRequest {
    @NonNull
    @Size(min = 6, message = "PASSWORD_INVALID")
    String oldPassword;
    @NonNull
    @Size(min = 6, message = "PASSWORD_INVALID")
    String newPassword;
    @NonNull
    @Size(min = 6, message = "PASSWORD_INVALID")
    String confirmPassword;

}
