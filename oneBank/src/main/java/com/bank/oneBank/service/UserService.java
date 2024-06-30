package com.bank.oneBank.service;

import com.bank.oneBank.dto.BankResponse;
import com.bank.oneBank.dto.EnquiryRequest;
import com.bank.oneBank.dto.UserDetailsDto;
import com.bank.oneBank.dto.UserRequest;

public interface UserService {

    BankResponse createAccount(UserRequest userRequest);

    BankResponse balanceEnquiry(EnquiryRequest enquiryRequest);

    String nameEnquiry(EnquiryRequest enquiryRequest);

    UserDetailsDto userDetailsRequest(String accountNumber);
}
