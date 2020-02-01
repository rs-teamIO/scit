package com.scit.xml.repository;

import com.scit.xml.common.Constants;
import com.scit.xml.common.util.ResourceSetUtils;
import com.scit.xml.config.RdfQueryBuilder;
import com.scit.xml.config.RdfQueryExecutor;
import com.scit.xml.config.XQueryBuilder;
import com.scit.xml.config.XQueryExecutor;
import com.scit.xml.model.paper.Paper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.xmldb.api.base.ResourceSet;

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

    public PaperRepository(XQueryBuilder xQueryBuilder, XQueryExecutor xQueryExecutor, RdfQueryBuilder rdfQueryBuilder, RdfQueryExecutor rdfQueryExecutor) {
        super(xQueryBuilder, xQueryExecutor, Constants.PAPER_DOCUMENT_ID, rdfQueryBuilder, rdfQueryExecutor);
    }

    public String save(Paper paper) {
        String id = String.format(PAPERS_NAMESPACE_FORMAT, randomUUID().toString());
        paper.setId(id);
        String xml = this.marshal(Paper.class, paper);
        String query = this.xQueryBuilder.buildQuery(this.appendTemplate, PAPER_NAMESPACE_ALIAS, PAPER_NAMESPACE, PAPERS_COLLECTION, xml, PAPERS_NAMESPACE);

        this.xQueryExecutor.updateResource(this.documentId, query);

        return id;
    }

    public String remove(String paperId) {
        String id = String.format(PAPERS_NAMESPACE_FORMAT, randomUUID().toString());
        String query = this.xQueryBuilder.buildQuery(this.removeTemplate, PAPER_NAMESPACE_ALIAS, PAPER_NAMESPACE, PAPER_INSTANCE, PAPER_PREFIX, paperId, PAPERS_NAMESPACE);

        this.xQueryExecutor.updateResource(this.documentId, query);

        return id;
    }

    public String findById(String id) {
        String query = xQueryBuilder.buildQuery(findByIdQuery, id);
        ResourceSet resourceSet = xQueryExecutor.execute(this.documentId, query);

        return ResourceSetUtils.toXml(resourceSet);
    }
}
