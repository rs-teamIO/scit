
package com.scit.xml.model.evaluation_form;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
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
 *       &lt;sequence>
 *         &lt;element name="general">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="relevance">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;attribute name="score" type="{http://www.scit.org/schema/evaluation_form}score" />
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="readability">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;attribute name="score" type="{http://www.scit.org/schema/evaluation_form}score" />
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="language">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;attribute name="score" type="{http://www.scit.org/schema/evaluation_form}score" />
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="organization">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;attribute name="score" type="{http://www.scit.org/schema/evaluation_form}score" />
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="abstract">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;attribute name="score" type="{http://www.scit.org/schema/evaluation_form}score" />
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="keywords">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;attribute name="score" type="{http://www.scit.org/schema/evaluation_form}score" />
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="figures">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;attribute name="score" type="{http://www.scit.org/schema/evaluation_form}score" />
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="conclusion">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;attribute name="score" type="{http://www.scit.org/schema/evaluation_form}score" />
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="references">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;attribute name="score" type="{http://www.scit.org/schema/evaluation_form}score" />
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="overall_quality">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;attribute name="score" type="{http://www.scit.org/schema/evaluation_form}score" />
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="technical">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="originality">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;attribute name="score" type="{http://www.scit.org/schema/evaluation_form}score" />
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="contribution_value">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;attribute name="score" type="{http://www.scit.org/schema/evaluation_form}score" />
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="academic_standards">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;attribute name="score" type="{http://www.scit.org/schema/evaluation_form}score" />
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="rationale">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;attribute name="score" type="{http://www.scit.org/schema/evaluation_form}score" />
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="methodology">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;attribute name="score" type="{http://www.scit.org/schema/evaluation_form}score" />
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="accuracy">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;attribute name="score" type="{http://www.scit.org/schema/evaluation_form}score" />
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="evidence">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;attribute name="score" type="{http://www.scit.org/schema/evaluation_form}score" />
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="scientific_quality">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;attribute name="score" type="{http://www.scit.org/schema/evaluation_form}score" />
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="recommendation" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "general",
    "technical",
    "recommendation"
})
@XmlRootElement(name = "evaluation_form", namespace = "http://www.scit.org/schema/evaluation_form")
public class EvaluationForm {

    @XmlElement(namespace = "http://www.scit.org/schema/evaluation_form", required = true)
    protected EvaluationForm.General general;
    @XmlElement(namespace = "http://www.scit.org/schema/evaluation_form", required = true)
    protected EvaluationForm.Technical technical;
    @XmlElement(namespace = "http://www.scit.org/schema/evaluation_form", required = true)
    protected String recommendation;
    @XmlAttribute(name = "id", namespace = "http://www.scit.org/schema/evaluation_form")
    protected String id;

    /**
     * Gets the value of the general property.
     * 
     * @return
     *     possible object is
     *     {@link EvaluationForm.General }
     *     
     */
    public EvaluationForm.General getGeneral() {
        return general;
    }

    /**
     * Sets the value of the general property.
     * 
     * @param value
     *     allowed object is
     *     {@link EvaluationForm.General }
     *     
     */
    public void setGeneral(EvaluationForm.General value) {
        this.general = value;
    }

    /**
     * Gets the value of the technical property.
     * 
     * @return
     *     possible object is
     *     {@link EvaluationForm.Technical }
     *     
     */
    public EvaluationForm.Technical getTechnical() {
        return technical;
    }

    /**
     * Sets the value of the technical property.
     * 
     * @param value
     *     allowed object is
     *     {@link EvaluationForm.Technical }
     *     
     */
    public void setTechnical(EvaluationForm.Technical value) {
        this.technical = value;
    }

    /**
     * Gets the value of the recommendation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRecommendation() {
        return recommendation;
    }

    /**
     * Sets the value of the recommendation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRecommendation(String value) {
        this.recommendation = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }


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
     *         &lt;element name="relevance">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;attribute name="score" type="{http://www.scit.org/schema/evaluation_form}score" />
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="readability">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;attribute name="score" type="{http://www.scit.org/schema/evaluation_form}score" />
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="language">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;attribute name="score" type="{http://www.scit.org/schema/evaluation_form}score" />
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="organization">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;attribute name="score" type="{http://www.scit.org/schema/evaluation_form}score" />
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="abstract">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;attribute name="score" type="{http://www.scit.org/schema/evaluation_form}score" />
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="keywords">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;attribute name="score" type="{http://www.scit.org/schema/evaluation_form}score" />
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="figures">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;attribute name="score" type="{http://www.scit.org/schema/evaluation_form}score" />
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="conclusion">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;attribute name="score" type="{http://www.scit.org/schema/evaluation_form}score" />
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="references">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;attribute name="score" type="{http://www.scit.org/schema/evaluation_form}score" />
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="overall_quality">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;attribute name="score" type="{http://www.scit.org/schema/evaluation_form}score" />
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
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
        "relevance",
        "readability",
        "language",
        "organization",
        "_abstract",
        "keywords",
        "figures",
        "conclusion",
        "references",
        "overallQuality"
    })
    public static class General {

        @XmlElement(namespace = "http://www.scit.org/schema/evaluation_form", required = true)
        protected EvaluationForm.General.Relevance relevance;
        @XmlElement(namespace = "http://www.scit.org/schema/evaluation_form", required = true)
        protected EvaluationForm.General.Readability readability;
        @XmlElement(namespace = "http://www.scit.org/schema/evaluation_form", required = true)
        protected EvaluationForm.General.Language language;
        @XmlElement(namespace = "http://www.scit.org/schema/evaluation_form", required = true)
        protected EvaluationForm.General.Organization organization;
        @XmlElement(name = "abstract", namespace = "http://www.scit.org/schema/evaluation_form", required = true)
        protected EvaluationForm.General.Abstract _abstract;
        @XmlElement(namespace = "http://www.scit.org/schema/evaluation_form", required = true)
        protected EvaluationForm.General.Keywords keywords;
        @XmlElement(namespace = "http://www.scit.org/schema/evaluation_form", required = true)
        protected EvaluationForm.General.Figures figures;
        @XmlElement(namespace = "http://www.scit.org/schema/evaluation_form", required = true)
        protected EvaluationForm.General.Conclusion conclusion;
        @XmlElement(namespace = "http://www.scit.org/schema/evaluation_form", required = true)
        protected EvaluationForm.General.References references;
        @XmlElement(name = "overall_quality", namespace = "http://www.scit.org/schema/evaluation_form", required = true)
        protected EvaluationForm.General.OverallQuality overallQuality;

        /**
         * Gets the value of the relevance property.
         * 
         * @return
         *     possible object is
         *     {@link EvaluationForm.General.Relevance }
         *     
         */
        public EvaluationForm.General.Relevance getRelevance() {
            return relevance;
        }

        /**
         * Sets the value of the relevance property.
         * 
         * @param value
         *     allowed object is
         *     {@link EvaluationForm.General.Relevance }
         *     
         */
        public void setRelevance(EvaluationForm.General.Relevance value) {
            this.relevance = value;
        }

        /**
         * Gets the value of the readability property.
         * 
         * @return
         *     possible object is
         *     {@link EvaluationForm.General.Readability }
         *     
         */
        public EvaluationForm.General.Readability getReadability() {
            return readability;
        }

        /**
         * Sets the value of the readability property.
         * 
         * @param value
         *     allowed object is
         *     {@link EvaluationForm.General.Readability }
         *     
         */
        public void setReadability(EvaluationForm.General.Readability value) {
            this.readability = value;
        }

        /**
         * Gets the value of the language property.
         * 
         * @return
         *     possible object is
         *     {@link EvaluationForm.General.Language }
         *     
         */
        public EvaluationForm.General.Language getLanguage() {
            return language;
        }

        /**
         * Sets the value of the language property.
         * 
         * @param value
         *     allowed object is
         *     {@link EvaluationForm.General.Language }
         *     
         */
        public void setLanguage(EvaluationForm.General.Language value) {
            this.language = value;
        }

        /**
         * Gets the value of the organization property.
         * 
         * @return
         *     possible object is
         *     {@link EvaluationForm.General.Organization }
         *     
         */
        public EvaluationForm.General.Organization getOrganization() {
            return organization;
        }

        /**
         * Sets the value of the organization property.
         * 
         * @param value
         *     allowed object is
         *     {@link EvaluationForm.General.Organization }
         *     
         */
        public void setOrganization(EvaluationForm.General.Organization value) {
            this.organization = value;
        }

        /**
         * Gets the value of the abstract property.
         * 
         * @return
         *     possible object is
         *     {@link EvaluationForm.General.Abstract }
         *     
         */
        public EvaluationForm.General.Abstract getAbstract() {
            return _abstract;
        }

        /**
         * Sets the value of the abstract property.
         * 
         * @param value
         *     allowed object is
         *     {@link EvaluationForm.General.Abstract }
         *     
         */
        public void setAbstract(EvaluationForm.General.Abstract value) {
            this._abstract = value;
        }

        /**
         * Gets the value of the keywords property.
         * 
         * @return
         *     possible object is
         *     {@link EvaluationForm.General.Keywords }
         *     
         */
        public EvaluationForm.General.Keywords getKeywords() {
            return keywords;
        }

        /**
         * Sets the value of the keywords property.
         * 
         * @param value
         *     allowed object is
         *     {@link EvaluationForm.General.Keywords }
         *     
         */
        public void setKeywords(EvaluationForm.General.Keywords value) {
            this.keywords = value;
        }

        /**
         * Gets the value of the figures property.
         * 
         * @return
         *     possible object is
         *     {@link EvaluationForm.General.Figures }
         *     
         */
        public EvaluationForm.General.Figures getFigures() {
            return figures;
        }

        /**
         * Sets the value of the figures property.
         * 
         * @param value
         *     allowed object is
         *     {@link EvaluationForm.General.Figures }
         *     
         */
        public void setFigures(EvaluationForm.General.Figures value) {
            this.figures = value;
        }

        /**
         * Gets the value of the conclusion property.
         * 
         * @return
         *     possible object is
         *     {@link EvaluationForm.General.Conclusion }
         *     
         */
        public EvaluationForm.General.Conclusion getConclusion() {
            return conclusion;
        }

        /**
         * Sets the value of the conclusion property.
         * 
         * @param value
         *     allowed object is
         *     {@link EvaluationForm.General.Conclusion }
         *     
         */
        public void setConclusion(EvaluationForm.General.Conclusion value) {
            this.conclusion = value;
        }

        /**
         * Gets the value of the references property.
         * 
         * @return
         *     possible object is
         *     {@link EvaluationForm.General.References }
         *     
         */
        public EvaluationForm.General.References getReferences() {
            return references;
        }

        /**
         * Sets the value of the references property.
         * 
         * @param value
         *     allowed object is
         *     {@link EvaluationForm.General.References }
         *     
         */
        public void setReferences(EvaluationForm.General.References value) {
            this.references = value;
        }

        /**
         * Gets the value of the overallQuality property.
         * 
         * @return
         *     possible object is
         *     {@link EvaluationForm.General.OverallQuality }
         *     
         */
        public EvaluationForm.General.OverallQuality getOverallQuality() {
            return overallQuality;
        }

        /**
         * Sets the value of the overallQuality property.
         * 
         * @param value
         *     allowed object is
         *     {@link EvaluationForm.General.OverallQuality }
         *     
         */
        public void setOverallQuality(EvaluationForm.General.OverallQuality value) {
            this.overallQuality = value;
        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;attribute name="score" type="{http://www.scit.org/schema/evaluation_form}score" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        public static class Abstract {

            @XmlAttribute(name = "score")
            protected Integer score;

            /**
             * Gets the value of the score property.
             * 
             * @return
             *     possible object is
             *     {@link Integer }
             *     
             */
            public Integer getScore() {
                return score;
            }

            /**
             * Sets the value of the score property.
             * 
             * @param value
             *     allowed object is
             *     {@link Integer }
             *     
             */
            public void setScore(Integer value) {
                this.score = value;
            }

        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;attribute name="score" type="{http://www.scit.org/schema/evaluation_form}score" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        public static class Conclusion {

            @XmlAttribute(name = "score")
            protected Integer score;

            /**
             * Gets the value of the score property.
             * 
             * @return
             *     possible object is
             *     {@link Integer }
             *     
             */
            public Integer getScore() {
                return score;
            }

            /**
             * Sets the value of the score property.
             * 
             * @param value
             *     allowed object is
             *     {@link Integer }
             *     
             */
            public void setScore(Integer value) {
                this.score = value;
            }

        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;attribute name="score" type="{http://www.scit.org/schema/evaluation_form}score" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        public static class Figures {

            @XmlAttribute(name = "score")
            protected Integer score;

            /**
             * Gets the value of the score property.
             * 
             * @return
             *     possible object is
             *     {@link Integer }
             *     
             */
            public Integer getScore() {
                return score;
            }

            /**
             * Sets the value of the score property.
             * 
             * @param value
             *     allowed object is
             *     {@link Integer }
             *     
             */
            public void setScore(Integer value) {
                this.score = value;
            }

        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;attribute name="score" type="{http://www.scit.org/schema/evaluation_form}score" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        public static class Keywords {

            @XmlAttribute(name = "score")
            protected Integer score;

            /**
             * Gets the value of the score property.
             * 
             * @return
             *     possible object is
             *     {@link Integer }
             *     
             */
            public Integer getScore() {
                return score;
            }

            /**
             * Sets the value of the score property.
             * 
             * @param value
             *     allowed object is
             *     {@link Integer }
             *     
             */
            public void setScore(Integer value) {
                this.score = value;
            }

        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;attribute name="score" type="{http://www.scit.org/schema/evaluation_form}score" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        public static class Language {

            @XmlAttribute(name = "score")
            protected Integer score;

            /**
             * Gets the value of the score property.
             * 
             * @return
             *     possible object is
             *     {@link Integer }
             *     
             */
            public Integer getScore() {
                return score;
            }

            /**
             * Sets the value of the score property.
             * 
             * @param value
             *     allowed object is
             *     {@link Integer }
             *     
             */
            public void setScore(Integer value) {
                this.score = value;
            }

        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;attribute name="score" type="{http://www.scit.org/schema/evaluation_form}score" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        public static class Organization {

            @XmlAttribute(name = "score")
            protected Integer score;

            /**
             * Gets the value of the score property.
             * 
             * @return
             *     possible object is
             *     {@link Integer }
             *     
             */
            public Integer getScore() {
                return score;
            }

            /**
             * Sets the value of the score property.
             * 
             * @param value
             *     allowed object is
             *     {@link Integer }
             *     
             */
            public void setScore(Integer value) {
                this.score = value;
            }

        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;attribute name="score" type="{http://www.scit.org/schema/evaluation_form}score" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        public static class OverallQuality {

            @XmlAttribute(name = "score")
            protected Integer score;

            /**
             * Gets the value of the score property.
             * 
             * @return
             *     possible object is
             *     {@link Integer }
             *     
             */
            public Integer getScore() {
                return score;
            }

            /**
             * Sets the value of the score property.
             * 
             * @param value
             *     allowed object is
             *     {@link Integer }
             *     
             */
            public void setScore(Integer value) {
                this.score = value;
            }

        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;attribute name="score" type="{http://www.scit.org/schema/evaluation_form}score" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        public static class Readability {

            @XmlAttribute(name = "score")
            protected Integer score;

            /**
             * Gets the value of the score property.
             * 
             * @return
             *     possible object is
             *     {@link Integer }
             *     
             */
            public Integer getScore() {
                return score;
            }

            /**
             * Sets the value of the score property.
             * 
             * @param value
             *     allowed object is
             *     {@link Integer }
             *     
             */
            public void setScore(Integer value) {
                this.score = value;
            }

        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;attribute name="score" type="{http://www.scit.org/schema/evaluation_form}score" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        public static class References {

            @XmlAttribute(name = "score")
            protected Integer score;

            /**
             * Gets the value of the score property.
             * 
             * @return
             *     possible object is
             *     {@link Integer }
             *     
             */
            public Integer getScore() {
                return score;
            }

            /**
             * Sets the value of the score property.
             * 
             * @param value
             *     allowed object is
             *     {@link Integer }
             *     
             */
            public void setScore(Integer value) {
                this.score = value;
            }

        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;attribute name="score" type="{http://www.scit.org/schema/evaluation_form}score" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        public static class Relevance {

            @XmlAttribute(name = "score")
            protected Integer score;

            /**
             * Gets the value of the score property.
             * 
             * @return
             *     possible object is
             *     {@link Integer }
             *     
             */
            public Integer getScore() {
                return score;
            }

            /**
             * Sets the value of the score property.
             * 
             * @param value
             *     allowed object is
             *     {@link Integer }
             *     
             */
            public void setScore(Integer value) {
                this.score = value;
            }

        }

    }


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
     *         &lt;element name="originality">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;attribute name="score" type="{http://www.scit.org/schema/evaluation_form}score" />
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="contribution_value">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;attribute name="score" type="{http://www.scit.org/schema/evaluation_form}score" />
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="academic_standards">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;attribute name="score" type="{http://www.scit.org/schema/evaluation_form}score" />
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="rationale">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;attribute name="score" type="{http://www.scit.org/schema/evaluation_form}score" />
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="methodology">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;attribute name="score" type="{http://www.scit.org/schema/evaluation_form}score" />
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="accuracy">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;attribute name="score" type="{http://www.scit.org/schema/evaluation_form}score" />
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="evidence">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;attribute name="score" type="{http://www.scit.org/schema/evaluation_form}score" />
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="scientific_quality">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;attribute name="score" type="{http://www.scit.org/schema/evaluation_form}score" />
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
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
        "originality",
        "contributionValue",
        "academicStandards",
        "rationale",
        "methodology",
        "accuracy",
        "evidence",
        "scientificQuality"
    })
    public static class Technical {

        @XmlElement(namespace = "http://www.scit.org/schema/evaluation_form", required = true)
        protected EvaluationForm.Technical.Originality originality;
        @XmlElement(name = "contribution_value", namespace = "http://www.scit.org/schema/evaluation_form", required = true)
        protected EvaluationForm.Technical.ContributionValue contributionValue;
        @XmlElement(name = "academic_standards", namespace = "http://www.scit.org/schema/evaluation_form", required = true)
        protected EvaluationForm.Technical.AcademicStandards academicStandards;
        @XmlElement(namespace = "http://www.scit.org/schema/evaluation_form", required = true)
        protected EvaluationForm.Technical.Rationale rationale;
        @XmlElement(namespace = "http://www.scit.org/schema/evaluation_form", required = true)
        protected EvaluationForm.Technical.Methodology methodology;
        @XmlElement(namespace = "http://www.scit.org/schema/evaluation_form", required = true)
        protected EvaluationForm.Technical.Accuracy accuracy;
        @XmlElement(namespace = "http://www.scit.org/schema/evaluation_form", required = true)
        protected EvaluationForm.Technical.Evidence evidence;
        @XmlElement(name = "scientific_quality", namespace = "http://www.scit.org/schema/evaluation_form", required = true)
        protected EvaluationForm.Technical.ScientificQuality scientificQuality;

        /**
         * Gets the value of the originality property.
         * 
         * @return
         *     possible object is
         *     {@link EvaluationForm.Technical.Originality }
         *     
         */
        public EvaluationForm.Technical.Originality getOriginality() {
            return originality;
        }

        /**
         * Sets the value of the originality property.
         * 
         * @param value
         *     allowed object is
         *     {@link EvaluationForm.Technical.Originality }
         *     
         */
        public void setOriginality(EvaluationForm.Technical.Originality value) {
            this.originality = value;
        }

        /**
         * Gets the value of the contributionValue property.
         * 
         * @return
         *     possible object is
         *     {@link EvaluationForm.Technical.ContributionValue }
         *     
         */
        public EvaluationForm.Technical.ContributionValue getContributionValue() {
            return contributionValue;
        }

        /**
         * Sets the value of the contributionValue property.
         * 
         * @param value
         *     allowed object is
         *     {@link EvaluationForm.Technical.ContributionValue }
         *     
         */
        public void setContributionValue(EvaluationForm.Technical.ContributionValue value) {
            this.contributionValue = value;
        }

        /**
         * Gets the value of the academicStandards property.
         * 
         * @return
         *     possible object is
         *     {@link EvaluationForm.Technical.AcademicStandards }
         *     
         */
        public EvaluationForm.Technical.AcademicStandards getAcademicStandards() {
            return academicStandards;
        }

        /**
         * Sets the value of the academicStandards property.
         * 
         * @param value
         *     allowed object is
         *     {@link EvaluationForm.Technical.AcademicStandards }
         *     
         */
        public void setAcademicStandards(EvaluationForm.Technical.AcademicStandards value) {
            this.academicStandards = value;
        }

        /**
         * Gets the value of the rationale property.
         * 
         * @return
         *     possible object is
         *     {@link EvaluationForm.Technical.Rationale }
         *     
         */
        public EvaluationForm.Technical.Rationale getRationale() {
            return rationale;
        }

        /**
         * Sets the value of the rationale property.
         * 
         * @param value
         *     allowed object is
         *     {@link EvaluationForm.Technical.Rationale }
         *     
         */
        public void setRationale(EvaluationForm.Technical.Rationale value) {
            this.rationale = value;
        }

        /**
         * Gets the value of the methodology property.
         * 
         * @return
         *     possible object is
         *     {@link EvaluationForm.Technical.Methodology }
         *     
         */
        public EvaluationForm.Technical.Methodology getMethodology() {
            return methodology;
        }

        /**
         * Sets the value of the methodology property.
         * 
         * @param value
         *     allowed object is
         *     {@link EvaluationForm.Technical.Methodology }
         *     
         */
        public void setMethodology(EvaluationForm.Technical.Methodology value) {
            this.methodology = value;
        }

        /**
         * Gets the value of the accuracy property.
         * 
         * @return
         *     possible object is
         *     {@link EvaluationForm.Technical.Accuracy }
         *     
         */
        public EvaluationForm.Technical.Accuracy getAccuracy() {
            return accuracy;
        }

        /**
         * Sets the value of the accuracy property.
         * 
         * @param value
         *     allowed object is
         *     {@link EvaluationForm.Technical.Accuracy }
         *     
         */
        public void setAccuracy(EvaluationForm.Technical.Accuracy value) {
            this.accuracy = value;
        }

        /**
         * Gets the value of the evidence property.
         * 
         * @return
         *     possible object is
         *     {@link EvaluationForm.Technical.Evidence }
         *     
         */
        public EvaluationForm.Technical.Evidence getEvidence() {
            return evidence;
        }

        /**
         * Sets the value of the evidence property.
         * 
         * @param value
         *     allowed object is
         *     {@link EvaluationForm.Technical.Evidence }
         *     
         */
        public void setEvidence(EvaluationForm.Technical.Evidence value) {
            this.evidence = value;
        }

        /**
         * Gets the value of the scientificQuality property.
         * 
         * @return
         *     possible object is
         *     {@link EvaluationForm.Technical.ScientificQuality }
         *     
         */
        public EvaluationForm.Technical.ScientificQuality getScientificQuality() {
            return scientificQuality;
        }

        /**
         * Sets the value of the scientificQuality property.
         * 
         * @param value
         *     allowed object is
         *     {@link EvaluationForm.Technical.ScientificQuality }
         *     
         */
        public void setScientificQuality(EvaluationForm.Technical.ScientificQuality value) {
            this.scientificQuality = value;
        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;attribute name="score" type="{http://www.scit.org/schema/evaluation_form}score" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        public static class AcademicStandards {

            @XmlAttribute(name = "score")
            protected Integer score;

            /**
             * Gets the value of the score property.
             * 
             * @return
             *     possible object is
             *     {@link Integer }
             *     
             */
            public Integer getScore() {
                return score;
            }

            /**
             * Sets the value of the score property.
             * 
             * @param value
             *     allowed object is
             *     {@link Integer }
             *     
             */
            public void setScore(Integer value) {
                this.score = value;
            }

        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;attribute name="score" type="{http://www.scit.org/schema/evaluation_form}score" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        public static class Accuracy {

            @XmlAttribute(name = "score")
            protected Integer score;

            /**
             * Gets the value of the score property.
             * 
             * @return
             *     possible object is
             *     {@link Integer }
             *     
             */
            public Integer getScore() {
                return score;
            }

            /**
             * Sets the value of the score property.
             * 
             * @param value
             *     allowed object is
             *     {@link Integer }
             *     
             */
            public void setScore(Integer value) {
                this.score = value;
            }

        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;attribute name="score" type="{http://www.scit.org/schema/evaluation_form}score" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        public static class ContributionValue {

            @XmlAttribute(name = "score")
            protected Integer score;

            /**
             * Gets the value of the score property.
             * 
             * @return
             *     possible object is
             *     {@link Integer }
             *     
             */
            public Integer getScore() {
                return score;
            }

            /**
             * Sets the value of the score property.
             * 
             * @param value
             *     allowed object is
             *     {@link Integer }
             *     
             */
            public void setScore(Integer value) {
                this.score = value;
            }

        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;attribute name="score" type="{http://www.scit.org/schema/evaluation_form}score" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        public static class Evidence {

            @XmlAttribute(name = "score")
            protected Integer score;

            /**
             * Gets the value of the score property.
             * 
             * @return
             *     possible object is
             *     {@link Integer }
             *     
             */
            public Integer getScore() {
                return score;
            }

            /**
             * Sets the value of the score property.
             * 
             * @param value
             *     allowed object is
             *     {@link Integer }
             *     
             */
            public void setScore(Integer value) {
                this.score = value;
            }

        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;attribute name="score" type="{http://www.scit.org/schema/evaluation_form}score" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        public static class Methodology {

            @XmlAttribute(name = "score")
            protected Integer score;

            /**
             * Gets the value of the score property.
             * 
             * @return
             *     possible object is
             *     {@link Integer }
             *     
             */
            public Integer getScore() {
                return score;
            }

            /**
             * Sets the value of the score property.
             * 
             * @param value
             *     allowed object is
             *     {@link Integer }
             *     
             */
            public void setScore(Integer value) {
                this.score = value;
            }

        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;attribute name="score" type="{http://www.scit.org/schema/evaluation_form}score" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        public static class Originality {

            @XmlAttribute(name = "score")
            protected Integer score;

            /**
             * Gets the value of the score property.
             * 
             * @return
             *     possible object is
             *     {@link Integer }
             *     
             */
            public Integer getScore() {
                return score;
            }

            /**
             * Sets the value of the score property.
             * 
             * @param value
             *     allowed object is
             *     {@link Integer }
             *     
             */
            public void setScore(Integer value) {
                this.score = value;
            }

        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;attribute name="score" type="{http://www.scit.org/schema/evaluation_form}score" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        public static class Rationale {

            @XmlAttribute(name = "score")
            protected Integer score;

            /**
             * Gets the value of the score property.
             * 
             * @return
             *     possible object is
             *     {@link Integer }
             *     
             */
            public Integer getScore() {
                return score;
            }

            /**
             * Sets the value of the score property.
             * 
             * @param value
             *     allowed object is
             *     {@link Integer }
             *     
             */
            public void setScore(Integer value) {
                this.score = value;
            }

        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;attribute name="score" type="{http://www.scit.org/schema/evaluation_form}score" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        public static class ScientificQuality {

            @XmlAttribute(name = "score")
            protected Integer score;

            /**
             * Gets the value of the score property.
             * 
             * @return
             *     possible object is
             *     {@link Integer }
             *     
             */
            public Integer getScore() {
                return score;
            }

            /**
             * Sets the value of the score property.
             * 
             * @param value
             *     allowed object is
             *     {@link Integer }
             *     
             */
            public void setScore(Integer value) {
                this.score = value;
            }

        }

    }

}
