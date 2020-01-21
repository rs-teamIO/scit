package com.scit.xml.controller;

import com.google.common.io.ByteStreams;
import com.scit.xml.common.Constants;
import com.scit.xml.common.api.RestApiConstants;
import com.scit.xml.common.api.RestApiEndpoints;
import com.scit.xml.common.api.RestApiRequestParameters;
import com.scit.xml.common.util.XmlResponseUtils;
import com.scit.xml.dto.XmlResponse;
import com.scit.xml.model.cover_letter.CoverLetter;
import com.scit.xml.service.CoverLetterService;
import com.scit.xml.service.EmailService;
import com.scit.xml.service.UserService;
import com.scit.xml.service.validator.dto.CoverLetterDtoValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

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
            consumes = MediaType.APPLICATION_XML_VALUE,  produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> create(@RequestBody String xml,
                                         @RequestParam(RestApiRequestParameters.PAPER_ID) String paperId) throws MessagingException, IOException {
        CoverLetter coverLetter = this.coverLetterDtoValidator.validate(xml);
        String id = this.coverLetterService.createCoverLetter(coverLetter, paperId);

        String editorEmail = this.userService.getUserEmail(Constants.EDITOR_USERNAME);

        byte[] pdf = ByteStreams.toByteArray(this.coverLetterService.exportToPdf(id).getInputStream());
        Resource htmlResource = this.coverLetterService.exportToHtml(id);
        Reader htmlResourceReader = new InputStreamReader(htmlResource.getInputStream(), StandardCharsets.UTF_8);
        String html = FileCopyUtils.copyToString(htmlResourceReader);

        this.emailService.sendCoverLetterSubmissionNotificationEmail(editorEmail, coverLetter, pdf, html);

        String responseBody = XmlResponseUtils.toXmlString(new XmlResponse(RestApiConstants.ID, id));
        return ResponseEntity.ok(responseBody);
    }
}
