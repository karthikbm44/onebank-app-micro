package com.bank.oneBank.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountInfo {
    @Schema(
            name = "Account Holder Name"
    )
    private String accountName;
    @Schema(
            name = "Account Number"
    )
    private String accountNumber;
    @Schema(
            name = "Account Balance"
    )
    private BigDecimal accountBalance;
}
