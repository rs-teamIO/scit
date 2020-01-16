package com.scit.xml.common.util;

import com.scit.xml.common.api.RestApiErrors;
import org.apache.commons.lang3.StringUtils;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.xml.xpath.XPathConstants;

public class XmlExtractorUtil {

    private XmlExtractorUtil() { }

    public static String extractStringAndValidateNotBlank(Document document, String xPathExpression) {
        Node node = ((Node) XPathUtils.evaluate(xPathExpression, document, XPathConstants.NODE));
        String field = node != null ? node.getTextContent() : null;

        BadRequestUtils.throwInvalidRequestDataExceptionIf(StringUtils.isEmpty(field),
                RestApiErrors.fieldShouldNotBeEmptyString(xPathExpression));

        return field;
    }
}
