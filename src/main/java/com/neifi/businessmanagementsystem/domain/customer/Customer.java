package com.neifi.businessmanagementsystem.domain.customer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.neifi.businessmanagementsystem.domain.invoice.Invoice;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "customers")
@Data
public class Customer {

    @Id
    private String dni;
    private String name;
    private String surnames;
    private String email;
    private String phone;
    private String address;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Invoice> invoices;
}
