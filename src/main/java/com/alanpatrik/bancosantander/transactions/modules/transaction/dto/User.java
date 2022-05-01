package com.alanpatrik.bancosantander.transactions.modules.transaction.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Long id;
    private String cpf;
    private String password;
    private String name;
    private LocalDateTime descriptionDate;
    private LocalDateTime updateDate;
}
