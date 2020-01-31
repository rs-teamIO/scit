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

import javax.mail.MessagingException;
import java.util.List;

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
     * AUTHORIZATION: Author only
     *
     * Creates a new {@link Paper}.
     * @param xml XML string representation of the {@link Paper}
     */
    @PreAuthorize("hasAuthority('author')")
    @PostMapping(consumes = MediaType.APPLICATION_XML_VALUE,
                 produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> create(@RequestBody String xml) throws MessagingException {
        Paper paper = this.paperDtoValidator.validate(xml);
        String paperId = this.paperService.createPaper(paper, JwtTokenDetailsUtil.getCurrentUserId());

        String editorEmail = this.userService.getUserEmail(Constants.EDITOR_USERNAME);
        byte[] pdf = ResourceUtils.convertResourceToByteArray(this.paperService.exportToPdf(paperId));
        String html = ResourceUtils.convertResourceToString(this.paperService.exportToHtml(paperId));
        this.emailService.sendPaperSubmissionNotificationEmail(editorEmail, paper, pdf, html);

        String responseBody = XmlResponseUtils.toXmlString(new XmlResponse(RestApiConstants.ID, paperId));
        return ResponseEntity.ok(responseBody);
    }

    // TODO
    @GetMapping(produces = MediaType.APPLICATION_XML_VALUE )
    public ResponseEntity getPapers(@RequestParam(name = RestApiRequestParameters.CURRENT_USER, required = false) Boolean ofCurrentUser) {
        String userId = JwtTokenDetailsUtil.getCurrentUserId();
        if(userId != null && ofCurrentUser != null && ofCurrentUser) {
            String papers = this.paperService.getPapersByUserId(userId);
            return ResponseEntity.ok(XmlResponseUtils.toXmlString(new XmlResponse("papers", papers)));
        }
        List<String> papers = this.paperService.getPublishedPapers(userId);
        return ResponseEntity.ok(XmlResponseUtils.toXmlString(new XmlResponse("papers", papers)));
    }

    /**
     * PUT api/v1/papers/assign
     * AUTHORIZATION: Editor only
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

        final XmlWrapper paperWrapper = new XmlWrapper(paperService.findById(paperId));
        final XmlWrapper userWrapper = new XmlWrapper(userService.findById(userId));
        this.paperService.assignReviewer(paperId, paperWrapper, userId);

        String paperTitle = XmlExtractorUtil.extractStringAndValidateNotBlank(paperWrapper.getDocument(), "/paper/title");
        String reviewerEmail = XmlExtractorUtil.extractStringAndValidateNotBlank(userWrapper.getDocument(), "/user/email");
        this.emailService.sendPaperAssignmentNotificationEmail(reviewerEmail, paperTitle);

        return ResponseEntity.ok().build();
    }

    /**
     * GET api/v1/papers/
     * AUTHORIZATION: Anyone
     *
     * Returns the HTML representation of a {@link Paper}
     * @param paperId unique identifier of the {@link Paper}
     */
    @GetMapping(produces = { MediaType.APPLICATION_XHTML_XML_VALUE } )
    public ResponseEntity findById(@RequestParam(RestApiRequestParameters.PAPER_ID) String paperId) {
        String html = ResourceUtils.convertResourceToString(this.paperService.exportToHtml(paperId));

        String xml = this.userService.findById(JwtTokenDetailsUtil.getCurrentUserId());
        XmlWrapper userWrapper = new XmlWrapper(xml);
        String userId = XmlExtractorUtil.extractString(userWrapper.getDocument(), "/user/@id");
        String userRole = XmlExtractorUtil.extractString(userWrapper.getDocument(), "/user/role");

        this.paperService.checkCurrentUserAccess(userId, userRole, paperId);

        return ResponseEntity.ok(html);
    }

    /**
     * GET api/v1/papers/assigned
     * AUTHORIZATION: Author only
     *
     * Returns the IDs and titles of {@link Paper}s assigned to the current user for review
     */
    @PreAuthorize("hasAuthority('author')")
    @GetMapping(value = RestApiEndpoints.ASSIGNED,
                produces = { MediaType.APPLICATION_XML_VALUE } )
    public ResponseEntity getAssignedPapers() {
        String papers = this.paperService.getAssignedPapers(JwtTokenDetailsUtil.getCurrentUserId());
        return ResponseEntity.ok(XmlResponseUtils.toXmlString(new XmlResponse("papers", papers)));
    }

    /**
     * GET api/v1/papers/in-review
     * AUTHORIZATION: Author only
     *
     * Returns the IDs and titles of {@link Paper}s the current user is currently reviewing
     */
    @PreAuthorize("hasAuthority('author')")
    @GetMapping(value = RestApiEndpoints.IN_REVIEW,
                produces = { MediaType.APPLICATION_XML_VALUE } )
    public ResponseEntity getPapersInReview() {
        String papers = this.paperService.getPapersInReview(JwtTokenDetailsUtil.getCurrentUserId());
        return ResponseEntity.ok(XmlResponseUtils.toXmlString(new XmlResponse("papers", papers)));
    }

    /**
     * GET api/v1/papers/submitted
     * AUTHORIZATION: Editor only
     *
     * Returns the IDs and titles of {@link Paper}s that have been submitted
     */
    @PreAuthorize("hasAuthority('editor')")
    @GetMapping(value = RestApiEndpoints.SUBMITTED,
                produces = { MediaType.APPLICATION_XML_VALUE } )
    public ResponseEntity getSubmittedPapers() {
        String papers = this.paperService.getSubmittedPapers();
        return ResponseEntity.ok(XmlResponseUtils.toXmlString(new XmlResponse("papers", papers)));
    }
}
