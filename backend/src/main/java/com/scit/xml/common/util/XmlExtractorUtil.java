package com.scit.xml.common.util;

import com.scit.xml.common.api.RestApiErrors;
import org.apache.commons.lang3.StringUtils;
import org.exist.xquery.XPathUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPathConstants;
import java.util.ArrayList;
import java.util.List;

/**
 * Contains utility methods for extracting values from {@link Document} instances using XPath expressions.
 */
public class XmlExtractorUtil {

    private XmlExtractorUtil() { }

    public static String extractStringAndValidateNotBlank(Document document, String xPathExpression) {
        final Node node = ((Node) XPathUtils.evaluate(xPathExpression, document, XPathConstants.NODE));
        final String field = node != null ? node.getTextContent() : null;

        BadRequestUtils.throwInvalidRequestDataExceptionIf(StringUtils.isEmpty(field),
                RestApiErrors.fieldShouldNotBeEmptyString(xPathExpression));

        return field;
    }

    public static String extractString(Document document, String xPathExpression) {
        final Node node = ((Node) XPathUtils.evaluate(xPathExpression, document, XPathConstants.NODE));

        return node != null ? node.getTextContent() : null;
    }

    public static List<String> extractSetOfAttributeValuesAndValidateNotEmpty(Document document, String xPathExpression, String attributeName) {
        final NodeList nodeList = (NodeList) XPathUtils.evaluate(xPathExpression, document, XPathConstants.NODESET);
        final List<String> attributeValues = XmlExtractorUtil.extractAttributeValuesFromNodeListElements(nodeList, attributeName);
        BadRequestUtils.throwInvalidRequestDataExceptionIf(attributeValues.isEmpty(),
                RestApiErrors.fieldShouldNotBeEmptyString(xPathExpression));

        return attributeValues;
    }

    private static List<String> extractAttributeValuesFromNodeListElements(NodeList nodeList, String attributeName) {
        final List<String> attributeValues = new ArrayList<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            final Element element = (Element) nodeList.item(i);
            final String attributeValue = element.getAttribute(attributeName);
            attributeValues.add(attributeValue);
        }
        return attributeValues;
    }

    public static List<String> extractChildrenContentToList(Document document, String xPathExpression) {
        final NodeList nodeList = (NodeList) XPathUtils.evaluate(xPathExpression, document, XPathConstants.NODESET);
        final List<String> content = new ArrayList<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            final Element element = (Element) nodeList.item(i);
            final String text = element.getTextContent();
            content.add(text);
        }
        return content;
    }

    public static String extractPaperTitle(String paperXml) {
        XmlWrapper xmlWrapper = new XmlWrapper(paperXml);
        return XmlExtractorUtil.extractStringAndValidateNotBlank(xmlWrapper.getDocument(), "/paper/title");
    }

    public static String extractUserEmail(String userXml) {
        XmlWrapper xmlWrapper = new XmlWrapper(userXml);
        return XmlExtractorUtil.extractStringAndValidateNotBlank(xmlWrapper.getDocument(), "/user/email");
    }
}
