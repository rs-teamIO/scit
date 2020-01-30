package com.scit.xml.service;

import com.google.common.collect.Lists;
import com.scit.xml.common.Predicate;
import com.scit.xml.common.util.ForbiddenUtils;
import com.scit.xml.rdf.RdfExtractor;
import com.scit.xml.rdf.RdfTriple;
import com.scit.xml.repository.RdfRepository;
import com.scit.xml.service.converter.DocumentConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    @Value("classpath:xsl/review.xsl")
    private Resource stylesheet;

    //private final ReviewRepository reviewRepository;
    //private final ReviewDatabaseValidator reviewDatabaseValidator;
    private final DocumentConverter documentConverter;
    private final RdfRepository rdfRepository;

    public void acceptReviewRequest(String userId, String paperId) {
        RdfTriple acceptedRdfTriple = new RdfTriple(RdfExtractor.wrapId(userId), Predicate.CURRENTLY_REVIEWING, RdfExtractor.wrapId(paperId));
        List<RdfTriple> rdfTriples = Lists.newArrayList(acceptedRdfTriple);
        this.rdfRepository.insertTriples(rdfTriples);
    }

    public void declineReviewRequest(String userId, String paperId) {
        this.rdfRepository.deleteTriple(userId, Predicate.ASSIGNED_TO, paperId);
    }

    private final String SPARQL_ASK_IS_USER_ASSIGNED_TO_PAPER_QUERY = "PREFIX rv: <http://www.scit.org/rdfvocabulary/>\n" +
            "\n" + "ASK\n" + "WHERE {\n" + "\t<%s> rv:assigned_to <%s>.\n" + "}";

    public void checkIfUserIsAssigned(String userId, String paperId) {
        boolean isAssigned = rdfRepository.ask(String.format(SPARQL_ASK_IS_USER_ASSIGNED_TO_PAPER_QUERY, userId, paperId));
        ForbiddenUtils.throwInsufficientPrivilegesExceptionIf(!isAssigned);
    }
}
