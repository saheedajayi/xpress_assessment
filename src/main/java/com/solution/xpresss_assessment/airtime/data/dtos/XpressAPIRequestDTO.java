package com.solution.xpresss_assessment.airtime.data.dtos;

import lombok.*;

import java.security.SecureRandom;
import java.util.Base64;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class XpressAPIRequestDTO {

    private final String requestId = generateToken(10);

    private String uniqueCode;

    private Details details;

    public String generateToken(int length) {
        byte[] bytes = new byte[length];
        new SecureRandom().nextBytes(bytes);
        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(bytes);
    }
}
