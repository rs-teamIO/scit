<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:paper="http://www.scit.org/schema/paper"
                xmlns:review="http://www.scit.org/schema/review"
                exclude-result-prefixes="paper review">
    <xsl:output method="xml" omit-xml-declaration="yes" />
    <xsl:template match="/paper:paper">
        <review:review>
            <xsl:apply-templates select="//paper:comment"/>
        </review:review >
    </xsl:template>

    <xsl:template match="//paper:comment">
        <xsl:copy-of select="self::node()"/>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="//paper:comment">
        <xsl:element name="review:comment">
            <xsl:attribute name="reference_id"><xsl:value-of select="./@paper:id"/></xsl:attribute>

            <xsl:apply-templates/>
        </xsl:element>
    </xsl:template>

</xsl:stylesheet>
