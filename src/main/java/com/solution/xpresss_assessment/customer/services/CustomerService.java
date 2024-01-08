package com.solution.xpresss_assessment.customer.services;


import com.solution.xpresss_assessment.airtime.data.dtos.AirtimePurchaseResponse;
import com.solution.xpresss_assessment.airtime.data.dtos.PurchaseAirtimeRequestDTO;
import com.solution.xpresss_assessment.customer.data.dtos.*;
import com.solution.xpresss_assessment.customer.data.models.Customer;

import java.io.IOException;

public interface CustomerService {

    CustomerResponse signUp(CustomerRegisterRequest request);

    LoginResponse singIn(LoginRequest requestDto);

    AirtimePurchaseResponse buyAirtime(PurchaseAirtimeRequestDTO requestDTO) throws IOException;

    AirtimePurchaseResponse buyMyselfAirtime(MyAirtimeRequestDTO requestDTO) throws IOException;

    Customer currentCustomer();
}
