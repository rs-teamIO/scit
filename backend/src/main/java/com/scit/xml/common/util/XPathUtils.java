package com.scit.xml.common.util;

import com.scit.xml.exception.InternalServerException;
import org.w3c.dom.Document;

import javax.xml.namespace.QName;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

/**
 * Contains utility methods for evaluating XPath expressions.
 */
public class XPathUtils {

    private XPathUtils() { }

    /**
     * Evaluates the provided XPath expression.
     *
     * @param expression an XPath expression to be evaluated
     * @param document a subject of evaluation
     * @param returnType desired return type
     */
    public static Object evaluate(String expression, Document document, QName returnType) {
        final XPathFactory xPathFactory = XPathFactory.newInstance();
        final XPath xPath = xPathFactory.newXPath();

        try {
            final XPathExpression xPathExpression = xPath.compile(expression);
            return xPathExpression.evaluate(document, returnType);
        } catch (XPathExpressionException e) {
            throw new InternalServerException(e);
        }
    }
}
