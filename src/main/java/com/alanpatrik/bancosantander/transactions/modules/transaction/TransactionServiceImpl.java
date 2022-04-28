package com.alanpatrik.bancosantander.transactions.modules.transaction;

import com.alanpatrik.bancosantander.transactions.exceptions.CustomBadRequestException;
import com.alanpatrik.bancosantander.transactions.exceptions.CustomInternalServerException;
import com.alanpatrik.bancosantander.transactions.modules.clients.GetInfoAccount;
import com.alanpatrik.bancosantander.transactions.modules.clients.dto.AccountDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final GetInfoAccount getInfoAccount;
    private final TransactionRepository transactionRepository;
    private final String URL_GET_ACCOUNT_BY_ID = "http://localhost:8080/contas/";
    private final String URL_GET_ACCOUNT_BY_NUMBER = "http://localhost:8080/contas/pesquisar?numero=";

    @Override
    public Page<Transaction> getAll(int page, int size, String sort) {
        return null;
    }

    @Override
    public Transaction create(Transaction transaction) throws CustomInternalServerException, JsonProcessingException, CustomBadRequestException {

        AccountDTO senderAccount = getInfoAccount.execute(URL_GET_ACCOUNT_BY_NUMBER + transaction.getNumber());
        AccountDTO destinationAccount = getInfoAccount.execute(URL_GET_ACCOUNT_BY_ID + transaction.getAccountId());

        Transaction receivedTransaction = new Transaction();
        TransactionValidator transactionValidator = new TransactionValidator();

        switch (transaction.getTransactionType()) {
            case TRANSFERENCIA:
                transactionValidator = transferValidator(senderAccount, destinationAccount, transaction);
                receivedTransaction = transactionRepository.save(transactionValidator.getTransaction());
                break;

            case DEPOSITO:
                transactionValidator = depositValidator(senderAccount, destinationAccount, transaction);
                receivedTransaction = transactionRepository.save(transactionValidator.getTransaction());
                break;

            case SAQUE:
                transactionValidator = withdrawValidator(senderAccount, destinationAccount, transaction);
                receivedTransaction = transactionRepository.save(transactionValidator.getTransaction());
                break;

            default:
                throw new CustomBadRequestException("Essa transação não existe!");
        }

        return receivedTransaction;
    }

    private TransactionValidator transferValidator(
            AccountDTO senderAccount, AccountDTO destinationAccount, Transaction transaction
    ) throws CustomBadRequestException {
        Transaction receivedTransaction = new Transaction();

        if (senderAccount.getNumber().equals(destinationAccount.getNumber())) {
            throw new CustomBadRequestException("Operação recusada. Você não pode efetuar uma transferência para si mesmo!");
        }

        if (senderAccount.getBalance() <= 0) {
            throw new CustomBadRequestException("Saldo insuficiente!");
        }

        receivedTransaction.setValue(transaction.getValue());
        receivedTransaction.setTransactionType(transaction.getTransactionType());
        receivedTransaction.setNumber(transaction.getNumber());
        receivedTransaction.setAgency(transaction.getAgency());
        receivedTransaction.setAccountId(destinationAccount.getId());

        senderAccount.setBalance(senderAccount.getBalance() - transaction.getValue());
        destinationAccount.setBalance(destinationAccount.getBalance() + transaction.getValue());

        TransactionValidator transactionValidator = new TransactionValidator();
        transactionValidator.setSenderAccount(senderAccount);
        transactionValidator.setDestinationAccount(destinationAccount);
        transactionValidator.setTransaction(receivedTransaction);

        return transactionValidator;
    }

    private TransactionValidator depositValidator(
            AccountDTO senderAccount, AccountDTO destinationAccount, Transaction transaction
    ) throws CustomBadRequestException {
        Transaction receivedTransaction = new Transaction();

        if (transaction.getValue() <= 0) {
            throw new CustomBadRequestException("Não é possível depositar valor menor ou igual a 0!");
        }

        if (senderAccount.getNumber().equals(destinationAccount.getNumber())) {
            receivedTransaction.setValue(transaction.getValue());
            receivedTransaction.setTransactionType(transaction.getTransactionType());
            receivedTransaction.setNumber(transaction.getNumber());
            receivedTransaction.setAgency(transaction.getAgency());
            receivedTransaction.setAccountId(senderAccount.getId());

            destinationAccount.setBalance(destinationAccount.getBalance() + transaction.getValue());

        } else if (senderAccount.getNumber() != destinationAccount.getNumber()) {
            receivedTransaction.setValue(transaction.getValue());
            receivedTransaction.setTransactionType(transaction.getTransactionType());
            receivedTransaction.setNumber(transaction.getNumber());
            receivedTransaction.setAgency(transaction.getAgency());
            receivedTransaction.setAccountId(destinationAccount.getId());

            senderAccount.setBalance(senderAccount.getBalance() - transaction.getValue());
            destinationAccount.setBalance(destinationAccount.getBalance() + transaction.getValue());
        }

        TransactionValidator transactionValidator = new TransactionValidator();
        transactionValidator.setSenderAccount(senderAccount);
        transactionValidator.setDestinationAccount(destinationAccount);
        transactionValidator.setTransaction(receivedTransaction);

        return transactionValidator;
    }

    private TransactionValidator withdrawValidator(
            AccountDTO senderAccount, AccountDTO destinationAccount, Transaction transaction
    ) throws CustomBadRequestException {
        Transaction receivedTransaction = new Transaction();

        if (senderAccount.getNumber() != destinationAccount.getNumber()) {
            throw new CustomBadRequestException("Operação recusada. Você não pode efetuar saque de outra conta!");
        }

        if (senderAccount.getBalance() <= 0) {
            throw new CustomBadRequestException("Saldo insuficiente!");
        }

        if (transaction.getValue() > senderAccount.getBalance()) {
            throw new CustomBadRequestException("Operação recusada. Você não pode sacar um valor acima do seu saldo.");
        }

        receivedTransaction.setValue(transaction.getValue());
        receivedTransaction.setTransactionType(transaction.getTransactionType());
        receivedTransaction.setNumber(transaction.getNumber());
        receivedTransaction.setAgency(transaction.getAgency());
        receivedTransaction.setAccountId(senderAccount.getId());

        senderAccount.setBalance(senderAccount.getBalance() - transaction.getValue());

        TransactionValidator transactionValidator = new TransactionValidator();
        transactionValidator.setSenderAccount(senderAccount);
        transactionValidator.setDestinationAccount(destinationAccount);
        transactionValidator.setTransaction(receivedTransaction);

        return transactionValidator;
    }
}
