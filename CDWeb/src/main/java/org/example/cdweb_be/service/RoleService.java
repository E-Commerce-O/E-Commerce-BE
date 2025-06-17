package org.example.cdweb_be.service;

import org.example.cdweb_be.enums.Role;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleService {
    public static List<String> allRoles() {
        List<String> result = new ArrayList<>();
        for (Role role : Role.values()) {
            if (!role.equals(Role.USER)) {
                result.add(role.name());
            }
        }
        return result;
    }
    public static List<String> exceptUser() {
        List<String> result = new ArrayList<>();
        for (Role role : Role.values()) {
                result.add(role.name());
        }
        return result;
    }
    public static String admin(){
        return Role.ADMIN.name();
    }
    public static String employee(){
        return Role.EMPLOYEE.name();
    }
    public static String user(){
        return Role.USER.name();
    }

}
