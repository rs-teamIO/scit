<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:evaluation_form="http://www.scit.org/schema/evaluation_form">
    <xsl:template match="/evaluation_form:evaluation_form">
        <html>
            <head>
                <title>Evaluation form</title>
            </head>
            <body style="font-family: Times New Roman;">
                <h2>Evaluation form</h2>
                <xsl:apply-templates select="./evaluation_form:general"/>
                <xsl:apply-templates select="./evaluation_form:technical"/>
                <xsl:apply-templates select="./evaluation_form:recommendation"/>
            </body>
        </html>
    </xsl:template>

    <xsl:template match="evaluation_form:general">
        <h3>General</h3>
        <table>
            <tr><th>Name</th><th>Score</th></tr>
            <xsl:apply-templates select="./evaluation_form:relevance"/>
            <xsl:apply-templates select="./evaluation_form:readability"/>
            <xsl:apply-templates select="./evaluation_form:language"/>
            <xsl:apply-templates select="./evaluation_form:organization"/>
            <xsl:apply-templates select="./evaluation_form:abstract"/>
            <xsl:apply-templates select="./evaluation_form:keywords"/>
            <xsl:apply-templates select="./evaluation_form:figures"/>
            <xsl:apply-templates select="./evaluation_form:conclusion"/>
            <xsl:apply-templates select="./evaluation_form:references"/>
            <xsl:apply-templates select="./evaluation_form:overall_quality"/>
        </table>
    </xsl:template>

    <xsl:template match="evaluation_form:technical">
        <h3>Technical</h3>
        <table>
            <tr><th>Name</th><th>Score</th></tr>
            <xsl:apply-templates select="./evaluation_form:originality"/>
            <xsl:apply-templates select="./evaluation_form:contribution_value"/>
            <xsl:apply-templates select="./evaluation_form:academic_standards"/>
            <xsl:apply-templates select="./evaluation_form:rationale"/>
            <xsl:apply-templates select="./evaluation_form:methodology"/>
            <xsl:apply-templates select="./evaluation_form:accuracy"/>
            <xsl:apply-templates select="./evaluation_form:evidence"/>
            <xsl:apply-templates select="./evaluation_form:scientific_quality"/>
        </table>
    </xsl:template>

    <xsl:template match="evaluation_form:recommendation">
        <h3>Reviewer's recommendation</h3>
        <p><xsl:value-of select="."/></p>
    </xsl:template>

    <xsl:template match="evaluation_form:relevance">
        <tr>
            <td>Relevance</td>
            <td><xsl:value-of select="./@score"/></td>
        </tr>
    </xsl:template>
    <xsl:template match="evaluation_form:readability">
        <tr>
            <td>Readability</td>
            <td><xsl:value-of select="./@score"/></td>
        </tr>
    </xsl:template>
    <xsl:template match="evaluation_form:language">
        <tr>
            <td>Language</td>
            <td><xsl:value-of select="./@score"/></td>
        </tr>
    </xsl:template>
    <xsl:template match="evaluation_form:organization">
        <tr>
            <td>Organization</td>
            <td><xsl:value-of select="./@score"/></td>
        </tr>
    </xsl:template>
    <xsl:template match="evaluation_form:abstract">
        <tr>
            <td>Abstract</td>
            <td><xsl:value-of select="./@score"/></td>
        </tr>
    </xsl:template>
    <xsl:template match="evaluation_form:keywords">
        <tr>
            <td>Keywords</td>
            <td><xsl:value-of select="./@score"/></td>
        </tr>
    </xsl:template>
    <xsl:template match="evaluation_form:figures">
        <tr>
            <td>Figures</td>
            <td><xsl:value-of select="./@score"/></td>
        </tr>
    </xsl:template>
    <xsl:template match="evaluation_form:conclusion">
        <tr>
            <td>Conclusion</td>
            <td><xsl:value-of select="./@score"/></td>
        </tr>
    </xsl:template>
    <xsl:template match="evaluation_form:references">
        <tr>
            <td>References</td>
            <td><xsl:value-of select="./@score"/></td>
        </tr>
    </xsl:template>
    <xsl:template match="evaluation_form:overall_quality">
        <tr>
            <td>Overall quality</td>
            <td><xsl:value-of select="./@score"/></td>
        </tr>
    </xsl:template>

    <xsl:template match="evaluation_form:originality">
        <tr>
            <td>Originality</td>
            <td><xsl:value-of select="./@score"/></td>
        </tr>
    </xsl:template>
    <xsl:template match="evaluation_form:contribution_value">
        <tr>
            <td>Contribution Value</td>
            <td><xsl:value-of select="./@score"/></td>
        </tr>
    </xsl:template>
    <xsl:template match="evaluation_form:academic_standards">
        <tr>
            <td>Academic Standards</td>
            <td><xsl:value-of select="./@score"/></td>
        </tr>
    </xsl:template>
    <xsl:template match="evaluation_form:rationale">
        <tr>
            <td>Rationale</td>
            <td><xsl:value-of select="./@score"/></td>
        </tr>
    </xsl:template>
    <xsl:template match="evaluation_form:methodology">
        <tr>
            <td>Methodology</td>
            <td><xsl:value-of select="./@score"/></td>
        </tr>
    </xsl:template>
    <xsl:template match="evaluation_form:accuracy">
        <tr>
            <td>Accuracy</td>
            <td><xsl:value-of select="./@score"/></td>
        </tr>
    </xsl:template>
    <xsl:template match="evaluation_form:evidence">
        <tr>
            <td>Evidence</td>
            <td><xsl:value-of select="./@score"/></td>
        </tr>
    </xsl:template>
    <xsl:template match="evaluation_form:scientific_quality">
        <tr>
            <td>Scientific Quality</td>
            <td><xsl:value-of select="./@score"/></td>
        </tr>
    </xsl:template>
</xsl:stylesheet>