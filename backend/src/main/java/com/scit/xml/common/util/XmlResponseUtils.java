package com.scit.xml.common.util;

import com.scit.xml.dto.XmlResponse;

/**
 * Contains utility methods used to construct an XML response from the API
 */
public final class XmlResponseUtils {

    private final static String INDENT = "  ";

    private final static String USER_ID_XPATH = "/user/@id";
    private final static String USER_USERNAME_XPATH = "/user/username";
    private final static String USER_EMAIL_XPATH = "/user/email";
    private final static String USER_FIRST_NAME_XPATH = "/user/first_name";
    private final static String USER_LAST_NAME_XPATH = "/user/last_name";

    private XmlResponseUtils() { }

    public static String wrapResponse(XmlResponse xmlResponse) {
        StringBuilder sb = new StringBuilder();

        sb.append("<response>\n");
        xmlResponse.keySet().stream().forEach(key -> {
            sb.append(INDENT).append(String.format("<%s>\n", key));
            sb.append(INDENT).append(INDENT).append(xmlResponse.get(key)).append("\n");
            sb.append(INDENT).append(String.format("</%s>\n", key));
        });
        sb.append("</response>");

        return sb.toString();
    }

    public static String convertToUserXmlResponse(String xml) {
        XmlWrapper xmlWrapper = new XmlWrapper(xml);
        String id = XmlExtractorUtil.extractStringAndValidateNotBlank(xmlWrapper.getDocument(), USER_ID_XPATH);
        String username = XmlExtractorUtil.extractStringAndValidateNotBlank(xmlWrapper.getDocument(), USER_USERNAME_XPATH);
        String email = XmlExtractorUtil.extractStringAndValidateNotBlank(xmlWrapper.getDocument(), USER_EMAIL_XPATH);
        String firstName = XmlExtractorUtil.extractStringAndValidateNotBlank(xmlWrapper.getDocument(), USER_FIRST_NAME_XPATH);
        String lastName = XmlExtractorUtil.extractStringAndValidateNotBlank(xmlWrapper.getDocument(), USER_LAST_NAME_XPATH);

        StringBuilder sb = new StringBuilder();

        sb.append("<user>\n").append(INDENT)
                .append("<id>").append(id).append("</id>").append(INDENT)
                .append("<username>").append(username).append("</username>").append(INDENT)
                .append("<email>").append(email).append("</email>")
                .append("<first_name>").append(firstName).append("</first_name>")
                .append("<last_name>").append(lastName).append("</last_name>")
        .append("</user>");

        return sb.toString();
    }
}
