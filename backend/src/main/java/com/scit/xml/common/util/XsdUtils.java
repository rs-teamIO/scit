package com.scit.xml.common.util;

import com.scit.xml.common.api.ResponseCode;
import com.scit.xml.exception.BadRequestException;
import org.springframework.core.io.Resource;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.FileInputStream;
import java.io.StringReader;

public class XsdUtils {

    private XsdUtils() { }

    /**
     * Validates XML document against given XML schema.
     * @param xml XML to be validated
     * @param resource XML Schema resource
     */
    public static void validate(String xml, Resource resource) {
        final SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        try {
            final String path = resource.getURI().getPath();
            final Schema schema = factory.newSchema(new StreamSource(new FileInputStream(path)));
            final Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new StringReader(xml)));
        } catch (Exception e) {
            throw new BadRequestException(ResponseCode.INVALID_MEDIA_TYPE, "Schema validation error: Given XML document is invalid.");
        }
    }
}
