package org.example.cdweb_be.configuration;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.cdweb_be.entity.User;
import org.example.cdweb_be.enums.Role;
import org.example.cdweb_be.respository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j // annotation để sử dụng log
public class ApplicationInitConfig {
    @Autowired // có thể dùng @RequiredArgsConstructor để thay thế
    PasswordEncoder passwordEncoder;
    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository){ // được chạy mỗi khi chương trình dc start
        return args -> {
            if(userRepository.findByUsername("admin").isEmpty()){
                User user = User.builder()
                        .username("admin")
                        .avtPath("https://i.imgur.com/W60xqJf.png")
                        .fullName("At Van Min")
                        .email("admin@min.ad")
                        .password(passwordEncoder.encode("123"))
                        .role(Role.ADMIN.name())
                        .build();
                userRepository.save(user);
                log.warn("Default admin user has been created with username: admin and password: 123");
            }
        };
    }

}
