package com.solution.xpresss_assessment.customer.services;

import com.solution.xpresss_assessment.airtime.data.dtos.AirtimePurchaseResponse;
import com.solution.xpresss_assessment.airtime.data.dtos.PurchaseAirtimeRequestDTO;
import com.solution.xpresss_assessment.airtime.service.AirtimePurchaseService;
import com.solution.xpresss_assessment.config.auth.data.models.Roles;
import com.solution.xpresss_assessment.config.auth.data.models.User;
import com.solution.xpresss_assessment.config.auth.services.UserService;
import com.solution.xpresss_assessment.customer.data.dtos.*;
import com.solution.xpresss_assessment.customer.data.models.Customer;
import com.solution.xpresss_assessment.customer.data.repositories.CustomerRepository;
import com.solution.xpresss_assessment.exceptions.UserNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collections;

@Slf4j
@Service
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final AirtimePurchaseService airtimePurchaseService;


    @Override    //the method that register user
    public CustomerResponse signUp(CustomerRegisterRequest request) {
        //customer object is created and initialized with its field parameters
        Customer customer = Customer.builder()
                .user(User.builder()    //user object is created inside the customer object to manage memory
                        .fullName(request.getFullName())
                        .emailAddress(request.getEmailAddress())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .isEnabled(false)
                        .roles(Collections.singleton(Roles.CUSTOMER))
                        .build())
                .phoneNumber(request.getPhoneNumber())
                .balance(BigDecimal.ZERO)
                .build();
        customerRepository.save(customer);
        return CustomerResponse.builder()
                .message("Successful! Check your mail to verify")
                .build();
    }

    @Override
    public LoginResponse singIn(LoginRequest requestDto) {
        return userService.singIn(requestDto);
    }


    @Override
    public AirtimePurchaseResponse buyAirtime(PurchaseAirtimeRequestDTO requestDTO) throws IOException {
        //this method is called when user wants to purchase airtime
        return airtimePurchaseService.buyAirtime(requestDTO);
    }

    @Override
    public AirtimePurchaseResponse buyMyselfAirtime(MyAirtimeRequestDTO requestDTO) throws IOException {
        Customer customer = currentCustomer();
        return airtimePurchaseService.buyAirtime(
                PurchaseAirtimeRequestDTO.builder()
                        .amount(requestDTO.getAmount())
                        .phoneNumber(customer.getPhoneNumber())
                        .userId(customer.getUser().getId())
                        .build()
        );
    }

    @Override
    public Customer currentCustomer() {
        return customerRepository.findCustomerByUser(
                userService.getCurrentUser()
        ).orElseThrow(UserNotFoundException::new);
    }
}
