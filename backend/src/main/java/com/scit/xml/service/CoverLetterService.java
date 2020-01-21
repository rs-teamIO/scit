package com.scit.xml.service;

import com.scit.xml.exception.InternalServerException;
import com.scit.xml.model.cover_letter.CoverLetter;
import com.scit.xml.repository.CoverLetterRepository;
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
public class CoverLetterService {

    @Value("classpath:xsl/cover_letter.xsl")
    private Resource stylesheet;

    private final CoverLetterRepository coverLetterRepository;
    //private final CoverLetterDatabaseValidator coverLetterDatabaseValidator;
    private final DocumentConverter documentConverter;

    public String createCoverLetter(CoverLetter coverLetter, String documentId) {
        // TODO: DB Validation
        // this.coverLetterDatabaseValidator.validate(coverLetter, documentId);

        return this.coverLetterRepository.save(coverLetter);
    }

    public Resource exportToPdf(String coverLetterId) {
        try {
            // TODO: Validate PDF request to DB.
            String coverLetterXml = this.coverLetterRepository.findById(coverLetterId);
            ByteArrayOutputStream pdfOutputStream = this.documentConverter.xmlToPdf(coverLetterXml, Paths.get(this.stylesheet.getURI()).toString());
            return new ByteArrayResource(pdfOutputStream.toByteArray());
        } catch (IOException e) {
            throw new InternalServerException(e);
        }
    }

    public Resource exportToHtml(String coverLetterId) {
        try {
            // TODO: Validate HTML request to DB.
            String coverLetterXml = this.coverLetterRepository.findById(coverLetterId);
            ByteArrayOutputStream htmlOutputStream = this.documentConverter.xmlToHtml(coverLetterXml, Paths.get(this.stylesheet.getURI()).toString());
            return new ByteArrayResource(htmlOutputStream.toByteArray());
        } catch (IOException e) {
            throw new InternalServerException(e);
        }
    }
}
