package com.solution.xpresss_assessment.customer.data.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.solution.xpresss_assessment.Utilities.Constants.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    @NotBlank(message = EMAIL_REQUIRED)
    @Email(message = INVALID_EMAIL)
    private String email;

    @NotBlank(message = PASSWORD_REQUIRED)
    @Size(min = 6, max = 30)
    private String password;

}
