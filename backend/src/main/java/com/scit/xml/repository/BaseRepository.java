package com.scit.xml.repository;

import com.scit.xml.common.api.ResponseCode;
import com.scit.xml.common.util.DefaultNamespacePrefixMapper;
import com.scit.xml.common.util.SchemaValidationEventHandler;
import com.scit.xml.config.XQueryBuilder;
import com.scit.xml.config.XQueryExecutor;
import com.scit.xml.exception.BadRequestException;
import com.scit.xml.exception.InternalServerException;

import com.scit.xml.exception.handlers.SpecificExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.FileInputStream;
import java.io.StringReader;
import java.io.StringWriter;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public abstract class BaseRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseRepository.class);

    @Value("classpath:xq/common/append.xml")
    protected Resource appendTemplate;

    protected final XQueryBuilder xQueryBuilder;
    protected final XQueryExecutor xQueryExecutor;

    protected <T> String marshal(Class<T> clazz, T instance) {
        LOGGER.info(String.format("Marshalling %s instance...", clazz.getName()));
        try {
            final StringWriter stringWriter = new StringWriter();
            final JAXBContext context = JAXBContext.newInstance(clazz);
            final Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
            marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", new DefaultNamespacePrefixMapper());
            marshaller.marshal(instance, stringWriter);
            LOGGER.info(String.format("Marshalling of %s instance successful.", clazz.getName()));
            return stringWriter.toString();
        } catch (Exception e) {
            LOGGER.error(String.format("Marshalling of %s instance unsuccessful.", clazz.getName()));
            throw new InternalServerException(e);
        }
    }

    /**
     * Performs validation of XML document against given XML schema and
     * unmarshalling to given type.
     * @param clazz target type class
     * @param resource XML Schema resource
     * @param xml XML to be validated and unmarshalled
     * @param <T> target type
     * @return deserialized instance of type T
     */
    protected <T> T unmarshal(Class<T> clazz, Resource resource, String xml) {
        LOGGER.info(String.format("Unmarshalling to %s instance...", clazz.getName()));
        final SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        try {
            final JAXBContext context = JAXBContext.newInstance(clazz);
            final Unmarshaller unmarshaller = context.createUnmarshaller();
            final String path = resource.getURI().getPath();
            final Schema schema = factory.newSchema(new StreamSource(new FileInputStream(path)));
            unmarshaller.setSchema(schema);
            unmarshaller.setEventHandler(new SchemaValidationEventHandler());
            LOGGER.info(String.format("Unmarshalling to %s instance successful.", clazz.getName()));
            return (T)unmarshaller.unmarshal(new StringReader(xml));
        } catch (Exception e) {
            LOGGER.error(String.format("Unmarshalling to %s unsuccessful.", clazz.getName()));
            throw new BadRequestException(ResponseCode.INVALID_MEDIA_TYPE, "Schema validation error: Given XML document is invalid.");
        }
    }
}
