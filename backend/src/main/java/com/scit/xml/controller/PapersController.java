package com.scit.xml.controller;

import com.scit.xml.common.Constants;
import com.scit.xml.common.api.RestApiConstants;
import com.scit.xml.common.api.RestApiEndpoints;
import com.scit.xml.common.util.BadRequestUtils;
import com.scit.xml.common.util.ResourceUtils;
import com.scit.xml.common.util.XmlResponseUtils;
import com.scit.xml.common.util.XmlWrapper;
import com.scit.xml.dto.XmlResponse;
import com.scit.xml.exception.BadRequestException;
import com.scit.xml.model.paper.Paper;
import com.scit.xml.security.JwtTokenDetailsUtil;
import com.scit.xml.security.JwtTokenUtil;
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
import java.io.IOException;

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
        String creatorId = JwtTokenDetailsUtil.getCurrentUserId();
        String id = this.paperService.createPaper(paper, creatorId);

        String editorEmail = this.userService.getUserEmail(Constants.EDITOR_USERNAME);
        byte[] pdf = ResourceUtils.convertResourceToByteArray(this.paperService.exportToPdf(id));
        String html = ResourceUtils.convertResourceToString(this.paperService.exportToHtml(id));
        this.emailService.sendPaperSubmissionNotificationEmail(editorEmail, paper, pdf, html);

        String responseBody = XmlResponseUtils.toXmlString(new XmlResponse(RestApiConstants.ID, id));
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping(produces = MediaType.APPLICATION_XML_VALUE )
    public ResponseEntity<String> getPapers(@RequestParam(required = false) Boolean ofCurrentUser) {
        String userId = JwtTokenDetailsUtil.getCurrentUserId();
        if(userId != null && ofCurrentUser) {
            String papersXml = this.paperService.getPapersByUserId(userId);
            return ResponseEntity.ok(papersXml);
        }
        String papersXml = this.paperService.getPublishedPapers(userId);
        return ResponseEntity.ok(papersXml);
    }

    @PreAuthorize("hasAuthority('editor')")
    @PutMapping(value = "/assign")
    public ResponseEntity assignReviewer(@RequestParam("paper_id") String paperId, @RequestParam("username") String username) throws MessagingException {

        // TODO: Check if it works without this shit
        //this.userService.findById(username);

        BadRequestUtils.throwInvalidRequestDataExceptionIf(username.equals(JwtTokenDetailsUtil.getCurrentUserUsername()),
                String.format("Unable to assign the paper for review to user with username %s", username));

        final XmlWrapper paperWrapper = new XmlWrapper(paperService.findById(paperId));
        this.paperService.assignReviewer(paperId, paperWrapper, username);

        String paperTitle = this.paperService.findByIdReturnsPaper(paperId).getTitle();
        this.emailService.sendPaperAssignmentNotificationEmail("f.ivkovic16@gmail.com", paperTitle);

        return new ResponseEntity(HttpStatus.OK);
    }

}
