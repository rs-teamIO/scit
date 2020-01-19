package com.scit.xml.controller;

import com.scit.xml.common.api.RestApiConstants;
import com.scit.xml.common.api.RestApiEndpoints;
import com.scit.xml.common.util.XmlResponseUtils;
import com.scit.xml.dto.XmlResponse;
import com.scit.xml.model.paper.Paper;
import com.scit.xml.service.PaperService;
import com.scit.xml.service.validator.dto.PaperDtoValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(RestApiEndpoints.PAPERS)
@RequiredArgsConstructor
public class PapersController {

    private final PaperService paperService;
    private final PaperDtoValidator paperDtoValidator;

    @PreAuthorize("hasAuthority('author')")
    @PostMapping(consumes = MediaType.APPLICATION_XML_VALUE,  produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> create(@RequestBody String xml) {
        Paper paper = this.paperDtoValidator.validate(xml);
        String id = this.paperService.createPaper(paper);

        // TODO: Send email
        // ...

        String responseBody = XmlResponseUtils.toXmlString(new XmlResponse(RestApiConstants.ID, id));
        return ResponseEntity.ok(responseBody);
    }
}
