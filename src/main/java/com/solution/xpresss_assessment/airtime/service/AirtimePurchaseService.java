package com.solution.xpresss_assessment.airtime.service;


import com.solution.xpresss_assessment.airtime.data.dtos.AirtimePurchaseResponse;
import com.solution.xpresss_assessment.airtime.data.dtos.PurchaseAirtimeRequestDTO;

import java.io.IOException;

public interface AirtimePurchaseService {
  AirtimePurchaseResponse buyAirtime(PurchaseAirtimeRequestDTO requestDTO) throws IOException;
}
