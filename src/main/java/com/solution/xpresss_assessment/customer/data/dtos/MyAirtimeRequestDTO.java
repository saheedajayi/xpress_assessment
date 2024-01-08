package com.solution.xpresss_assessment.customer.data.dtos;

import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyAirtimeRequestDTO {

    private BigDecimal amount;
}
