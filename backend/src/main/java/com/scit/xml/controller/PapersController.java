package com.scit.xml.controller;

import com.scit.xml.common.Constants;
import com.scit.xml.common.api.RestApiConstants;
import com.scit.xml.common.api.RestApiEndpoints;
import com.scit.xml.common.api.RestApiRequestParameters;
import com.scit.xml.common.util.*;
import com.scit.xml.dto.XmlResponse;
import com.scit.xml.model.paper.Paper;
import com.scit.xml.security.JwtTokenDetailsUtil;
import com.scit.xml.service.EmailService;
import com.scit.xml.service.PaperService;
import com.scit.xml.service.UserService;
import com.scit.xml.service.validator.dto.PaperDtoValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

        return new ResponseEntity(HttpStatus.OK);
    }
}
