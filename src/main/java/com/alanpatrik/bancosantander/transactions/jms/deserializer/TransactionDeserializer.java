package com.alanpatrik.bancosantander.transactions.jms.deserializer;

import com.alanpatrik.bancosantander.transactions.modules.transaction.dto.TransactionDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Deserializer;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Slf4j
public class TransactionDeserializer implements Deserializer<TransactionDTO> {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public TransactionDTO deserialize(String s, byte[] bytes) {
        try {
            if (Objects.nonNull(bytes)) {
                return mapper.registerModule(new JavaTimeModule()).readValue(new String(bytes, StandardCharsets.UTF_8), TransactionDTO.class);

            }
        } catch (JsonProcessingException exception) {
            log.error("Erro ao deserializar objeto de transação", exception);
            throw new RuntimeException(exception);
        }

        return null;
    }
}
