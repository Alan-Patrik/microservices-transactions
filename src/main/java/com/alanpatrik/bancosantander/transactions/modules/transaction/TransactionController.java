package com.alanpatrik.bancosantander.transactions.modules.transaction;

import com.alanpatrik.bancosantander.transactions.exceptions.CustomBadRequestException;
import com.alanpatrik.bancosantander.transactions.exceptions.CustomInternalServerException;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/transacao")
public class TransactionController {

    @Autowired
    private TransactionServiceImpl transactionService;

    @GetMapping
    public ResponseEntity<List<Transaction>> getAll() {
        List<Transaction> transactionList = transactionService.getAll();

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(transactionList);
    }

    @PostMapping
    public ResponseEntity<Transaction> create(
            @RequestBody Transaction transaction
    ) throws CustomInternalServerException, JsonProcessingException, CustomBadRequestException {
        Transaction receivedTransactionResponseDTO = transactionService.create(transaction);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(receivedTransactionResponseDTO);
    }
}
