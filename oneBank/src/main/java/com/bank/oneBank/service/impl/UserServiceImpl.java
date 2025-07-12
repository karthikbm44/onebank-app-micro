package com.bank.oneBank.service.impl;

import com.bank.oneBank.dto.*;
import com.bank.oneBank.entity.Address;
import com.bank.oneBank.entity.User;
import com.bank.oneBank.exception.BusinessExecption;
import com.bank.oneBank.repository.UserRepository;
import com.bank.oneBank.service.EmailService;
import com.bank.oneBank.service.UserService;
import com.bank.oneBank.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository  userRepository;

    @Autowired
    private EmailService emailService;

    @Override
    public BankResponse createAccount(UserRequest userRequest) {

        if(userRepository.existsByEmail(userRequest.getEmail())){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_RESPONSE_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_RESPONSE_MESSAGE)
                    .build();
        }
        else{
        User newUser = User.builder()
                .firstName(userRequest.getFirstName())
                .middleName(userRequest.getMiddleName())
                .lastName(userRequest.getLastName())
                .gender(userRequest.getGender())
                .address(Address.builder()
                        .houseNoOrName(userRequest.getAddress().getHouseNoOrName())
                        .addressLine1(userRequest.getAddress().getAddressLine1())
                        .addressLine2(userRequest.getAddress().getAddressLine2())
                        .city(userRequest.getAddress().getCity())
                        .state(userRequest.getAddress().getState())
                        .pinCode(userRequest.getAddress().getPinCode())
                        .build())
                .accountNumber(AccountUtils.generateAccountNumber())
                .accountBalance(BigDecimal.ZERO)
                .email(userRequest.getEmail())
                .phoneNumber(userRequest.getPhoneNumber())
                .alternativePhoneNumber(userRequest.getAlternativePhoneNumber())
                .status("ACTIVE")
                .build();

        User savedUser = userRepository.save(newUser);

        //sending mail to the user once the account created
            EmailDetails emailDetails =EmailDetails.builder()
                    .recipient(savedUser.getEmail())
                    .subject("ACCOUNT CREATION")
                    .messageBody("Congratulations!!! Your Account has been Successfully Created.\nWelcome to One Bank Family.\nYour Account Details: \n" +
                            "Account Name : "+ savedUser.getFirstName() + " "+ savedUser.getMiddleName()+" " +savedUser.getLastName()+
                            "\nAccount Number : "+ savedUser.getAccountNumber()+"\n\nSincerely,\nTeam One Bank\n\nThis is an auto generated email please do not reply.")
                    .build();

            emailService.sendEmailAlerts(emailDetails);



        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREATION_RESPONSE_CODE)
                .responseMessage(AccountUtils.ACCOUNT_CREATION_RESPONSE_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountName(savedUser.getFirstName()+ " " +savedUser.getMiddleName()+" "+ savedUser.getLastName())
                        .accountNumber(savedUser.getAccountNumber())
                        .accountBalance(savedUser.getAccountBalance())
                        .build())
                .build();
        }

    }

    @Override
    public BankResponse balanceEnquiry(EnquiryRequest enquiryRequest) throws BusinessExecption {

        Boolean isAccountExists = userRepository.existsByAccountNumber(enquiryRequest.getAccountNumber());
            if (!isAccountExists) {
               throw new BusinessExecption(UUID.randomUUID().toString(),"User Not Found");
            }
        User foundUser = userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_FOUND_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_FOUND_MESSAGE)
                    .accountInfo(AccountInfo.builder()
                            .accountBalance(foundUser.getAccountBalance())
                            .accountNumber(foundUser.getAccountNumber())
                            .accountName(foundUser.getFirstName() + " " + foundUser.getMiddleName() + " " + foundUser.getLastName())
                            .build())
                    .build();

    }


    @Override
    public String nameEnquiry(EnquiryRequest enquiryRequest) {
        Boolean isAccountExists = userRepository.existsByAccountNumber(enquiryRequest.getAccountNumber());
        if (!isAccountExists){
            return AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE;
        }
        else{
            User foundUser = userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());
            return foundUser.getFirstName() + " " +foundUser.getMiddleName()+" "+ foundUser.getLastName();
        }
    }

    @Override
    public UserDetailsDto userDetailsRequest(String accountNumber) {

        User userDetails = userRepository.findByAccountNumber(accountNumber);

        return UserDetailsDto.builder()
                .accountHolderName(userDetails.getFirstName()+" "+userDetails.getMiddleName()+" "+userDetails.getLastName())
                .address(AddressDto.builder()
                        .houseNoOrName(userDetails.getAddress().getHouseNoOrName())
                        .addressLine1(userDetails.getAddress().getAddressLine1())
                        .addressLine2(userDetails.getAddress().getAddressLine2())
                        .city(userDetails.getAddress().getCity())
                        .pinCode(userDetails.getAddress().getPinCode())
                        .state(userDetails.getAddress().getState())
                        .build())
                .mobileNumber(userDetails.getPhoneNumber())
                .emailId(userDetails.getEmail())
                .accountNumber(userDetails.getAccountNumber())
                .build();

    }


}
