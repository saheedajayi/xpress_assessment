package com.solution.xpresss_assessment.customer.data.dtos;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    private String token;
    private Long customerId;
    private boolean isSuccessful;
}
