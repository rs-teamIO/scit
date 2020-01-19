package com.scit.xml.service.validator.dto;

import com.scit.xml.common.util.SchemaValidationEventHandler;
import com.scit.xml.common.util.XmlExtractorUtil;
import com.scit.xml.common.util.XmlWrapper;
import com.scit.xml.common.util.XsdUtils;
import com.scit.xml.exception.InternalServerException;
import com.scit.xml.model.Paper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.*;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;

@Service
public class PaperDtoValidator {

    @Value("classpath:xsd/paper.xsd")
    private Resource paperSchema;

    public Paper validate(String xml) {
        return XsdUtils.unmarshal(Paper.class, paperSchema, xml);
    }
}
