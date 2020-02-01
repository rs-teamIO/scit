<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:paper="http://www.scit.org/schema/paper">
    <xsl:template match="/paper:paper">
        <html>
            <head>
                <title>Scientific paper</title>
            </head>
            <body style="font-family: Times New Roman;">
                <xsl:apply-templates select="./paper:title"/>
                <xsl:apply-templates select="./paper:authors"/>
                <xsl:apply-templates select="./paper:abstract"/>
                <xsl:apply-templates select="./paper:section"/>
                <xsl:apply-templates select="./paper:references"/>
                <xsl:apply-templates select="./paper:comment"/>
            </body>
        </html>
    </xsl:template>

    <xsl:template match="paper:title">
        <br/><br/>
        <h1 style="font-family: Times New Roman;font-size: 24;text-align: center"><xsl:value-of select="."/></h1>
    </xsl:template>

    <xsl:template match="paper:authors">
        <xsl:apply-templates select="./paper:author"/>
    </xsl:template>

    <xsl:template match="paper:author">
        <p style="font-family: Times New Roman;font-size: 12;text-align: center"><xsl:value-of select="./paper:name"/></p>
        <p style="font-family: Times New Roman;font-size: 10;text-align: center"><xsl:value-of select="./paper:affilliation"/></p>
        <p style="font-family: Times New Roman;font-size: 10;text-align: center"><xsl:value-of select="./paper:email"/></p>
    </xsl:template>

    <xsl:template match="paper:abstract">
        <div style="margin-top: 30px; margin-right: 40px; margin-bottom:15px; margin-left: 40px;">
            <p style="font-family: Times New Roman;font-size: 9;font-weight: bold; font-style: italic; text-align: justify">Abstract - <xsl:value-of select="./paper:content"/></p>
            <p style="font-family: Times New Roman;font-size: 9;font-weight: bold; font-style: italic; text-align: justify">Keywords - <xsl:apply-templates select="./paper:keywords"/></p>
        </div>
    </xsl:template>

    <xsl:template match="paper:keywords">
        <xsl:value-of select="."/>,
    </xsl:template>

    <xsl:template match="paper:section">
        <div style="margin-top: 15px; margin-right: 40px; margin-bottom:15px; margin-left: 40px;">
            <xsl:apply-templates select="./paper:heading"/>
            <xsl:apply-templates select="./paper:content"/>
            <xsl:apply-templates select="./paper:comment"/>
        </div>
    </xsl:template>

    <xsl:template match="paper:heading">
        <h2 style="font-family: Times New Roman;font-size: 14;font-weight: bold;text-align: center"><xsl:value-of select="."/></h2>
    </xsl:template>

    <xsl:template match="paper:content">
        <p style="font-family: Times New Roman;font-size: 10;text-align: justify"><xsl:value-of select="."/></p>
        <xsl:apply-templates select="./paper:image"/>
    </xsl:template>

    <xsl:template match="//paper:image">
        <p style="text-align: center;">
            <img style=" width: 210px; height:100px; margin: 15px;" >
                <xsl:attribute name="src"><xsl:value-of select="./@link"/>
                </xsl:attribute>
            </img>
            <p style="font-family: Times New Roman;font-size: 10;font-weight: bold; text-align: center"><xsl:value-of select="./@img_title"/></p>
        </p>
    </xsl:template>

    <xsl:template match="paper:references">
        <div style="font-family: Times New Roman;font-size: 9;margin-top: 15px; margin-right: 40px; margin-bottom:15px; margin-left: 40px;">
            <h2 style="font-family: Times New Roman;font-size: 14;font-weight: bold;text-align: center">References</h2>
            <xsl:for-each select="paper:reference">
                [<xsl:number value="position()" format="1"/>]
                <xsl:value-of select="."/>
                <br/>
            </xsl:for-each>
        </div>
    </xsl:template>

    <xsl:template match="//paper:comment">
        <div style="margin-top: 15px; margin-right: 40px; margin-bottom:15px; margin-left: 40px;">
            <p style="font-family: Times New Roman;font-size: 12;text-align: justify; color: red;"><xsl:value-of select="."/></p>
        </div>
    </xsl:template>

</xsl:stylesheet>