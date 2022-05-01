package com.alanpatrik.bancosantander.transactions.modules.transaction.dto;

import com.alanpatrik.bancosantander.transactions.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponseDTO {

    private Long id;
    private double value;
    private TransactionType transactionType;
    private LocalDateTime descriptionDate;
    private Account senderAccount;
    private Account destinationAccount;
}
