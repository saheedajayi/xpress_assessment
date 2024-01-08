package com.solution.xpresss_assessment.admin;

import com.solution.xpresss_assessment.config.auth.services.UserService;
import com.solution.xpresss_assessment.customer.data.dtos.LoginRequest;
import com.solution.xpresss_assessment.customer.data.dtos.LoginResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Tag(name = "Admin Controller")
public class AdminController {

    private UserService userService;

    @PostMapping("login")
    public ResponseEntity<LoginResponse> signIn(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(userService.singIn(request));
    }
}
