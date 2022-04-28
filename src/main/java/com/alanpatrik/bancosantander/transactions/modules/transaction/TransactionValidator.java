package com.alanpatrik.bancosantander.transactions.modules.transaction;

import com.alanpatrik.bancosantander.transactions.modules.clients.dto.AccountDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TransactionValidator {

    private AccountDTO senderAccount;
    private AccountDTO destinationAccount;
    private Transaction transaction;
}
