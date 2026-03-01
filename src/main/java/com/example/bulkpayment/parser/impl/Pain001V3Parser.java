package com.example.bulkpayment.parser.impl;

import com.example.bulkpayment.domain.FileFormat;
import com.example.bulkpayment.validation.XmlSchemaValidator;
import org.springframework.stereotype.Component;

@Component
public class Pain001V3Parser extends AbstractPain001Parser {

    public Pain001V3Parser(XmlSchemaValidator xmlSchemaValidator) {
        super(xmlSchemaValidator);
    }

    @Override
    protected String schemaPath() {
        return "xsd/pain.001.001.03.xsd";
    }

    @Override
    protected FileFormat format() {
        return FileFormat.PAIN_001_V3;
    }
}
