package com.scit.xml.repository;

import static java.util.UUID.randomUUID;

import com.scit.xml.common.util.XmlMapper;
import com.scit.xml.config.XQueryBuilder;
import com.scit.xml.config.XQueryExecutor;
import com.scit.xml.exception.InternalServerException;
import com.scit.xml.model.Paper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.OutputStream;
import java.io.StringWriter;

@Component
@RequiredArgsConstructor
public class PaperRepository {

    private final String DOCUMENT_ID = "papers.xml";
    private final String PAPER_NAMESPACE_ALIAS = "paper";
    private final String PAPER_NAMESPACE = "http://www.scit.org/schema/paper";
    private final String PAPERS_COLLECTION = "/papers:papers";
    private final String PAPERS_NAMESPACE = "xmlns:papers=\"http://www.scit.org/schema/papers\"";

    private final String PAPERS_NAMESPACE_FORMAT = "http://www.scit.org/papers/%s";

    @Value("classpath:xq/common/append.xml")
    private Resource appendTemplate;

    private final XQueryBuilder xQueryBuilder;
    private final XQueryExecutor xQueryExecutor;

    public String save(Paper paper) {
        paper.setId(randomUUID().toString());

        String xml = this.marshal(Paper.class, paper);
        String query = xQueryBuilder.buildQuery(appendTemplate, PAPER_NAMESPACE_ALIAS, PAPER_NAMESPACE, PAPERS_COLLECTION, xml, PAPERS_NAMESPACE);

        xQueryExecutor.updateResource(DOCUMENT_ID, query);

        return String.format(PAPERS_NAMESPACE_FORMAT, paper.getId());
    }

    // TODO: Put into base class
    private <T> String marshal(Class<T> clazz, T instance) {
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
