package com.example.bulkpayment.validation;

import com.example.bulkpayment.domain.PaymentRecord;
import com.example.bulkpayment.domain.ValidationResult;

import java.util.List;

public interface BusinessValidator {
    ValidationResult validate(List<PaymentRecord> paymentRecords);
}
