package com.neifi.businessmanagementsystem.service;

import com.neifi.businessmanagementsystem.domain.customer.Customer;
import com.neifi.businessmanagementsystem.domain.customer.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Optional<Customer> getCustomerByDni(String dni) {
        return customerRepository.findById(dni);
    }

    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public Customer updateCustomer(String dni, Customer newCustomer) {
        Optional<Customer> customerByDni = customerRepository.findById(dni);
        if (customerByDni.isPresent()) {
            newCustomer.setDni(customerByDni.get().getDni());
            newCustomer.setInvoices(customerByDni.get().getInvoices());
            return customerRepository.save(newCustomer);

        }
        throw new RuntimeException("not found");
    }

    public void deleteCustomer(String id) {
        customerRepository.deleteById(id);
    }


}
