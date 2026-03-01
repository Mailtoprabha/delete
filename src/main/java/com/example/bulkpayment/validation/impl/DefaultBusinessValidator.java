package com.example.bulkpayment.validation.impl;

import com.example.bulkpayment.config.BusinessValidationProperties;
import com.example.bulkpayment.domain.PaymentRecord;
import com.example.bulkpayment.domain.ValidationError;
import com.example.bulkpayment.domain.ValidationResult;
import com.example.bulkpayment.validation.BusinessValidator;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

@Component
public class DefaultBusinessValidator implements BusinessValidator {

    private final BusinessValidationProperties properties;

    public DefaultBusinessValidator(BusinessValidationProperties properties) {
        this.properties = properties;
    }

    @Override
    public ValidationResult validate(List<PaymentRecord> paymentRecords) {
        ValidationResult result = new ValidationResult();

        for (PaymentRecord record : paymentRecords) {
            if (record.amount().compareTo(properties.getMinAmount()) < 0 ||
                    record.amount().compareTo(properties.getMaxAmount()) > 0) {
                result.addError(new ValidationError(
                        "AMOUNT_RANGE",
                        "Amount not within configured range",
                        record.transactionId()));
            }

            if (!properties.getAllowedCurrencies().contains(record.currency())) {
                result.addError(new ValidationError(
                        "CURRENCY",
                        "Currency not permitted",
                        record.transactionId()));
            }

            if (properties.isRequireDebtorAndCreditor() &&
                    (!StringUtils.hasText(record.debtorAccount()) || !StringUtils.hasText(record.creditorAccount()))) {
                result.addError(new ValidationError(
                        "ACCOUNT_MISSING",
                        "Debtor/Creditor account is mandatory",
                        record.transactionId()));
            }
        }

        return result;
    }
}
