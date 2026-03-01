package com.example.bulkpayment.validation.impl;

import com.example.bulkpayment.validation.XmlSchemaValidator;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import java.io.StringReader;

@Component
public class DefaultXmlSchemaValidator implements XmlSchemaValidator {

    @Override
    public void validate(String xml, String classpathXsd) {
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            var schema = factory.newSchema(new StreamSource(new ClassPathResource(classpathXsd).getInputStream()));
            var validator = schema.newValidator();
            validator.validate(new StreamSource(new StringReader(xml)));
        } catch (Exception ex) {
            throw new IllegalArgumentException("PAIN XML failed XSD validation for " + classpathXsd, ex);
        }
    }
}
