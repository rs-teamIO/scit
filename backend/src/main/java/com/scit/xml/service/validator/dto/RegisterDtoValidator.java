package com.scit.xml.service.validator.dto;

import com.scit.xml.common.util.XsdUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

/**
 * Contains DTO vaidation methods for registration
 */
@Service
public class RegisterDtoValidator {

    @Value("classpath:xsd/user.xsd")
    private Resource registerRequestSchema;

    /**
     * Validates registration request based on user schema
     * @param xml XML String representation to be validated
     */
    public void validate(String xml) {
        XsdUtils.validate(xml, registerRequestSchema);
    }
}
