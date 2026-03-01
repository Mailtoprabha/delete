package com.example.bulkpayment.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(BusinessValidationProperties.class)
public class PaymentConfig {
}
