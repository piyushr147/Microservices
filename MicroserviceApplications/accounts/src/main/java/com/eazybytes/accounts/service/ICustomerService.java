package com.eazybytes.accounts.service;

import com.eazybytes.accounts.dto.CustomerDetailsDto;
import org.springframework.http.ResponseEntity;

public interface ICustomerService {

    public CustomerDetailsDto fetchCustomerDetails(String mobileNumber);
}
