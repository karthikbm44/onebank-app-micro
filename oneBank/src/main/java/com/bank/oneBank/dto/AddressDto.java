package com.bank.oneBank.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressDto {
    private String houseNoOrName;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String pinCode;
}
