package com.alanpatrik.bancosantander.transactions.modules.clients;

import com.alanpatrik.bancosantander.transactions.exceptions.CustomInternalServerException;
import com.alanpatrik.bancosantander.transactions.modules.clients.dto.AccountDTO;
import com.alanpatrik.bancosantander.transactions.modules.transaction.dto.UserAccountDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class GetInfoAccount {

    private ObjectMapper objectMapper = new ObjectMapper();

    public AccountDTO execute(String url) throws CustomInternalServerException, JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<Map> infoAccountResponseEntity = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                Map.class);

        JSONObject jsonObject = new JSONObject(infoAccountResponseEntity.getBody());
        JSONObject account = (JSONObject) jsonObject.get("data");
        JSONObject user = (JSONObject) account.get("user");

        UserAccountDTO userAccountDTO = new UserAccountDTO();
        userAccountDTO.setName(user.getString("name"));
        userAccountDTO.setCpf(user.getString("cpf"));

        AccountDTO responseAccount = new AccountDTO(
                account.getLong("id"),
                account.getInt("number"),
                account.getInt("agency"),
                LocalDateTime.parse((CharSequence) account.get("descriptionDate")),
                LocalDateTime.parse((CharSequence) account.get("updateDate")),
                account.getDouble("balance"),
                account.getString("accountType"),
                userAccountDTO
        );


        if (infoAccountResponseEntity.getStatusCode().isError()) {
            throw new CustomInternalServerException("Erro ao conectar serviço de retornar conta pelo numero.");
        }

        return responseAccount;
    }
}
