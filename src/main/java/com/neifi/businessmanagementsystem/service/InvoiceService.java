package com.neifi.businessmanagementsystem.service;

import com.neifi.businessmanagementsystem.controller.dto.OrderLineRequest;
import com.neifi.businessmanagementsystem.domain.customer.Customer;
import com.neifi.businessmanagementsystem.domain.customer.CustomerRepository;
import com.neifi.businessmanagementsystem.domain.invoice.Invoice;

import com.neifi.businessmanagementsystem.domain.invoice.InvoiceRepository;
import com.neifi.businessmanagementsystem.domain.orderLine.OrderLine;
import com.neifi.businessmanagementsystem.domain.orderLine.OrderLineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final CustomerRepository customerRepository;
    private final OrderLineRepository orderLineRepository;


    @Autowired
    public InvoiceService(InvoiceRepository invoiceRepository, CustomerRepository customerRepository, OrderLineRepository orderLineRepository) {
        this.invoiceRepository = invoiceRepository;
        this.customerRepository = customerRepository;
        this.orderLineRepository = orderLineRepository;

    }

    public long getInvoiceCount(){
        return this.invoiceRepository.count();
    }

    public Invoice createInvoice(String customerDni, List<OrderLineRequest> orderItems, double discount, double taxRate) {
        Optional<Customer> customerByDni = customerRepository.findById(customerDni);
        if (customerByDni.isPresent()) {
            Invoice invoice = new Invoice();
            invoice.setCustomer(customerByDni.get());
            addOrderLines(orderItems, invoice);
            invoice.setTaxRate(taxRate);
            invoice.setDiscount(discount);
            invoiceRepository.save(invoice);
            return invoice;
        }

        throw new RuntimeException("customer not found");
    }


    public void addOrderLines(Long invoiceId, List<OrderLineRequest> orderLinesRequest) {
        Optional<Invoice> optionalInvoice = this.invoiceRepository.findById(invoiceId);
        if (optionalInvoice.isPresent()) {
            Invoice invoice = optionalInvoice.get();
            addOrderLines(orderLinesRequest, invoice);
            invoiceRepository.save(invoice);
            return;
        }
        throw new RuntimeException("invoice not found");
    }

    private void addOrderLines(List<OrderLineRequest> orderLinesRequest, Invoice invoice) {
        List<OrderLine> orderLines = orderLinesRequest
                .stream()
                .map(r -> new OrderLine(r.productName(),
                        r.unitPrice(),
                        r.quantity(),
                        r.discount(),invoice)).toList();
        invoice.setOrderLines(orderLines);
            double t = 0;
        for (OrderLine r : orderLines) {
            t += r.getTotal();
        }
        double totalInvoicePrice = invoice.getOrderLines().stream().mapToDouble(OrderLine::getTotal).sum();
        invoice.setBasePrice(totalInvoicePrice);

    }

    public void deleteOrderLine(Long invoiceId, Long orderLineId) {
        Optional<Invoice> optionalInvoice = this.invoiceRepository.findById(invoiceId);
        if (optionalInvoice.isPresent()) {
            Invoice invoice = optionalInvoice.get();
            Optional<OrderLine> optionalOrderLine = orderLineRepository.findById(orderLineId);
            if (optionalOrderLine.isPresent()) {
                OrderLine orderLine = optionalOrderLine.get();
                invoice.removeOrderLine(orderLine);
                orderLineRepository.delete(orderLine);
            }
        }
    }

    public List<Invoice> listInvoices() {
        return invoiceRepository.findAll();
    }

    public Optional<Invoice> getInvoiceById(Long invoiceId) {
        return this.invoiceRepository.findById(invoiceId);
    }


    public void closeInvoice(Long invoiceId) {
        Optional<Invoice> optionalInvoice = this.invoiceRepository.findById(invoiceId);
        if (optionalInvoice.isPresent()) {
            Invoice invoice = optionalInvoice.get();
            invoice.setClosed(true);
            this.invoiceRepository.save(invoice);
        }
    }


    public void deleteInvoice(Long invoiceId) {
        Optional<Invoice> optionalInvoice = this.invoiceRepository.findById(invoiceId);
        if (optionalInvoice.isPresent()) {
            this.invoiceRepository.deleteById(invoiceId);
        }
    }


}
