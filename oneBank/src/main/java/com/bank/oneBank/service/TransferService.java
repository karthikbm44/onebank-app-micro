package com.bank.oneBank.service;

import com.bank.oneBank.dto.BankResponse;
import com.bank.oneBank.dto.TransferRequest;

public interface TransferService {
    BankResponse transfer(TransferRequest request);
}
