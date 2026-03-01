package com.example.bulkpayment.parser.impl;

import com.example.bulkpayment.domain.FileFormat;
import com.example.bulkpayment.domain.ParsedPaymentFile;
import com.example.bulkpayment.domain.PaymentRecord;
import com.example.bulkpayment.parser.BulkPaymentParser;
import com.example.bulkpayment.validation.XmlSchemaValidator;
import org.springframework.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractPain001Parser implements BulkPaymentParser {

    private final XmlSchemaValidator xmlSchemaValidator;

    protected AbstractPain001Parser(XmlSchemaValidator xmlSchemaValidator) {
        this.xmlSchemaValidator = xmlSchemaValidator;
    }

    protected abstract String schemaPath();

    protected abstract FileFormat format();

    @Override
    public boolean supports(FileFormat format) {
        return format() == format;
    }

    @Override
    public ParsedPaymentFile parse(String fileReference, String rawContent) {
        xmlSchemaValidator.validate(rawContent, schemaPath());

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            Document document = factory.newDocumentBuilder()
                    .parse(new ByteArrayInputStream(rawContent.getBytes()));

            NodeList txNodes = document.getElementsByTagNameNS("*", "CdtTrfTxInf");
            List<PaymentRecord> records = new ArrayList<>();
            for (int i = 0; i < txNodes.getLength(); i++) {
                Element tx = (Element) txNodes.item(i);
                records.add(mapTransaction(fileReference, tx));
            }

            return new ParsedPaymentFile(format(), fileReference, records);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Failed parsing PAIN file", ex);
        }
    }

    private PaymentRecord mapTransaction(String fileReference, Element tx) {
        String txnId = text(tx, "InstrId");
        String debtor = text(tx, "DbtrAcct");
        String creditor = text(tx, "CdtrAcct");
        String amountRaw = text(tx, "InstdAmt");
        String currency = attribute(tx, "InstdAmt", "Ccy");
        String date = text(tx, "ReqdExctnDt");
        String remittance = text(tx, "Ustrd");

        return new PaymentRecord(
                fileReference,
                StringUtils.hasText(txnId) ? txnId : "UNKNOWN",
                debtor,
                creditor,
                new BigDecimal(StringUtils.hasText(amountRaw) ? amountRaw : "0"),
                StringUtils.hasText(currency) ? currency : "EUR",
                StringUtils.hasText(date) ? LocalDate.parse(date) : LocalDate.now(),
                remittance,
                format().name()
        );
    }

    private String text(Element parent, String tagName) {
        NodeList nodes = parent.getElementsByTagNameNS("*", tagName);
        if (nodes.getLength() == 0) {
            return "";
        }
        return nodes.item(0).getTextContent().trim();
    }

    private String attribute(Element parent, String tagName, String attributeName) {
        NodeList nodes = parent.getElementsByTagNameNS("*", tagName);
        if (nodes.getLength() == 0) {
            return "";
        }
        return ((Element) nodes.item(0)).getAttribute(attributeName);
    }
}
