package com.alanpatrik.bancosantander.transactions.modules.transaction.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionAccountDTO {

    private Integer number;
    private Integer agency;
    private String accountType;
    private UserAccountDTO user;
}
