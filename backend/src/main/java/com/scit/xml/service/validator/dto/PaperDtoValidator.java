package com.scit.xml.service.validator.dto;

import com.scit.xml.common.util.XsdUtils;
import com.scit.xml.model.paper.Paper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class PaperDtoValidator {

    @Value("classpath:xsd/paper.xsd")
    private Resource paperSchema;

    public Paper validate(String xml) {
        return XsdUtils.unmarshal(Paper.class, paperSchema, xml);
    }
}
