package com.scit.xml.controller;

import com.scit.xml.common.Constants;
import com.scit.xml.common.api.RestApiConstants;
import com.scit.xml.common.api.RestApiEndpoints;
import com.scit.xml.common.api.RestApiRequestParameters;
import com.scit.xml.common.util.ResourceUtils;
import com.scit.xml.common.util.XmlResponseUtils;
import com.scit.xml.dto.XmlResponse;
import com.scit.xml.model.cover_letter.CoverLetter;
import com.scit.xml.service.CoverLetterService;
import com.scit.xml.service.EmailService;
import com.scit.xml.service.UserService;
import com.scit.xml.service.validator.dto.CoverLetterDtoValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;

@RestController
@RequestMapping(RestApiEndpoints.COVER_LETTERS)
@RequiredArgsConstructor
public class CoverLettersController {

    private final CoverLetterService coverLetterService;
    private final CoverLetterDtoValidator coverLetterDtoValidator;
    private final EmailService emailService;
    private final UserService userService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping(params = { RestApiRequestParameters.PAPER_ID },
                 consumes = MediaType.APPLICATION_XML_VALUE,
                 produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> create(@RequestBody String xml,
                                         @RequestParam(RestApiRequestParameters.PAPER_ID) String paperId) throws MessagingException {
        CoverLetter coverLetter = this.coverLetterDtoValidator.validate(xml);
        coverLetter.setPaperId(paperId);
        String id = this.coverLetterService.createCoverLetter(coverLetter);

        String editorEmail = this.userService.getUserEmail(Constants.EDITOR_USERNAME);
        byte[] pdf = ResourceUtils.convertResourceToByteArray(this.coverLetterService.exportToPdf(id));
        String html = ResourceUtils.convertResourceToString(this.coverLetterService.exportToHtml(id));
        this.emailService.sendCoverLetterSubmissionNotificationEmail(editorEmail, coverLetter, pdf, html);

        String responseBody = XmlResponseUtils.toXmlString(new XmlResponse(RestApiConstants.ID, id));
        return ResponseEntity.ok(responseBody);
    }
}
