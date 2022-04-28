package com.alanpatrik.bancosantander.transactions.modules.transaction;

import com.alanpatrik.bancosantander.transactions.exceptions.CustomBadRequestException;
import com.alanpatrik.bancosantander.transactions.exceptions.CustomInternalServerException;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TransactionService {

    List<Transaction> getAll();

    Transaction create(Transaction transaction) throws CustomInternalServerException, JsonProcessingException, CustomBadRequestException;
}
