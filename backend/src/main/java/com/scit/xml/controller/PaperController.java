package com.scit.xml.controller;

import com.scit.xml.common.api.RestApiConstants;
import com.scit.xml.common.api.RestApiEndpoints;
import com.scit.xml.common.api.RestApiRequestParameters;
import com.scit.xml.common.util.ForbiddenUtils;
import com.scit.xml.common.util.ResourceUtils;
import com.scit.xml.common.util.XmlResponseUtils;
import com.scit.xml.common.util.XmlWrapper;
import com.scit.xml.dto.XmlResponse;
import com.scit.xml.model.paper.Paper;
import com.scit.xml.security.JwtTokenDetailsUtil;
import com.scit.xml.service.PaperService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(RestApiEndpoints.PAPER)
@RequiredArgsConstructor
public class PaperController {

    private final PaperService paperService;

    /**
     * GET api/v1/paper/raw/download/
     * AUTHORIZATION: Anyone
     *
     * Returns the {@link Paper} in raw XML format for download.
     * @param paperId unique identifier of the {@link Paper} to be downloaded
     */
    @GetMapping(value = RestApiEndpoints.DOWNLOAD_RAW,
                params = { RestApiRequestParameters.PAPER_ID },
                produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity getRaw(@RequestParam(RestApiRequestParameters.PAPER_ID) String paperId) {
        final String paperXml = this.paperService.getRawPaperForDownload(paperId);
        final String paperTitle = this.paperService.getPaperTitle(paperId);
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Content-Disposition", String.format("attachment; filename=%s.xml", paperTitle));

        return new ResponseEntity<>(new ByteArrayResource(paperXml.getBytes()), httpHeaders, HttpStatus.OK);
    }

    /**
     * GET api/v1/paper/pdf/download/
     * AUTHORIZATION: Anyone
     *
     * Returns the {@link Paper} in PDF format for download.
     * @param paperId unique identifier of the {@link Paper} to be downloaded
     */
    @GetMapping(value = RestApiEndpoints.DOWNLOAD_PDF,
                params = { RestApiRequestParameters.PAPER_ID },
                produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity getPdf(@RequestParam(RestApiRequestParameters.PAPER_ID) String paperId) {
        final String paperXml = this.paperService.getRawPaperForDownload(paperId);
        final String paperTitle = this.paperService.getPaperTitle(paperId);
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Content-Disposition", String.format("attachment; filename=%s.pdf", paperTitle));
        byte[] paperPdf = ResourceUtils.convertResourceToByteArray(this.paperService.exportToPdf(paperId));

        return new ResponseEntity<>(new ByteArrayResource(paperPdf), httpHeaders, HttpStatus.OK);
    }

    /**
     * GET api/v1/paper/author/
     * AUTHORIZATION: Anyone
     *
     * Returns the ID of the author of the {@link Paper}.
     * @param paperId unique identifier of the {@link Paper}
     */
    @GetMapping(value = RestApiEndpoints.AUTHOR,
                produces = { MediaType.APPLICATION_XML_VALUE } )
    public ResponseEntity getAuthorOfPaper(@RequestParam(RestApiRequestParameters.PAPER_ID) String paperId) {
        String authorId = this.paperService.getAuthorOfPaper(paperId);
        String responseBody = XmlResponseUtils.toXmlString(new XmlResponse(RestApiConstants.ID, authorId));
        return ResponseEntity.ok(responseBody);
    }

    /**
     * GET api/v1/paper/anonymous
     * AUTHORIZATION: Author only
     *
     * Returns the {@link Paper} in an anonymous form where the authors have been removed from the document.
     * @param paperId unique identifier of the {@link Paper}
     */
    @PreAuthorize("hasAuthority('author')")
    @GetMapping(value = RestApiEndpoints.ANONYMOUS,
                produces = { MediaType.APPLICATION_XML_VALUE } )
    public ResponseEntity getAnonymousPaper(@RequestParam(RestApiRequestParameters.PAPER_ID) String paperId) {
        String paperXml = this.paperService.findById(paperId);
        XmlWrapper paperWrapper = new XmlWrapper(paperXml);
        this.paperService.anonymizePaper(paperWrapper, paperId, JwtTokenDetailsUtil.getCurrentUserId());
        String xml = paperWrapper.getXml();

        return ResponseEntity.ok(xml);
    }

    /**
     * DELETE api/v1/paper/
     * AUTHORIZATION: Author only
     *
     * Revokes the {@link Paper} instance from the system.
     * @param paperId unique identifier of the {@link Paper} to be revoked.
     */
    @PreAuthorize("hasAuthority('author')")
    @DeleteMapping
    public ResponseEntity revokePaper(@RequestParam(RestApiRequestParameters.PAPER_ID) String paperId) {
        String authorId = this.paperService.getAuthorOfPaper(paperId);
        ForbiddenUtils.throwInsufficientPrivilegesExceptionIf(!authorId.equals(JwtTokenDetailsUtil.getCurrentUserId()));

        this.paperService.revokePaper(paperId);

        return ResponseEntity.ok().build();
    }
}
