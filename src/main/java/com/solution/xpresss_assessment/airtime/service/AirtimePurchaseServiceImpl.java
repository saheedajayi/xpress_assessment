package com.solution.xpresss_assessment.airtime.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.solution.xpresss_assessment.airtime.data.dtos.AirtimePurchaseResponse;
import com.solution.xpresss_assessment.airtime.data.dtos.Details;
import com.solution.xpresss_assessment.airtime.data.dtos.PurchaseAirtimeRequestDTO;
import com.solution.xpresss_assessment.airtime.data.dtos.XpressAPIRequestDTO;
import com.solution.xpresss_assessment.airtime.data.models.AirtimePurchase;
import com.solution.xpresss_assessment.airtime.data.models.Status;
import com.solution.xpresss_assessment.airtime.data.repository.AirtimePurchaseRepository;
import com.solution.xpresss_assessment.config.auth.data.models.User;
import com.solution.xpresss_assessment.config.auth.services.UserService;
import com.solution.xpresss_assessment.exceptions.XpressException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class AirtimePurchaseServiceImpl implements AirtimePurchaseService {
    @Value("${private_key}")
    private String privateKey;
    @Value("${public_key}")
    private String publicKey;
    @Value("${base_url}")
    private String url;
    @Value("${MTN_UNIQUE_CODE}")
    private String mtnUniqueCode;
    @Value("${GLO_UNIQUE_CODE}")
    private String gloUniqueCode;
    @Value("${AIRTEL_UNIQUE_CODE}")
    private String airtelUniqueCode;
    @Value("${ETISALAT_UNIQUE_CODE}")
    private String etisalatUniqueCode;

    private final AirtimePurchaseRepository airtimePurchaseRepository;
    private final UserService userService;

    @Override
    public AirtimePurchaseResponse buyAirtime(PurchaseAirtimeRequestDTO requestDTO) throws IOException {
        log.info("Airtime Request with Payload {}", requestDTO);

        User user = userService.findUserById(requestDTO.getUserId());
        BigDecimal amount = requestDTO.getAmount();
        String uniqueCode = uniqueCode(requestDTO.getPhoneNumber());

        AirtimePurchase airtimePurchase = buildAirtimePurchase(requestDTO, user, amount, uniqueCode);
        AirtimePurchase savedAirtimePurchase = airtimePurchaseRepository.save(airtimePurchase);

        XpressAPIRequestDTO xpressAPIRequestDTO = buildXpressAPIRequestDTO(savedAirtimePurchase);

        return callToXpressAPI(xpressAPIRequestDTO);
    }

    private AirtimePurchase buildAirtimePurchase(
            PurchaseAirtimeRequestDTO requestDTO,
            User user, BigDecimal amount, String uniqueCode) {
        return AirtimePurchase.builder()
                .phoneNumber(requestDTO.getPhoneNumber())
                .amount(amount)
                .uniqueCode(uniqueCode)
                .user(user)
                .transactionTime(LocalDateTime.now())
                .status(Status.PENDING)
                .build();
    }

    private XpressAPIRequestDTO buildXpressAPIRequestDTO(AirtimePurchase savedAirtimePurchase) {

        String phoneNumber = savedAirtimePurchase.getPhoneNumber();
        return XpressAPIRequestDTO.builder()
                .uniqueCode(savedAirtimePurchase.getUniqueCode())
                .details(
                        Details.builder()
                                .amount(savedAirtimePurchase.getAmount())
                                .phoneNumber(phoneNumber)
                                .build()
                ).build();
    }


    private String uniqueCode(String phoneNumber) {
        switch (phoneNumber.substring(0, 4)) {
            case "0803", "0806", "0703", "0706", "0813", "0816", "0810", "0814" -> {
                return mtnUniqueCode;
            }
            case "0802", "0808", "0708", "0812" -> {
                return airtelUniqueCode;
            }
            case "0809", "0818", "0817", "0909" -> {
                return etisalatUniqueCode;
            }
            case "0805", "0807", "0705", "0815", "0811" -> {
                return gloUniqueCode;
            }
        }
        throw new XpressException("Invalid number");
    }



    private AirtimePurchaseResponse callToXpressAPI(XpressAPIRequestDTO xpressAPIRequestDTO) throws IOException {
        String jsonString = new ObjectMapper().writeValueAsString(xpressAPIRequestDTO);
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        String PaymentHash = calculateHMAC512(jsonString, privateKey);
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, jsonString);

        Request request = new Request.Builder()
                .url(url)
                .method("POST", body)
                .addHeader("Authorization", publicKey)
                .addHeader("PaymentHash", PaymentHash)
                .addHeader("channel", "api")
                .build();

        Response response = client.newCall(request).execute();
        ResponseBody responseBody = response.body();

        ObjectMapper objectMapper = new ObjectMapper();
        assert responseBody != null;
        AirtimePurchaseResponse airtimePurchaseResponse = objectMapper.readValue(
                responseBody.string(),
                AirtimePurchaseResponse.class
        );
        response.close();
        return airtimePurchaseResponse;
    }

      public  String calculateHMAC512(String data, String key) {
        String HMAC_SHA512 = "HmacSHA512";
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(
                    key.getBytes(StandardCharsets.UTF_8),
                    HMAC_SHA512
            );
            Mac mac = Mac.getInstance(HMAC_SHA512);
            mac.init(secretKeySpec);
            return String.valueOf(
                    Hex.encode(
                            mac.doFinal(
                                    data.getBytes(StandardCharsets.UTF_8)
                            )
                    )
            );
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
}