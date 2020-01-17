package com.scit.xml.common.util;

import com.scit.xml.dto.XmlResponse;

public class XmlResponseUtils {

    private XmlResponseUtils() { }

    public static String toXmlString(XmlResponse xmlResponse) {
        StringBuilder sb = new StringBuilder();
        String indent = "  ";

        sb.append("<response>\n");
        for (String key : xmlResponse.keySet()) {
            sb.append(indent).append(String.format("<%s>\n", key));
            sb.append(indent).append(indent).append(xmlResponse.get(key)).append("\n");
            sb.append(indent).append(String.format("</%s>\n", key));
        }
        sb.append("</response>");

        return sb.toString();
    }
}
