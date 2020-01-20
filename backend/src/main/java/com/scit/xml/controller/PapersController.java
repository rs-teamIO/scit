package com.scit.xml.controller;

import com.scit.xml.common.api.RestApiConstants;
import com.scit.xml.common.api.RestApiEndpoints;
import com.scit.xml.common.util.XmlResponseUtils;
import com.scit.xml.dto.XmlResponse;
import com.scit.xml.model.paper.Paper;
import com.scit.xml.service.EmailService;
import com.scit.xml.service.PaperService;
import com.scit.xml.service.UserService;
import com.scit.xml.service.validator.dto.PaperDtoValidator;

import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import com.google.common.io.ByteStreams;
import javax.mail.MessagingException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping(RestApiEndpoints.PAPERS)
@RequiredArgsConstructor
public class PapersController {

    private final PaperService paperService;
    private final PaperDtoValidator paperDtoValidator;
    private final EmailService emailService;
    private final UserService userService;

    @PreAuthorize("hasAuthority('author')")
    @PostMapping(consumes = MediaType.APPLICATION_XML_VALUE,  produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> create(@RequestBody String xml) throws MessagingException, IOException {
        Paper paper = this.paperDtoValidator.validate(xml);
        String id = this.paperService.createPaper(paper);

        // TODO: Editor username should be a constant
        String editorEmail = this.userService.getUserEmail("editor1");

        byte[] pdf = ByteStreams.toByteArray(this.paperService.exportToPdf(id).getInputStream());
        Resource htmlResource = this.paperService.exportToHtml(id);
        Reader htmlResourceReader = new InputStreamReader(htmlResource.getInputStream(), StandardCharsets.UTF_8);
        String html = FileCopyUtils.copyToString(htmlResourceReader);
        this.emailService.sendPaperSubmissionNotificationEmail(editorEmail, paper, pdf, html);

        String responseBody = XmlResponseUtils.toXmlString(new XmlResponse(RestApiConstants.ID, id));
        return ResponseEntity.ok(responseBody);
    }
}
