package com.eazybytes.accounts.service.openfeing;

import com.eazybytes.accounts.dto.CardsDto;
import com.eazybytes.accounts.dto.LoansDto;
import jakarta.validation.constraints.Pattern;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "loans", fallback = LoansFallback.class)
public interface LoansFeingClient {

    @GetMapping("api/fetch")
    public ResponseEntity<LoansDto> fetchLoanDetails(@RequestHeader("correlation-id") String correlationId, @RequestParam String mobileNumber);
}
