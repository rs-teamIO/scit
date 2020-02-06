package com.scit.xml.rdf;

import com.scit.xml.common.util.XmlWrapper;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements methods for extracting RDF Triples.
 */
public class RdfExtractor {

    private static final String TYPE_OF = "type_of";

    private final String documentId;
    private final String documentType;
    private final String predicatePrefix;

    private final List<RdfTriple> rdfTriples = new ArrayList<>();

    /**
     * Initializes an instance of the {@link RdfExtractor} class.
     *
     * @param documentId unique identifier of the document
     * @param documentType type of the document
     * @param predicatePrefix predicate prefix
     */
    public RdfExtractor(String documentId, String documentType, String predicatePrefix) {
        this.documentId = documentId;
        this.documentType = documentType;
        this.predicatePrefix = predicatePrefix;

        this.addDocumentTypeRdfTriple(documentId, documentType, predicatePrefix);
    }

    /**
     * Wraps given ID into "<id>" format.
     *
     * @param id ID to be wrapped
     * @return wrapped ID
     */
    private static String wrapId(String id) {
        return String.format("<%s>", id);
    }

    /**
     * Performs extraction of RDF Triples from given {@link XmlWrapper} instance.
     *
     * @param xmlWrapper extraction source
     * @return list of extracted {@link RdfTriple} instances
     */
    public List<RdfTriple> extractRdfTriples(XmlWrapper xmlWrapper) {
        return this.extractTriples(xmlWrapper.getDocument().getFirstChild(), predicatePrefix);
    }

    /**
     * Adds a document type RDF triple on class initialization.
     *
     * @param documentId unique identifier of the document
     * @param documentType document type
     * @param predicatePrefix predicate prefix
     */
    private void addDocumentTypeRdfTriple(String documentId, String documentType, String predicatePrefix) {
        this.createAndAddRdfTriple(documentId, String.format("%s:%s", predicatePrefix, TYPE_OF), documentType);
    }

    /**
     * Creates an {@link RdfTriple} instance and adds it to the instance's internal list of RDF Triples.
     *
     * @param subject
     * @param predicate
     * @param object
     */
    private void createAndAddRdfTriple(String subject, String predicate, String object) {
        boolean objectIsOfSameType = object.equals(this.documentType);
        boolean predicateIsInvalid = !predicate.equals(String.format("%s:%s", this.predicatePrefix, TYPE_OF));
        boolean objectIsEmpty = object.trim().isEmpty();
        if((objectIsOfSameType && predicateIsInvalid) || objectIsEmpty)
            return;

        subject = this.wrapIfId(subject.trim());
        object = this.wrapIfId(object.trim());
        predicate = predicate.trim();

        this.rdfTriples.add(new RdfTriple(subject, predicate, object));
    }

    /**
     * Wraps given text as ID if the text contains an ID, otherwise, it returns just formatted text.
     * @param text
     * @return
     */
    private static String wrapIfId(String text) {
        if (text.startsWith("http://"))
            return wrapId(text);
        return String.format("\"%s\"", text);
    }

    public static String wrap(String text) {
        if(text.startsWith("<"))
            return text;
        if(text.startsWith("\""))
            return text;
        if (text.startsWith("http://"))
            return wrapId(text);
        return text;
    }

    /**
     * Extracts RDF triples from given {@link Node} based on a given predicate.
     *
     * @param node extraction source
     * @param basePredicate base predicate
     * @return list of extracted {@link RdfTriple} instances
     */
    private List<RdfTriple> extractTriples(Node node, String basePredicate) {
        NodeList children = node.getChildNodes();
        this.extractAttributeTriples(node, basePredicate);
        this.extractTriplesFromChildNode(node, basePredicate, children);

        return this.rdfTriples;
    }

    /**
     * Extracts attribute RDF triples from given {@link Node} based on a given predicate
     * and adds it to the instance's internal list of {@link RdfTriple}s.
     *
     * @param node extraction source
     * @param basePredicate base predicate
     */
    private void extractAttributeTriples(Node node, String basePredicate) {
        NamedNodeMap attributes = node.getAttributes();
        for (int i = 0; i < attributes.getLength(); i++) {
            Node attribute = attributes.item(i);
            String attributeName = this.extractSimpleNodeName(attribute);
            String predicate = String.format("%s:%s", basePredicate, attributeName);
            this.createAndAddRdfTriple(documentId, predicate, attribute.getTextContent());
        }
    }

    /**
     * Extracts name of given {@link Node}
     *
     * @param node extraction source
     * @return extracted name of given node
     */
    private String extractSimpleNodeName(Node node) {
        String name = node.getNodeName();
        if (name == null) {
            return null;
        }
        if (name.contains(":")) {
            String[] tokens = name.split(":");
            return tokens[tokens.length - 1];
        }
        return name;
    }

    /**
     * Extracts RDF triples from a list of child nodes recursively.
     * and adds it to the instance's internal list of {@link RdfTriple}s.
     *
     * @param node
     * @param basePredicate
     * @param children
     */
    private void extractTriplesFromChildNode(Node node, String basePredicate, NodeList children) {
        Boolean isLeafNode = true;
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            String childName = extractSimpleNodeName(child);
            Boolean currentNodeIsStringNode = childName == null || childName.equals("#text");
            isLeafNode = isLeafNode && currentNodeIsStringNode;

            if (!currentNodeIsStringNode) {
                this.extractTriples(child, String.format("%s:%s", basePredicate, childName));
            }
        }

        if (isLeafNode) {
            this.createAndAddRdfTriple(documentId, basePredicate, node.getTextContent());
        }
    }
}
