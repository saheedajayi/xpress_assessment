package com.solution.xpresss_assessment.config.auth.services;

import com.solution.xpresss_assessment.config.AuthenticatedUser;
import com.solution.xpresss_assessment.config.JwtService;
import com.solution.xpresss_assessment.config.auth.data.models.User;
import com.solution.xpresss_assessment.config.auth.data.repositories.UserRepository;
import com.solution.xpresss_assessment.customer.data.dtos.LoginRequest;
import com.solution.xpresss_assessment.customer.data.dtos.LoginResponse;
import com.solution.xpresss_assessment.exceptions.UserNotFoundException;
import com.solution.xpresss_assessment.exceptions.XpressException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    public LoginResponse singIn(LoginRequest requestDto) {
        log.info("Attempt to login by {}", requestDto.getEmail());
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDto.getEmail(),
                            requestDto.getPassword()
                    )
            );
            Map<String, Object> claims = authentication.getAuthorities().stream()
                    .collect(
                            Collectors.toMap(
                                    authority -> "claim",
                                    Function.identity()
                            )
                    );
            log.info("Authentication successful for {}, generating token...", requestDto.getEmail());
            AuthenticatedUser authenticatedUser = (AuthenticatedUser) authentication.getPrincipal();
            String token = jwtService.generateAccessToken(claims, requestDto.getEmail());
            return LoginResponse.builder()
                    .customerId(authenticatedUser.getUser().getId())
                    .isSuccessful(true)
                    .token(token)
                    .build();
        } catch (Exception e) {
            throw new XpressException("Invalid login details");
        }
    }

    @Override    //this gets the current user logged in at a particular time instead of using id to find user
    public User getCurrentUser() {
        try {
            final AuthenticatedUser authenticatedUser =
                    (AuthenticatedUser) SecurityContextHolder
                            .getContext()
                            .getAuthentication()
                            .getPrincipal();
            return authenticatedUser.getUser();
        } catch (Exception ex) {
            throw new UserNotFoundException();
        }
    }

    @Override
    public User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
    }
}
