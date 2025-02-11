package com.yunusakin.credit.module.api.repository.domain;

import com.yunusakin.credit.module.common.domain.BaseEntity;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Customer extends BaseEntity {
    private String name;
    private String surname;
    private BigDecimal creditLimit;
    private BigDecimal usedCreditLimit = BigDecimal.ZERO;
}
