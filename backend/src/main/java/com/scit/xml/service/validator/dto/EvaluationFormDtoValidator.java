package com.scit.xml.service.validator.dto;

import com.scit.xml.common.util.XsdUtils;
import com.scit.xml.model.evaluation_form.EvaluationForm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class EvaluationFormDtoValidator {

    @Value("classpath:xsd/evaluation_form.xsd")
    private Resource evaluationFormSchema;

    public EvaluationForm validate(String xml) {
        return XsdUtils.unmarshal(EvaluationForm.class, evaluationFormSchema, xml);
    }
}
