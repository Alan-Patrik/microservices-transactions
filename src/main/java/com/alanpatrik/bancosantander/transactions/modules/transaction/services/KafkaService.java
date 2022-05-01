package com.alanpatrik.bancosantander.transactions.modules.transaction.services;

import com.alanpatrik.bancosantander.transactions.exceptions.CustomBadRequestException;
import com.alanpatrik.bancosantander.transactions.modules.transaction.Transaction;
import com.alanpatrik.bancosantander.transactions.modules.transaction.TransactionMapper;
import com.alanpatrik.bancosantander.transactions.modules.transaction.TransactionRepository;
import com.alanpatrik.bancosantander.transactions.modules.transaction.dto.TransactionDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaService {

    private final KafkaTemplate<Object, TransactionDTO> kafkaTemplate;
    private final TransactionServiceImpl transactionService;
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper = TransactionMapper.INSTANCE;

    @KafkaListener(topics = "EfetuarTransacao", groupId = "MicroServicoEfetuarTransacao")
    private void execute(ConsumerRecord<String, TransactionDTO> consumerRecord) throws CustomBadRequestException, JsonProcessingException {
        log.info("transacão recebida {}", consumerRecord.value());

        Transaction transaction = transactionService.create(consumerRecord.value());
        save(transaction);
    }

    public void save(Transaction transaction) throws JsonProcessingException {
        Transaction transactionResponse = transactionRepository.save(transaction);
        create("SalvarTransacao", transaction);
    }

    public void create(String topic, Transaction transaction) throws JsonProcessingException {
        kafkaTemplate.send(topic, transactionMapper.toDTO(transaction));
    }
}
