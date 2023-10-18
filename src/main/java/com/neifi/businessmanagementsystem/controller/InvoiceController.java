package com.neifi.businessmanagementsystem.controller;

import com.lowagie.text.DocumentException;
import com.neifi.businessmanagementsystem.controller.dto.OrderLineRequest;
import com.neifi.businessmanagementsystem.domain.invoice.Invoice;
import com.neifi.businessmanagementsystem.domain.orderLine.OrderLine;
import com.neifi.businessmanagementsystem.service.InvoiceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/invoices")
public class InvoiceController {
    private final InvoiceService invoiceService;


    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping
    public List<Invoice> listInvoices() {
        return invoiceService.listInvoices();
    }
    @GetMapping("/count")
    public long getInvoiceCount(){
        return this.invoiceService.getInvoiceCount();
    }

    @GetMapping("/{invoice_id}")
    public ResponseEntity<Invoice> findInvoiceById(@PathVariable Long invoice_id) {
        Optional<Invoice> invoiceById = invoiceService.getInvoiceById(invoice_id);
        return invoiceById.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    @PostMapping
    public ResponseEntity<Invoice> createInvoice(@RequestParam String customerDni, @RequestBody List<OrderLineRequest> orderItems, @RequestParam double discount,@RequestParam double taxRate) {
        Invoice invoice = this.invoiceService.createInvoice(customerDni, orderItems,discount,taxRate);
        return ResponseEntity.ok(invoice);
    }

    @PutMapping("/{invoice_id}/order")
    public ResponseEntity<Void> addOrderLine(@PathVariable Long invoice_id, @RequestBody List<OrderLineRequest> orderLines){
            this.invoiceService.addOrderLines(invoice_id,orderLines);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{invoice_id}/order/{orderLineId}")
    public ResponseEntity<Void> addOrderLine(@PathVariable Long invoice_id, @PathVariable Long orderLineId){
        this.invoiceService.deleteOrderLine(invoice_id,orderLineId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{invoice_id}")
    public ResponseEntity<Void> deleteInvoice(@PathVariable Long invoice_id){
        this.invoiceService.deleteInvoice(invoice_id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{invoice_id}")
    public ResponseEntity<Void> closeInvoice(@PathVariable Long invoice_id){
        this.invoiceService.closeInvoice(invoice_id);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/{invoice_id}/pdf")
    public ResponseEntity<byte[]> generateInvoicePDF(@PathVariable Long invoice_id) throws DocumentException, IOException {
        return new ResponseEntity<>( HttpStatus.OK);
    }

}
