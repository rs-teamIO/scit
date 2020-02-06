<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:cover_letter="http://www.scit.org/schema/cover_letter">
    <xsl:template match="/cover_letter:cover_letter">
        <html>
            <head>
                <title>Cover letter</title>
            </head>
            <body style="font-family: Times New Roman;">
                <h2>Cover letter</h2>
                <p><xsl:value-of select="."/></p>
                <p style="font-style: italic;"><xsl:value-of select="@cover_letter:date"/></p>
            </body>
        </html>
    </xsl:template>
</xsl:stylesheet>