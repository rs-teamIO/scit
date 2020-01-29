package com.scit.xml.service.validator.database;

import com.scit.xml.common.api.RestApiConstants;
import com.scit.xml.common.api.RestApiErrors;
import com.scit.xml.common.api.RestApiRequestParameters;
import com.scit.xml.common.util.NotFoundUtils;
import com.scit.xml.model.evaluation_form.EvaluationForm;
import com.scit.xml.repository.EvaluationFormRepository;
import com.scit.xml.repository.PaperRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EvaluationFormDatabaseValidator {

    private final EvaluationFormRepository evaluationFormRepository;
    private final PaperRepository paperRepository;

    public void validateCreateRequest(EvaluationForm evaluationForm, String paperId) {
        // TODO: validateCreateRequest for EvaluationForm
        this.validateThatPaperExists(paperId);
    }

    public String validateExportRequest(String coverLetterId) {
        return this.validateThatEvaluationFormExists(coverLetterId);
    }

    private String validateThatEvaluationFormExists(String evaluationFormId) {
        String xml = this.evaluationFormRepository.findById(evaluationFormId);
        NotFoundUtils.throwNotFoundExceptionIf(StringUtils.isEmpty(xml),
                RestApiErrors.entityWithGivenFieldNotFound(RestApiConstants.EVALUATION_FORM, RestApiRequestParameters.ID));
        return xml;
    }

    private void validateThatPaperExists(String paperId) {
        String xml = this.paperRepository.findById(paperId);
        NotFoundUtils.throwNotFoundExceptionIf(StringUtils.isEmpty(xml),
                RestApiErrors.entityWithGivenFieldNotFound(RestApiConstants.PAPER, RestApiRequestParameters.ID));
    }

}
