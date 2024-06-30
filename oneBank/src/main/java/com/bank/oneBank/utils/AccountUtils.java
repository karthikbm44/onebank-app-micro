package com.bank.oneBank.utils;

import java.time.LocalDate;

public class AccountUtils {

    public static final String ACCOUNT_RESPONSE_CODE = "001";
    public static final String ACCOUNT_RESPONSE_MESSAGE="This user already have an existing account!";
    public static final String ACCOUNT_CREATION_RESPONSE_CODE="002";
    public static final String ACCOUNT_CREATION_RESPONSE_MESSAGE="Your account has been created successfully!";
    public static final String ACCOUNT_NOT_EXIST_CODE="003";
    public static final String ACCOUNT_NOT_EXIST_MESSAGE="User with the provided Account Number dose not exist";
    public static final String ACCOUNT_FOUND_CODE="004";
    public static final String ACCOUNT_FOUND_MESSAGE="User Account Found";
    public static final String ACCOUNT_CREDIT_SUCCESS_CODE="005";
    public static final String ACCOUNT_CREDIT_SUCCESS_MESSAGE="Amount Credited Successfully";
    public static final String ACCOUNT_DEBIT_SUCCESS_CODE="006";
    public static final String ACCOUNT_DEBIT_SUCCESS_CODE_MESSAGE="Amount Debited Successfully";
    public static final String INSUFFICIENT_BALANCE_CODE="007";
    public static final String INSUFFICIENT_BALANCE_CODE_MESSAGE="Insufficient Balance";
    public static final String TRANSFER_SUCCESS_CODE="008";
    public static final String TRANSFER_SUCCESS_CODE_MESSAGE="Transfer Successful";
    public static final String DESTINATION_ACCOUNT_NOT_FOUND_CODE="009";
    public static final String DESTINATION_ACCOUNT_NOT_FOUND_CODE_MESSAGE="Destination Account not found";

    public static String generateAccountNumber(){

        int min = 100000;
        int max = 999999;
        int randomNumber = (int) Math.floor(Math.random()*(max-min+1)+min);
        System.out.println(randomNumber);

        String year = String.valueOf(LocalDate.now().getYear());
        String randomNumberString = String.valueOf(randomNumber);

        StringBuilder accountNumber = new StringBuilder();

        return accountNumber.append(year).append(randomNumberString).toString();



    }
}
