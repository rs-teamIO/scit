package com.scit.xml.service;

import com.google.common.collect.Lists;
import com.scit.xml.common.Constants;
import com.scit.xml.common.Predicate;
import com.scit.xml.common.api.RestApiConstants;
import com.scit.xml.common.api.RestApiErrors;
import com.scit.xml.common.util.*;
import com.scit.xml.exception.InternalServerException;
import com.scit.xml.model.paper.Paper;
import com.scit.xml.model.user.Role;
import com.scit.xml.rdf.RdfExtractor;
import com.scit.xml.rdf.RdfTriple;
import com.scit.xml.repository.PaperRepository;
import com.scit.xml.service.converter.DocumentConverter;
import com.scit.xml.service.validator.database.PaperDatabaseValidator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.w3c.dom.Element;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaperService {

    @Value("classpath:xsl/paper.xsl")
    private Resource stylesheet;

    private final PaperRepository paperRepository;
    private final PaperDatabaseValidator paperDatabaseValidator;
    private final DocumentConverter documentConverter;
    private final UserService userService;

    public String createPaper(Paper paper, String creatorId) {
        return this.paperRepository.save(paper, creatorId);
    }

    public Resource exportToPdf(String paperId) {
        try {
            String paperXml = this.paperDatabaseValidator.validateExportRequest(paperId);
            ByteArrayOutputStream pdfOutputStream = this.documentConverter.xmlToPdf(paperXml, Paths.get(this.stylesheet.getURI()).toString());
            return new ByteArrayResource(pdfOutputStream.toByteArray());
        } catch (IOException e) {
            throw new InternalServerException(e);
        }
    }

    public Resource exportToHtml(String paperId) {
        try {
            String paperXml = this.paperDatabaseValidator.validateExportRequest(paperId);
            ByteArrayOutputStream htmlOutputStream = this.documentConverter.xmlToHtml(paperXml, Paths.get(this.stylesheet.getURI()).toString());
            return new ByteArrayResource(htmlOutputStream.toByteArray());
        } catch (IOException e) {
            throw new InternalServerException(e);
        }
    }

    public Resource convertPaperToPdf(String paperXml) {
        try {
            ByteArrayOutputStream pdfOutputStream = this.documentConverter.xmlToPdf(paperXml, Paths.get(this.stylesheet.getURI()).toString());
            return new ByteArrayResource(pdfOutputStream.toByteArray());
        } catch (IOException e) {
            throw new InternalServerException(e);
        }
    }

    public Resource convertPaperToHtml(String paperXml) {
        try {
            ByteArrayOutputStream htmlOutputStream = this.documentConverter.xmlToHtml(paperXml, Paths.get(this.stylesheet.getURI()).toString());
            return new ByteArrayResource(htmlOutputStream.toByteArray());
        } catch (IOException e) {
            throw new InternalServerException(e);
        }
    }


    // ======================================= getPaperById =======================================

    private final String SPARQL_ASK_IS_PAPER_PUBLISHED_QUERY = "PREFIX rv: <http://www.scit.org/rdfvocabulary/>\n" +
            "\n" + "ASK\n" + "WHERE {\n" + "\t?s rv:published <%s>.\n" + "}";

    private final String SPARQL_ASK_IS_USER_AUTHOR_OF_PAPER_QUERY = "PREFIX rv: <http://www.scit.org/rdfvocabulary/>\n" +
            "\n" + "ASK\n" + "WHERE {\n" + "\t<%s> rv:submitted <%s>.\n" + "}";

    public Resource getPaperById(String paperId, String userId, String userRole) {
        boolean paperPublished = this.paperRepository.ask(String.format(SPARQL_ASK_IS_PAPER_PUBLISHED_QUERY, paperId));
        ForbiddenUtils.throwInsufficientPrivilegesExceptionIf(userId == null && !paperPublished);
        boolean userIsAuthor = this.paperRepository.ask(String.format(SPARQL_ASK_IS_USER_AUTHOR_OF_PAPER_QUERY, userId, paperId));
        boolean userIsEditor = Role.EDITOR.getName().equals(userRole);
        ForbiddenUtils.throwInsufficientPrivilegesExceptionIf(!userIsAuthor && !userIsEditor && !paperPublished);

        return this.exportToHtml(paperId);
    }


    // ======================================= getSubmittedPapers =======================================

    private final String SPARQL_GET_SUBMITTED_PAPERS_QUERY = "PREFIX rv: <http://www.scit.org/rdfvocabulary/>\n" +
            "\n" + "SELECT ?o\n" + "WHERE {\n" + "\t?s rv:submitted ?o.\n" + "}";

    public List<String> getSubmittedPapers() {
        List<String> paperIds = this.paperRepository.selectSubjects(SPARQL_GET_SUBMITTED_PAPERS_QUERY);
        return paperIds.stream()
                .map(id -> this.findById(id))
                .collect(Collectors.toList());
    }


    // ======================================= getAssignedPapers =======================================

    private final String SPARQL_GET_ASSIGNED_PAPERS_QUERY = "PREFIX rv: <http://www.scit.org/rdfvocabulary/>\n" +
            "\n" + "SELECT ?o\n" + "WHERE {\n" + "\t<%s> rv:assigned_to ?o.\n" + "}";

    public List<String> getAssignedPapers(String currentUserId) {
        List<String> paperIds = this.paperRepository.selectSubjects(String.format(SPARQL_GET_ASSIGNED_PAPERS_QUERY, currentUserId));
        return paperIds.stream()
                .map(id -> this.findById(id))
                .collect(Collectors.toList());
    }


    // ======================================= getPapersInReview =======================================

    private final String SPARQL_GET_PAPERS_IN_REVIEW_QUERY = "PREFIX rv: <http://www.scit.org/rdfvocabulary/>\n" +
            "\n" + "SELECT ?o\n" + "WHERE {\n" + "\t<%s> rv:currently_reviewing ?o.\n" + "}";

    public List<String> getPapersInReview(String currentUserId) {
        List<String> paperIds = this.paperRepository.selectSubjects(String.format(SPARQL_GET_PAPERS_IN_REVIEW_QUERY, currentUserId));
        return paperIds.stream()
                .map(id -> this.findById(id))
                .collect(Collectors.toList());
    }


    // ======================================= assignReviewer =======================================

    public void assignReviewer(String paperId, XmlWrapper paperWrapper, String userId) {
        // TODO: Fix bug with userId and username - there is no id in author in paper, only a username.
        boolean reviewerIsAuthorOfPaper = XmlExtractorUtil
                .extractSetOfAttributeValuesAndValidateNotEmpty(paperWrapper.getDocument(), "/paper/authors/*", "id")
                .stream().anyMatch(userId::equals);
        BadRequestUtils.throwInvalidRequestDataExceptionIf(reviewerIsAuthorOfPaper,
                String.format("Unable to assign the paper for review to user with ID %s", userId));

        this.paperRepository.writeAssignMetadata(userId, paperId);
    }


    // ======================================= getCurrentUserPapers =======================================

    private final String SPARQL_GET_PAPERS_OF_USER_QUERY = "PREFIX rv: <http://www.scit.org/rdfvocabulary/>\n" +
            "\n" + "SELECT ?o\n" + "WHERE {\n" + "\t<%s> rv:created ?o.\n" + "}";

    public List<String> getPapersByUserId(String currentUserId) {
        List<String> paperIds = this.paperRepository.selectSubjects(String.format(SPARQL_GET_PAPERS_OF_USER_QUERY, currentUserId));
        return paperIds.stream()
                .map(id -> this.findById(id))
                .collect(Collectors.toList());
    }


    // ======================================= getPublishedPapers =======================================

    private final String SPARQL_GET_PUBLISHED_PAPERS_QUERY = "PREFIX rv: <http://www.scit.org/rdfvocabulary/>\n" +
            "\n" + "SELECT ?o\n" + "WHERE {\n" + "\t?s rv:published ?o.\n" + "}";

    public List<String> getPublishedPapers() {
        List<String> paperIds = this.paperRepository.selectSubjects(String.format(SPARQL_GET_PUBLISHED_PAPERS_QUERY));
        return paperIds.stream()
                .map(id -> this.findById(id))
                .collect(Collectors.toList());
    }


    // ======================================= getRaw and getPdf =======================================

    public String getRawPaperForDownload(String paperId) {
        boolean isPublished = this.paperRepository.ask(String.format(SPARQL_ASK_IS_PAPER_PUBLISHED_QUERY, paperId));
        ForbiddenUtils.throwInsufficientPrivilegesExceptionIf(!isPublished);

        return this.findById(paperId);
    }


    // ======================================= getAnonymizedPaper =======================================

    public String getAnonymizedPaper(String paperId, String userId) {
        String paperXml = this.findById(paperId);
        XmlWrapper paperWrapper = new XmlWrapper(paperXml);

        boolean userIsAuthor = this.paperRepository.ask(String.format(SPARQL_ASK_IS_USER_AUTHOR_OF_PAPER_QUERY, userId, paperId));
        if(!userIsAuthor) {
            final Element element = paperWrapper.getDocument().getDocumentElement();
            // TODO: Remove string value, use constant instead
            element.removeChild(element.getElementsByTagName("paper:authors").item(0));
            paperWrapper.updateXml();
        }

        return paperWrapper.getXml();
    }


    // ======================================= getAuthorsOfPaper =======================================

    private final String SPARQL_GET_AUTHORS_OF_PAPER_QUERY = "PREFIX rv: <http://www.scit.org/rdfvocabulary/>\n" +
            "\n" + "SELECT ?s\n" + "WHERE {\n" + "\t?s rv:created <%s>.\n" + "}";

    public List<String> getIdentifiersOfPaperAuthors(String paperId) {
        List<String> userIds = this.paperRepository.selectSubjects(String.format(SPARQL_GET_AUTHORS_OF_PAPER_QUERY, paperId));
        NotFoundUtils.throwNotFoundExceptionIf(userIds.isEmpty(),
                RestApiErrors.entityWithGivenFieldNotFound(RestApiConstants.PAPER, RestApiConstants.ID));
        return userIds;
    }


    // ======================================= getAuthorsOfPaper =======================================

    private final String SPARQL_GET_REVIEWERS_OF_PAPER_QUERY = "PREFIX rv: <http://www.scit.org/rdfvocabulary/>\n" +
            "\n" + "SELECT DISTINCT ?s\n" + "WHERE {\n" + "\t?s rv:assigned_to|rv:currently_reviewing|rv:reviewed <%s>.\n" + "}";

    public List<String> getReviewersOfPaper(String paperId) {
        this.findById(paperId);
        List<String> userIds = this.paperRepository.selectSubjects(String.format(SPARQL_GET_REVIEWERS_OF_PAPER_QUERY, paperId));
        return userIds.stream()
                .map(id -> this.userService.findById(id))
                .collect(Collectors.toList());
    }


    // ======================================= revokePaper =======================================

    public void revokePaper(String paperId) {
        this.paperDatabaseValidator.validateExportRequest(paperId);
        this.paperRepository.remove(paperId);
        this.paperRepository.deleteAllMetadata(paperId);
    }


    // ======================================= rejectPaper =======================================

    public void rejectPaper(String paperId) {

        // TODO: Delete paper from database
        this.paperRepository.deleteMetadataByObject(paperId);
    }


    // ======================================= publishPaper =======================================

    public void publishPaper(String paperId) {

        // TODO: BUG - Shouldn't delete who submitted the paper
        this.paperRepository.deleteMetadataByObject(paperId);

        String paperXml = this.paperRepository.findById(paperId);
        XmlWrapper paperWrapper = new XmlWrapper(paperXml);
        List<String> authorUsernames = XmlExtractorUtil.extractSetOfAttributeValuesAndValidateNotEmpty(paperWrapper.getDocument(), "/paper/authors/*", "username");
        List<RdfTriple> rdfTriples = new ArrayList<>();

        List<String> authodIds = authorUsernames.stream().map(authorUsername -> {
            XmlWrapper xmlWrapper = new XmlWrapper(this.userService.findByUsername(authorUsername));
            return XmlExtractorUtil.extractStringAndValidateNotBlank(xmlWrapper.getDocument(), "/user/@id");
        }).collect(Collectors.toList());

        authodIds.stream().forEach(authorId -> {
            RdfTriple createdTriple = new RdfTriple(authorId, Predicate.CREATED, paperId);
            RdfTriple publishedTriple = new RdfTriple(authorId, Predicate.PUBLISHED, paperId);
            RdfTriple wasPublishedByTriple = new RdfTriple(paperId, Predicate.WAS_PUBLISHED_BY, authorId);
            rdfTriples.addAll(Lists.newArrayList(createdTriple, publishedTriple, wasPublishedByTriple));
        });

        this.paperRepository.insertTriples(rdfTriples);
    }


    // ======================================= common stuff =======================================

    public String findById(String paperId) {
        final String paperXml = this.paperRepository.findById(paperId);
        NotFoundUtils.throwNotFoundExceptionIf(StringUtils.isEmpty(paperXml),
                RestApiErrors.entityWithGivenFieldNotFound(RestApiConstants.PAPER, RestApiConstants.ID));
        return paperXml;
    }
}
