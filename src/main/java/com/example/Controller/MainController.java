package com.example.Controller;

import com.example.Model.RequestDTO;
import com.example.Model.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

@RestController
public class MainController {

    private Logger log = LoggerFactory.getLogger(MainController.class);

    ObjectMapper mapper = new ObjectMapper();

    @PostMapping(
            value = "/info/postBalances",  // адрес до заглушки
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public Object postBalances(@RequestBody RequestDTO requestDTO) {
        try {
            String clientId = requestDTO.getClientId();
            char firstDigit = clientId.charAt(0);
            BigDecimal maxLimit;
            String currency_type;
            Random random = new Random();
            float randomValue;
            BigDecimal balance;

            if (firstDigit == '8') {
                maxLimit = new BigDecimal(2000);
                currency_type = "US";
                randomValue = random.nextFloat() * 2000;
            } else if (firstDigit == '9') {
                maxLimit = new BigDecimal(1000);
                currency_type = "EU";
                randomValue = random.nextFloat() * 1000;
            } else {
                maxLimit = new BigDecimal(10000);
                currency_type = "RUB";
                randomValue = random.nextFloat() * 10000;
            }

            balance = BigDecimal.valueOf(randomValue).setScale(2, RoundingMode.HALF_UP);
            ResponseDTO responseDTO = new ResponseDTO();
            responseDTO.setRqUID(requestDTO.getRqUID());
            responseDTO.setClientId(clientId);
            responseDTO.setAccount(requestDTO.getAccount());
            responseDTO.setCurrency(currency_type);
            responseDTO.setBalance(balance);
            responseDTO.setMaxLimit(maxLimit);

            log.info("********** RequestDTO ********** " + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(requestDTO));
            log.info("********** ResponseDTO ********** " + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(responseDTO));

            return responseDTO;

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}