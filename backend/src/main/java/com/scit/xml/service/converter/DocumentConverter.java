package com.scit.xml.service.converter;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.scit.xml.common.util.XmlWrapper;
import com.scit.xml.exception.InternalServerException;
import org.apache.tools.ant.filters.StringInputStream;
import org.springframework.stereotype.Component;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;

@Component
public class DocumentConverter {

    private final TransformerFactory transformerFactory;
    private final DocumentBuilderFactory documentBuilderFactory;

    public DocumentConverter() {
        transformerFactory = TransformerFactory.newInstance();
        documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        documentBuilderFactory.setIgnoringComments(true);
        documentBuilderFactory.setIgnoringElementContentWhitespace(true);
    }

    public ByteArrayOutputStream xmlToPdf(String xml, String xslPath) {
        String html = new String(xmlToHtml(xml, xslPath).toByteArray());
        return htmlToPdf(html);
    }

    public ByteArrayOutputStream xmlToHtml(String xml, String xslPath) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        StreamResult streamResult = new StreamResult();
        streamResult.setOutputStream(byteArrayOutputStream);
        try {
            StreamSource transformationSource = getTransformSource(xslPath);
            Transformer transformer = getXmlToHtmlTransformer(transformationSource);

            DOMSource xmlDomSource = getXmlDomSource(xml);
            transformer.transform(xmlDomSource, streamResult);
        } catch (Exception e) {
            throw new InternalServerException(e);
        }
        return byteArrayOutputStream;
    }

    public String xmlToXml(XmlWrapper wrapper, String xslPath) {
        StringWriter stringWriter = new StringWriter();
        StreamResult streamResult = new StreamResult(stringWriter);
        try {
            StreamSource transformationSource = getTransformSource(xslPath);
            Transformer transformer = getXmlToXmlTransformer(transformationSource);

            DOMSource xmlDomSource = getXmlDomSource(wrapper.getXml());
            transformer.transform(xmlDomSource, streamResult);
        } catch (Exception e) {
            throw new InternalServerException(e);
        }
        return stringWriter.toString();
    }

    public ByteArrayOutputStream htmlToPdf(String html) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);
            document.open();
            // TODO: Remove placeholder when XSLT is implemented
            document.add(new Chunk("SCIT - Scientific Paper Publishing System"));
            XMLWorkerHelper.getInstance().parseXHtml(writer, document, new StringInputStream(html));
            document.close();
        } catch (Exception e) {
            throw new InternalServerException(e);
        }
        return outputStream;
    }

    private StreamSource getTransformSource(String pathToXsl) {
        return new StreamSource(new File(pathToXsl));
    }

    private Transformer getXmlToHtmlTransformer(StreamSource transformationSource) throws TransformerConfigurationException {
        Transformer transformer = transformerFactory.newTransformer(transformationSource);
        transformer.setOutputProperty("{http://xml.apache.org/xalan}indent-amount", "2");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.METHOD, "xhtml");
        return transformer;
    }

    private Transformer getXmlToXmlTransformer(StreamSource transformationSource) throws TransformerConfigurationException {
        Transformer transformer = transformerFactory.newTransformer(transformationSource);
        transformer.setOutputProperty("{http://xml.apache.org/xalan}indent-amount", "2");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        return transformer;
    }

    private DOMSource getXmlDomSource(String xml) throws SAXException, IOException {
        try {
            DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();
            return new DOMSource(builder.parse(new InputSource(new StringReader(xml))));
        } catch (ParserConfigurationException e) {
            throw new InternalServerException(e);
        }
    }
}
