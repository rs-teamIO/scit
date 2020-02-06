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

    private final static String PAPER_ID_XPATH = "/paper/@id";
    private final static String PAPER_TITLE_XPATH = "/paper/title";
    private final static String PAPER_STATUS_XPATH = "/paper/paper_info/status";

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

    public static String convertToUserXmlResponse(String userXml) {
        XmlWrapper userWrapper = new XmlWrapper(userXml);
        String id = XmlExtractorUtil.extractStringAndValidateNotBlank(userWrapper.getDocument(), USER_ID_XPATH);
        String username = XmlExtractorUtil.extractStringAndValidateNotBlank(userWrapper.getDocument(), USER_USERNAME_XPATH);
        String email = XmlExtractorUtil.extractStringAndValidateNotBlank(userWrapper.getDocument(), USER_EMAIL_XPATH);
        String firstName = XmlExtractorUtil.extractStringAndValidateNotBlank(userWrapper.getDocument(), USER_FIRST_NAME_XPATH);
        String lastName = XmlExtractorUtil.extractStringAndValidateNotBlank(userWrapper.getDocument(), USER_LAST_NAME_XPATH);

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

    public static String convertPaperToXmlResponse(String paperXml) {
        XmlWrapper paperWrapper = new XmlWrapper(paperXml);
        String id = XmlExtractorUtil.extractStringAndValidateNotBlank(paperWrapper.getDocument(), PAPER_ID_XPATH);
        String title = XmlExtractorUtil.extractStringAndValidateNotBlank(paperWrapper.getDocument(), PAPER_TITLE_XPATH);
        String status = XmlExtractorUtil.extractStringAndValidateNotBlank(paperWrapper.getDocument(), PAPER_STATUS_XPATH);

        StringBuilder sb = new StringBuilder();

        sb.append("<paper>\n").append(INDENT)
                .append("<id>").append(id).append("</id>").append(INDENT)
                .append("<title>").append(title).append("</title>").append(INDENT)
                .append("<status>").append(status).append("</status>")
        .append("</paper>");

        return sb.toString();
    }
}
