package com.bank.oneBank.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String houseNoOrName;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String pinCode;
    @OneToOne
    @JoinColumn(name = "id")
    private User user;


}
