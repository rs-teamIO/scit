package com.scit.xml.service;

import com.google.common.collect.Lists;
import com.scit.xml.common.Constants;
import com.scit.xml.common.Predicate;
import com.scit.xml.common.api.RestApiConstants;
import com.scit.xml.common.api.RestApiErrors;
import com.scit.xml.common.util.*;
import com.scit.xml.exception.InternalServerException;
import com.scit.xml.model.paper.Paper;
import com.scit.xml.model.paper.PaperStatus;
import com.scit.xml.model.user.Role;
import com.scit.xml.rdf.RdfTriple;
import com.scit.xml.repository.PaperRepository;
import com.scit.xml.security.JwtTokenDetailsUtil;
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
import java.util.Collections;
import java.util.List;
import java.util.Map;
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
        paper.getPaperInfo().setStatus(PaperStatus.SUBMITTED.getName());
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

    public Resource getPaperById(String paperId, String userId, String userRole) {
        boolean paperPublished = this.paperRepository.ask(String.format(Constants.SPARQLQueries.ASK_IS_PAPER_PUBLISHED_QUERY, paperId));
        ForbiddenUtils.throwInsufficientPrivilegesExceptionIf(userId == null && !paperPublished);
        boolean userIsAuthor = this.paperRepository.ask(String.format(Constants.SPARQLQueries.ASK_IS_USER_AUTHOR_OF_PAPER_QUERY, userId, paperId));
        boolean userIsEditor = Role.EDITOR.getName().equals(userRole);
        ForbiddenUtils.throwInsufficientPrivilegesExceptionIf(!userIsAuthor && !userIsEditor && !paperPublished);

        return this.exportToHtml(paperId);
    }


    // ======================================= getSubmittedPapers =======================================

    public List<String> getSubmittedPapers() {
        List<String> paperIds = this.paperRepository.selectSubjects(Constants.SPARQLQueries.GET_SUBMITTED_PAPERS_QUERY);
        return paperIds.stream()
                .map(id -> this.findById(id))
                .collect(Collectors.toList());
    }


    // ======================================= getAssignedPapers =======================================

    public List<String> getAssignedPapers(String currentUserId) {
        List<String> paperIds = this.paperRepository.selectSubjects(String.format(Constants.SPARQLQueries.GET_ASSIGNED_PAPERS_QUERY, currentUserId));
        return paperIds.stream()
                .map(id -> this.findById(id))
                .collect(Collectors.toList());
    }


    // ======================================= getPapersInReview =======================================

    public List<String> getPapersInReview(String currentUserId) {
        List<String> paperIds = this.paperRepository.selectSubjects(String.format(Constants.SPARQLQueries.GET_PAPERS_IN_REVIEW_QUERY, currentUserId));
        return paperIds.stream()
                .map(id -> this.findById(id))
                .collect(Collectors.toList());
    }


    // ======================================= assignReviewer =======================================

    public void assignReviewer(String paperId, String userId) {
        String username = XmlExtractorUtil.extractStringAndValidateNotBlank(new XmlWrapper(this.userService.findById(userId)).getDocument(), "/user/username");
        boolean reviewerIsAuthorOfPaper = XmlExtractorUtil
                .extractSetOfAttributeValuesAndValidateNotEmpty(new XmlWrapper(this.findById(paperId)).getDocument(), "/paper/authors/*", "username")
                .stream().anyMatch(username::equals);
        BadRequestUtils.throwInvalidRequestDataExceptionIf(reviewerIsAuthorOfPaper,
                String.format("Unable to assign the paper for review to user with ID %s", userId));

        this.paperRepository.writeAssignMetadata(userId, paperId);
    }


    // ======================================= getCurrentUserPapers =======================================

    public List<String> getPapersByUserId(String currentUserId) {
        List<String> paperIds = this.paperRepository.selectSubjects(String.format(Constants.SPARQLQueries.GET_PAPERS_OF_USER_QUERY, currentUserId));
        return paperIds.stream()
                .map(id -> this.findById(id))
                .collect(Collectors.toList());
    }


    // ======================================= getReviewedPapers =======================================

    public List<String> getReviewedPapers() {
        List<String> paperIds = this.paperRepository.selectSubjects(String.format(Constants.SPARQLQueries.GET_REVIEWED_PAPERS_QUERY));
        return paperIds.stream()
                .map(id -> this.findById(id))
                .collect(Collectors.toList());
    }


    // ======================================= getPublishedPapers =======================================

    public List<String> getPublishedPapers() {
        List<String> paperIds = this.paperRepository.selectSubjects(String.format(Constants.SPARQLQueries.GET_PUBLISHED_PAPERS_QUERY));
        return paperIds.stream()
                .map(id -> this.findById(id))
                .collect(Collectors.toList());
    }


    // ======================================= getPublishedPapersByText =======================================

    public List<String> getPublishedPapersByText(String text) {
        List<String> publishedPapers = this.getPublishedPapers().stream()
                .map(p -> XmlExtractorUtil.extractPaperId(p))
                .collect(Collectors.toList());
        return this.paperRepository.findByText(text).stream()
                .filter(publishedPapers::contains)
                .collect(Collectors.toList());
    }


    // ======================================= getUsersPapersByText =======================================

    public List<String> getUsersPapersByText(String text, String currentUserId) {
        List<String> papersOfUser = this.getPapersByUserId(currentUserId).stream()
                .map(p -> XmlExtractorUtil.extractPaperId(p))
                .collect(Collectors.toList());
        return this.paperRepository.findByText(text).stream()
                .filter(papersOfUser::contains)
                .collect(Collectors.toList());
    }


    // ======================================= getPublishedPapersByMetadata =======================================

    public List<String> getPublishedPapersByMetadata(String doi, String journalId, String category, Integer year, String authorName) {

        doi = doi == null || doi.isEmpty() ? "?doi" : "\"" + doi + "\"";
        journalId = journalId == null || journalId.isEmpty() ? "?journalId" : "\"" + journalId + "\"";
        category = category == null || category.isEmpty() ? "?category" : "\"" + category + "\"";
        String yearStr = year == null ? "" : year.toString();
        authorName = authorName == null ? "" : authorName;

        String query = String.format(Constants.SPARQLQueries.GET_PUBLISHED_PAPERS_BY_METADATA_QUERY,
                doi, journalId, category, yearStr, authorName);

        List<String> paperIds = this.paperRepository.selectSubjects(query);

        return paperIds.stream()
                .map(id -> this.findById(id))
                .collect(Collectors.toList());
    }

    // ======================================= getPublishedPapersByMetadata =======================================

    public List<String> getUsersPapersByMetadata(String userId, String doi, String journalId, String category, Integer year, String authorName) {

        doi = doi == null || doi.isEmpty() ? "?doi" : "\"" + doi + "\"";
        journalId = journalId == null || journalId.isEmpty() ? "?journalId" : "\"" + journalId + "\"";
        category = category == null || category.isEmpty() ? "?category" : "\"" + category + "\"";
        String yearStr = year == null ? "" : year.toString();
        authorName = authorName == null ? "" : authorName;

        String query = String.format(Constants.SPARQLQueries.GET_USERS_PAPERS_BY_METADATA_QUERY,
                userId, doi, journalId, category, yearStr, authorName);

        List<String> paperIds = this.paperRepository.selectSubjects(query);

        return paperIds.stream()
                .map(id -> this.findById(id))
                .collect(Collectors.toList());
    }


    // ======================================= getRaw and getPdf =======================================

    public String getRawPaperForDownload(String paperId) {
        boolean isPublished = this.paperRepository.ask(String.format(Constants.SPARQLQueries.ASK_IS_PAPER_PUBLISHED_QUERY, paperId));
        ForbiddenUtils.throwInsufficientPrivilegesExceptionIf(!isPublished);

        return this.findById(paperId);
    }


    // ======================================= getAnonymizedPaper =======================================

    public String getAnonymizedPaper(String paperId, String userId) {
        String paperXml = this.findById(paperId);
        XmlWrapper paperWrapper = new XmlWrapper(paperXml);

        boolean userIsAuthor = this.paperRepository.ask(String.format(Constants.SPARQLQueries.ASK_IS_USER_AUTHOR_OF_PAPER_QUERY, userId, paperId));
        if(!userIsAuthor) {
            final Element element = paperWrapper.getDocument().getDocumentElement();
            // TODO: Remove string value, use constant instead
            element.removeChild(element.getElementsByTagName("paper:authors").item(0));
            paperWrapper.updateXml();
        }

        return paperWrapper.getXml();
    }


    // ======================================= getAuthorsOfPaper =======================================

    public List<String> getIdentifiersOfPaperAuthors(String paperId) {
        List<String> userIds = this.paperRepository.selectSubjects(String.format(Constants.SPARQLQueries.GET_AUTHORS_OF_PAPER_QUERY, paperId));
        NotFoundUtils.throwNotFoundExceptionIf(userIds.isEmpty(),
                RestApiErrors.entityWithGivenFieldNotFound(RestApiConstants.PAPER, RestApiConstants.ID));
        return userIds;
    }


    // ======================================= getAuthorsOfPaper =======================================

    public List<String> getReviewersOfPaper(String paperId) {
        this.findById(paperId);
        List<String> userIds = this.paperRepository.selectSubjects(String.format(Constants.SPARQLQueries.GET_REVIEWERS_OF_PAPER_QUERY, paperId));
        return userIds.stream()
                .map(id -> this.userService.findById(id))
                .collect(Collectors.toList());
    }


    // ======================================= editPaper =======================================

    public void editPaper(String xml, String paperId) {
        String currentUserId = JwtTokenDetailsUtil.getCurrentUserId();
        boolean currentUserAllowedToEdit = this.getIdentifiersOfPaperAuthors(paperId).contains(currentUserId);
        boolean paperIsPublished = this.paperRepository.ask(String.format(Constants.SPARQLQueries.ASK_IS_PAPER_PUBLISHED_QUERY, paperId));
        ForbiddenUtils.throwInsufficientPrivilegesExceptionIf(!currentUserAllowedToEdit || paperIsPublished);

        // TODO: Change status of paper to SUBMITTED (or maybe to something else?)

        this.paperRepository.updateAndRemoveMetadata(xml, paperId);
    }


    // ======================================= revokePaper =======================================

    public void revokePaper(String paperId) {
        this.paperDatabaseValidator.validateExportRequest(paperId);
        // TODO: Don't physically remove paper, just set it's status to revoked (probably no)?
        this.paperRepository.remove(paperId);
        this.paperRepository.deleteAllMetadata(paperId);
    }


    // ======================================= rejectPaper =======================================

    public void rejectPaper(String paperId) {
        // TODO: Set paper's status to rejected (TEST IF IT WORKS)
        String paperXml = this.paperRepository.findById(paperId);
        XmlWrapper paperWrapper = new XmlWrapper(paperXml);
        paperWrapper.set("/paper/paper_info/status", PaperStatus.REJECTED.getName());
        this.paperRepository.update(paperWrapper.getXml(), paperId);

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

        paperWrapper.set("/paper/paper_info/status", PaperStatus.PUBLISHED.getName());
        this.paperRepository.update(paperWrapper.getXml(), paperId);

        this.paperRepository.insertTriples(rdfTriples);
    }


    // ======================================= recommendAuthors =======================================

    public List<String> recommendAuthors(String paperId) {
        String paperXml = this.findById(paperId);
        Map<String, Integer> authorHeatMap = this.paperRepository.selectSubjects(String.format(Constants.SPARQLQueries.GET_SUGGESTED_AUTHORS_QUERY, paperId))
                .stream().collect(Collectors.toMap(x -> x, x -> 0));
        List<String> keywords = XmlExtractorUtil.extractChildrenContentToList(new XmlWrapper(paperXml).getDocument(), "//paper/abstract/keywords");
        authorHeatMap.keySet().stream().forEach(authorId -> {
            keywords.stream().forEach(keyword -> {
                Integer papersCount = this.paperRepository.count(String.format(Constants.SPARQLQueries.COUNT_PAPERS_OF_AUTHOR_CONTAINING_KEYWORD_QUERY, keyword, authorId));
                authorHeatMap.computeIfPresent(authorId, (k, v) -> v * papersCount);
            });
        });
        List<String> authorIds = authorHeatMap.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .map(kv -> kv.getKey())
                .collect(Collectors.toList());

        return authorIds;
    }


    // ======================================= common stuff =======================================

    public String findById(String paperId) {
        final String paperXml = this.paperRepository.findById(paperId);
        NotFoundUtils.throwNotFoundExceptionIf(StringUtils.isEmpty(paperXml),
                RestApiErrors.entityWithGivenFieldNotFound(RestApiConstants.PAPER, RestApiConstants.ID));
        return paperXml;
    }

    public void update(String paperXml, String paperId) {
        this.paperRepository.update(paperXml, paperId);
    }
}
