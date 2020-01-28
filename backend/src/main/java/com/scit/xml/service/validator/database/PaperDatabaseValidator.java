package com.scit.xml.service.validator.database;

import com.scit.xml.common.api.RestApiConstants;
import com.scit.xml.common.api.RestApiErrors;
import com.scit.xml.common.api.RestApiRequestParameters;
import com.scit.xml.common.util.NotFoundUtils;
import com.scit.xml.repository.PaperRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaperDatabaseValidator {

    private final PaperRepository paperRepository;

    public String validateExportRequest(String paperId) {
        return this.validateThatPaperExists(paperId);
    }

    private String validateThatPaperExists(String paperId) {
        String xml = this.paperRepository.findById(paperId);
        NotFoundUtils.throwNotFoundExceptionIf(StringUtils.isEmpty(xml),
                RestApiErrors.entityWithGivenFieldNotFound(RestApiConstants.PAPER, RestApiRequestParameters.ID));
        return xml;
    }
}
