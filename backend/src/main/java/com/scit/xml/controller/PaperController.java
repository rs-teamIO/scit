package com.scit.xml.controller;

import com.scit.xml.common.Constants;
import com.scit.xml.common.api.RestApiConstants;
import com.scit.xml.common.api.RestApiEndpoints;
import com.scit.xml.common.api.RestApiRequestParameters;
import com.scit.xml.common.util.*;
import com.scit.xml.dto.XmlResponse;
import com.scit.xml.exception.InternalServerException;
import com.scit.xml.model.paper.Paper;
import com.scit.xml.security.JwtTokenDetailsUtil;
import com.scit.xml.service.EmailService;
import com.scit.xml.service.PaperService;
import com.scit.xml.service.UserService;
import com.scit.xml.service.validator.dto.PaperDtoValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(RestApiEndpoints.PAPER)
@RequiredArgsConstructor
public class PaperController {

    private final PaperService paperService;
    private final PaperDtoValidator paperDtoValidator;
    private final EmailService emailService;
    private final UserService userService;

    /**
     * POST api/v1/paper/transform
     * ACCESS LEVEL: Anyone
     *
     * Returns the {@link Paper} transformed to HTML format.
     *
     * @param xml XML String representation of the {@link Paper} to be transformed
     */
    @PostMapping(value = RestApiEndpoints.TRANSFORM,
            consumes = MediaType.APPLICATION_XML_VALUE,
            produces = MediaType.APPLICATION_XHTML_XML_VALUE)
    public ResponseEntity transformPaper(@RequestBody String xml) {
        this.paperDtoValidator.validate(xml);
        String html = ResourceUtils.convertResourceToString(this.paperService.convertPaperToHtml(xml));

        return ResponseEntity.ok(html);
    }

    /**
     * GET api/v1/paper/raw/download/
     * ACCESS LEVEL: Anyone
     *
     * Returns the {@link Paper} in raw XML format for download.
     *
     * @param paperId unique identifier of the {@link Paper} to be downloaded
     */
    @GetMapping(value = RestApiEndpoints.DOWNLOAD_RAW,
                params = {RestApiRequestParameters.PAPER_ID},
                produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity getRaw(@RequestParam(RestApiRequestParameters.PAPER_ID) String paperId) {
        final String paperXml = this.paperService.getRawPaperForDownload(paperId);
        final String paperTitle = XmlExtractorUtil.extractPaperTitle(paperXml);

        return ResponseEntity.ok()
                .header("Content-Disposition", String.format("attachment; filename=%s.xml", paperTitle))
                .body(new ByteArrayResource(paperXml.getBytes()));
    }

    /**
     * GET api/v1/paper/pdf/download/
     * ACCESS LEVEL: Anyone
     *
     * Returns the {@link Paper} in PDF format for download.
     *
     * @param paperId unique identifier of the {@link Paper} to be downloaded
     */
    @GetMapping(value = RestApiEndpoints.DOWNLOAD_PDF,
                params = {RestApiRequestParameters.PAPER_ID},
                produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity getPdf(@RequestParam(RestApiRequestParameters.PAPER_ID) String paperId) {
        final String paperXml = this.paperService.getRawPaperForDownload(paperId);
        final String paperTitle = XmlExtractorUtil.extractPaperTitle(paperXml);

        return ResponseEntity.ok()
                .header("Content-Disposition", String.format("attachment; filename=%s.xml", paperTitle))
                .body(ResourceUtils.convertResourceToByteArray(this.paperService.exportToPdf(paperId)));
    }

    /**
     * GET api/v1/paper/anonymous
     * ACCESS LEVEL: Author only
     * <p>
     * Returns the {@link Paper} in an anonymous form where the authors have been removed from the document.
     *
     * @param paperId unique identifier of the {@link Paper}
     */
    @PreAuthorize("hasAuthority('author')")
    @GetMapping(value = RestApiEndpoints.ANONYMOUS,
                produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity getAnonymizedPaper(@RequestParam(RestApiRequestParameters.PAPER_ID) String paperId) {
        String paperXml = this.paperService.getAnonymizedPaper(paperId, JwtTokenDetailsUtil.getCurrentUserId());

        return ResponseEntity.ok(paperXml);
    }

    /**
     * GET api/v1/paper/authors/
     * ACCESS LEVEL: Anyone
     *
     * Returns the IDs of authors of the {@link Paper}.
     *
     * @param paperId unique identifier of the {@link Paper}
     */
    @GetMapping(value = RestApiEndpoints.AUTHORS,
                produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity getAuthorsOfPaper(@RequestParam(RestApiRequestParameters.PAPER_ID) String paperId) {
        List<String> authorIds = this.paperService.getIdentifiersOfPaperAuthors(paperId);

        StringBuilder sb = new StringBuilder();
        authorIds.stream()
                .map(id -> this.userService.findById(id))
                .map(XmlResponseUtils::convertToUserXmlResponse)
                .collect(Collectors.toList())
                .forEach(s -> sb.append(s.trim()));

        String responseBody = XmlResponseUtils.wrapResponse(new XmlResponse(RestApiConstants.USERS, sb.toString()));

        return ResponseEntity.ok(responseBody);
    }

    /**
     * GET api/v1/paper/reviewers/
     * ACCESS LEVEL: Editor only
     *
     * Returns the reviewers of the {@link Paper}.
     *
     * @param paperId unique identifier of the {@link Paper}
     */
    @PreAuthorize("hasAuthority('editor')")
    @GetMapping(value = RestApiEndpoints.REVIEWERS,
                produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity getReviewersOfPaper(@RequestParam(RestApiRequestParameters.PAPER_ID) String paperId) {
        List<String> authorIds = this.paperService.getReviewersOfPaper(paperId);

        StringBuilder sb = new StringBuilder();
        authorIds.stream()
                .map(XmlResponseUtils::convertToUserXmlResponse)
                .collect(Collectors.toList())
                .forEach(s -> sb.append(s.trim()));

        String responseBody = XmlResponseUtils.wrapResponse(new XmlResponse(RestApiConstants.USERS, sb.toString()));

        return ResponseEntity.ok(responseBody);
    }

    // TODO: Doc
    @PreAuthorize("isAuthenticated()")
    @PutMapping(consumes = MediaType.APPLICATION_XML_VALUE,
                produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> editPaper(@RequestBody String xml,
                                            @RequestParam(RestApiRequestParameters.PAPER_ID) String paperId) throws MessagingException {
        Paper paper = this.paperDtoValidator.validate(xml);
        this.paperService.editPaper(xml, paperId);

        String editorEmail =  XmlExtractorUtil.extractUserEmail(this.userService.findByUsername(Constants.EDITOR_USERNAME));
        byte[] pdf = ResourceUtils.convertResourceToByteArray(this.paperService.exportToPdf(paperId));
        String html = ResourceUtils.convertResourceToString(this.paperService.exportToHtml(paperId));
        this.emailService.sendPaperRevisionNotificationEmail(editorEmail, paper, pdf, html);

        String responseBody = XmlResponseUtils.wrapResponse(new XmlResponse(RestApiConstants.ID, paperId));
        return ResponseEntity.ok(responseBody);
    }

    /**
     * DELETE api/v1/paper/
     * ACCESS LEVEL: Author only
     *
     * Revokes the {@link Paper} instance from the system.
     *
     * @param paperId unique identifier of the {@link Paper} to be revoked.
     */
    @PreAuthorize("hasAuthority('author')")
    @DeleteMapping
    public ResponseEntity revokePaper(@RequestParam(RestApiRequestParameters.PAPER_ID) String paperId) {
        List<String> authorIds = this.paperService.getIdentifiersOfPaperAuthors(paperId);
        ForbiddenUtils.throwInsufficientPrivilegesExceptionIf(!authorIds.contains(JwtTokenDetailsUtil.getCurrentUserId()));

        this.paperService.revokePaper(paperId);

        return ResponseEntity.ok().build();
    }

    /**
     * PUT api/v1/paper/reject
     * ACCESS LEVEL: Editor only
     *
     * Rejects the {@link Paper} instance and sends notification e-mails to authors.
     *
     * @param paperId unique identifier of the {@link Paper} to be rejected.
     */
    @PreAuthorize("hasAuthority('editor')")
    @PutMapping(value = RestApiEndpoints.REJECT,
                produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity rejectPaper(@RequestParam(RestApiRequestParameters.PAPER_ID) String paperId) throws MessagingException {
        String paperTitle =  XmlExtractorUtil.extractPaperTitle(this.paperService.findById(paperId));
        List<String> authorIds = this.paperService.getIdentifiersOfPaperAuthors(paperId);
        this.paperService.rejectPaper(paperId);
        authorIds.stream().forEach(authorId -> {
            XmlWrapper userWrapper = new XmlWrapper(this.userService.findById(authorId));
            String recipient = XmlExtractorUtil.extractStringAndValidateNotBlank(userWrapper.getDocument(), "/user/email");
            try {
                this.emailService.sendPaperRejectedNotificationEmail(recipient, paperTitle);
            } catch (MessagingException e) {
                throw new InternalServerException(e);
            }
        });

        return ResponseEntity.ok().build();
    }

    /**
     * PUT api/v1/paper/publish
     * ACCESS LEVEL: Editor only
     *
     * Publishes the {@link Paper} instance and sends notification e-mails to authors.
     *
     * @param paperId unique identifier of the {@link Paper} to be published.
     */
    @PreAuthorize("hasAuthority('editor')")
    @PutMapping(value = RestApiEndpoints.PUBLISH,
                produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity publishPaper(@RequestParam(RestApiRequestParameters.PAPER_ID) String paperId) throws MessagingException {
        String paperTitle =  XmlExtractorUtil.extractPaperTitle(this.paperService.findById(paperId));
        List<String> authorIds = this.paperService.getIdentifiersOfPaperAuthors(paperId);
        this.paperService.publishPaper(paperId);
        authorIds.stream().forEach(authorId -> {
            XmlWrapper userWrapper = new XmlWrapper(this.userService.findById(authorId));
            String recipient = XmlExtractorUtil.extractStringAndValidateNotBlank(userWrapper.getDocument(), "/user/email");
            try {
                this.emailService.sendPaperPublishedNotificationEmail(recipient, paperTitle);
            } catch (MessagingException e) {
                throw new InternalServerException(e);
            }
        });

        return ResponseEntity.ok().build();
    }
}