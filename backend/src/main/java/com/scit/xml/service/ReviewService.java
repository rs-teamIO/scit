package com.scit.xml.service;

import com.google.common.collect.Lists;
import com.scit.xml.common.Predicate;
import com.scit.xml.common.util.ForbiddenUtils;
import com.scit.xml.common.util.XmlWrapper;
import com.scit.xml.exception.BadRequestException;
import com.scit.xml.exception.InternalServerException;
import com.scit.xml.model.review.Review;
import com.scit.xml.rdf.RdfExtractor;
import com.scit.xml.rdf.RdfTriple;
import com.scit.xml.repository.RdfRepository;
import com.scit.xml.repository.ReviewRepository;
import com.scit.xml.service.converter.DocumentConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.naming.PartialResultException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    @Value("classpath:xsl/review-paper.xsl")
    private Resource stylesheet;

    private final ReviewRepository reviewRepository;
    //private final ReviewDatabaseValidator reviewDatabaseValidator;
    private final DocumentConverter documentConverter;
    private final RdfRepository rdfRepository;

    public String create(XmlWrapper paperWrapper, String paperId, String userId) {

        paperWrapper.setElementAttribute("/paper/comment", "paper:id", paperId);

        try {
            String xml = this.documentConverter.xmlToXml(paperWrapper, Paths.get(this.stylesheet.getURI()).toString());
            XmlWrapper reviewWrapper = new XmlWrapper(xml);
            reviewWrapper.getDocument().getDocumentElement().setAttribute("paper_id", paperId);
            reviewWrapper.getDocument().getDocumentElement().setAttribute("reviewer_id", userId);

            String reviewId = this.reviewRepository.save(reviewWrapper);

            RdfTriple writtenByTriple = new RdfTriple(RdfExtractor.wrapId(reviewId), Predicate.WRITTEN_BY, RdfExtractor.wrapId(userId));
            RdfTriple reviewedTriple = new RdfTriple(RdfExtractor.wrapId(userId), Predicate.REVIEWED, RdfExtractor.wrapId(paperId));
            List<RdfTriple> rdfTriples = Lists.newArrayList(writtenByTriple, reviewedTriple);
            this.rdfRepository.insertTriples(rdfTriples);
            this.rdfRepository.deleteTriple(userId, Predicate.CURRENTLY_REVIEWING, paperId);

            return reviewId;

        } catch (IOException e) {
            throw new InternalServerException(e);
        }
    }

    public void acceptReviewRequest(String userId, String paperId) {
        RdfTriple acceptedRdfTriple = new RdfTriple(RdfExtractor.wrapId(userId), Predicate.CURRENTLY_REVIEWING, RdfExtractor.wrapId(paperId));
        List<RdfTriple> rdfTriples = Lists.newArrayList(acceptedRdfTriple);
        this.rdfRepository.insertTriples(rdfTriples);
        this.rdfRepository.deleteTriple(userId, Predicate.ASSIGNED_TO, paperId);
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
