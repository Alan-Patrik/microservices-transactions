package com.alanpatrik.bancosantander.transactions.modules.transaction;

import com.alanpatrik.bancosantander.transactions.modules.clients.dto.AccountDTO;
import com.alanpatrik.bancosantander.transactions.modules.transaction.dto.Account;
import com.alanpatrik.bancosantander.transactions.modules.transaction.dto.TransactionDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TransactionValidator {

    private Account senderAccount;
    private Account destinationAccount;
    private TransactionDTO transaction;
}
