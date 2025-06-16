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
@FieldDefaults(level = AccessLevel.PROTECTED)
public class UserUpdateRequest {
//    @NotNull
//    @Size(min = 6, message = "PASSWORD_INVALID")
//    String password;
    @NotNull
    @Email(message = "EMAIL_INVALID")
    String email;
    @PhoneNumberConstraint(message = "PHONENUMBER_INVALID")
    @NotNull
    String phoneNumber;
    String fullName;
    String avtPath;
    Date dateOfBirth;
    int gender;
}
