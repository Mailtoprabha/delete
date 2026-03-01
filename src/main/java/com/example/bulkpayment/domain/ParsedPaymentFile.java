package com.example.bulkpayment.domain;

import java.util.List;

public record ParsedPaymentFile(
        FileFormat format,
        String fileReference,
        List<PaymentRecord> records
) {
}
