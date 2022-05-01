package com.alanpatrik.bancosantander.transactions.modules.transaction;

import com.alanpatrik.bancosantander.transactions.modules.transaction.dto.Account;
import com.alanpatrik.bancosantander.transactions.modules.transaction.dto.TransactionDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TransactionMapper {
    TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);
    ObjectMapper mapper = new ObjectMapper();

    default Transaction toModel(TransactionDTO transactiontDTO) throws JsonProcessingException {
        Transaction transaction = new Transaction();
        transaction.setValue(transactiontDTO.getValue());
        transaction.setTransactionType(transactiontDTO.getTransactionType());
        transaction.setSenderAccount(mapper.registerModule(new JavaTimeModule()).writeValueAsString(transactiontDTO.getSenderAccount()));
        transaction.setDestinationAccount(mapper.registerModule(new JavaTimeModule()).writeValueAsString(transactiontDTO.getDestinationAccount()));

        return transaction;
    }

    default TransactionDTO toDTO(Transaction transaction) throws JsonProcessingException {
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setValue(transaction.getValue());
        transactionDTO.setTransactionType(transaction.getTransactionType());
        transactionDTO.setSenderAccount(mapper.registerModule(new JavaTimeModule()).readValue(transaction.getSenderAccount(), Account.class));
        transactionDTO.setDestinationAccount(mapper.registerModule(new JavaTimeModule()).readValue(transaction.getDestinationAccount(), Account.class));

        return transactionDTO;
    }
}
