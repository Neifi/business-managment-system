package com.neifi.businessmanagementsystem.controller.dto;

public record OrderLineRequest(String productName, double unitPrice, double quantity, double discount) {
}
