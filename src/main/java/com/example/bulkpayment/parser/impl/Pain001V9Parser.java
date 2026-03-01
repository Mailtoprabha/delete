package com.example.bulkpayment.parser.impl;

import com.example.bulkpayment.domain.FileFormat;
import com.example.bulkpayment.validation.XmlSchemaValidator;
import org.springframework.stereotype.Component;

@Component
public class Pain001V9Parser extends AbstractPain001Parser {

    public Pain001V9Parser(XmlSchemaValidator xmlSchemaValidator) {
        super(xmlSchemaValidator);
    }

    @Override
    protected String schemaPath() {
        return "xsd/pain.001.001.09.xsd";
    }

    @Override
    protected FileFormat format() {
        return FileFormat.PAIN_001_V9;
    }
}
