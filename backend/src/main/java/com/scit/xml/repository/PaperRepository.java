package com.scit.xml.repository;

import com.google.common.collect.Lists;
import com.scit.xml.common.Constants;
import com.scit.xml.common.Predicate;
import com.scit.xml.common.util.ResourceSetUtils;
import com.scit.xml.common.util.XmlWrapper;
import com.scit.xml.config.RdfQueryBuilder;
import com.scit.xml.config.RdfQueryExecutor;
import com.scit.xml.config.XQueryBuilder;
import com.scit.xml.config.XQueryExecutor;
import com.scit.xml.model.paper.Paper;
import com.scit.xml.rdf.RdfExtractor;
import com.scit.xml.rdf.RdfTriple;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.xmldb.api.base.ResourceSet;

import java.util.List;

import static java.util.UUID.randomUUID;

@Component
public class PaperRepository extends BaseRepository {

    private final String PAPER_NAMESPACE_ALIAS = "paper";
    private final String PAPER_NAMESPACE = "http://www.scit.org/schema/paper";
    private final String PAPERS_COLLECTION = "/papers:papers";
    private final String PAPER_INSTANCE = "/papers:papers/paper:paper";
    private final String PAPER_PREFIX = "paper:";
    private final String PAPERS_NAMESPACE = "xmlns:papers=\"http://www.scit.org/schema/papers\"";

    private final String PAPERS_NAMESPACE_FORMAT = "http://www.scit.org/papers/%s";

    @Value("classpath:xq/paper/findById.xq")
    private Resource findByIdQuery;

    @Value("classpath:xq/paper/findIdsByText.xq")
    private Resource findIdsByTextQuery;

    public PaperRepository(XQueryBuilder xQueryBuilder, XQueryExecutor xQueryExecutor, RdfQueryBuilder rdfQueryBuilder, RdfQueryExecutor rdfQueryExecutor) {
        super(xQueryBuilder, xQueryExecutor, Constants.PAPER_DOCUMENT_ID, rdfQueryBuilder, rdfQueryExecutor);
    }

    public String save(Paper paper, String creatorId) {
        String id = String.format(PAPERS_NAMESPACE_FORMAT, randomUUID().toString());
        paper.setId(id);
        String xml = this.marshal(Paper.class, paper);
        String query = this.xQueryBuilder.buildQuery(this.appendTemplate, PAPER_NAMESPACE_ALIAS, PAPER_NAMESPACE, PAPERS_COLLECTION, xml, PAPERS_NAMESPACE);

        this.xQueryExecutor.updateResource(this.documentId, query);

        List<RdfTriple> rdfTriples = this.extractRdfTriples(id, creatorId);
        this.insertTriples(rdfTriples);

        return id;
    }

    public String remove(String paperId) {
        String id = String.format(PAPERS_NAMESPACE_FORMAT, randomUUID().toString());
        String query = this.xQueryBuilder.buildQuery(this.removeTemplate, PAPER_NAMESPACE_ALIAS, PAPER_NAMESPACE, PAPER_INSTANCE, PAPER_PREFIX, paperId, PAPERS_NAMESPACE);

        this.xQueryExecutor.updateResource(this.documentId, query);

        return id;
    }

    public void updateAndRemoveMetadata(String newPaperXml, String paperId) {
        XmlWrapper paperWrapper = new XmlWrapper(newPaperXml);
        paperWrapper.setElementAttribute("/paper", "paper:id", paperId);

        String query = this.xQueryBuilder.buildQuery(this.appendTemplate, PAPER_NAMESPACE_ALIAS, PAPER_NAMESPACE, PAPERS_COLLECTION, paperWrapper.getXml(), PAPERS_NAMESPACE);

        this.remove(paperId);
        this.xQueryExecutor.updateResource(this.documentId, query);

        // TODO: Re-generate Metadata in case keywords changed
        //List<RdfTriple> rdfTriples = this.extractRdfTriples(id, creatorId);
        //this.insertTriples(rdfTriples);

        this.removeMetadataOnUpdate(paperId);
    }

    public void update(String newPaperXml, String paperId) {
        XmlWrapper paperWrapper = new XmlWrapper(newPaperXml);
        paperWrapper.setElementAttribute("/paper", "paper:id", paperId);

        String query = this.xQueryBuilder.buildQuery(this.appendTemplate, PAPER_NAMESPACE_ALIAS, PAPER_NAMESPACE, PAPERS_COLLECTION, paperWrapper.getXml(), PAPERS_NAMESPACE);

        this.remove(paperId);
        this.xQueryExecutor.updateResource(this.documentId, query);
    }

    public String findById(String id) {
        String query = xQueryBuilder.buildQuery(findByIdQuery, id);
        ResourceSet resourceSet = xQueryExecutor.execute(this.documentId, query);

        return ResourceSetUtils.toXml(resourceSet);
    }

    public List<String> findByText(String text) {
        String query = xQueryBuilder.buildQuery(findIdsByTextQuery, text);
        ResourceSet resourceSet = xQueryExecutor.execute(this.documentId, query);

        return ResourceSetUtils.toList(resourceSet);
    }

    public void writeAssignMetadata(String userId, String paperId) {
        RdfTriple assignedRdfTriple = new RdfTriple(userId, Predicate.ASSIGNED_TO, paperId);
        List<RdfTriple> rdfTriples = Lists.newArrayList(assignedRdfTriple);
        this.insertTriples(rdfTriples);
    }

    public void removeMetadataOnUpdate(String paperId) {
        this.deleteMetadataByPredicateAndObject(Predicate.REVIEWS, paperId);
        this.deleteMetadataByPredicateAndObject(Predicate.REVIEWED, paperId);
    }

    private final String SPARQL_GET_REVIEWS_BY_ID = "PREFIX rv: <http://www.scit.org/rdfvocabulary/>\n" +
            "\n" +
            "SELECT DISTINCT ?reviewId\n" +
            "WHERE \n" +
            "{ \n" +
            "  ?reviewId rv:reviews <%s> .\n" +
            "}";

    public List<String> getReviewsOfPaper(String id) {
        List<String> reviewIds = this.selectSubjects(String.format(SPARQL_GET_REVIEWS_BY_ID, id));

        return reviewIds;
    }

    private List<RdfTriple> extractRdfTriples(String id, String creatorId) {
        final String paperXml = this.findById(id);
        final XmlWrapper paperWrapper = new XmlWrapper(paperXml);
        final RdfExtractor rdfExtractor = new RdfExtractor(id, Constants.PAPER_SCHEMA_URL, Predicate.PREFIX);
        List<RdfTriple> rdfTriples = rdfExtractor.extractRdfTriples(paperWrapper);

        RdfTriple createdRdfTriple = new RdfTriple(creatorId, Predicate.CREATED, id);
        RdfTriple submittedRdfTriple = new RdfTriple(creatorId, Predicate.SUBMITTED, id);
        rdfTriples.add(createdRdfTriple);
        rdfTriples.add(submittedRdfTriple);

        return rdfTriples;
    }
}
