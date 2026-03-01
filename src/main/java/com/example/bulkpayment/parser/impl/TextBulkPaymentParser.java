package com.example.bulkpayment.parser.impl;

import com.example.bulkpayment.domain.FileFormat;
import com.example.bulkpayment.domain.ParsedPaymentFile;
import com.example.bulkpayment.domain.PaymentRecord;
import com.example.bulkpayment.parser.BulkPaymentParser;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Component
public class TextBulkPaymentParser implements BulkPaymentParser {

    @Override
    public boolean supports(FileFormat format) {
        return format == FileFormat.TEXT;
    }

    @Override
    public ParsedPaymentFile parse(String fileReference, String rawContent) {
        List<PaymentRecord> records = Arrays.stream(rawContent.split("\\R"))
                .filter(line -> !line.isBlank())
                .map(this::mapLine)
                .toList();

        return new ParsedPaymentFile(FileFormat.TEXT, fileReference, records);
    }

    private PaymentRecord mapLine(String line) {
        String[] tokens = line.split("\\|");
        if (tokens.length < 8) {
            throw new IllegalArgumentException("Invalid TXT payment line; expected 8+ fields");
        }

        return new PaymentRecord(
                tokens[0],
                tokens[1],
                tokens[2],
                tokens[3],
                new BigDecimal(tokens[4]),
                tokens[5],
                LocalDate.parse(tokens[6]),
                tokens[7],
                FileFormat.TEXT.name()
        );
    }
}
