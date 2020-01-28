package com.scit.xml.repository;

import static java.util.UUID.randomUUID;

import com.scit.xml.common.util.ResourceSetUtils;
import com.scit.xml.config.XQueryBuilder;
import com.scit.xml.config.XQueryExecutor;
import com.scit.xml.model.paper.Paper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.xmldb.api.base.ResourceSet;

@Component
public class PaperRepository extends BaseRepository {

    private final String DOCUMENT_ID = "papers.xml";
    private final String PAPER_NAMESPACE_ALIAS = "paper";
    private final String PAPER_NAMESPACE = "http://www.scit.org/schema/paper";
    private final String PAPERS_COLLECTION = "/papers:papers";
    private final String PAPERS_NAMESPACE = "xmlns:papers=\"http://www.scit.org/schema/papers\"";

    private final String PAPERS_NAMESPACE_FORMAT = "http://www.scit.org/papers/%s";

    @Value("classpath:xq/paper/findById.xq")
    private Resource findByIdQuery;

    public PaperRepository(XQueryBuilder xQueryBuilder, XQueryExecutor xQueryExecutor) {
        super(xQueryBuilder, xQueryExecutor);
    }

    public String save(Paper paper) {
        String id = String.format(PAPERS_NAMESPACE_FORMAT, randomUUID().toString());
        paper.setId(id);
        String xml = this.marshal(Paper.class, paper);
        String query = this.xQueryBuilder.buildQuery(this.appendTemplate, PAPER_NAMESPACE_ALIAS, PAPER_NAMESPACE, PAPERS_COLLECTION, xml, PAPERS_NAMESPACE);

        this.xQueryExecutor.updateResource(DOCUMENT_ID, query);

        return id;
    }

    public String findById(String id) {
        String query = xQueryBuilder.buildQuery(findByIdQuery, id);
        ResourceSet resourceSet = xQueryExecutor.execute(DOCUMENT_ID, query);

        return ResourceSetUtils.toXml(resourceSet);
    }
}