package com.scit.xml.repository;

import com.scit.xml.config.XQueryBuilder;
import com.scit.xml.config.XQueryExecutor;
import com.scit.xml.exception.InternalServerException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public abstract class BaseRepository {

    @Value("classpath:xq/common/append.xml")
    protected Resource appendTemplate;

    protected final XQueryBuilder xQueryBuilder;
    protected final XQueryExecutor xQueryExecutor;

    protected <T> String marshal(Class<T> clazz, T instance) {
        try {
            final StringWriter stringWriter = new StringWriter();
            final JAXBContext context = JAXBContext.newInstance(clazz);
            final Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
            marshaller.marshal(instance, stringWriter);
            return stringWriter.toString();
        } catch (Exception e) {
            throw new InternalServerException(e);
        }
    }
}
