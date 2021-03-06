
package com.scit.xml.model.paper;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.scit.xml.model.paper package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Title_QNAME = new QName("http://www.scit.org/schema/paper", "title");
    private final static QName _Comment_QNAME = new QName("http://www.scit.org/schema/paper", "comment");
    private final static QName _ContentReference_QNAME = new QName("http://www.scit.org/schema/paper", "reference");
    private final static QName _ContentList_QNAME = new QName("http://www.scit.org/schema/paper", "list");
    private final static QName _ContentTable_QNAME = new QName("http://www.scit.org/schema/paper", "table");
    private final static QName _PaperAbstractKeywords_QNAME = new QName("http://www.scit.org/schema/paper", "keywords");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.scit.xml.model.paper
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Paper }
     * 
     */
    public Paper createPaper() {
        return new Paper();
    }

    /**
     * Create an instance of {@link Content }
     * 
     */
    public Content createContent() {
        return new Content();
    }

    /**
     * Create an instance of {@link Content.Table }
     * 
     */
    public Content.Table createContentTable() {
        return new Content.Table();
    }

    /**
     * Create an instance of {@link com.scit.xml.model.paper.Reference }
     * 
     */
    public com.scit.xml.model.paper.Reference createReference() {
        return new com.scit.xml.model.paper.Reference();
    }

    /**
     * Create an instance of {@link CrossReference }
     * 
     */
    public CrossReference createCrossReference() {
        return new CrossReference();
    }

    /**
     * Create an instance of {@link Image }
     * 
     */
    public Image createImage() {
        return new Image();
    }

    /**
     * Create an instance of {@link PaperInfo }
     * 
     */
    public PaperInfo createPaperInfo() {
        return new PaperInfo();
    }

    /**
     * Create an instance of {@link Paper.Authors }
     * 
     */
    public Paper.Authors createPaperAuthors() {
        return new Paper.Authors();
    }

    /**
     * Create an instance of {@link Paper.Abstract }
     * 
     */
    public Paper.Abstract createPaperAbstract() {
        return new Paper.Abstract();
    }

    /**
     * Create an instance of {@link Section }
     * 
     */
    public Section createSection() {
        return new Section();
    }

    /**
     * Create an instance of {@link Content.List }
     * 
     */
    public Content.List createContentList() {
        return new Content.List();
    }

    /**
     * Create an instance of {@link Content.Reference }
     * 
     */
    public Content.Reference createContentReference() {
        return new Content.Reference();
    }

    /**
     * Create an instance of {@link Paper.References }
     * 
     */
    public Paper.References createPaperReferences() {
        return new Paper.References();
    }

    /**
     * Create an instance of {@link Author }
     * 
     */
    public Author createAuthor() {
        return new Author();
    }

    /**
     * Create an instance of {@link Content.Table.Tr }
     * 
     */
    public Content.Table.Tr createContentTableTr() {
        return new Content.Table.Tr();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.scit.org/schema/paper", name = "title")
    public JAXBElement<String> createTitle(String value) {
        return new JAXBElement<String>(_Title_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.scit.org/schema/paper", name = "comment")
    public JAXBElement<String> createComment(String value) {
        return new JAXBElement<String>(_Comment_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Content.Reference }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.scit.org/schema/paper", name = "reference", scope = Content.class)
    public JAXBElement<Content.Reference> createContentReference(Content.Reference value) {
        return new JAXBElement<Content.Reference>(_ContentReference_QNAME, Content.Reference.class, Content.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Content.List }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.scit.org/schema/paper", name = "list", scope = Content.class)
    public JAXBElement<Content.List> createContentList(Content.List value) {
        return new JAXBElement<Content.List>(_ContentList_QNAME, Content.List.class, Content.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Content.Table }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.scit.org/schema/paper", name = "table", scope = Content.class)
    public JAXBElement<Content.Table> createContentTable(Content.Table value) {
        return new JAXBElement<Content.Table>(_ContentTable_QNAME, Content.Table.class, Content.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link java.util.List }{@code <}{@link String }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.scit.org/schema/paper", name = "keywords", scope = Paper.Abstract.class)
    public JAXBElement<java.util.List<String>> createPaperAbstractKeywords(java.util.List<String> value) {
        return new JAXBElement<java.util.List<String>>(_PaperAbstractKeywords_QNAME, ((Class) java.util.List.class), Paper.Abstract.class, ((java.util.List<String> ) value));
    }

}
