package com.scit.xml.service.validator.dto;

import com.scit.xml.common.util.XsdUtils;
import com.scit.xml.model.review.Review;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class ReviewDtoValidator {

    @Value("classpath:xsd/review.xsd")
    private Resource reviewSchema;

    public Review validate(String xml) {
        return XsdUtils.unmarshal(Review.class, reviewSchema, xml);
    }
}
