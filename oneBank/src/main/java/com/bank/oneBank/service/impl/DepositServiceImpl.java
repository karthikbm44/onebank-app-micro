package com.bank.oneBank.service.impl;

import com.bank.oneBank.dto.*;
import com.bank.oneBank.entity.User;
import com.bank.oneBank.repository.AddressRepository;
import com.bank.oneBank.repository.UserRepository;
import com.bank.oneBank.service.DepositService;
import com.bank.oneBank.service.EmailService;
import com.bank.oneBank.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class DepositServiceImpl implements DepositService {

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
            User userToDeposit = userRepository.findByAccountNumber(request.getAccountNumber());
            userToDeposit.setAccountBalance(userToDeposit.getAccountBalance().add(request.getAmount()));
            userRepository.save(userToDeposit);

            TransactionRequest transactionRequest = TransactionRequest.builder()
                    .accountNumber(userToDeposit.getAccountNumber())
                    .creditAmount(request.getAmount())
                    .debitAmount(BigDecimal.ZERO)
                    .currentBalance(userToDeposit.getAccountBalance().toString())
                    .build();

            //call the saveTransaction API from the transaction microservice
            webClient.post()
                    .uri("/saveTransaction")
                    .bodyValue(transactionRequest)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();


            LocalDate date = LocalDate.now();
            String accountNumberString = request.getAccountNumber().substring(7);

            EmailDetails emailDetails =EmailDetails.builder()
                    .recipient(userToDeposit.getEmail())
                    .subject("Transaction Alert for Online Banking")
                    .messageBody("Dear Customer,\n\nOne Bank Account XXX"+accountNumberString+" is credited with INR "+request.getAmount()+" on "+date+".\nYour Available Balance: "+userToDeposit.getAccountBalance()+"\n\nSincerely,\nTeam One Bank\n\nThis is an auto generated email please do not reply.")
                    .build();

            emailService.sendEmailAlerts(emailDetails);


            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_CREDIT_SUCCESS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_CREDIT_SUCCESS_MESSAGE)
                    .accountInfo(AccountInfo.builder()
                            .accountName(userToDeposit.getFirstName()+" "+userToDeposit.getMiddleName()+" "+userToDeposit.getLastName())
                            .accountNumber(request.getAccountNumber())
                            .accountBalance(userToDeposit.getAccountBalance())
                            .build())
                    .build();
        }
    }
}
