package com.bank.oneBank.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreditDebitRequest {

    @Schema(
            name = "Account Number"
    )
    private String accountNumber;
    @Schema(
            name = "Amount"
    )
    private BigDecimal amount;
}
