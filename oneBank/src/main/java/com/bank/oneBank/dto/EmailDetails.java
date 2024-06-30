package com.bank.oneBank.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailDetails {

    @Schema(
            name = "Recipient"
    )
    private String recipient;
    @Schema(
            name = "Message Body"
    )
    private String messageBody;
    @Schema(
            name = "Subject"
    )
    private String subject;
    @Schema(
            name = "Attachment"
    )
    private String attachment;
}
