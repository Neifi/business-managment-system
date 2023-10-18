package com.neifi.businessmanagementsystem.domain.invoice;

import com.neifi.businessmanagementsystem.domain.customer.Customer;
import com.neifi.businessmanagementsystem.domain.orderLine.OrderLine;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Table(name = "invoices")
@Data
@Entity
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invoice_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_dni")
    private Customer customer;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderLine> orderLines = new ArrayList<>();

    private double discount = 0;

    @Column(name = "base_price")
    private Double basePrice = 0d;

    @Column(name = "final_price")
    private Double finalPrice = 0d;

    @Column(name = "tax_rate")
    private double taxRate = 0d;

    @Column(name = "tax_rate_value")
    private double taxRateValue = 0;

    @Column(name = "closed")
    private boolean closed = false;

    @Column(name = "date")
    private OffsetDateTime date = OffsetDateTime.now();

    public void removeOrderLine(OrderLine orderLine) {
        orderLines.remove(orderLine);
    }

    public void setDiscount(double discount) {
        if (discount > 0) {
            this.discount = discount;
            double discountValue = this.basePrice * discount / 100;
            this.taxRateValue = this.basePrice * taxRate / 100;
            this.basePrice -= discountValue;
            this.finalPrice += this.taxRateValue;
            return;
        }
        this.taxRateValue = this.basePrice * taxRate / 100;
        this.finalPrice = this.basePrice+this.taxRateValue;

    }

}


