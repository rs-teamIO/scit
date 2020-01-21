<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:template match="/">
        <html>
            <head>
                <title>Cover letter</title>
            </head>
            <body>
                <xsl:apply-templates select="cover_letter:cover_letter"/>
            </body>
        </html>
    </xsl:template>
    <xsl:template match="cover_letter:cover_letter">
        <p>
            <xsl:value-of select="text"/>
        </p>

        <p>
            <xsl:value-of select="date"/>
        </p>
    </xsl:template>
</xsl:stylesheet>