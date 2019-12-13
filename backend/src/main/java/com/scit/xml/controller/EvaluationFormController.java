package com.scit.xml.controller;

import com.scit.xml.service.EvaluationFormService;
import com.scit.xml.service.PaperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/evaluationForm")
public class EvaluationFormController {

    @Autowired
    private EvaluationFormService evaluationFormService;

    @Value("${schema.evaluationform}")
    private String evaluationFormXsd;
}
