package com.example.bulkpayment.parser;

import com.example.bulkpayment.domain.FileFormat;
import com.example.bulkpayment.domain.ParsedPaymentFile;

public interface BulkPaymentParser {
    boolean supports(FileFormat format);

    ParsedPaymentFile parse(String fileReference, String rawContent);
}
