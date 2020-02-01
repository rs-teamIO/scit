
package com.scit.xml.model.review;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;sequence>
 *           &lt;element name="comment" maxOccurs="unbounded">
 *             &lt;complexType>
 *               &lt;simpleContent>
 *                 &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *                   &lt;attribute name="reference_id" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *                 &lt;/extension>
 *               &lt;/simpleContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *         &lt;/sequence>
 *       &lt;/sequence>
 *       &lt;attribute name="paper_id" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="reviewer_id" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "comment"
})
@XmlRootElement(name = "review", namespace = "http://www.scit.org/schema/review")
public class Review {

    @XmlElement(namespace = "http://www.scit.org/schema/review", required = true)
    protected List<Review.Comment> comment;
    @XmlAttribute(name = "paper_id")
    protected String paperId;
    @XmlAttribute(name = "reviewer_id")
    protected String reviewerId;

    /**
     * Gets the value of the comment property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the comment property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getComment().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Review.Comment }
     * 
     * 
     */
    public List<Review.Comment> getComment() {
        if (comment == null) {
            comment = new ArrayList<Review.Comment>();
        }
        return this.comment;
    }

    /**
     * Gets the value of the paperId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPaperId() {
        return paperId;
    }

    /**
     * Sets the value of the paperId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPaperId(String value) {
        this.paperId = value;
    }

    /**
     * Gets the value of the reviewerId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReviewerId() {
        return reviewerId;
    }

    /**
     * Sets the value of the reviewerId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReviewerId(String value) {
        this.reviewerId = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;simpleContent>
     *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
     *       &lt;attribute name="reference_id" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *     &lt;/extension>
     *   &lt;/simpleContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "value"
    })
    public static class Comment {

        @XmlValue
        protected String value;
        @XmlAttribute(name = "reference_id")
        @XmlSchemaType(name = "anySimpleType")
        protected String referenceId;

        /**
         * Gets the value of the value property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getValue() {
            return value;
        }

        /**
         * Sets the value of the value property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setValue(String value) {
            this.value = value;
        }

        /**
         * Gets the value of the referenceId property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getReferenceId() {
            return referenceId;
        }

        /**
         * Sets the value of the referenceId property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setReferenceId(String value) {
            this.referenceId = value;
        }

    }

}
