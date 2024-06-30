package com.bank.oneBank.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BankResponse  {
    @Schema(
            name = "Response Code"
    )
    private String responseCode;
    @Schema(
            name = "Response Code Message"
    )
    private String responseMessage;
    @Schema(
            name = "Account Info"
    )
    private AccountInfo accountInfo;
}
