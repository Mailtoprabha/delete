package com.example.bulkpayment.service;

import com.example.bulkpayment.ack.AckService;
import com.example.bulkpayment.domain.FileFormat;
import com.example.bulkpayment.domain.ValidationError;
import com.example.bulkpayment.parser.ParserRegistry;
import com.example.bulkpayment.persistence.PaymentRepository;
import com.example.bulkpayment.validation.BusinessValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
public class BulkPaymentProcessingService {

    private final ParserRegistry parserRegistry;
    private final BusinessValidator businessValidator;
    private final PaymentRepository paymentRepository;
    private final AckService ackService;

    public BulkPaymentProcessingService(ParserRegistry parserRegistry,
                                        BusinessValidator businessValidator,
                                        PaymentRepository paymentRepository,
                                        AckService ackService) {
        this.parserRegistry = parserRegistry;
        this.businessValidator = businessValidator;
        this.paymentRepository = paymentRepository;
        this.ackService = ackService;
    }

    @Transactional
    public void process(String fileReference, FileFormat format, String content) {
        try {
            var parsed = parserRegistry.getParser(format).parse(fileReference, content);
            var validationResult = businessValidator.validate(parsed.records());

            if (!validationResult.isValid()) {
                String details = validationResult.getErrors().stream()
                        .map(this::errorToMessage)
                        .collect(Collectors.joining("; "));
                ackService.sendNak(fileReference, details);
                return;
            }

            paymentRepository.saveToStaging(parsed.records());
            paymentRepository.promoteToMain(fileReference);
            ackService.sendAck(fileReference, "Loaded records=" + parsed.records().size());
        } catch (Exception ex) {
            ackService.sendNak(fileReference, ex.getMessage());
            throw ex;
        }
    }

    private String errorToMessage(ValidationError error) {
        return error.code() + "(" + error.transactionId() + "):" + error.message();
    }
}
