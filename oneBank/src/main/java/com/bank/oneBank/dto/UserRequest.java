package com.bank.oneBank.dto;

import com.bank.oneBank.entity.Address;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    @Schema(
            name = "First Name"
    )
    private String firstName;

    @Schema(
            name = "Middle Name"
    )
    private String middleName;

    @Schema(
            name = "Last Name"
    )
    private String lastName;
    @Schema(
            name = "Gender"
    )
    private String gender;

    private AddressDto address;

    @Schema(
            name = "Email Id"
    )
    private String email;
    @Schema(
            name = "Phone Number"
    )
    private String phoneNumber;
    @Schema(
            name = "Alternative Phone Number"
    )
    private String alternativePhoneNumber;

}
