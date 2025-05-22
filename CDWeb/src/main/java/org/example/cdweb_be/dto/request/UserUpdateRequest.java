package org.example.cdweb_be.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.cdweb_be.validator.PhoneNumberConstraint;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {
    @NotNull
    @Size(min = 6, message = "PASSWORD_INVALID")
    @NotNull
    String password;
//    @Email(message = "EMAIL_INVALID")
//    @NotNull
    String email;
    @PhoneNumberConstraint(message = "PHONENUMER_INVALID")
    @NotNull
    String phoneNumber;
    String fullName;
    String avtPath;
    Date dateOfBirth;
    int gender;
}
