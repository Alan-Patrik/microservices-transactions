package com.alanpatrik.bancosantander.transactions.modules.transaction;

import com.alanpatrik.bancosantander.transactions.exceptions.CustomBadRequestException;
import com.alanpatrik.bancosantander.transactions.exceptions.CustomInternalServerException;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Page;

public interface TransactionService {

    Page<Transaction> getAll(int page, int size, String sort);

    Transaction create(Transaction transaction) throws CustomInternalServerException, JsonProcessingException, CustomBadRequestException;
}
