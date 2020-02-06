
package com.scit.xml.model.paper;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="link" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="img_title" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "image", namespace = "http://www.scit.org/schema/paper")
public class Image {

    @XmlAttribute(name = "link", required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String link;
    @XmlAttribute(name = "img_title")
    protected String imgTitle;

    /**
     * Gets the value of the link property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLink() {
        return link;
    }

    /**
     * Sets the value of the link property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLink(String value) {
        this.link = value;
    }

    /**
     * Gets the value of the imgTitle property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getImgTitle() {
        return imgTitle;
    }

    /**
     * Sets the value of the imgTitle property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setImgTitle(String value) {
        this.imgTitle = value;
    }

}
