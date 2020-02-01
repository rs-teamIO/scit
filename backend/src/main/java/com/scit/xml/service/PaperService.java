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

    // ======================================= create =======================================

    public String createPaper(Paper paper, String creatorId) {
        // TODO: DB Validation (if necessary)
        // this.paperDatabaseValidator.validateCreateRequest(paper, creatorUsername);
        String id = this.paperRepository.save(paper);

        List<RdfTriple> rdfTriples = this.extractRdfTriples(id, creatorId);
        this.paperRepository.insertTriples(rdfTriples);

        return id;
    }

    private List<RdfTriple> extractRdfTriples(String id, String creatorId) {
        final String paperXml = this.paperRepository.findById(id);
        final XmlWrapper paperWrapper = new XmlWrapper(paperXml);
        final RdfExtractor rdfExtractor = new RdfExtractor(id, Constants.PAPER_SCHEMA_URL, Predicate.PREFIX);
        List<RdfTriple> rdfTriples = rdfExtractor.extractRdfTriples(paperWrapper);

        RdfTriple createdRdfTriple = new RdfTriple(creatorId, Predicate.CREATED, id);
        RdfTriple submittedRdfTriple = new RdfTriple(creatorId, Predicate.SUBMITTED, id);
        rdfTriples.add(createdRdfTriple);
        rdfTriples.add(submittedRdfTriple);

        return rdfTriples;
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

    public Resource convertToPdf(String xml) {
        try {
            ByteArrayOutputStream pdfOutputStream = this.documentConverter.xmlToPdf(xml, Paths.get(this.stylesheet.getURI()).toString());
            return new ByteArrayResource(pdfOutputStream.toByteArray());
        } catch (IOException e) {
            throw new InternalServerException(e);
        }
    }

    public Resource convertToHtml(String xml) {
        try {
            ByteArrayOutputStream htmlOutputStream = this.documentConverter.xmlToHtml(xml, Paths.get(this.stylesheet.getURI()).toString());
            return new ByteArrayResource(htmlOutputStream.toByteArray());
        } catch (IOException e) {
            throw new InternalServerException(e);
        }
    }

    // ======================================= getPapers =======================================

    private final String SPARQL_GET_PAPERS_OF_USER_QUERY = "PREFIX rv: <http://www.scit.org/rdfvocabulary/>\n" +
            "\n" + "SELECT ?o\n" + "WHERE {\n" + "\t<%s> rv:created ?o.\n" + "}";

    public String getPapersByUserId(String currentUserId) {
        List<String> paperIds = this.paperRepository.selectSubjects(String.format(SPARQL_GET_PAPERS_OF_USER_QUERY, currentUserId));

        List<String> papers = paperIds.stream().map(id -> {
            return convertToXmlResponseString(this.findById(id));
        }).collect(Collectors.toList());

        // TODO: Refactor
        StringBuilder sb = new StringBuilder();
        for(String p : papers) {
            sb.append(p);
        }

        return sb.toString();
    }

    public List<String> getPublishedPapers(String userId) {
        // TODO: Get published papers
        return null;
    }

    private String convertToXmlResponseString(String xml) {
        XmlWrapper xmlWrapper = new XmlWrapper(xml);
        String id = XmlExtractorUtil.extractStringAndValidateNotBlank(xmlWrapper.getDocument(), "/paper/@id");
        String title = XmlExtractorUtil.extractStringAndValidateNotBlank(xmlWrapper.getDocument(), "/paper/title");

        // TODO: Refactor
        StringBuilder sb = new StringBuilder();
        String indent = "  ";

        sb.append("<paper>\n");
        sb.append(indent).append("<id>");
        sb.append(id);
        sb.append("</id>");
        sb.append(indent).append("<title>");
        sb.append(title);
        sb.append("</title>");
        sb.append("</paper>");

        return sb.toString();
    }

    // ======================================= assignReviewer =======================================

    public void assignReviewer(String paperId, XmlWrapper paperWrapper, String userId) {
        // TODO: Fix bug with userId and username
        boolean reviewerIsAuthorOfPaper = XmlExtractorUtil
                .extractSetOfAttributeValuesAndValidateNotEmpty(paperWrapper.getDocument(), "/paper/authors/*", "id")
                .stream().anyMatch(userId::equals);
        BadRequestUtils.throwInvalidRequestDataExceptionIf(reviewerIsAuthorOfPaper, String.format("Unable to assign the paper for review to user with ID %s", userId));

        RdfTriple assignedRdfTriple = new RdfTriple(userId, Predicate.ASSIGNED_TO, paperId);
        List<RdfTriple> rdfTriples = Lists.newArrayList(assignedRdfTriple);
        this.paperRepository.insertTriples(rdfTriples);
    }

    // ======================================= getRaw and getPdf =======================================

    public String getRawPaperForDownload(String paperId) {
        // TODO: Check if paper is published
        // If it's not published, only the author of paper and editor can download it.
        final boolean isPublished = true;
        ForbiddenUtils.throwInsufficientPrivilegesExceptionIf(!isPublished);

        return this.findById(paperId);
    }

    // ======================================= findById =======================================

    private final String SPARQL_ASK_IS_PAPER_PUBLISHED_QUERY = "PREFIX rv: <http://www.scit.org/rdfvocabulary/>\n" +
            "\n" + "ASK\n" + "WHERE {\n" + "\t?s rv:published <%s>.\n" + "}";

    private final String SPARQL_ASK_IS_USER_AUTHOR_OF_PAPER_QUERY = "PREFIX rv: <http://www.scit.org/rdfvocabulary/>\n" +
            "\n" + "ASK\n" + "WHERE {\n" + "\t<%s> rv:submitted <%s>.\n" + "}";

    public void checkCurrentUserAccess(String userId, String userRole, String paperId) {
        boolean paperPublished = this.paperRepository.ask(String.format(SPARQL_ASK_IS_PAPER_PUBLISHED_QUERY, paperId));
        ForbiddenUtils.throwInsufficientPrivilegesExceptionIf(userId == null && !paperPublished);
        boolean userIsAuthor = this.paperRepository.ask(String.format(SPARQL_ASK_IS_USER_AUTHOR_OF_PAPER_QUERY, userId, paperId));
        boolean userIsEditor = Role.EDITOR.getName().equals(userRole);
        ForbiddenUtils.throwInsufficientPrivilegesExceptionIf(!userIsAuthor && !userIsEditor && !paperPublished);
    }

    // ======================================= getAnonymousPaper =======================================

    public void anonymizePaper(XmlWrapper paperWrapper, String paperId, String userId) {
        boolean userIsAuthor = this.paperRepository.ask(String.format(SPARQL_ASK_IS_USER_AUTHOR_OF_PAPER_QUERY, userId, paperId));
        if(!userIsAuthor) {
            final Element element = paperWrapper.getDocument().getDocumentElement();
            element.removeChild(element.getElementsByTagName("paper:authors").item(0));
            paperWrapper.updateXml();
        }
    }

    // ======================================= getAuthorOfPaper =======================================
    private final String SPARQL_GET_AUTHOR_OF_PAPER_QUERY = "PREFIX rv: <http://www.scit.org/rdfvocabulary/>\n" +
            "\n" + "SELECT ?s\n" + "WHERE {\n" + "\t?s rv:submitted <%s>.\n" + "}";

    public String getAuthorOfPaper(String paperId) {
        List<String> userIds = this.paperRepository.selectSubjects(String.format(SPARQL_GET_AUTHOR_OF_PAPER_QUERY, paperId));
        NotFoundUtils.throwNotFoundExceptionIf(userIds.isEmpty(),
                RestApiErrors.entityWithGivenFieldNotFound(RestApiConstants.PAPER, RestApiConstants.ID));
        return userIds.get(0);
    }

    // ======================================= getAssignedPapers =======================================

    private final String SPARQL_GET_ASSIGNED_PAPERS_QUERY = "PREFIX rv: <http://www.scit.org/rdfvocabulary/>\n" +
            "\n" + "SELECT ?o\n" + "WHERE {\n" + "\t<%s> rv:assigned_to ?o.\n" + "}";

    public String getAssignedPapers(String currentUserId) {
        List<String> paperIds = this.paperRepository.selectSubjects(String.format(SPARQL_GET_ASSIGNED_PAPERS_QUERY, currentUserId));

        List<String> papers = paperIds.stream().map(id -> {
            return convertToXmlResponseString(this.findById(id));
        }).collect(Collectors.toList());

        // TODO: Refactor
        StringBuilder sb = new StringBuilder();
        for(String p : papers) {
            sb.append(p);
        }

        return sb.toString();

    }

    // ======================================= getSubmittedPapers =======================================

    private final String SPARQL_GET_SUBMITTED_PAPERS_QUERY = "PREFIX rv: <http://www.scit.org/rdfvocabulary/>\n" +
            "\n" + "SELECT ?o\n" + "WHERE {\n" + "\t?s rv:submitted ?o.\n" + "}";

    public String getSubmittedPapers() {
        List<String> paperIds = this.paperRepository.selectSubjects(SPARQL_GET_SUBMITTED_PAPERS_QUERY);

        List<String> papers = paperIds.stream().map(id -> {
            return convertToXmlResponseString(this.findById(id));
        }).collect(Collectors.toList());

        // TODO: Refactor
        StringBuilder sb = new StringBuilder();
        for(String p : papers) {
            sb.append(p);
        }

        return sb.toString();
    }

    // ======================================= getPapersInReview =======================================

    private final String SPARQL_GET_PAPERS_IN_REVIEW_QUERY = "PREFIX rv: <http://www.scit.org/rdfvocabulary/>\n" +
            "\n" + "SELECT ?o\n" + "WHERE {\n" + "\t<%s> rv:currently_reviewing ?o.\n" + "}";

    public String getPapersInReview(String currentUserId) {
        List<String> paperIds = this.paperRepository.selectSubjects(String.format(SPARQL_GET_PAPERS_IN_REVIEW_QUERY, currentUserId));

        List<String> papers = paperIds.stream().map(id -> {
            return convertToXmlResponseString(this.findById(id));
        }).collect(Collectors.toList());

        // TODO: Refactor
        StringBuilder sb = new StringBuilder();
        for(String p : papers) {
            sb.append(p);
        }

        return sb.toString();
    }

    // ======================================= common stuff =======================================

    public String findById(String paperId) {
        final String paperXml = this.paperRepository.findById(paperId);
        NotFoundUtils.throwNotFoundExceptionIf(StringUtils.isEmpty(paperXml),
                RestApiErrors.entityWithGivenFieldNotFound(RestApiConstants.PAPER, RestApiConstants.ID));
        return paperXml;
    }

    public String getPaperTitle(String paperId) {
        String paperXml = this.findById(paperId);
        XmlWrapper xmlWrapper = new XmlWrapper(paperXml);
        return XmlExtractorUtil.extractStringAndValidateNotBlank(xmlWrapper.getDocument(), "/paper/title");
    }

    public void revokePaper(String paperId) {
        this.paperDatabaseValidator.validateExportRequest(paperId);
        this.paperRepository.remove(paperId);
        this.paperRepository.deleteAllMetadata(paperId);
    }

    private final String SPARQL_ASK_IS_USER_REVIEWING_PAPER_QUERY = "PREFIX rv: <http://www.scit.org/rdfvocabulary/>\n" +
            "\n" + "ASK\n" + "WHERE {\n" + "\t<%s> rv:currently_reviewing <%s>.\n" + "}";

    public void checkIsUserReviewingPaper(String userId, String paperId) {
        boolean userIsReviewing = this.paperRepository.ask(String.format(SPARQL_ASK_IS_USER_REVIEWING_PAPER_QUERY, userId, paperId));
        ForbiddenUtils.throwInsufficientPrivilegesExceptionIf(!userIsReviewing);
    }



    public void publishPaper(String paperId) {

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

    public void rejectPaper(String paperId) {

        this.paperRepository.deleteMetadataByObject(paperId);
    }
}
