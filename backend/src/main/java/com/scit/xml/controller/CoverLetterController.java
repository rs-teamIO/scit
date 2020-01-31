package com.scit.xml.controller;

import com.scit.xml.service.CoverLetterService;
import com.scit.xml.utility.XsdValidator;

import java.io.IOException;
import java.util.UUID;

import org.exist.http.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.xmldb.api.base.XMLDBException;

@RestController
@RequestMapping("api/coverLetters")
public class CoverLetterController {

    @Autowired
    private CoverLetterService coverLetterService;

    @Value("${schema.coverletter}")
    private String coverLetterXsd;
    
    
    @PostMapping(value = "/",
            consumes = MediaType.APPLICATION_XML_VALUE,
            produces = {MediaType.APPLICATION_XHTML_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> save(@RequestBody String content) throws IOException, BadRequestException, XMLDBException {
    	XsdValidator.validate(content, coverLetterXsd);
    	
    	UUID id = coverLetterService.save(content);
    	UriComponents urlLocation = UriComponentsBuilder.newInstance().path("/api/coverLetters/{id}").buildAndExpand(id);
		return ResponseEntity.created(urlLocation.toUri()).build();
    }
    
}
