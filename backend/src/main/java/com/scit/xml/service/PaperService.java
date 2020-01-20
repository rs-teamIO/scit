package com.scit.xml.service;

import com.scit.xml.exception.InternalServerException;
import com.scit.xml.model.paper.Paper;
import com.scit.xml.repository.PaperRepository;
import com.scit.xml.service.converter.DocumentConverter;
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
public class PaperService {

    @Value("classpath:xsl/paper.xsl")
    private Resource stylesheet;

    private final PaperRepository paperRepository;
    // private final PaperDatabaseValidator paperDatabaseValidator;
    private final DocumentConverter documentConverter;

    public String createPaper(Paper paper) {
        // TODO: DB Validation
        // this.paperDatabaseValidator.validate(paperDto);

        return this.paperRepository.save(paper);
    }

    public Resource exportToPdf(String paperId) {
        try {
            // TODO: Validate PDF request to DB.
            String paperXml = this.paperRepository.findById(paperId);
            ByteArrayOutputStream pdfOutputStream = this.documentConverter.xmlToPdf(paperXml, Paths.get(this.stylesheet.getURI()).toString());
            return new ByteArrayResource(pdfOutputStream.toByteArray());
        } catch (IOException e) {
            throw new InternalServerException(e);
        }
    }

    public Resource exportToHtml(String paperId) {
        try {
            // TODO: Validate HTML request to DB.
            String paperXml = this.paperRepository.findById(paperId);
            ByteArrayOutputStream htmlOutputStream = this.documentConverter.xmlToHtml(paperXml, Paths.get(this.stylesheet.getURI()).toString());
            return new ByteArrayResource(htmlOutputStream.toByteArray());
        } catch (IOException e) {
            throw new InternalServerException(e);
        }
    }
}
