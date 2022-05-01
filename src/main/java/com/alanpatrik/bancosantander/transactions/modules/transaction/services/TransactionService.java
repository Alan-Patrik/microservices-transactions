package com.alanpatrik.bancosantander.transactions.modules.transaction.services;

import com.alanpatrik.bancosantander.transactions.exceptions.CustomBadRequestException;
import com.alanpatrik.bancosantander.transactions.exceptions.CustomInternalServerException;
import com.alanpatrik.bancosantander.transactions.modules.transaction.Transaction;
import com.alanpatrik.bancosantander.transactions.modules.transaction.dto.TransactionDTO;
import com.alanpatrik.bancosantander.transactions.modules.transaction.dto.TransactionResponseDTO;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface TransactionService {

    List<TransactionResponseDTO> getAll();

    Transaction create(TransactionDTO transactionDTO) throws CustomInternalServerException, JsonProcessingException, CustomBadRequestException;
}
