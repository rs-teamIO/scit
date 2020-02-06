package com.scit.xml.controller;

import com.scit.xml.common.Constants;
import com.scit.xml.common.api.RestApiConstants;
import com.scit.xml.common.api.RestApiEndpoints;
import com.scit.xml.common.api.RestApiRequestParameters;
import com.scit.xml.common.util.*;
import com.scit.xml.dto.XmlResponse;
import com.scit.xml.model.paper.Paper;
import com.scit.xml.model.user.User;
import com.scit.xml.security.JwtTokenDetailsUtil;
import com.scit.xml.service.EmailService;
import com.scit.xml.service.PaperService;
import com.scit.xml.service.UserService;
import com.scit.xml.service.validator.dto.PaperDtoValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.mail.MessagingException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(RestApiEndpoints.PAPERS)
@RequiredArgsConstructor
public class PapersController {

    private final PaperService paperService;
    private final PaperDtoValidator paperDtoValidator;
    private final EmailService emailService;
    private final UserService userService;

    /**
     * POST /api/v1/papers
     * ACCESS LEVEL: Author only
     *
     * Creates a new {@link Paper}.
     * @param xml XML string representation of the {@link Paper}
     */
    @PreAuthorize("hasAuthority('author')")
    @PostMapping(consumes = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> create(@RequestBody String xml) throws MessagingException {
        Paper paper = this.paperDtoValidator.validate(xml);
        String paperId = this.paperService.createPaper(paper, JwtTokenDetailsUtil.getCurrentUserId());

        String editorEmail =  XmlExtractorUtil.extractUserEmail(this.userService.findByUsername(Constants.EDITOR_USERNAME));
        byte[] pdf = ResourceUtils.convertResourceToByteArray(this.paperService.exportToPdf(paperId));
        String html = ResourceUtils.convertResourceToString(this.paperService.exportToHtml(paperId));
        this.emailService.sendPaperSubmissionNotificationEmail(editorEmail, paper, pdf, html);
        
        UriComponents urlLocation = UriComponentsBuilder.newInstance()
        		.path(RestApiEndpoints.PAPERS)
        		.query(RestApiRequestParameters.ID+"={id}")
        		.buildAndExpand(paperId);
        return ResponseEntity.created(urlLocation.toUri()).build();
    }

    // TODO: Move to PaperController class?
    /**
     * GET api/v1/papers/html
     * ACCESS LEVEL: Anyone
     *
     * Returns the HTML representation of a {@link Paper}
     * @param paperId unique identifier of the {@link Paper}
     */
    @GetMapping(value = RestApiEndpoints.HTML,
                produces = MediaType.APPLICATION_XHTML_XML_VALUE)
    public ResponseEntity getPaperById(@RequestParam(RestApiRequestParameters.PAPER_ID) String paperId) {
        String userXml = this.userService.findById(JwtTokenDetailsUtil.getCurrentUserId());
        XmlWrapper userWrapper = new XmlWrapper(userXml);
        String userId = XmlExtractorUtil.extractString(userWrapper.getDocument(), "/user/@id");
        String userRole = XmlExtractorUtil.extractString(userWrapper.getDocument(), "/user/role");

        String html = ResourceUtils.convertResourceToString(this.paperService.getPaperById(paperId, userId, userRole));

        return ResponseEntity.ok(html);
    }

    /**
     * GET api/v1/papers/submitted
     * ACCESS LEVEL: Editor only
     *
     * Returns the IDs and titles of {@link Paper}s that have been submitted
     */
    @PreAuthorize("hasAuthority('editor')")
    @GetMapping(value = RestApiEndpoints.SUBMITTED,
                produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity getSubmittedPapers() {
        List<String> papers = this.paperService.getSubmittedPapers();

        StringBuilder sb = new StringBuilder();
        papers.stream()
                .map(XmlResponseUtils::convertPaperToXmlResponse)
                .collect(Collectors.toList())
                .forEach(s -> sb.append(s.trim()));

        String responseBody = XmlResponseUtils.wrapResponse(new XmlResponse(RestApiConstants.PAPERS, sb.toString()));

        return ResponseEntity.ok(responseBody);
    }

    /**
     * GET api/v1/papers/assigned
     * ACCESS LEVEL: Author only
     *
     * Returns the IDs and titles of {@link Paper}s assigned to the current user for review
     */
    @PreAuthorize("hasAuthority('author')")
    @GetMapping(value = RestApiEndpoints.ASSIGNED,
            produces = { MediaType.APPLICATION_XML_VALUE } )
    public ResponseEntity getAssignedPapers() {
        List<String> papers = this.paperService.getAssignedPapers(JwtTokenDetailsUtil.getCurrentUserId());

        StringBuilder sb = new StringBuilder();
        papers.stream()
                .map(XmlResponseUtils::convertPaperToXmlResponse)
                .collect(Collectors.toList())
                .forEach(s -> sb.append(s.trim()));

        String responseBody = XmlResponseUtils.wrapResponse(new XmlResponse(RestApiConstants.PAPERS, sb.toString()));

        return ResponseEntity.ok(responseBody);
    }

    /**
     * GET api/v1/papers/in-review
     * ACCESS LEVEL: Author only
     *
     * Returns the IDs and titles of {@link Paper}s the current user is currently reviewing
     */
    @PreAuthorize("hasAuthority('author')")
    @GetMapping(value = RestApiEndpoints.IN_REVIEW,
            produces = { MediaType.APPLICATION_XML_VALUE } )
    public ResponseEntity getPapersInReview() {
        List<String> papers = this.paperService.getPapersInReview(JwtTokenDetailsUtil.getCurrentUserId());

        StringBuilder sb = new StringBuilder();
        papers.stream()
                .map(XmlResponseUtils::convertPaperToXmlResponse)
                .collect(Collectors.toList())
                .forEach(s -> sb.append(s.trim()));

        String responseBody = XmlResponseUtils.wrapResponse(new XmlResponse(RestApiConstants.PAPERS, sb.toString()));

        return ResponseEntity.ok(responseBody);
    }

    // TODO: Move to PaperController class?
    /**
     * PUT api/v1/papers/assign
     * ACCESS LEVEL: Editor only
     *
     * Assigns the {@link Paper} to the given {@link User} for review
     * @param paperId unique identifier of the {@link Paper} to be assigned
     * @param userId unique identifier of the {@link User} the paper is being assigned to
     */
    @PreAuthorize("hasAuthority('editor')")
    @PutMapping(value = RestApiEndpoints.ASSIGN)
    public ResponseEntity assignReviewer(@RequestParam(RestApiRequestParameters.PAPER_ID) String paperId,
                                         @RequestParam(RestApiRequestParameters.USER_ID) String userId) throws MessagingException {
        BadRequestUtils.throwInvalidRequestDataExceptionIf(userId.equals(JwtTokenDetailsUtil.getCurrentUserId()),
                String.format("Unable to assign the paper for review to user with ID %s", userId));

        this.paperService.assignReviewer(paperId, userId);

        String paperTitle = XmlExtractorUtil.extractPaperTitle(paperService.findById(paperId));
        String reviewerEmail = XmlExtractorUtil.extractUserEmail(userService.findById(userId));
        this.emailService.sendPaperAssignmentNotificationEmail(reviewerEmail, paperTitle);

        return ResponseEntity.ok().build();
    }

    /**
     * GET api/v1/papers/me
     * ACCESS LEVEL: Author only
     *
     * Returns the IDs and titles of {@link Paper}s the current user has created.
     */
    @PreAuthorize("hasAuthority('author')")
    @GetMapping(value = RestApiEndpoints.CURRENT_USER,
                produces = MediaType.APPLICATION_XML_VALUE )
    public ResponseEntity getCurrentUserPapers() {
        String userId = JwtTokenDetailsUtil.getCurrentUserId();

        List<String> papers = this.paperService.getPapersByUserId(userId);
        StringBuilder sb = new StringBuilder();
        papers.stream()
                .map(XmlResponseUtils::convertPaperToXmlResponse)
                .collect(Collectors.toList())
                .forEach(s -> sb.append(s.trim()));

        String responseBody = XmlResponseUtils.wrapResponse(new XmlResponse(RestApiConstants.PAPERS, sb.toString()));
        return ResponseEntity.ok(responseBody);
    }

    /**
     * GET api/v1/papers/reviewed
     * ACCESS LEVEL: Editor only
     *
     * Returns the IDs and titles of {@link Paper}s that have been published.
     */
    @PreAuthorize("hasAuthority('editor')")
    @GetMapping(value = RestApiEndpoints.REVIEWED,
                produces = MediaType.APPLICATION_XML_VALUE )
    public ResponseEntity getReviewedPapers() {
        List<String> papers = this.paperService.getReviewedPapers();
        StringBuilder sb = new StringBuilder();
        papers.stream()
                .map(XmlResponseUtils::convertPaperToXmlResponse)
                .collect(Collectors.toList())
                .forEach(s -> sb.append(s.trim()));

        String responseBody = XmlResponseUtils.wrapResponse(new XmlResponse(RestApiConstants.PAPERS, sb.toString()));
        return ResponseEntity.ok(responseBody);
    }

    /**
     * GET api/v1/papers/published
     * ACCESS LEVEL: Anyone
     *
     * Returns the IDs and titles of {@link Paper}s that have been published.
     */
    @GetMapping(value = RestApiEndpoints.PUBLISHED,
                produces = MediaType.APPLICATION_XML_VALUE )
    public ResponseEntity getPublishedPapers() {
        List<String> papers = this.paperService.getPublishedPapers();
        StringBuilder sb = new StringBuilder();
        papers.stream()
                .map(XmlResponseUtils::convertPaperToXmlResponse)
                .collect(Collectors.toList())
                .forEach(s -> sb.append(s.trim()));

        String responseBody = XmlResponseUtils.wrapResponse(new XmlResponse(RestApiConstants.PAPERS, sb.toString()));
        return ResponseEntity.ok(responseBody);
    }

    /**
     * GET api/v1/papers/
     * ACCESS LEVEL: Anyone
     *
     * Returns the IDs and titles of {@link Paper}s that have been published.
     */
    @GetMapping(produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity getPapersOfAuthor(@RequestParam(RestApiRequestParameters.USER_ID) String userId) {
        List<String> papers = this.paperService.getPapersByUserId(userId);
        StringBuilder sb = new StringBuilder();
        papers.stream()
                .map(XmlResponseUtils::convertPaperToXmlResponse)
                .collect(Collectors.toList())
                .forEach(s -> sb.append(s.trim()));

        String responseBody = XmlResponseUtils.wrapResponse(new XmlResponse(RestApiConstants.PAPERS, sb.toString()));
        return ResponseEntity.ok(responseBody);
    }

    /**
     * GET api/v1/papers/search-by-text
     * ACCESS LEVEL: Anyone
     *
     * Returns the IDs and titles of published {@link Paper}s that contain given text.
     */
    @GetMapping(value = RestApiEndpoints.SEARCH_BY_TEXT,
                produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity searchPublishedPapersByText(@RequestParam(RestApiRequestParameters.TEXT) String text) {
        List<String> paperIds = this.paperService.getPublishedPapersByText(text);
        StringBuilder sb = new StringBuilder();
        paperIds.stream()
                .map(id -> this.paperService.findById(id))
                .map(XmlResponseUtils::convertPaperToXmlResponse)
                .collect(Collectors.toList())
                .forEach(s -> sb.append(s.trim()));

        String responseBody = XmlResponseUtils.wrapResponse(new XmlResponse(RestApiConstants.PAPERS, sb.toString()));
        return ResponseEntity.ok(responseBody);
    }

    /**
     * GET api/v1/papers/search-by-text/private
     * ACCESS LEVEL: Only authenticated users
     *
     * Returns the IDs and titles of {@link Paper}s that contain given text and are created by the current user.
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = RestApiEndpoints.SEARCH_BY_TEXT_PRIVATE,
                produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity searchUsersPapersByText(@RequestParam(RestApiRequestParameters.TEXT) String text) {
        List<String> paperIds = this.paperService.getUsersPapersByText(text, JwtTokenDetailsUtil.getCurrentUserId());
        StringBuilder sb = new StringBuilder();
        paperIds.stream()
                .map(id -> this.paperService.findById(id))
                .map(XmlResponseUtils::convertPaperToXmlResponse)
                .collect(Collectors.toList())
                .forEach(s -> sb.append(s.trim()));

        String responseBody = XmlResponseUtils.wrapResponse(new XmlResponse(RestApiConstants.PAPERS, sb.toString()));
        return ResponseEntity.ok(responseBody);
    }

    /**
     * GET api/v1/papers/search-by-metadata
     * ACCESS LEVEL: Anyone
     *
     * Returns the IDs and titles of published {@link Paper}s that match given metadata parameters.
     */
    @GetMapping(value = RestApiEndpoints.SEARCH_BY_METADATA,
            produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity searchPublishedPapersByMetadata(@RequestParam(value = RestApiRequestParameters.DOI, required = false) String doi,
                                                          @RequestParam(value = RestApiRequestParameters.JOURNAL_ID, required = false) String journalId,
                                                          @RequestParam(value = RestApiRequestParameters.CATEGORY, required = false) String category,
                                                          @RequestParam(value = RestApiRequestParameters.YEAR, required = false) Integer year,
                                                          @RequestParam(value = RestApiRequestParameters.AUTHOR_NAME, required = false) String authorName) {
        List<String> paperIds = this.paperService.getPublishedPapersByMetadata(doi, journalId, category, year, authorName);
        StringBuilder sb = new StringBuilder();
        paperIds.stream()
                .map(XmlResponseUtils::convertPaperToXmlResponse)
                .collect(Collectors.toList())
                .forEach(s -> sb.append(s.trim()));

        String responseBody = XmlResponseUtils.wrapResponse(new XmlResponse(RestApiConstants.PAPERS, sb.toString()));
        return ResponseEntity.ok(responseBody);
    }

    /**
     * GET api/v1/papers/search-by-metadata/private
     * ACCESS LEVEL: Only authenticated users
     *
     * Returns the IDs and titles of {@link Paper}s that match given metadata parameters and are created by the current user.
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = RestApiEndpoints.SEARCH_BY_METADATA_PRIVATE,
            produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity searchUsersPapersByMetadata(@RequestParam(value = RestApiRequestParameters.DOI, required = false) String doi,
                                                      @RequestParam(value = RestApiRequestParameters.JOURNAL_ID, required = false) String journalId,
                                                      @RequestParam(value = RestApiRequestParameters.CATEGORY, required = false) String category,
                                                      @RequestParam(value = RestApiRequestParameters.YEAR, required = false) int year,
                                                      @RequestParam(value = RestApiRequestParameters.AUTHOR_NAME, required = false) String authorName) {
        List<String> paperIds = this.paperService.getUsersPapersByMetadata(JwtTokenDetailsUtil.getCurrentUserId(), doi, journalId, category, year, authorName);
        StringBuilder sb = new StringBuilder();
        paperIds.stream()
                .map(XmlResponseUtils::convertPaperToXmlResponse)
                .collect(Collectors.toList())
                .forEach(s -> sb.append(s.trim()));

        String responseBody = XmlResponseUtils.wrapResponse(new XmlResponse(RestApiConstants.PAPERS, sb.toString()));
        return ResponseEntity.ok(responseBody);
    }
}
