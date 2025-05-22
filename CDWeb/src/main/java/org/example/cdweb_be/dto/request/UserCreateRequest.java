package org.example.cdweb_be.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.cdweb_be.validator.CustomEmail;
import org.example.cdweb_be.validator.PhoneNumberConstraint;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreateRequest {
    @Size(min = 6, message = "USERNAME_INVALID")
    String username;
    @Size(min = 6, message = "PASSWORD_INVALID")
    String password;
//    @CustomEmail(message = "EMAIL_INVALID")
    @Email(message = "EMAIL_INVALID")
    String email;
    @PhoneNumberConstraint(message = "PHONENUMER_INVALID")
    String phoneNumber;

}
