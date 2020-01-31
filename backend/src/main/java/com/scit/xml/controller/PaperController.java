package com.scit.xml.controller;

import com.scit.xml.common.api.RestApiConstants;
import com.scit.xml.common.api.RestApiEndpoints;
import com.scit.xml.common.api.RestApiRequestParameters;
import com.scit.xml.common.util.ResourceUtils;
import com.scit.xml.common.util.XmlResponseUtils;
import com.scit.xml.dto.XmlResponse;
import com.scit.xml.service.PaperService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(RestApiEndpoints.PAPER)
@RequiredArgsConstructor
public class PaperController {

    private final PaperService paperService;

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

    @GetMapping(value = RestApiEndpoints.AUTHOR,
            produces = { MediaType.APPLICATION_XML_VALUE } )
    public ResponseEntity getAuthorOfPaper(@RequestParam(RestApiRequestParameters.PAPER_ID) String paperId) {
        String authorId = this.paperService.getAuthorOfPaper(paperId);
        String responseBody = XmlResponseUtils.toXmlString(new XmlResponse(RestApiConstants.ID, authorId));
        return ResponseEntity.ok(responseBody);
    }
}
