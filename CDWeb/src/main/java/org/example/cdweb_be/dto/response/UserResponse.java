package org.example.cdweb_be.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.sql.Date;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level =  AccessLevel.PRIVATE)
public class UserResponse {
    long id;
    String username;
    String phoneNumber;
    String email;
    String fullName;
    String avtPath;
    Date dateOfBirth;
    int gender;
    String role;
    Timestamp createdAt;
    Timestamp updatedAt;
}
