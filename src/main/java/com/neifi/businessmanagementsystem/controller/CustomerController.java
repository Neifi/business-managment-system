package com.neifi.businessmanagementsystem.controller;

import com.neifi.businessmanagementsystem.domain.customer.Customer;
import com.neifi.businessmanagementsystem.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/customers")
public class CustomerController {
    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/{dni}")
    public ResponseEntity<Customer> getCustomerByDni(@PathVariable String dni) {
        Optional<Customer> customer = customerService.getCustomerByDni(dni);
        return customer.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        Customer newCustomer = customerService.createCustomer(customer);
        return new ResponseEntity<>(newCustomer, HttpStatus.CREATED);
    }

    @PutMapping("/{dni}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable String dni, @RequestBody Customer newCustomer) {
        Customer updatedCustomer = customerService.updateCustomer(dni, newCustomer);
        return new ResponseEntity<>(updatedCustomer, HttpStatus.OK);
    }

    @DeleteMapping("/{dni}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable String dni) {
        customerService.deleteCustomer(dni);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
