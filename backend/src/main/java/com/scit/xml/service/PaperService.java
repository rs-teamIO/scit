package com.scit.xml.service;

import com.google.common.collect.Lists;
import com.scit.xml.common.Constants;
import com.scit.xml.common.Predicate;
import com.scit.xml.common.api.RestApiConstants;
import com.scit.xml.common.api.RestApiErrors;
import com.scit.xml.common.util.*;
import com.scit.xml.exception.InternalServerException;
import com.scit.xml.model.paper.Paper;
import com.scit.xml.rdf.RdfExtractor;
import com.scit.xml.rdf.RdfTriple;
import com.scit.xml.repository.PaperRepository;
import com.scit.xml.repository.RdfRepository;
import com.scit.xml.service.converter.DocumentConverter;
import com.scit.xml.service.validator.database.PaperDatabaseValidator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaperService {

    @Value("classpath:xsl/paper.xsl")
    private Resource stylesheet;

    private final PaperRepository paperRepository;
    private final PaperDatabaseValidator paperDatabaseValidator;
    private final DocumentConverter documentConverter;
    private final RdfRepository rdfRepository;

    public String createPaper(Paper paper, String creatorId) {
        // TODO: DB Validation (if necessary)
        // this.paperDatabaseValidator.validateCreateRequest(paper, creatorUsername);
        String id = this.paperRepository.save(paper);

        List<RdfTriple> rdfTriples = this.extractRdfTriples(id, creatorId);
        this.rdfRepository.insertTriples(rdfTriples);

        return id;
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

    private List<RdfTriple> extractRdfTriples(String id, String _creatorId) {
        // TODO: This method can't stay like this

        final String paperXml = this.paperRepository.findById(id);
        final XmlWrapper paperWrapper = new XmlWrapper(paperXml);
        final RdfExtractor rdfExtractor = new RdfExtractor(id, Constants.PAPER_SCHEMA_URL, Predicate.PREFIX);
        List<RdfTriple> rdfTriples = rdfExtractor.extractRdfTriples(paperWrapper);

        final String paperId = RdfExtractor.wrapId(id);
        // TODO: Fix this - shouldn't be wrapped
        final String creatorId = RdfExtractor.wrapId(_creatorId);
        RdfTriple createdRdfTriple = new RdfTriple(creatorId, Predicate.CREATED, paperId);
        RdfTriple submittedRdfTriple = new RdfTriple(creatorId, Predicate.SUBMITTED, paperId);
        rdfTriples.add(createdRdfTriple);
        rdfTriples.add(submittedRdfTriple);

        return rdfTriples;
    }

    public String getRawPaperForDownload(String paperId) {
        // 1. Check if paper is published
        final boolean isPublished = this.isPaperPublished(paperId);
        ForbiddenUtils.throwInsufficientPrivilegesExceptionIf(!isPublished);

        return this.findById(paperId);
    }

    public boolean isPaperPublished(String paperId) {
        // TODO: Check if paper is published.
        return true;
    }

    private final String SPARQL_GET_PAPERS_OF_USER_QUERY = "PREFIX rv: <http://www.scit.org/rdfvocabulary/>\n" +
            "\n" + "SELECT ?o\n" + "WHERE {\n" + "\t<%s> rv:created ?o.\n" + "}";

    public String getPapersByUserId(String currentUserId) {
        List<String> paperIds = rdfRepository.selectSubjects(String.format(SPARQL_GET_PAPERS_OF_USER_QUERY, currentUserId));

        StringBuilder sb = new StringBuilder();
        sb.append("<papers>\n");
        paperIds.forEach(id -> sb.append(String.format(this.findById(id))));
        sb.append("</papers>\n");

        return sb.toString();
    }

    public String getPublishedPapers(String userId) {
        // TODO: Get published papers
        return "";
    }

    public void assignReviewer(String paperId, XmlWrapper paperWrapper, String username) {
        List<String> authorUsernames = XmlExtractorUtil.extractSetOfAttributeValuesAndValidateNotEmpty(paperWrapper.getDocument(), "/paper/authors/*", "username");
        boolean invalidAssignment = authorUsernames.stream().anyMatch(username::equals);
        BadRequestUtils.throwInvalidRequestDataExceptionIf(invalidAssignment, String.format("Unable to assign the paper for review to user with username %s", username));

        RdfTriple assignedRdfTriple = new RdfTriple(RdfExtractor.wrapId(username), Predicate.ASSIGNED_TO, RdfExtractor.wrapId(paperId));
        List<RdfTriple> rdfTriples = Lists.newArrayList(assignedRdfTriple);
        this.rdfRepository.insertTriples(rdfTriples);
    }

    public Paper findByIdReturnsPaper(String paperId) {
        final Paper paper = this.paperRepository.findByIdReturnsPaper(paperId);
        NotFoundUtils.throwNotFoundExceptionIf(paper == null,
                RestApiErrors.entityWithGivenFieldNotFound(RestApiConstants.PAPER, RestApiConstants.ID));
        return paper;

    }
}
