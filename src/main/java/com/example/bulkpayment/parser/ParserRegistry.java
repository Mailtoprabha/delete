package com.example.bulkpayment.parser;

import com.example.bulkpayment.domain.FileFormat;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ParserRegistry {
    private final List<BulkPaymentParser> parsers;

    public ParserRegistry(List<BulkPaymentParser> parsers) {
        this.parsers = parsers;
    }

    public BulkPaymentParser getParser(FileFormat format) {
        return parsers.stream()
                .filter(parser -> parser.supports(format))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No parser available for format " + format));
    }
}
