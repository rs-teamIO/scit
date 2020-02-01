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

    @Value("classpath:xq/common/remove.xml")
    protected Resource removeTemplate;

    protected final XQueryBuilder xQueryBuilder;
    protected final XQueryExecutor xQueryExecutor;
    protected final String documentId;

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
}
