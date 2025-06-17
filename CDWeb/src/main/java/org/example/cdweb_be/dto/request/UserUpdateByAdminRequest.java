package org.example.cdweb_be.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateByAdminRequest extends UserUpdateRequest{
    String role;
}
