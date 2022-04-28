package com.alanpatrik.bancosantander.transactions.modules.clients.dto;

import com.alanpatrik.bancosantander.transactions.modules.transaction.dto.UserAccountDTO;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDTO {

    private Long id;
    private Integer number;
    private Integer agency;
    private LocalDateTime descriptionDate;
    private LocalDateTime updateDate;
    private double balance;
    private String accountType;
    private UserAccountDTO user;
}
