package com.bank.oneBank.controller;

import com.bank.oneBank.dto.BankResponse;
import com.bank.oneBank.dto.TransferRequest;
import com.bank.oneBank.service.TransferService;
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
@Tag(name = "Transfer Management APIs")
public class TransferController {

    @Autowired
    private TransferService transferService;

    @Operation(
            summary = "Transfer Amount Between User Accounts",
            description = "Transfer the requested amount from the source user account to the destination user account and update the available balance in both user accounts.Email will be sent to update on credit and debit of amount to the respective account users.If the source account user do not have sufficient fund in their account, an email will be sent to the user to update on insufficient funds."
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP Status 200 Success"
    )
    @PostMapping("/transfer")
    public BankResponse transfer(@RequestBody TransferRequest request){
        return transferService.transfer(request);
    }
}
