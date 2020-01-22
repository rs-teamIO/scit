package com.scit.xml.repository;

import com.scit.xml.common.util.ResourceSetUtils;
import com.scit.xml.config.XQueryBuilder;
import com.scit.xml.config.XQueryExecutor;
import com.scit.xml.exception.InternalServerException;
import com.scit.xml.model.cover_letter.CoverLetter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.xmldb.api.base.ResourceSet;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import java.util.Date;
import java.util.GregorianCalendar;

import static java.util.UUID.randomUUID;

@Component
public class CoverLetterRepository extends BaseRepository {

    private final String DOCUMENT_ID = "cover_letters.xml";
    private final String COVER_LETTER_NAMESPACE_ALIAS = "cover_letter";
    private final String COVER_LETTER_NAMESPACE = "http://www.scit.org/schema/cover_letter";
    private final String COVER_LETTERS_COLLECTION = "/cover_letters:cover_letters";
    private final String COVER_LETTERS_NAMESPACE = "xmlns:cover_letters=\"http://www.scit.org/schema/cover_letters\"";

    private final String COVER_LETTERS_NAMESPACE_FORMAT = "http://www.scit.org/cover_letter/%s";

    @Value("classpath:xq/cover_letter/findById.xq")
    private Resource findByIdQuery;

    public CoverLetterRepository(XQueryBuilder xQueryBuilder, XQueryExecutor xQueryExecutor) {
        super(xQueryBuilder, xQueryExecutor);
    }

    public String save(CoverLetter coverLetter) {
        String id = String.format(COVER_LETTERS_NAMESPACE_FORMAT, randomUUID().toString());
        coverLetter.setId(id);

        try {
            GregorianCalendar gregorianCalendar = new GregorianCalendar();
            gregorianCalendar.setTime(new Date());
            XMLGregorianCalendar currentDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
            coverLetter.setDate(currentDate);
        } catch (DatatypeConfigurationException e) {
            throw new InternalServerException();
        }


        String xml = this.marshal(CoverLetter.class, coverLetter);
        String query = this.xQueryBuilder.buildQuery(this.appendTemplate, COVER_LETTER_NAMESPACE_ALIAS,
                COVER_LETTER_NAMESPACE, COVER_LETTERS_COLLECTION, xml, COVER_LETTERS_NAMESPACE);

        this.xQueryExecutor.updateResource(DOCUMENT_ID, query);

        return id;
    }

    public String findById(String id) {
        String query = xQueryBuilder.buildQuery(findByIdQuery, id);
        ResourceSet resourceSet = xQueryExecutor.execute(DOCUMENT_ID, query);

        return ResourceSetUtils.toXml(resourceSet);
    }
}
