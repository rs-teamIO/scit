package com.scit.xml.service.validator.dto;

import com.scit.xml.common.util.XsdUtils;
import com.scit.xml.model.cover_letter.CoverLetter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class CoverLetterDtoValidator {

    @Value("classpath:xsd/cover_letter.xsd")
    private Resource coverLetterSchema;

    public CoverLetter validate(String xml) {
        return XsdUtils.unmarshal(CoverLetter.class, coverLetterSchema, xml);
    }
}
