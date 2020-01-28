package com.scit.xml.common.util;

import com.scit.xml.common.api.RestApiErrors;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPathConstants;
import java.util.ArrayList;
import java.util.List;

public class XmlExtractorUtil {

    private XmlExtractorUtil() { }

    public static String extractStringAndValidateNotBlank(Document document, String xPathExpression) {
        final Node node = ((Node) XPathUtils.evaluate(xPathExpression, document, XPathConstants.NODE));
        final String field = node != null ? node.getTextContent() : null;

        BadRequestUtils.throwInvalidRequestDataExceptionIf(StringUtils.isEmpty(field),
                RestApiErrors.fieldShouldNotBeEmptyString(xPathExpression));

        return field;
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
}
