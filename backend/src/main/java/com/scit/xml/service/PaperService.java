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
            //String paperXml = this.findById(paperId);
            String paperXml = "<ns1:paper xmlns:ns1=\"http://www.scit.org/schema/paper\" ns1:id=\"e1bedf76-8ced-435e-9b99-8926dd67be84\">\n" +
                    "        <ns1:paper_info>\n" +
                    "            <ns1:journal_id>JOURNAL_1</ns1:journal_id>\n" +
                    "            <ns1:category>Scientific paper</ns1:category>\n" +
                    "            <ns1:status>submitted</ns1:status>\n" +
                    "            <ns1:submission_dates>2020-01-19</ns1:submission_dates>\n" +
                    "            <ns1:revision_dates/>\n" +
                    "            <ns1:acceptance_dates/>\n" +
                    "        </ns1:paper_info>\n" +
                    "        <ns1:title>XML Science Paper Instance</ns1:title>\n" +
                    "        <ns1:authors>\n" +
                    "            <ns1:author username=\"author1\">\n" +
                    "                <ns1:name>First Author</ns1:name>\n" +
                    "                <ns1:email>f.ivkovic16@gmail.com</ns1:email>\n" +
                    "                <ns1:affilliation>Affilitation 123, Street 1, City, Country</ns1:affilliation>\n" +
                    "            </ns1:author>\n" +
                    "        </ns1:authors>\n" +
                    "        <ns1:abstract>\n" +
                    "            <ns1:content>Abstract's content</ns1:content>\n" +
                    "            <ns1:keywords>keyword1</ns1:keywords>\n" +
                    "        </ns1:abstract>\n" +
                    "        <ns1:section ns1:id=\"a8d4e907-739f-460f-a416-b5e8538203e7\">\n" +
                    "            <ns1:heading>Section 1 heading</ns1:heading>\n" +
                    "            <ns1:content>\n" +
                    "            Section 1 content\n" +
                    "            \n" +
                    "            \n" +
                    "            <ns1:image img_title=\"Test image\" link=\"http://www.scit.org/testimage.jpg\"/>\n" +
                    "                <ns1:table>\n" +
                    "                    <ns1:tr>\n" +
                    "                        <ns1:td>Table item</ns1:td>\n" +
                    "                    </ns1:tr>\n" +
                    "                </ns1:table>\n" +
                    "                <ns1:list>\n" +
                    "                    <ns1:item>List item</ns1:item>\n" +
                    "                </ns1:list>\n" +
                    "                <ns1:reference ns1:reference_id=\"reference_1\">Reference 1</ns1:reference>\n" +
                    "            After reference\n" +
                    "        </ns1:content>\n" +
                    "            <ns1:comment>First section comment</ns1:comment>\n" +
                    "        </ns1:section>\n" +
                    "        <ns1:references>\n" +
                    "            <ns1:reference ns1:reference_id=\"string\">\n" +
                    "            Before cross reference \n" +
                    "            \n" +
                    "            \n" +
                    "            <ns1:cross_reference ns1:cross_reference_id=\"cross_reference_1\">Cross reference 1</ns1:cross_reference>\n" +
                    "            After cross reference \n" +
                    "        </ns1:reference>\n" +
                    "        </ns1:references>\n" +
                    "        <ns1:comment>First paper comment</ns1:comment>\n" +
                    "    </ns1:paper>";
            ByteArrayOutputStream pdfOutputStream = this.documentConverter.xmlToPdf(paperXml, Paths.get(this.stylesheet.getURI()).toString());
            return new ByteArrayResource(pdfOutputStream.toByteArray());
        } catch (IOException e) {
            throw new InternalServerException(e);
        }
    }

    public Resource exportToHtml(String paperId) {
        try {
            // TODO: Validate HTML request to DB.
            //String paperXml = this.findById(paperId);
            String paperXml = "<ns1:paper xmlns:ns1=\"http://www.scit.org/schema/paper\" ns1:id=\"e1bedf76-8ced-435e-9b99-8926dd67be84\">\n" +
                    "        <ns1:paper_info>\n" +
                    "            <ns1:journal_id>JOURNAL_1</ns1:journal_id>\n" +
                    "            <ns1:category>Scientific paper</ns1:category>\n" +
                    "            <ns1:status>submitted</ns1:status>\n" +
                    "            <ns1:submission_dates>2020-01-19</ns1:submission_dates>\n" +
                    "            <ns1:revision_dates/>\n" +
                    "            <ns1:acceptance_dates/>\n" +
                    "        </ns1:paper_info>\n" +
                    "        <ns1:title>XML Science Paper Instance</ns1:title>\n" +
                    "        <ns1:authors>\n" +
                    "            <ns1:author username=\"author1\">\n" +
                    "                <ns1:name>First Author</ns1:name>\n" +
                    "                <ns1:email>f.ivkovic16@gmail.com</ns1:email>\n" +
                    "                <ns1:affilliation>Affilitation 123, Street 1, City, Country</ns1:affilliation>\n" +
                    "            </ns1:author>\n" +
                    "        </ns1:authors>\n" +
                    "        <ns1:abstract>\n" +
                    "            <ns1:content>Abstract's content</ns1:content>\n" +
                    "            <ns1:keywords>keyword1</ns1:keywords>\n" +
                    "        </ns1:abstract>\n" +
                    "        <ns1:section ns1:id=\"a8d4e907-739f-460f-a416-b5e8538203e7\">\n" +
                    "            <ns1:heading>Section 1 heading</ns1:heading>\n" +
                    "            <ns1:content>\n" +
                    "            Section 1 content\n" +
                    "            \n" +
                    "            \n" +
                    "            <ns1:image img_title=\"Test image\" link=\"http://www.scit.org/testimage.jpg\"/>\n" +
                    "                <ns1:table>\n" +
                    "                    <ns1:tr>\n" +
                    "                        <ns1:td>Table item</ns1:td>\n" +
                    "                    </ns1:tr>\n" +
                    "                </ns1:table>\n" +
                    "                <ns1:list>\n" +
                    "                    <ns1:item>List item</ns1:item>\n" +
                    "                </ns1:list>\n" +
                    "                <ns1:reference ns1:reference_id=\"reference_1\">Reference 1</ns1:reference>\n" +
                    "            After reference\n" +
                    "        </ns1:content>\n" +
                    "            <ns1:comment>First section comment</ns1:comment>\n" +
                    "        </ns1:section>\n" +
                    "        <ns1:references>\n" +
                    "            <ns1:reference ns1:reference_id=\"string\">\n" +
                    "            Before cross reference \n" +
                    "            \n" +
                    "            \n" +
                    "            <ns1:cross_reference ns1:cross_reference_id=\"cross_reference_1\">Cross reference 1</ns1:cross_reference>\n" +
                    "            After cross reference \n" +
                    "        </ns1:reference>\n" +
                    "        </ns1:references>\n" +
                    "        <ns1:comment>First paper comment</ns1:comment>\n" +
                    "    </ns1:paper>";
            ByteArrayOutputStream htmlOutputStream = this.documentConverter.xmlToHtml(paperXml, Paths.get(this.stylesheet.getURI()).toString());
            return new ByteArrayResource(htmlOutputStream.toByteArray());
        } catch (IOException e) {
            throw new InternalServerException(e);
        }
    }
}
