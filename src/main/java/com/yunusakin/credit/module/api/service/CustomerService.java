package com.yunusakin.credit.module.api.service;

import com.yunusakin.credit.module.api.controller.dto.CustomerDTO;
import com.yunusakin.credit.module.api.repository.CustomerRepository;
import com.yunusakin.credit.module.api.repository.domain.Customer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }
    public Optional<Customer> findCustomerById(Long customerId) {
        return customerRepository.findById(customerId);
    }

    public Customer createCustomer(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        customer.setName(customerDTO.getName());
        customer.setSurname(customerDTO.getSurname());
        customer.setCreditLimit(customerDTO.getCreditLimit());
        return customerRepository.save(customer);
    }

    public List<Customer> findAllCustomers() {
        return customerRepository.findAll();
    }
}
