package com.scit.xml.controller;

import com.scit.xml.service.PaperService;
import com.scit.xml.utility.XsdValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/papers")
public class PaperController {

    @Autowired
    private PaperService paperService;

    @Value("${schema.paper}")
    private String paperXsd;

    @PostMapping(value = "/validate", consumes = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity foo(@RequestBody String xml){
        XsdValidator.validate(xml, paperXsd);
        return new ResponseEntity(HttpStatus.OK);
    }
}
