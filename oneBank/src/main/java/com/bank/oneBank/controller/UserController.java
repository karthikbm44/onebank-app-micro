package com.bank.oneBank.controller;

import com.bank.oneBank.dto.BankResponse;
import com.bank.oneBank.dto.EnquiryRequest;
import com.bank.oneBank.dto.UserDetailsDto;
import com.bank.oneBank.dto.UserRequest;
import com.bank.oneBank.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

@RestController
@RequestMapping("api/user")
@Tag(name = "User Account Management APIs")
public class UserController {

    @Autowired
    private UserService  userService;

    @Operation(
            summary = "Create New User Account",
            description = "Creating new bank account for a user and assigned with new account number.Email will be sent to the user to update on account creation status."
    )
    @ApiResponse(
            responseCode = "201",
            description = "HTTP Status 201 Created"
    )
    @PostMapping("/createAccount")
    public BankResponse createAccount(@RequestBody UserRequest userRequest){
        return userService.createAccount(userRequest);
    }

    @Operation(
            summary = "Balance Enquiry",
            description = "Check the account balance using the account number assigned to the user"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP Status 200 Success"
    )
    @PostMapping("/balanceEnquiry")
    public BankResponse balanceEnquiry(@RequestBody EnquiryRequest enquiryRequest){
        return userService.balanceEnquiry(enquiryRequest);
    }


    @Operation(
            summary = "Name Enquiry",
            description = "Check the account holder name using the account number assigned to the user"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP Status 200 Success"
    )
    @GetMapping("/nameEnquiry")
    public String nameEnquiry(@RequestBody EnquiryRequest enquiryRequest){
        return userService.nameEnquiry(enquiryRequest);
    }

    @GetMapping("/userDetails")
    public UserDetailsDto userDetails(@RequestParam String accountNumber){
        return userService.userDetailsRequest(accountNumber);
    }


}
