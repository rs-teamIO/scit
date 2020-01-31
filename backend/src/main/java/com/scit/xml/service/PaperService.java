package com.scit.xml.service;

import com.google.common.collect.Lists;
import com.scit.xml.common.Constants;
import com.scit.xml.common.Predicate;
import com.scit.xml.common.api.RestApiConstants;
import com.scit.xml.common.api.RestApiErrors;
import com.scit.xml.common.util.*;
import com.scit.xml.exception.InsufficientPrivilegesException;
import com.scit.xml.exception.InternalServerException;
import com.scit.xml.model.paper.Paper;
import com.scit.xml.model.user.Role;
import com.scit.xml.rdf.RdfExtractor;
import com.scit.xml.rdf.RdfTriple;
import com.scit.xml.repository.PaperRepository;
import com.scit.xml.repository.RdfRepository;
import com.scit.xml.service.converter.DocumentConverter;
import com.scit.xml.service.validator.database.PaperDatabaseValidator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.tools.ant.taskdefs.condition.Not;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.w3c.dom.Element;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
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
    private final RdfRepository rdfRepository;

    // ======================================= create =======================================

    public String createPaper(Paper paper, String creatorId) {
        // TODO: DB Validation (if necessary)
        // this.paperDatabaseValidator.validateCreateRequest(paper, creatorUsername);
        String id = this.paperRepository.save(paper);

        List<RdfTriple> rdfTriples = this.extractRdfTriples(id, creatorId);
        this.rdfRepository.insertTriples(rdfTriples);

        return id;
    }

    private List<RdfTriple> extractRdfTriples(String id, String creatorId) {
        final String paperXml = this.paperRepository.findById(id);
        final XmlWrapper paperWrapper = new XmlWrapper(paperXml);
        final RdfExtractor rdfExtractor = new RdfExtractor(id, Constants.PAPER_SCHEMA_URL, Predicate.PREFIX);
        List<RdfTriple> rdfTriples = rdfExtractor.extractRdfTriples(paperWrapper);

        final String paperId = RdfExtractor.wrapId(id);
        final String userId = RdfExtractor.wrapId(creatorId);
        RdfTriple createdRdfTriple = new RdfTriple(userId, Predicate.CREATED, paperId);
        RdfTriple submittedRdfTriple = new RdfTriple(userId, Predicate.SUBMITTED, paperId);
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

    // ======================================= getPapers =======================================

    private final String SPARQL_GET_PAPERS_OF_USER_QUERY = "PREFIX rv: <http://www.scit.org/rdfvocabulary/>\n" +
            "\n" + "SELECT ?o\n" + "WHERE {\n" + "\t<%s> rv:created ?o.\n" + "}";

    public String getPapersByUserId(String currentUserId) {
        List<String> paperIds = rdfRepository.selectSubjects(String.format(SPARQL_GET_PAPERS_OF_USER_QUERY, currentUserId));

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
        sb.append(indent).append("</id>");
        sb.append(indent).append("<title>");
        sb.append(title);
        sb.append(indent).append("</title>");
        sb.append("</paper>");

        return sb.toString();
    }

    // ======================================= assignReviewer =======================================

    public void assignReviewer(String paperId, XmlWrapper paperWrapper, String userId) {
        boolean reviewerIsAuthorOfPaper = XmlExtractorUtil
                .extractSetOfAttributeValuesAndValidateNotEmpty(paperWrapper.getDocument(), "/paper/authors/*", "id")
                .stream().anyMatch(userId::equals);
        BadRequestUtils.throwInvalidRequestDataExceptionIf(reviewerIsAuthorOfPaper, String.format("Unable to assign the paper for review to user with ID %s", userId));

        RdfTriple assignedRdfTriple = new RdfTriple(RdfExtractor.wrapId(userId), Predicate.ASSIGNED_TO, RdfExtractor.wrapId(paperId));
        List<RdfTriple> rdfTriples = Lists.newArrayList(assignedRdfTriple);
        this.rdfRepository.insertTriples(rdfTriples);
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
        boolean paperPublished = rdfRepository.ask(String.format(SPARQL_ASK_IS_PAPER_PUBLISHED_QUERY, paperId));
        ForbiddenUtils.throwInsufficientPrivilegesExceptionIf(userId == null && !paperPublished);
        boolean userIsAuthor = rdfRepository.ask(String.format(SPARQL_ASK_IS_USER_AUTHOR_OF_PAPER_QUERY, userId, paperId));
        boolean userIsEditor = Role.EDITOR.getName().equals(userRole);
        ForbiddenUtils.throwInsufficientPrivilegesExceptionIf(!userIsAuthor && !userIsEditor && !paperPublished);
    }

    // ======================================= getAnonymousPaper =======================================

    public void anonymizePaper(XmlWrapper paperWrapper, String paperId, String userId) {
        boolean userIsAuthor = rdfRepository.ask(String.format(SPARQL_ASK_IS_USER_AUTHOR_OF_PAPER_QUERY, userId, paperId));
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
        List<String> userIds = rdfRepository.selectSubjects(String.format(SPARQL_GET_AUTHOR_OF_PAPER_QUERY, paperId));
        NotFoundUtils.throwNotFoundExceptionIf(userIds.isEmpty(),
                RestApiErrors.entityWithGivenFieldNotFound(RestApiConstants.PAPER, RestApiConstants.ID));
        return userIds.get(0);
    }

    // ======================================= getAssignedPapers =======================================

    private final String SPARQL_GET_ASSIGNED_PAPERS_QUERY = "PREFIX rv: <http://www.scit.org/rdfvocabulary/>\n" +
            "\n" + "SELECT ?o\n" + "WHERE {\n" + "\t<%s> rv:assigned_to ?o.\n" + "}";

    public String getAssignedPapers(String currentUserId) {
        List<String> paperIds = rdfRepository.selectSubjects(String.format(SPARQL_GET_ASSIGNED_PAPERS_QUERY, currentUserId));

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
        List<String> paperIds = rdfRepository.selectSubjects(SPARQL_GET_SUBMITTED_PAPERS_QUERY);

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
        List<String> paperIds = rdfRepository.selectSubjects(String.format(SPARQL_GET_PAPERS_IN_REVIEW_QUERY, currentUserId));

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
        this.rdfRepository.deleteAllMetadata(paperId);
    }
}
