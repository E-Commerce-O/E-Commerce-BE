package org.example.cdweb_be.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.example.cdweb_be.validator.PhoneNumberConstraint;

import java.sql.Date;
import java.sql.Timestamp;

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
@Table(name = "user", uniqueConstraints = @UniqueConstraint(columnNames = {"username", "email", "phone_number"}))
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    String username;
    String password;
    String fullName;
    String avtPath;
    Date dateOfBirth;
    String phoneNumber;
    String email;
    int gender;
    String role;
    Timestamp createdAt;
    Timestamp updatedAt;

}
