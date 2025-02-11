package com.yunusakin.credit.module.api.controller;

import com.yunusakin.credit.module.api.controller.dto.CustomerDTO;
import com.yunusakin.credit.module.api.mapper.EntityMapper;
import com.yunusakin.credit.module.api.service.CustomerService;
import com.yunusakin.credit.module.common.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/customer")
public class CustomerController {
    private final CustomerService customerService;
    private final EntityMapper entityMapper;

    public CustomerController(CustomerService customerService, EntityMapper entityMapper) {
        this.customerService = customerService;
        this.entityMapper = entityMapper;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CustomerDTO>> createCustomer(@Valid @RequestBody CustomerDTO customerDTO) {
        var customer = entityMapper.toCustomerEntity(customerDTO);
        var createdCustomer = customerService.createCustomer(customer);
        var responseDTO = entityMapper.toCustomerDTO(createdCustomer);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK, "Customer created successfully", responseDTO));
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<ApiResponse<CustomerDTO>> getCustomerById(@PathVariable Long customerId) {
        var customer = customerService.findCustomerById(customerId).orElseThrow(() ->
                new IllegalArgumentException("Customer not found"));
        var responseDTO = entityMapper.toCustomerDTO(customer);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK, "Customer retrieved successfully", responseDTO));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CustomerDTO>>> getAllCustomers() {
        List<CustomerDTO> customers = customerService.findAllCustomers().stream()
                .map(entityMapper::toCustomerDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK, "Customers retrieved successfully", customers));
    }
}
