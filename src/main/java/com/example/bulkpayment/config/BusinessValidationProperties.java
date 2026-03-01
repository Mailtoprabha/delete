package com.example.bulkpayment.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@ConfigurationProperties(prefix = "payment.validation")
public class BusinessValidationProperties {

    private BigDecimal minAmount = BigDecimal.ZERO;
    private BigDecimal maxAmount = new BigDecimal("1000000");
    private Set<String> allowedCurrencies = new HashSet<>(Set.of("EUR", "USD", "GBP"));
    private boolean requireDebtorAndCreditor = true;

    public BigDecimal getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(BigDecimal minAmount) {
        this.minAmount = minAmount;
    }

    public BigDecimal getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(BigDecimal maxAmount) {
        this.maxAmount = maxAmount;
    }

    public Set<String> getAllowedCurrencies() {
        return allowedCurrencies;
    }

    public void setAllowedCurrencies(Set<String> allowedCurrencies) {
        this.allowedCurrencies = allowedCurrencies;
    }

    public boolean isRequireDebtorAndCreditor() {
        return requireDebtorAndCreditor;
    }

    public void setRequireDebtorAndCreditor(boolean requireDebtorAndCreditor) {
        this.requireDebtorAndCreditor = requireDebtorAndCreditor;
    }
}
