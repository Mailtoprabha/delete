package com.example.bulkpayment.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PaymentRecord(
        String fileReference,
        String transactionId,
        String debtorAccount,
        String creditorAccount,
        BigDecimal amount,
        String currency,
        LocalDate valueDate,
        String remittanceInfo,
        String sourceFormat
) {
}
