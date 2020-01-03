package com.scit.xml.controller;

import java.io.IOException;
import java.util.UUID;

import org.exist.http.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.w3c.dom.Document;
import org.xmldb.api.base.XMLDBException;

import com.scit.xml.service.PaperService;
import com.scit.xml.utility.XsdValidator;
import com.scit.xml.utility.XsltUtil;

@RestController
@RequestMapping("api/papers")
public class PaperController {

    @Autowired
    private PaperService paperService;
    
    @Value("${schema.paper}")
    private String paperXsd;
    
    @Value("classpath:style/paper-xhtml.xsl")
    private Resource paperXsl;

    @PostMapping(value = "/validate", consumes = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity foo(@RequestBody String xml){
        XsdValidator.validate(xml, paperXsd);
        return new ResponseEntity(HttpStatus.OK);
    }
    
    @PostMapping(value = "/preview",
            consumes = MediaType.APPLICATION_XML_VALUE,
            produces = {MediaType.APPLICATION_XHTML_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> preview(@RequestBody String xml) throws IOException, BadRequestException, XMLDBException {
    	XsdValidator.validate(xml, paperXsd);
    	Document extractedXml = XsltUtil.stringToXml(xml);
        return new ResponseEntity<>(XsltUtil.transform(extractedXml, paperXsl), HttpStatus.OK);
    }
    
    @PostMapping(value = "/",
            consumes = MediaType.APPLICATION_XML_VALUE,
            produces = {MediaType.APPLICATION_XHTML_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> save(@RequestBody String content) throws IOException, BadRequestException, XMLDBException {
    	XsdValidator.validate(content, paperXsd);
    	
    	UUID id = paperService.save(content);
    	UriComponents urlLocation = UriComponentsBuilder.newInstance().path("/api/papers/{id}").buildAndExpand(id);
		return ResponseEntity.created(urlLocation.toUri()).build();
    }
    
    
    
    @GetMapping(
    		value = "/",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    		)
    public ResponseEntity<?> findAll() throws IOException, BadRequestException, XMLDBException {
    	
		return new ResponseEntity<>(paperService.findAll(), HttpStatus.OK);
    }
    
    
    
}
