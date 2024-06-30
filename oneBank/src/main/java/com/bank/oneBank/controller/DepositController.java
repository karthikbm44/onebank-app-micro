package com.bank.oneBank.controller;

import com.bank.oneBank.dto.BankResponse;
import com.bank.oneBank.dto.CreditDebitRequest;
import com.bank.oneBank.service.DepositService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/user")
@Tag(name = "Deposit Management APIs")
public class DepositController {

    @Autowired
    private DepositService depositService;

    @Operation(
            summary = "Credit Amount",
            description = "Credit the requested amount to the user account and update the available balance.Email will be sent to the user to update on credited amount."
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP Status 200 Success"
    )
    @PostMapping("/deposit")
    public BankResponse depositAmount(@RequestBody CreditDebitRequest request){
        return depositService.depositAmount(request);
    }
}
