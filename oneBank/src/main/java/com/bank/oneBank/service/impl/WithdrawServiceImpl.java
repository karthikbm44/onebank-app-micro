package com.bank.oneBank.service.impl;

import com.bank.oneBank.dto.*;
import com.bank.oneBank.entity.User;
import com.bank.oneBank.repository.UserRepository;
import com.bank.oneBank.service.EmailService;
import com.bank.oneBank.service.WithdrawService;
import com.bank.oneBank.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;

@Service
public class WithdrawServiceImpl implements WithdrawService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailService emailService;

    @Autowired
    private WebClient webClient;

    @Override
    public BankResponse depositAmount(CreditDebitRequest request) {
        Boolean isAccountExists = userRepository.existsByAccountNumber(request.getAccountNumber());
        if (!isAccountExists){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();

        }
        else{
            User userToWithdraw = userRepository.findByAccountNumber(request.getAccountNumber());
            //The amount is in BigDecimal so we need to use BigInteger as a conversion  not int with parseInt method
            BigInteger existingBalance=userToWithdraw.getAccountBalance().toBigInteger();
            BigInteger requestAmount = request.getAmount().toBigInteger();

            LocalDate date = LocalDate.now();
            String accountNnumberString = request.getAccountNumber().substring(7);


            if(existingBalance.intValue() < requestAmount.intValue()){

                EmailDetails emailDetails =EmailDetails.builder()
                        .recipient(userToWithdraw.getEmail())
                        .subject("Transaction Failed")
                        .messageBody("Dear Customer,\n\nYour transaction of INR "+request.getAmount()+" failed due to insufficient fund."+"\nYour Available Balance: "+userToWithdraw.getAccountBalance()+"\n\nSincerely,\nTeam One Bank\n\nThis is an auto generated email please do not reply.")
                        .build();

                emailService.sendEmailAlerts(emailDetails);

                return BankResponse.builder()
                        .responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
                        .responseMessage(AccountUtils.INSUFFICIENT_BALANCE_CODE_MESSAGE)
                        .accountInfo(null)
                        .build();
            }else {
                userToWithdraw.setAccountBalance(userToWithdraw.getAccountBalance().subtract(request.getAmount()));
                userRepository.save(userToWithdraw);

                TransactionRequest transactionRequest= TransactionRequest.builder()
                        .accountNumber(userToWithdraw.getAccountNumber())
                        .debitAmount(request.getAmount())
                        .creditAmount(BigDecimal.ZERO)
                        .currentBalance(userToWithdraw.getAccountBalance().toString())
                        .build();

                webClient.post()
                        .uri("/saveTransaction")
                        .bodyValue(transactionRequest)
                        .retrieve()
                        .bodyToMono(Void.class)
                        .block();

                EmailDetails emailDetails =EmailDetails.builder()
                        .recipient(userToWithdraw.getEmail())
                        .subject("Transaction Alert for Online Banking")
                        .messageBody("Dear Customer,\n\nOne Bank Account XXX"+accountNnumberString+" debited for INR "+request.getAmount()+" on "+date+".\nYour Available Balance: "+userToWithdraw.getAccountBalance()+"\n\nSincerely,\nTeam One Bank\n\nThis is an auto generated email please do not reply.")
                        .build();

                emailService.sendEmailAlerts(emailDetails);

                return BankResponse.builder()
                        .responseCode(AccountUtils.ACCOUNT_DEBIT_SUCCESS_CODE)
                        .responseMessage(AccountUtils.ACCOUNT_DEBIT_SUCCESS_CODE_MESSAGE)
                        .accountInfo(AccountInfo.builder()
                                .accountName(userToWithdraw.getFirstName()+" "+userToWithdraw.getMiddleName()+" "+ userToWithdraw.getLastName())
                                .accountNumber(request.getAccountNumber())
                                .accountBalance(userToWithdraw.getAccountBalance())
                                .build())
                        .build();
            }

        }
    }
}
