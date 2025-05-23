package com.eazybytes.accounts.service.openfeing;

import com.eazybytes.accounts.dto.CardsDto;
import jakarta.validation.constraints.Pattern;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "cards", fallback = CardsFallback.class)
public interface CardsFeingClient {

    @GetMapping("api/fetch")
    public ResponseEntity<CardsDto> fetchCardDetails(@RequestHeader("correlation-id") String correlationId, @RequestParam String mobileNumber);
}
