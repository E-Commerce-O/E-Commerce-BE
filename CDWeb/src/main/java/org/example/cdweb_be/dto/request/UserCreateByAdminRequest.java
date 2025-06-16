package org.example.cdweb_be.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.cdweb_be.validator.PhoneNumberConstraint;

import java.sql.Date;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateByAdminRequest {
    @Size(min = 5, message = "USERNAME_INVALID")
    String username;
    @Size(min = 6, message = "PASSWORD_INVALID")
    String password;
    String fullName;
    Date dateOfBirth;
    @PhoneNumberConstraint(message = "PHONENUMBER_INVALID")
    String phoneNumber;
    @Email(message = "EMAIL_INVALID")
    String email;
    int gender;
    String role;
}
