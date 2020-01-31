package com.scit.xml.utility;

import java.util.Iterator;

import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.exist.http.BadRequestException;
import org.w3c.dom.Node;

import com.scit.xml.repository.base.utility.DatabaseConstants;

public class XPathUtil {

    public static Object evaluate(String expression, Node document, QName returnType) throws BadRequestException {
        final XPathFactory xPathFactory = XPathFactory.newInstance();
        final XPath xPath = xPathFactory.newXPath();
        
        NamespaceContext ctx = new NamespaceContext() {
        	
            public String getNamespaceURI(String prefix) {
                return prefix.equals("d") ? DatabaseConstants.ROOT_NAMESPACE : null; 
            }
            
            
            public Iterator<?> getPrefixes(String val) {
                return null;
            }
            public String getPrefix(String uri) {
                return null;
            }
        };

        xPath.setNamespaceContext(ctx);

        
        try {
            final XPathExpression xPathExpression = xPath.compile(expression);
            return xPathExpression.evaluate(document, returnType);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
            throw new BadRequestException("XPath exception");
        }
    }
}
