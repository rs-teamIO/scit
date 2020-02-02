package com.scit.xml.repository;

import com.scit.xml.common.Constants;
import com.scit.xml.common.Predicate;
import com.scit.xml.common.util.ResourceSetUtils;
import com.scit.xml.common.util.XmlWrapper;
import com.scit.xml.config.RdfQueryBuilder;
import com.scit.xml.config.RdfQueryExecutor;
import com.scit.xml.config.XQueryBuilder;
import com.scit.xml.config.XQueryExecutor;
import com.scit.xml.model.cover_letter.CoverLetter;
import com.scit.xml.rdf.RdfExtractor;
import com.scit.xml.rdf.RdfTriple;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.xmldb.api.base.ResourceSet;

import java.util.List;

import static java.util.UUID.randomUUID;

@Component
public class CoverLetterRepository extends BaseRepository {

    private final String COVER_LETTER_NAMESPACE_ALIAS = "cover_letter";
    private final String COVER_LETTER_NAMESPACE = "http://www.scit.org/schema/cover_letter";
    private final String COVER_LETTERS_COLLECTION = "/cover_letters:cover_letters";
    private final String COVER_LETTERS_NAMESPACE = "xmlns:cover_letters=\"http://www.scit.org/schema/cover_letters\"";

    private final String COVER_LETTERS_NAMESPACE_FORMAT = "http://www.scit.org/cover_letter/%s";

    @Value("classpath:xq/cover_letter/findById.xq")
    private Resource findByIdQuery;

    @Value("classpath:xq/cover_letter/findByPaperId.xq")
    private Resource findByPaperIdQuery;

    public CoverLetterRepository(XQueryBuilder xQueryBuilder, XQueryExecutor xQueryExecutor, RdfQueryBuilder rdfQueryBuilder, RdfQueryExecutor rdfQueryExecutor) {
        super(xQueryBuilder, xQueryExecutor, Constants.COVER_LETTER_DOCUMENT_ID, rdfQueryBuilder, rdfQueryExecutor);
    }

    public String save(CoverLetter coverLetter, String paperId) {
        String id = String.format(COVER_LETTERS_NAMESPACE_FORMAT, randomUUID().toString());
        coverLetter.setId(id);

        String xml = this.marshal(CoverLetter.class, coverLetter);
        String query = this.xQueryBuilder.buildQuery(this.appendTemplate, COVER_LETTER_NAMESPACE_ALIAS,
                COVER_LETTER_NAMESPACE, COVER_LETTERS_COLLECTION, xml, COVER_LETTERS_NAMESPACE);

        this.xQueryExecutor.updateResource(this.documentId, query);

        List<RdfTriple> rdfTriples = this.extractRdfTriples(id, paperId);
        this.insertTriples(rdfTriples);

        return id;
    }

    public String findById(String id) {
        String query = xQueryBuilder.buildQuery(findByIdQuery, id);
        ResourceSet resourceSet = xQueryExecutor.execute(this.documentId, query);

        return ResourceSetUtils.toXml(resourceSet);
    }

    public String findByPaperId(String paperId) {
        String query = xQueryBuilder.buildQuery(findByPaperIdQuery, paperId);
        ResourceSet resourceSet = xQueryExecutor.execute(this.documentId, query);

        return ResourceSetUtils.toXml(resourceSet);
    }

    private List<RdfTriple> extractRdfTriples(String id, String paperId) {
        final String coverLetterXml = this.findById(id);
        final XmlWrapper coverLetterWrapper = new XmlWrapper(coverLetterXml);
        final RdfExtractor rdfExtractor = new RdfExtractor(id, Constants.COVER_LETTER_SCHEMA_URL, Predicate.PREFIX);
        List<RdfTriple> rdfTriples = rdfExtractor.extractRdfTriples(coverLetterWrapper);

        RdfTriple accompaniesRdfTriple = new RdfTriple(id, Predicate.ACCOMPANIES, paperId);
        rdfTriples.add(accompaniesRdfTriple);

        return rdfTriples;
    }
}
