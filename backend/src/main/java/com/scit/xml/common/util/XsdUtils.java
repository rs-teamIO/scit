package com.scit.xml.common.util;

import com.scit.xml.common.api.ResponseCode;
import com.scit.xml.exception.BadRequestException;
import com.scit.xml.exception.InternalServerException;
import com.scit.xml.model.Paper;
import org.springframework.core.io.Resource;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.FileInputStream;
import java.io.IOException;
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

    /**
     * Performs Validation of XML document against given XML schema and
     * unmarshalling to given type.
     * @param clazz target type class
     * @param resource XML Schema resource
     * @param xml XML to be validated and unmarshalled
     * @param <T> target type
     * @return deserialized instance of type <T>
     */
    public static <T> T unmarshal(Class<T> clazz, Resource resource, String xml) {
        final SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        try {
            final JAXBContext context = JAXBContext.newInstance(Paper.class);
            final Unmarshaller unmarshaller = context.createUnmarshaller();
            final String path = resource.getURI().getPath();
            final Schema schema = factory.newSchema(new StreamSource(new FileInputStream(path)));
            unmarshaller.setSchema(schema);
            unmarshaller.setEventHandler(new SchemaValidationEventHandler());
            return (T)unmarshaller.unmarshal(new StringReader(xml));
        } catch (Exception e) {
            throw new InternalServerException(e);
        }
    }
}
