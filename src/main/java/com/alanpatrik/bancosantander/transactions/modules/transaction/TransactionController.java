package com.alanpatrik.bancosantander.transactions.modules.transaction;

import com.alanpatrik.bancosantander.transactions.exceptions.CustomBadRequestException;
import com.alanpatrik.bancosantander.transactions.modules.transaction.dto.TransactionDTO;
import com.alanpatrik.bancosantander.transactions.modules.transaction.dto.TransactionResponseDTO;
import com.alanpatrik.bancosantander.transactions.modules.transaction.services.TransactionServiceImpl;
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
    public ResponseEntity<List<TransactionResponseDTO>> getAll() {
        List<TransactionResponseDTO> transactionResponseDTOList = transactionService.getAll();

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(transactionResponseDTOList);
    }

    @PostMapping
    public ResponseEntity<Transaction> create(
            @RequestBody TransactionDTO transactionDTO
    ) throws JsonProcessingException, CustomBadRequestException {
        Transaction receivedTransactionDTO = transactionService.create(transactionDTO);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(receivedTransactionDTO);
    }
}
