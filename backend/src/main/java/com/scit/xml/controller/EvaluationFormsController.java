package com.scit.xml.controller;

import com.scit.xml.common.Constants;
import com.scit.xml.common.api.RestApiConstants;
import com.scit.xml.common.api.RestApiEndpoints;
import com.scit.xml.common.api.RestApiRequestParameters;
import com.scit.xml.common.util.ResourceUtils;
import com.scit.xml.common.util.XPathUtils;
import com.scit.xml.common.util.XmlResponseUtils;
import com.scit.xml.dto.XmlResponse;
import com.scit.xml.model.cover_letter.CoverLetter;
import com.scit.xml.model.evaluation_form.EvaluationForm;
import com.scit.xml.model.paper.Paper;
import com.scit.xml.service.*;
import com.scit.xml.service.validator.dto.CoverLetterDtoValidator;
import com.scit.xml.service.validator.dto.EvaluationFormDtoValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;

@RestController
@RequestMapping(RestApiEndpoints.EVALUATION_FORMS)
@RequiredArgsConstructor
public class EvaluationFormsController {

    private final EvaluationFormService evaluationFormService;
    private final EvaluationFormDtoValidator evaluationFormDtoValidator;
    private final PaperService paperService;
    private final EmailService emailService;
    private final UserService userService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping(params = { RestApiRequestParameters.PAPER_ID },
                 consumes = MediaType.APPLICATION_XML_VALUE,
                 produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> create(@RequestBody String xml,
                                         @RequestParam(RestApiRequestParameters.PAPER_ID) String paperId) throws MessagingException {
        EvaluationForm evaluationForm = this.evaluationFormDtoValidator.validate(xml);

        // TODO: Change EvaluationFormSchema to contain paperId (probably not, because one paper can have multiple cover letters)
        // TODO: Include paper title and author data in the schema for evaluaiton form

        String id = this.evaluationFormService.createEvaluationForm(evaluationForm, paperId);

        String editorEmail = this.userService.getUserEmail(Constants.EDITOR_USERNAME);
        byte[] pdf = ResourceUtils.convertResourceToByteArray(this.evaluationFormService.exportToPdf(id));
        String html = ResourceUtils.convertResourceToString(this.evaluationFormService.exportToHtml(id));

        // TODO: Add paper title and username who submitted the evaluation as parameters to mailsender method
        // (ktukelic) This will probably require unmarshalling in the repository layer, or service layer.
        // Will require investigation to see if these changes will make a big impact on the structure.
        //
        this.emailService.sendEvaluationFormSubmissionNotificationEmail(editorEmail, evaluationForm, pdf, html);

        String responseBody = XmlResponseUtils.toXmlString(new XmlResponse(RestApiConstants.ID, id));
        return ResponseEntity.ok(responseBody);
    }
}
