package com.yunusakin.credit.module.api.repository;

import com.yunusakin.credit.module.api.repository.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
