package com.scit.xml.security;

import com.scit.xml.common.util.XmlExtractorUtil;
import com.scit.xml.common.util.XmlWrapper;
import com.scit.xml.common.util.XsdUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

@Service
public class JwtRequestValidator {

    private final String USERNAME = "/login/username";
    private final String PASSWORD = "/login/password";

    @Value("classpath:xsd/jwt_request.xsd")
    private Resource jwtRequestSchema;

    public JwtRequest validate(String xml) {
        XsdUtils.validate(xml, jwtRequestSchema);
        Document document = new XmlWrapper(xml).getDocument();
        JwtRequest jwtRequest = new JwtRequest();
        extractRequest(document, jwtRequest);
        return jwtRequest;
    }

    private void extractRequest(Document document, JwtRequest jwtRequest) {
        String username = XmlExtractorUtil.extractStringAndValidateNotBlank(document, USERNAME);
        String password = XmlExtractorUtil.extractStringAndValidateNotBlank(document, PASSWORD);

        jwtRequest.setUsername(username);
        jwtRequest.setPassword(password);
    }
}
