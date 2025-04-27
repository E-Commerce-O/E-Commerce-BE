package org.example.cdweb_be.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.cdweb_be.entity.User;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderUser {
    long id;
    String username;
    String phoneNumber;
    String email;
    String fullName;
    public OrderUser(User user){
        this.id = user.getId();
        this.username = user.getUsername();
        this.phoneNumber = user.getPhoneNumber();
        this.email = user.getEmail();
        this.fullName = user.getFullName();
    }
}
