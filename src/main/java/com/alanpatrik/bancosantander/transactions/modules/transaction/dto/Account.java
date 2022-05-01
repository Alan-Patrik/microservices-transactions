package com.alanpatrik.bancosantander.transactions.modules.transaction.dto;

import com.alanpatrik.bancosantander.transactions.enums.AccountType;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    private Long id;
    private Integer number;
    private Integer agency;
    private LocalDateTime descriptionDate;
    private LocalDateTime updateDate;
    private double balance;
    private AccountType accountType;
    private User user;
}
