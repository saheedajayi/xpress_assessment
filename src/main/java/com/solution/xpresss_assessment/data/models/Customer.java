package com.solution.xpresss_assessment.data.models;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

    @Setter
    @Getter
    @Builder
    @Entity
    @NoArgsConstructor
    @AllArgsConstructor
    @Table(name = "customer")
    public class Customer {

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private Long id;

        @OneToOne(cascade = CascadeType.ALL)
        private User user;

        private String phoneNumber;

        private BigDecimal balance;
    }

