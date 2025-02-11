package com.yunusakin.credit.module.api.controller;

import com.yunusakin.credit.module.api.controller.dto.CustomerDTO;
import com.yunusakin.credit.module.api.repository.domain.Customer;
import com.yunusakin.credit.module.api.service.CustomerService;
import com.yunusakin.credit.module.common.response.BaseApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
@Tag(name = "Customer API", description = "Operations related to customers")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Operation(summary = "Create a new customer", description = "Add a new customer to the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input provided")
    })
    @PostMapping
    public ResponseEntity<BaseApiResponse<Customer>> createCustomer(@Valid @RequestBody CustomerDTO customerDTO) {
        return ResponseEntity.ok(new BaseApiResponse<>(HttpStatus.OK, "Customer created successfully", customerService.createCustomer(customerDTO)));
    }

    @Operation(summary = "Retrieve a customer by ID", description = "Fetch details of a specific customer by their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    @GetMapping("/{customerId}")
    public ResponseEntity<BaseApiResponse<Customer>> getCustomerById(@PathVariable Long customerId) {
        var customer = customerService.findCustomerById(customerId).orElseThrow(() ->
                new IllegalArgumentException("Customer not found"));
        return ResponseEntity.ok(new BaseApiResponse<>(HttpStatus.OK, "Customer retrieved successfully", customer));
    }

    @Operation(summary = "List all customers", description = "Retrieve a list of all customers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customers retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<BaseApiResponse<List<Customer>>> getAllCustomers() {
        List<Customer> customers = customerService.findAllCustomers();
        return ResponseEntity.ok(new BaseApiResponse<>(HttpStatus.OK, "Customers retrieved successfully", customers));
    }
}
