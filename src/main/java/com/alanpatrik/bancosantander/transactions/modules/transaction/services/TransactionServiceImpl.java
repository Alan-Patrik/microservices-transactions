package com.alanpatrik.bancosantander.transactions.modules.transaction.services;

import com.alanpatrik.bancosantander.transactions.exceptions.CustomBadRequestException;
import com.alanpatrik.bancosantander.transactions.modules.transaction.Transaction;
import com.alanpatrik.bancosantander.transactions.modules.transaction.TransactionMapper;
import com.alanpatrik.bancosantander.transactions.modules.transaction.TransactionRepository;
import com.alanpatrik.bancosantander.transactions.modules.transaction.TransactionValidator;
import com.alanpatrik.bancosantander.transactions.modules.transaction.dto.Account;
import com.alanpatrik.bancosantander.transactions.modules.transaction.dto.TransactionDTO;
import com.alanpatrik.bancosantander.transactions.modules.transaction.dto.TransactionResponseDTO;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final ObjectMapper mapper = new ObjectMapper();
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper = TransactionMapper.INSTANCE;

    @Override
    public List<TransactionResponseDTO> getAll() {
        List<TransactionResponseDTO> transactionList = transactionRepository.findAll().stream().map(transaction -> {
            TransactionResponseDTO transactionResponseDTO = new TransactionResponseDTO();

            try {
                transactionResponseDTO.setId(transaction.getId());
                transactionResponseDTO.setValue(transaction.getValue());
                transactionResponseDTO.setTransactionType(transaction.getTransactionType());
                transactionResponseDTO.setSenderAccount(
                        mapper
                                .registerModule(new JavaTimeModule())
                                .readValue(transaction.getSenderAccount(), Account.class)
                );
                transactionResponseDTO.setDestinationAccount(
                        mapper
                                .registerModule(new JavaTimeModule())
                                .readValue(transaction.getDestinationAccount(), Account.class)
                );

            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            return transactionResponseDTO;
        }).collect(Collectors.toList());
        return transactionList;
    }

    @Override
    public Transaction create(TransactionDTO transactionDTO) throws CustomBadRequestException, JsonProcessingException {

        Transaction receivedTransaction = new Transaction();
        TransactionValidator transactionValidator = new TransactionValidator();

        var senderAccount = transactionDTO.getSenderAccount();
        var destinationAccount = transactionDTO.getDestinationAccount();

        switch (transactionDTO.getTransactionType()) {
            case TRANSFERENCIA:
                transactionValidator = transferValidator(senderAccount, destinationAccount, transactionDTO);
                log.info("SenderAccount => {}", transactionValidator.getSenderAccount());
                log.info("DestinationAccount => {}", transactionValidator.getDestinationAccount());
                receivedTransaction = transactionRepository.save(transactionMapper.toModel(transactionValidator.getTransaction()));
                break;

            case DEPOSITO:
                transactionValidator = depositValidator(senderAccount, destinationAccount, transactionDTO);
                log.info("SenderAccount => {}", transactionValidator.getSenderAccount());
                log.info("DestinationAccount => {}", transactionValidator.getDestinationAccount());
                receivedTransaction = transactionRepository.save(transactionMapper.toModel(transactionValidator.getTransaction()));
                break;

            case SAQUE:
                transactionValidator = withdrawValidator(senderAccount, destinationAccount, transactionDTO);
                log.info("SenderAccount => {}", transactionValidator.getSenderAccount());
                log.info("DestinationAccount => {}", transactionValidator.getDestinationAccount());
                receivedTransaction = transactionRepository.save(transactionMapper.toModel(transactionValidator.getTransaction()));
                break;

            default:
                throw new CustomBadRequestException("Essa transação não existe!");
        }

        return receivedTransaction;
    }

    private TransactionValidator transferValidator(
            Account senderAccount, Account destinationAccount, TransactionDTO transactionDTO
    ) throws CustomBadRequestException {
        TransactionDTO receivedTransaction = new TransactionDTO();

        if (senderAccount.getNumber().equals(destinationAccount.getNumber())) {
            throw new CustomBadRequestException("Operação recusada. Você não pode efetuar uma transferência para si mesmo!");
        }

        if (senderAccount.getBalance() <= 0) {
            throw new CustomBadRequestException("Saldo insuficiente!");
        }

        if (transactionDTO.getValue() <= 0 ||
                transactionDTO.getValue() > senderAccount.getBalance()
        ) {
            throw new CustomBadRequestException("Não é possóvel transferir valor maior ou menor que o saldo atual!");
        }

        receivedTransaction.setValue(transactionDTO.getValue());
        receivedTransaction.setTransactionType(transactionDTO.getTransactionType());
        receivedTransaction.setSenderAccount(senderAccount);
        receivedTransaction.setDestinationAccount(destinationAccount);

        senderAccount.setBalance(senderAccount.getBalance() - transactionDTO.getValue());
        destinationAccount.setBalance(destinationAccount.getBalance() + transactionDTO.getValue());

        TransactionValidator transactionValidator = new TransactionValidator();
        transactionValidator.setSenderAccount(senderAccount);
        transactionValidator.setDestinationAccount(destinationAccount);
        transactionValidator.setTransaction(receivedTransaction);

        return transactionValidator;
    }

    private TransactionValidator depositValidator(
            Account senderAccount, Account destinationAccount, TransactionDTO transaction
    ) throws CustomBadRequestException {
        TransactionDTO receivedTransaction = new TransactionDTO();

        if (transaction.getValue() <= 0) {
            throw new CustomBadRequestException("Não é possível depositar valor menor ou igual a 0!");
        }

        if (senderAccount.getNumber().equals(destinationAccount.getNumber())) {
            receivedTransaction.setValue(transaction.getValue());
            receivedTransaction.setTransactionType(transaction.getTransactionType());
            receivedTransaction.setSenderAccount(senderAccount);
            receivedTransaction.setDestinationAccount(destinationAccount);

            destinationAccount.setBalance(destinationAccount.getBalance() + transaction.getValue());

        } else if (senderAccount.getNumber() != destinationAccount.getNumber()) {
            receivedTransaction.setValue(transaction.getValue());
            receivedTransaction.setTransactionType(transaction.getTransactionType());
            receivedTransaction.setSenderAccount(senderAccount);
            receivedTransaction.setDestinationAccount(destinationAccount);

            destinationAccount.setBalance(destinationAccount.getBalance() + transaction.getValue());
        }

        TransactionValidator transactionValidator = new TransactionValidator();
        transactionValidator.setSenderAccount(senderAccount);
        transactionValidator.setDestinationAccount(destinationAccount);
        transactionValidator.setTransaction(receivedTransaction);

        return transactionValidator;
    }

    private TransactionValidator withdrawValidator(
            Account senderAccount, Account destinationAccount, TransactionDTO transaction
    ) throws CustomBadRequestException {
        TransactionDTO receivedTransaction = new TransactionDTO();
        TransactionValidator transactionValidator = new TransactionValidator();

        if (senderAccount.getBalance() <= 0) {
            throw new CustomBadRequestException("Saldo insuficiente!");
        }

        if (transaction.getValue() > senderAccount.getBalance()) {
            throw new CustomBadRequestException("Operação recusada. Você não pode sacar um valor acima do seu saldo.");
        }

        if (senderAccount.getNumber().equals(destinationAccount.getNumber())) {
            receivedTransaction.setValue(transaction.getValue());
            receivedTransaction.setTransactionType(transaction.getTransactionType());
            receivedTransaction.setSenderAccount(senderAccount);
            receivedTransaction.setDestinationAccount(destinationAccount);

            destinationAccount.setBalance(destinationAccount.getBalance() - transaction.getValue());

            transactionValidator.setSenderAccount(senderAccount);
            transactionValidator.setDestinationAccount(destinationAccount);
            transactionValidator.setTransaction(receivedTransaction);

            return transactionValidator;

        } else {
            throw new CustomBadRequestException("Operação recusada. Você não pode efetuar saque de outra conta!");
        }
    }
}
