package com.scit.xml.service;

import com.scit.xml.exception.InternalServerException;
import com.scit.xml.model.evaluation_form.EvaluationForm;
import com.scit.xml.repository.EvaluationFormRepository;
import com.scit.xml.service.converter.DocumentConverter;
import com.scit.xml.service.validator.database.EvaluationFormDatabaseValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class EvaluationFormService {

    @Value("classpath:xsl/evaluation_form.xsl")
    private Resource stylesheet;

    private final EvaluationFormDatabaseValidator evaluationFormDatabaseValidator;
    private final EvaluationFormRepository evaluationFormRepository;
    private final DocumentConverter documentConverter;

    public String createEvaluationForm(EvaluationForm evaluationForm, String paperId) {
        this.evaluationFormDatabaseValidator.validateCreateRequest(evaluationForm, paperId);
        return this.evaluationFormRepository.save(evaluationForm);
    }

    public Resource exportToPdf(String evaluationFormId) {
        try {
            String evaluationFormXml = this.evaluationFormDatabaseValidator.validateExportRequest(evaluationFormId);
            ByteArrayOutputStream pdfOutputStream = this.documentConverter.xmlToPdf(evaluationFormXml, Paths.get(this.stylesheet.getURI()).toString());
            return new ByteArrayResource(pdfOutputStream.toByteArray());
        } catch (IOException e) {
            throw new InternalServerException(e);
        }
    }

    public Resource exportToHtml(String evaluationFormId) {
        try {
            String evaluationFormXml = this.evaluationFormDatabaseValidator.validateExportRequest(evaluationFormId);
            ByteArrayOutputStream htmlOutputStream = this.documentConverter.xmlToHtml(evaluationFormXml, Paths.get(this.stylesheet.getURI()).toString());
            return new ByteArrayResource(htmlOutputStream.toByteArray());
        } catch (IOException e) {
            throw new InternalServerException(e);
        }
    }
}
