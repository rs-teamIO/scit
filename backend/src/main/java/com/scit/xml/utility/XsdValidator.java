package com.scit.xml.utility;

import javax.annotation.Resource;
import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;

public class XsdValidator {

    public static void validate(String xml, String xsd) {
        File schemaFile = new File(xsd);
        StreamSource xmlFile = new StreamSource(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        try {
            Schema schema = schemaFactory.newSchema(schemaFile);
            Validator validator = schema.newValidator();
            validator.validate(xmlFile);
            System.out.println("XML file is valid");
        } catch (Exception e) {
            System.out.println("XML file is NOT valid. Reason:" + e);
        }
    }

}
