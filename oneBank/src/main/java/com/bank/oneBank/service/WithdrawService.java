package com.bank.oneBank.service;

import com.bank.oneBank.dto.BankResponse;
import com.bank.oneBank.dto.CreditDebitRequest;

public interface WithdrawService {
    BankResponse depositAmount(CreditDebitRequest request);
}
