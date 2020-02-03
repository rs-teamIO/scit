package com.scit.xml.common.util;

import com.scit.xml.common.api.ResponseCode;
import com.scit.xml.exception.BadRequestException;
import com.scit.xml.exception.InternalServerException;
import lombok.Data;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathConstants;
import java.io.StringReader;
import java.io.StringWriter;

import static javax.xml.transform.OutputKeys.OMIT_XML_DECLARATION;

/**
 * Wraps the XML String and DOM representation in one object.
 */
@Data
public class XmlWrapper {

    private String xml;
    private Document document;

    /** Initializes an instance of the {@link XmlWrapper} class. */
    public XmlWrapper() { }

    /**
     * Initializes an instance of the {@link XmlWrapper} class.
     * Updates DOM based on given XML.
     * @param xml XML string to be set
     */
    public XmlWrapper(String xml) {
        this.xml = xml;
        updateDom();
    }

    /**
     * Initializes an instance of the {@link XmlWrapper} class.
     * Updates XML based on given DOM.
     * @param document DOM to be set
     */
    public XmlWrapper(Document document) {
        this.document = document;
        updateXml();
    }

    /**
     * Initializes an instance of the {@link XmlWrapper} class.
     * @param xml XML string to be set
     * @param document DOM to be set
     */
    public XmlWrapper(String xml, Document document) {
        this.xml = xml;
        this.document = document;
    }

    /**
     * Serializes {@link Document} DOM tree to XML string.
     */
    public void updateXml() {
        // TransformerFactory initialization
        final TransformerFactory transformerFactory = TransformerFactory.newInstance();
        // DOM Tree (source) that is being transformed
        final DOMSource domSource = new DOMSource(this.document);
        // Stream used to serialize (i.e.) write to
        final StringWriter writer = new StringWriter();
        // Transformation result holder
        final StreamResult result = new StreamResult(writer);

        try {
            // Create transformer instance that is used for serializing the DOM model
            final Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OMIT_XML_DECLARATION, "yes");
            // Execute transformation from XML source to result
            transformer.transform(domSource, result);
            // Write result to field
            this.xml = writer.toString();
        } catch (TransformerException e) {
            throw new InternalServerException(e);
        }
    }

    /**
     * Serializes single node in DOM tree to XML.
     * @param node {@link Node} instance to be serialized
     * @return XML string
     */
    public static String transformNodeToXml(Node node) {
        if (node == null) {
            return null;
        }

        final TransformerFactory transformerFactory = TransformerFactory.newInstance();
        final DOMSource domSource = new DOMSource(node);
        final StringWriter writer = new StringWriter();
        final StreamResult result = new StreamResult(writer);

        try {
            final Transformer transformer = transformerFactory.newTransformer();
            transformer.transform(domSource, result);
            return writer.toString();
        } catch (TransformerException e) {
            throw new InternalServerException(e);
        }
    }

    /**
     * Generates DOM (document object model) for a given XML string
     */
    public void updateDom() {
        // DocumentBuilderFactory initialization
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            final DocumentBuilder builder = factory.newDocumentBuilder();
            // Parse the content of the given input source as an XML document and return a new DOM.
            InputSource inputSource = new InputSource(new StringReader(this.xml));
            this.document = builder.parse(inputSource);
        } catch (Exception e) {
            throw new BadRequestException(ResponseCode.INVALID_DATA, "Invalid XML provided.");
        }
    }

    /**
     * Sets the text content of a {@link Node} instance
     * @param xQuerySelector used to select {@link Node} instance
     * @param content text content to be set
     */
    public void set(String xQuerySelector, String content) {
        Node node = ((Node) XPathUtils.evaluate(xQuerySelector, getDocument(), XPathConstants.NODE));
        if (node == null) {
            throw new InternalServerException();
        }

        node.setTextContent(content);
        updateXml();
    }

    /**
     * Sets the text content of a {@link Node} instance only in case the content is not null.
     * @param xQuerySelector used to select {@link Node} instance
     * @param content text content to be set
     */
    public void setIfNotNull(String xQuerySelector, String content) {
        if (content == null || content.equalsIgnoreCase("null")) {
            return;
        }
        set(xQuerySelector, content);
    }

    /**
     * Sets the attribute of an {@link Element} instance.
     * @param xQuerySelector used to select {@link Element} instance
     * @param attributeName attribute to be set
     * @param attributeValue value of the attribute to be set
     */
    public void setElementAttribute(String xQuerySelector, String attributeName, String attributeValue) {
        // TODO: BUG Ne radi kada nema comment-ova na paper-u

        Element element = ((Element) XPathUtils.evaluate(xQuerySelector, getDocument(), XPathConstants.NODE));
        if (element == null) {
            throw new InternalServerException();
        }

        element.setAttribute(attributeName, attributeValue);
        updateXml();
    }
}