<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <!-- Take a look at this generator -->
    <!-- https://people.eng.unimelb.edu.au/baileyj/xsltgen/XSLTGen.htm -->

    <xsl:template match="/">
        <html>
            <head>
                <title>Scientific Paper</title>
            </head>
            <body>
                <h1>
                    <xsl:value-of select="paper/title"/>
                </h1>
                <xsl:apply-templates select="paper"/>
            </body>
        </html>
    </xsl:template>

</xsl:stylesheet>