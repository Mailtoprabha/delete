package com.example.bulkpayment.domain;

public record ValidationError(String code, String message, String transactionId) {
}
