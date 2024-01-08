package com.solution.xpresss_assessment.admin;

import com.solution.xpresss_assessment.config.auth.data.models.Roles;
import com.solution.xpresss_assessment.config.auth.data.models.User;
import com.solution.xpresss_assessment.config.auth.data.repositories.UserRepository;
import com.solution.xpresss_assessment.config.auth.services.UserService;
import com.solution.xpresss_assessment.customer.data.dtos.LoginRequest;
import com.solution.xpresss_assessment.customer.data.dtos.LoginResponse;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Slf4j
@Service
@AllArgsConstructor
public class AdminService {
    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    private void createAdmin() {
        if (userRepository.findAll().isEmpty()) {
            User user = User.builder()
                    .emailAddress("admin@gmail.com")
                    .fullName("Super Admin")
                    .password(passwordEncoder.encode("12345"))
                    .roles(Collections.singleton(Roles.ADMIN))
                    .isEnabled(true)
                    .build();
            userRepository.save(user);
            log.info("Admin created successfully");
        }
    }

    public LoginResponse singIn(LoginRequest requestDto) {
        return userService.singIn(requestDto);
    }
}
