package com.bank.oneBank.service;

import com.bank.oneBank.dto.BankResponse;
import com.bank.oneBank.dto.CreditDebitRequest;

public interface DepositService {

    BankResponse depositAmount(CreditDebitRequest request);
}
