package com.scit.xml.service;

import com.scit.xml.exception.InternalServerException;
import com.scit.xml.model.cover_letter.CoverLetter;
import com.scit.xml.repository.CoverLetterRepository;
import com.scit.xml.service.converter.DocumentConverter;
import com.scit.xml.service.validator.database.CoverLetterDatabaseValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Date;
import java.util.GregorianCalendar;

@Service
@RequiredArgsConstructor
public class CoverLetterService {

    @Value("classpath:xsl/cover_letter.xsl")
    private Resource stylesheet;

    private final CoverLetterDatabaseValidator coverLetterDatabaseValidator;
    private final CoverLetterRepository coverLetterRepository;
    private final DocumentConverter documentConverter;

    public String createCoverLetter(CoverLetter coverLetter, String paperId) {
        this.coverLetterDatabaseValidator.validateCreateRequest(coverLetter, paperId);
        coverLetter.setDate(this.getCurrentDate());
        return this.coverLetterRepository.save(coverLetter, paperId);
    }

    public Resource exportToPdf(String coverLetterId) {
        try {
            String coverLetterXml = this.coverLetterDatabaseValidator.validateExportRequest(coverLetterId);
            ByteArrayOutputStream pdfOutputStream = this.documentConverter.xmlToPdf(coverLetterXml, Paths.get(this.stylesheet.getURI()).toString());
            return new ByteArrayResource(pdfOutputStream.toByteArray());
        } catch (IOException e) {
            throw new InternalServerException(e);
        }
    }

    public Resource exportToHtml(String coverLetterId) {
        try {
            String coverLetterXml = this.coverLetterDatabaseValidator.validateExportRequest(coverLetterId);
            ByteArrayOutputStream htmlOutputStream = this.documentConverter.xmlToHtml(coverLetterXml, Paths.get(this.stylesheet.getURI()).toString());
            return new ByteArrayResource(htmlOutputStream.toByteArray());
        } catch (IOException e) {
            throw new InternalServerException(e);
        }
    }

    private XMLGregorianCalendar getCurrentDate() {
        try {
            GregorianCalendar gregorianCalendar = new GregorianCalendar();
            gregorianCalendar.setTime(new Date());
            return DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
        } catch (DatatypeConfigurationException e) {
            throw new InternalServerException(e);
        }
    }
}
