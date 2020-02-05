package com.scit.xml.service;

import com.google.common.collect.Lists;
import com.scit.xml.common.Predicate;
import com.scit.xml.common.util.ForbiddenUtils;
import com.scit.xml.common.util.XmlWrapper;
import com.scit.xml.exception.InternalServerException;
import com.scit.xml.model.paper.PaperStatus;
import com.scit.xml.rdf.RdfTriple;
import com.scit.xml.repository.ReviewRepository;
import com.scit.xml.service.converter.DocumentConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

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
    private final PaperService paperService;

    public String createReview(XmlWrapper paperWrapper, String paperId, String userId) {

        this.checkIfUserIsReviewing(userId, paperId);

        paperWrapper.setElementAttribute("/paper/comment", "paper:id", paperId);

        try {
            String xml = this.documentConverter.xmlToXml(paperWrapper, Paths.get(this.stylesheet.getURI()).toString());
            XmlWrapper reviewWrapper = new XmlWrapper(xml);
            reviewWrapper.getDocument().getDocumentElement().setAttribute("paper_id", paperId);
            reviewWrapper.getDocument().getDocumentElement().setAttribute("reviewer_id", userId);

            String reviewId = this.reviewRepository.save(reviewWrapper);

            RdfTriple reviewsTriple = new RdfTriple(reviewId, Predicate.REVIEWS, paperId);
            RdfTriple writtenByTriple = new RdfTriple(reviewId, Predicate.WRITTEN_BY, userId);
            RdfTriple reviewedTriple = new RdfTriple(userId, Predicate.REVIEWED, paperId);
            List<RdfTriple> rdfTriples = Lists.newArrayList(reviewsTriple, writtenByTriple, reviewedTriple);
            this.reviewRepository.insertTriples(rdfTriples);
            this.reviewRepository.deleteTriple(userId, Predicate.CURRENTLY_REVIEWING, paperId);

            XmlWrapper persistentPaper = new XmlWrapper(this.paperService.findById(paperId));
            persistentPaper.set("/paper/paper_info/status", PaperStatus.REVIEWED.getName());
            this.paperService.update(persistentPaper.getXml(), paperId);

            return reviewId;

        } catch (IOException e) {
            throw new InternalServerException(e);
        }
    }

    private final String SPARQL_ASK_IS_USER_REVIEWING_PAPER_QUERY = "PREFIX rv: <http://www.scit.org/rdfvocabulary/>\n" +
            "\n" + "ASK\n" + "WHERE {\n" + "\t<%s> rv:currently_reviewing <%s>.\n" + "}";

    public void checkIsUserReviewingPaper(String userId, String paperId) {
        boolean userIsReviewing = this.reviewRepository.ask(String.format(SPARQL_ASK_IS_USER_REVIEWING_PAPER_QUERY, userId, paperId));
        ForbiddenUtils.throwInsufficientPrivilegesExceptionIf(!userIsReviewing);
    }


    // ======================================= acceptReviewRequest =======================================

    public void acceptReviewRequest(String userId, String paperId) {
        this.checkIfUserIsAssigned(userId, paperId);

        XmlWrapper paperWrapper = new XmlWrapper(this.paperService.findById(paperId));
        paperWrapper.set("/paper/paper_info/status", PaperStatus.IN_REVIEW.getName());
        this.paperService.update(paperWrapper.getXml(), paperId);

        RdfTriple acceptedRdfTriple = new RdfTriple(userId, Predicate.CURRENTLY_REVIEWING, paperId);
        List<RdfTriple> rdfTriples = Lists.newArrayList(acceptedRdfTriple);
        this.reviewRepository.insertTriples(rdfTriples);
        this.reviewRepository.deleteTriple(userId, Predicate.ASSIGNED_TO, paperId);
    }


    // ======================================= declineReviewRequest =======================================

    public void declineReviewRequest(String userId, String paperId) {
        this.checkIfUserIsAssigned(userId, paperId);
        this.reviewRepository.deleteTriple(userId, Predicate.ASSIGNED_TO, paperId);
    }


    // ======================================= common stuff =======================================

    private final String SPARQL_ASK_IS_USER_ASSIGNED_TO_PAPER_QUERY = "PREFIX rv: <http://www.scit.org/rdfvocabulary/>\n" +
            "\n" + "ASK\n" + "WHERE {\n" + "\t<%s> rv:assigned_to <%s>.\n" + "}";

    private void checkIfUserIsAssigned(String userId, String paperId) {
        boolean isAssigned = this.reviewRepository.ask(String.format(SPARQL_ASK_IS_USER_ASSIGNED_TO_PAPER_QUERY, userId, paperId));
        ForbiddenUtils.throwInsufficientPrivilegesExceptionIf(!isAssigned);
    }

    private final String SPARQL_ASK_HAS_USER_ACCEPTED_TO_REVIEW_PAPER_QUERY = "PREFIX rv: <http://www.scit.org/rdfvocabulary/>\n" +
            "\n" + "ASK\n" + "WHERE {\n" + "\t<%s> rv:currently_reviewing <%s>.\n" + "}";

    private void checkIfUserIsReviewing(String userId, String paperId) {
        boolean isReviewing = this.reviewRepository.ask(String.format(SPARQL_ASK_HAS_USER_ACCEPTED_TO_REVIEW_PAPER_QUERY, userId, paperId));
        ForbiddenUtils.throwInsufficientPrivilegesExceptionIf(!isReviewing);
    }
}
