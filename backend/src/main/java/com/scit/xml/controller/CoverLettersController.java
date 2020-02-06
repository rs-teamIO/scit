package com.scit.xml.controller;

import com.scit.xml.common.Constants;
import com.scit.xml.common.api.RestApiConstants;
import com.scit.xml.common.api.RestApiEndpoints;
import com.scit.xml.common.api.RestApiRequestParameters;
import com.scit.xml.common.util.ResourceUtils;
import com.scit.xml.common.util.XmlExtractorUtil;
import com.scit.xml.common.util.XmlResponseUtils;
import com.scit.xml.dto.XmlResponse;
import com.scit.xml.model.cover_letter.CoverLetter;
import com.scit.xml.model.paper.Paper;
import com.scit.xml.service.CoverLetterService;
import com.scit.xml.service.EmailService;
import com.scit.xml.service.PaperService;
import com.scit.xml.service.UserService;
import com.scit.xml.service.validator.dto.CoverLetterDtoValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.mail.MessagingException;

@RestController
@RequestMapping(RestApiEndpoints.COVER_LETTERS)
@RequiredArgsConstructor
public class CoverLettersController {

    private final CoverLetterService coverLetterService;
    private final CoverLetterDtoValidator coverLetterDtoValidator;
    private final EmailService emailService;
    private final UserService userService;
    private final PaperService paperService;

    /**
     * POST api/v1/cover-letters
     * ACCESS LEVEL: Only authenticated users
     *
     * Creates a {@link CoverLetter} that accompanies a {@link Paper} instance
     * @param xml XML string representation of the {@link CoverLetter}
     * @param paperId unique identifier of the {@link Paper} instance
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping(params = { RestApiRequestParameters.PAPER_ID },
                 consumes = MediaType.APPLICATION_XML_VALUE,
                 produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> create(@RequestBody String xml,
                                         @RequestParam(RestApiRequestParameters.PAPER_ID) String paperId) throws MessagingException {
        CoverLetter coverLetter = this.coverLetterDtoValidator.validate(xml);
        String id = this.coverLetterService.createCoverLetter(coverLetter, paperId);

        String editorEmail = XmlExtractorUtil.extractUserEmail(this.userService.findByUsername(Constants.EDITOR_USERNAME));
        byte[] pdf = ResourceUtils.convertResourceToByteArray(this.coverLetterService.exportToPdf(id));
        String html = ResourceUtils.convertResourceToString(this.coverLetterService.exportToHtml(id));
        String paperTitle = XmlExtractorUtil.extractPaperTitle(this.paperService.findById(paperId));
        this.emailService.sendCoverLetterSubmissionNotificationEmail(editorEmail, coverLetter, paperTitle, pdf, html);

        // TODO (idea only): Include paper title and author data in the schema for evaluation form (maybe)

        UriComponents urlLocation = UriComponentsBuilder.newInstance()
        		.path(RestApiEndpoints.COVER_LETTERS)
        		.query(RestApiRequestParameters.ID+"={id}")
        		.buildAndExpand(id);
        return ResponseEntity.created(urlLocation.toUri()).build();
    }
}