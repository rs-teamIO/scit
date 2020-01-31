package com.scit.xml.utility;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.exist.http.BadRequestException;
import org.springframework.core.io.Resource;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xmldb.api.base.XMLDBException;

public class XsltUtil {

    public static String transform(Document document, Resource xsl) throws BadRequestException, XMLDBException {
        final TransformerFactory factory = TransformerFactory.newInstance();
        final StringWriter writer = new StringWriter();
        final DOMSource source = new DOMSource(document);
        final StreamResult result = new StreamResult(writer);

        try {
            final StreamSource transformSource = new StreamSource(xsl.getFile());
            final Transformer transformer = factory.newTransformer(transformSource);

            transformer.setOutputProperty("{http://xml.apache.org/xalan}indent-amount", "2");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");

            transformer.transform(source, result);
            return writer.toString();
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new BadRequestException(e.getMessage());
        }
    }
    
    public static Document stringToXml(String xml) throws BadRequestException 
    {
        //Parser that produces DOM object trees from XML content
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
         
        //API to obtain DOM Document instance
        DocumentBuilder builder = null;
        try
        {
            //Create DocumentBuilder with default configuration
            builder = factory.newDocumentBuilder();
             
            //Parse the content to Document object
            Document doc = builder.parse(new InputSource(new StringReader(xml)));
            return doc;
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
            throw new BadRequestException(e.getMessage());
        }
    }
}