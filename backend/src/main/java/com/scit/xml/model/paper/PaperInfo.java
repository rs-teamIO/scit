
package com.scit.xml.model.paper;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


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
 *         &lt;element name="doi" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="journal_id" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="category">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="original_research"/>
 *               &lt;enumeration value="proceeding"/>
 *               &lt;enumeration value="short_report"/>
 *               &lt;enumeration value="review_article"/>
 *               &lt;enumeration value="case_study"/>
 *               &lt;enumeration value="methodology"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="status">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="submitted"/>
 *               &lt;enumeration value="in_review"/>
 *               &lt;enumeration value="reviewed"/>
 *               &lt;enumeration value="revoked"/>
 *               &lt;enumeration value="revision"/>
 *               &lt;enumeration value="rejected"/>
 *               &lt;enumeration value="published"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="submission_dates" type="{http://www.scit.org/schema/paper}dates"/>
 *         &lt;element name="revision_dates" type="{http://www.scit.org/schema/paper}dates"/>
 *         &lt;element name="acceptance_dates" type="{http://www.scit.org/schema/paper}dates" form="qualified"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "doi",
    "journalId",
    "category",
    "status",
    "submissionDates",
    "revisionDates",
    "acceptanceDates"
})
@XmlRootElement(name = "paper_info", namespace = "http://www.scit.org/schema/paper")
public class PaperInfo {

    @XmlElement(namespace = "http://www.scit.org/schema/paper")
    protected String doi;
    @XmlElement(name = "journal_id", namespace = "http://www.scit.org/schema/paper")
    protected String journalId;
    @XmlElement(namespace = "http://www.scit.org/schema/paper", required = true)
    protected String category;
    @XmlElement(namespace = "http://www.scit.org/schema/paper", required = true)
    protected String status;
    @XmlList
    @XmlElement(name = "submission_dates", namespace = "http://www.scit.org/schema/paper", required = true, nillable = true)
    protected List<XMLGregorianCalendar> submissionDates;
    @XmlList
    @XmlElement(name = "revision_dates", namespace = "http://www.scit.org/schema/paper", required = true, nillable = true)
    protected List<XMLGregorianCalendar> revisionDates;
    @XmlList
    @XmlElement(name = "acceptance_dates", namespace = "http://www.scit.org/schema/paper", required = true, nillable = true)
    protected List<XMLGregorianCalendar> acceptanceDates;

    /**
     * Gets the value of the doi property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDoi() {
        return doi;
    }

    /**
     * Sets the value of the doi property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDoi(String value) {
        this.doi = value;
    }

    /**
     * Gets the value of the journalId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJournalId() {
        return journalId;
    }

    /**
     * Sets the value of the journalId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJournalId(String value) {
        this.journalId = value;
    }

    /**
     * Gets the value of the category property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCategory() {
        return category;
    }

    /**
     * Sets the value of the category property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCategory(String value) {
        this.category = value;
    }

    /**
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStatus(String value) {
        this.status = value;
    }

    /**
     * Gets the value of the submissionDates property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the submissionDates property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSubmissionDates().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link XMLGregorianCalendar }
     * 
     * 
     */
    public List<XMLGregorianCalendar> getSubmissionDates() {
        if (submissionDates == null) {
            submissionDates = new ArrayList<XMLGregorianCalendar>();
        }
        return this.submissionDates;
    }

    /**
     * Gets the value of the revisionDates property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the revisionDates property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRevisionDates().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link XMLGregorianCalendar }
     * 
     * 
     */
    public List<XMLGregorianCalendar> getRevisionDates() {
        if (revisionDates == null) {
            revisionDates = new ArrayList<XMLGregorianCalendar>();
        }
        return this.revisionDates;
    }

    /**
     * Gets the value of the acceptanceDates property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the acceptanceDates property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAcceptanceDates().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link XMLGregorianCalendar }
     * 
     * 
     */
    public List<XMLGregorianCalendar> getAcceptanceDates() {
        if (acceptanceDates == null) {
            acceptanceDates = new ArrayList<XMLGregorianCalendar>();
        }
        return this.acceptanceDates;
    }

}
