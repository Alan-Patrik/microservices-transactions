package com.alanpatrik.bancosantander.transactions.modules.transaction.dto;

import com.alanpatrik.bancosantander.transactions.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequestDTO {

    private double value;
    private TransactionType transactionType;
    private int number;
    private int agency;
    private Long accountId;
}
