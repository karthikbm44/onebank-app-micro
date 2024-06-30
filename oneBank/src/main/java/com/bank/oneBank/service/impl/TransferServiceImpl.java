package com.bank.oneBank.service.impl;

import com.bank.oneBank.dto.*;
import com.bank.oneBank.entity.User;
import com.bank.oneBank.repository.UserRepository;
import com.bank.oneBank.service.EmailService;
import com.bank.oneBank.service.TransferService;
import com.bank.oneBank.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class TransferServiceImpl implements TransferService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailService emailService;

    @Autowired
    private WebClient webClient;

    @Override
    public BankResponse transfer(TransferRequest request) {

        Boolean isDestinationAccountExists = userRepository.existsByAccountNumber(request.getDestinationAccountNumber());

        if(!isDestinationAccountExists){
            return BankResponse.builder()
                    .responseCode(AccountUtils.DESTINATION_ACCOUNT_NOT_FOUND_CODE)
                    .responseMessage(AccountUtils.DESTINATION_ACCOUNT_NOT_FOUND_CODE_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        User sourceAccountUser = userRepository.findByAccountNumber(request.getSourceAccountNumber());
        //check the request amount is greater than the source account balanace.
        if (request.getAmount().compareTo(sourceAccountUser.getAccountBalance()) > 0){

            EmailDetails emailDetails =EmailDetails.builder()
                    .recipient(sourceAccountUser.getEmail())
                    .subject("Transaction Failed")
                    .messageBody("Dear Customer,\n\nYour transaction of INR "+request.getAmount()+" failed due to insufficient fund."+"\nYour Available Balance: "+sourceAccountUser.getAccountBalance()+"\n\nSincerely,\nTeam One Bank\n\nThis is an auto generated email please do not reply.")
                    .build();

            emailService.sendEmailAlerts(emailDetails);

            return BankResponse.builder()
                    .responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
                    .responseMessage(AccountUtils.INSUFFICIENT_BALANCE_CODE_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        sourceAccountUser.setAccountBalance(sourceAccountUser.getAccountBalance().subtract(request.getAmount()));
        userRepository.save(sourceAccountUser);

        TransactionRequest sourceTransactionRequest = TransactionRequest.builder()
                .accountNumber(sourceAccountUser.getAccountNumber())
                .debitAmount(request.getAmount())
                .creditAmount(BigDecimal.ZERO)
                .currentBalance(sourceAccountUser.getAccountBalance().toString())
                .build();

        webClient.post()
                .uri("/saveTransaction")
                .bodyValue(sourceTransactionRequest)
                .retrieve()
                .bodyToMono(Void.class)
                .block();

        //send mail to them who sent the money
        LocalDate sourceDate = LocalDate.now();
        String sourceAccountNumberString = request.getSourceAccountNumber().substring(7);

        EmailDetails sourceEmailDetails =EmailDetails.builder()
                .recipient(sourceAccountUser.getEmail())
                .subject("Debited Amount")
                .messageBody("Dear Customer,\n\nOne Bank Account XXX"+sourceAccountNumberString+" debited for INR "+request.getAmount()+" on "+sourceDate+".\nYour Available Balance: "+sourceAccountUser.getAccountBalance()+"\n\nSincerely,\nTeam One Bank\n\nThis is an auto generated email please do not reply.")
                .build();

        emailService.sendEmailAlerts(sourceEmailDetails);

        User destinationAccountUser = userRepository.findByAccountNumber(request.getDestinationAccountNumber());
        destinationAccountUser.setAccountBalance(destinationAccountUser.getAccountBalance().add(request.getAmount()));
        userRepository.save(destinationAccountUser);

        TransactionRequest destinationTransactionRequest = TransactionRequest.builder()
                .accountNumber(destinationAccountUser.getAccountNumber())
                .creditAmount(request.getAmount())
                .debitAmount(BigDecimal.ZERO)
                .currentBalance(destinationAccountUser.getAccountBalance().toString())
                .build();

        webClient.post()
                .uri("/saveTransaction")
                .bodyValue(destinationTransactionRequest)
                .retrieve()
                .bodyToMono(Void.class)
                .block();

        //send mail to who received the money
        LocalDate destinationDate = LocalDate.now();
        String destinationAccountNumberString = request.getDestinationAccountNumber().substring(7);

        EmailDetails destinationEmailDetails =EmailDetails.builder()
                .recipient(destinationAccountUser.getEmail())
                .subject("Credited Amount")
                .messageBody("Dear Customer,\n\nOne Bank Account XXX"+destinationAccountNumberString+" is credited with INR "+request.getAmount()+" from "+ sourceAccountUser.getFirstName()+" "+sourceAccountUser.getMiddleName()+" "+sourceAccountUser.getLastName()+" on "+destinationDate+".\nYour Available Balance: "+destinationAccountUser.getAccountBalance()+"\n\nSincerely,\nTeam One Bank\n\nThis is an auto generated email please do not reply.")
                .build();

        emailService.sendEmailAlerts(destinationEmailDetails);

        return BankResponse.builder()
                .responseCode(AccountUtils.TRANSFER_SUCCESS_CODE)
                .responseMessage(AccountUtils.TRANSFER_SUCCESS_CODE_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountName(destinationAccountUser.getFirstName()+" "+destinationAccountUser.getMiddleName()+" "+destinationAccountUser.getLastName())
                        .accountNumber(request.getDestinationAccountNumber())
                        .accountBalance(destinationAccountUser.getAccountBalance())
                        .build())
                .build();


    }
}
