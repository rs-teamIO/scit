package com.scit.xml.controller;

import com.scit.xml.common.Constants;
import com.scit.xml.common.api.RestApiConstants;
import com.scit.xml.common.api.RestApiEndpoints;
import com.scit.xml.common.api.RestApiRequestParameters;
import com.scit.xml.common.util.ResourceUtils;
import com.scit.xml.common.util.XmlExtractorUtil;
import com.scit.xml.common.util.XmlResponseUtils;
import com.scit.xml.dto.XmlResponse;
import com.scit.xml.model.evaluation_form.EvaluationForm;
import com.scit.xml.model.paper.Paper;
import com.scit.xml.service.EmailService;
import com.scit.xml.service.EvaluationFormService;
import com.scit.xml.service.PaperService;
import com.scit.xml.service.UserService;
import com.scit.xml.service.validator.dto.EvaluationFormDtoValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.mail.MessagingException;

@RestController
@RequestMapping(RestApiEndpoints.EVALUATION_FORMS)
@RequiredArgsConstructor
public class EvaluationFormsController {

    private final EvaluationFormService evaluationFormService;
    private final EvaluationFormDtoValidator evaluationFormDtoValidator;
    private final EmailService emailService;
    private final UserService userService;
    private final PaperService paperService;

    /**
     * POST api/v1/evaluation-forms
     * ACCESS LEVEL: Only authenticated users
     *
     * Creates an {@link EvaluationForm} for a {@link Paper} instance
     * @param xml XML string representation of the {@link EvaluationForm}
     * @param paperId unique identifier of the {@link Paper} instance
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping(params = { RestApiRequestParameters.PAPER_ID },
                 consumes = MediaType.APPLICATION_XML_VALUE,
                 produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> create(@RequestBody String xml,
                                         @RequestParam(RestApiRequestParameters.PAPER_ID) String paperId) throws MessagingException {
        EvaluationForm evaluationForm = this.evaluationFormDtoValidator.validate(xml);
        String id = this.evaluationFormService.createEvaluationForm(evaluationForm, paperId);

        String editorEmail = XmlExtractorUtil.extractUserEmail(this.userService.findByUsername(Constants.EDITOR_USERNAME));
        byte[] pdf = ResourceUtils.convertResourceToByteArray(this.evaluationFormService.exportToPdf(id));
        String html = ResourceUtils.convertResourceToString(this.evaluationFormService.exportToHtml(id));
        String paperTitle = XmlExtractorUtil.extractPaperTitle(this.paperService.findById(paperId));
        this.emailService.sendEvaluationFormSubmissionNotificationEmail(editorEmail, evaluationForm, paperTitle, pdf, html);

        UriComponents urlLocation = UriComponentsBuilder.newInstance()
        		.path(RestApiEndpoints.EVALUATION_FORMS)
        		.query(RestApiRequestParameters.ID+"={id}")
        		.buildAndExpand(id);
        return ResponseEntity.created(urlLocation.toUri()).build();
    }
}