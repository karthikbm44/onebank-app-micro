package com.bank.oneBank.service;

import com.bank.oneBank.dto.BankResponse;
import com.bank.oneBank.dto.EnquiryRequest;
import com.bank.oneBank.dto.UserDetailsDto;
import com.bank.oneBank.dto.UserRequest;
import com.bank.oneBank.exception.BusinessExecption;

public interface UserService {

    BankResponse createAccount(UserRequest userRequest);

    BankResponse balanceEnquiry(EnquiryRequest enquiryRequest) throws BusinessExecption;

    String nameEnquiry(EnquiryRequest enquiryRequest);

    UserDetailsDto userDetailsRequest(String accountNumber);
}
