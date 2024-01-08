package com.solution.xpresss_assessment.config;

import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

@Configuration
public class AppConfig {

    @Value("${Jwt_Secret_Key}")
    private String jwtSecret;

    @Bean
    public Key getSecretKey() {
        return new SecretKeySpec(
                jwtSecret.getBytes(),
                SignatureAlgorithm.HS512.getJcaName());
    }


}
