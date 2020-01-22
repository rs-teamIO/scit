package com.scit.xml.service.validator.database;


import com.scit.xml.common.api.RestApiConstants;
import com.scit.xml.common.api.RestApiErrors;
import com.scit.xml.common.api.RestApiRequestParameters;
import com.scit.xml.common.util.BadRequestUtils;
import com.scit.xml.common.util.NotFoundUtils;
import com.scit.xml.exception.RestApiError;
import com.scit.xml.model.cover_letter.CoverLetter;
import com.scit.xml.repository.CoverLetterRepository;
import com.scit.xml.repository.PaperRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CoverLetterDatabaseValidator {

    private final CoverLetterRepository coverLetterRepository;
    private final PaperRepository paperRepository;

    public void validateCreateRequest(CoverLetter coverLetter) {
        this.validateThatPaperExists(coverLetter.getPaperId());
        this.validateThatCoverLetterDoesNotExistForGivenPaper(coverLetter.getPaperId());
    }

    public String validateExportRequest(String coverLetterId) {
        return this.validateThatCoverLetterExists(coverLetterId);
    }

    private String validateThatCoverLetterExists(String coverLetterId) {
        String xml = this.coverLetterRepository.findById(coverLetterId);
        NotFoundUtils.throwNotFoundExceptionIf(StringUtils.isEmpty(xml),
                RestApiErrors.entityWithGivenFieldNotFound(RestApiConstants.COVER_LETTER, RestApiRequestParameters.ID));
        return xml;
    }

    private void validateThatPaperExists(String paperId) {
        String xml = this.paperRepository.findById(paperId);
        NotFoundUtils.throwNotFoundExceptionIf(StringUtils.isEmpty(xml),
                RestApiErrors.entityWithGivenFieldNotFound(RestApiConstants.PAPER, RestApiRequestParameters.ID));
    }

    private void validateThatCoverLetterDoesNotExistForGivenPaper(String paperId) {
        String xml = this.coverLetterRepository.findByPaperId(paperId);
        BadRequestUtils.throwInvalidRequestDataExceptionIf(StringUtils.isNotEmpty(xml),
                RestApiErrors.entityWithGivenFieldAlreadyExists(RestApiConstants.COVER_LETTER, RestApiRequestParameters.PAPER_ID));
    }
}
