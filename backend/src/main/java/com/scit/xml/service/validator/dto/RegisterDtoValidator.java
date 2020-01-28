package com.scit.xml.service.validator.dto;

import com.scit.xml.common.util.XsdUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class RegisterDtoValidator {

    @Value("classpath:xsd/user.xsd")
    private Resource registerRequestSchema;

    public void validate(String xml) {
        XsdUtils.validate(xml, registerRequestSchema);
    }
}
