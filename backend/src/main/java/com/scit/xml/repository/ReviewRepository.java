package com.scit.xml.repository;

import com.scit.xml.common.Constants;
import com.scit.xml.common.util.XmlWrapper;
import com.scit.xml.config.XQueryBuilder;
import com.scit.xml.config.XQueryExecutor;
import org.springframework.stereotype.Component;

import static java.util.UUID.randomUUID;

@Component
public class ReviewRepository extends BaseRepository {

    private final String REVIEW_NAMESPACE_ALIAS = "review";
    private final String REVIEW_NAMESPACE = "http://www.scit.org/schema/review";
    private final String REVIEWS_COLLECTION = "/reviews:reviews";
    private final String REVIEWS_NAMESPACE = "xmlns:reviews=\"http://www.scit.org/schema/reviews\"";

    private final String REVIEWS_NAMESPACE_FORMAT = "http://www.scit.org/review/%s";

    public ReviewRepository(XQueryBuilder xQueryBuilder, XQueryExecutor xQueryExecutor) {
        super(xQueryBuilder, xQueryExecutor);
    }

    public String save(XmlWrapper reviewWrapper) {
        String id = String.format(REVIEWS_NAMESPACE_FORMAT, randomUUID().toString());
        reviewWrapper.getDocument().getDocumentElement().setAttribute("id", id);
        reviewWrapper.updateXml();

        String query = this.xQueryBuilder.buildQuery(this.appendTemplate, REVIEW_NAMESPACE_ALIAS,
                REVIEW_NAMESPACE, REVIEWS_COLLECTION, reviewWrapper.getXml(), REVIEWS_NAMESPACE);

        this.xQueryExecutor.updateResource(Constants.REVIEW_DOCUMENT_ID, query);

        return id;
    }
}
