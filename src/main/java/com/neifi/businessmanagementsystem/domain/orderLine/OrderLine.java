package com.neifi.businessmanagementsystem.domain.orderLine;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.neifi.businessmanagementsystem.domain.invoice.Invoice;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order_line")
public class OrderLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_line_id")
    private Long id;
    @Column(name = "product_name")
    private String productName;
    @Column(name = "unit_price")
    private Double unitPrice = 0D;
    @Column(name = "quantity")
    private double quantity = 0;
    private double total = 0;
    private double orderLineDiscount = 0;
    @ManyToOne
    @JoinColumn(name = "invoice_id")
    @JsonIgnore
    private Invoice invoice;

    public OrderLine(String productName, Double unitPrice, double quantity, double orderLineDiscount, Invoice invoice) {
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.orderLineDiscount = orderLineDiscount;
        this.invoice = invoice;
        if (orderLineDiscount > 0) {
            double discountValue = unitPrice*quantity*(orderLineDiscount/100);
            this.total = unitPrice * quantity-discountValue;
            return;
        }
        this.total = unitPrice * quantity;
    }
}
