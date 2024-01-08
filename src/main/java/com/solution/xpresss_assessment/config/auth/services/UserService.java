package com.solution.xpresss_assessment.config.auth.services;

import com.solution.xpresss_assessment.config.auth.data.models.User;
import com.solution.xpresss_assessment.customer.data.dtos.LoginRequest;
import com.solution.xpresss_assessment.customer.data.dtos.LoginResponse;

public interface UserService {

    LoginResponse singIn(LoginRequest requestDto);

    User getCurrentUser();

    User findUserById(Long userId);
}


