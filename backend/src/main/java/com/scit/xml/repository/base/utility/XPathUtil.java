package com.scit.xml.repository.base.utility;

import javax.xml.namespace.QName;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.exist.http.BadRequestException;
import org.w3c.dom.Node;

public class XPathUtil {

    public static Object evaluate(String expression, Node document, QName returnType) throws BadRequestException {
        final XPathFactory xPathFactory = XPathFactory.newInstance();
        final XPath xPath = xPathFactory.newXPath();

        try {
            final XPathExpression xPathExpression = xPath.compile(expression);
            return xPathExpression.evaluate(document, returnType);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
            throw new BadRequestException("XPath exception");
        }
    }
}
