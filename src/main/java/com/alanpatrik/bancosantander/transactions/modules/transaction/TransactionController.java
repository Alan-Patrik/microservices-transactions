package com.alanpatrik.bancosantander.transactions.modules.transaction;

import com.alanpatrik.bancosantander.transactions.exceptions.CustomBadRequestException;
import com.alanpatrik.bancosantander.transactions.exceptions.CustomInternalServerException;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/transacao")
public class TransactionController {

    @Autowired
    private TransactionServiceImpl transactionService;

    @GetMapping
    public ResponseEntity<Page<Transaction>> getAll(
            @RequestParam int page,
            @RequestParam(required = false, defaultValue = "3") int size,
            @RequestParam(required = false, defaultValue = "Asc") String sort) {
        Page<Transaction> transaction = transactionService.getAll(page, size, sort);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(transaction);
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
