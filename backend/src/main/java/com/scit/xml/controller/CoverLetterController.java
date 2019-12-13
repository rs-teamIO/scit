package com.scit.xml.controller;

import com.scit.xml.service.CoverLetterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/coverLetters")
public class CoverLetterController {

    @Autowired
    private CoverLetterService coverLetterService;

    @Value("${schema.coverletter}")
    private String coverLetterXsd;
}
