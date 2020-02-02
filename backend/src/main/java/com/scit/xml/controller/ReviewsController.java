package com.scit.xml.controller;

import com.scit.xml.common.Constants;
import com.scit.xml.common.api.RestApiConstants;
import com.scit.xml.common.api.RestApiEndpoints;
import com.scit.xml.common.util.ResourceUtils;
import com.scit.xml.common.util.XmlExtractorUtil;
import com.scit.xml.common.util.XmlResponseUtils;
import com.scit.xml.common.util.XmlWrapper;
import com.scit.xml.dto.XmlResponse;
import com.scit.xml.model.paper.Paper;
import com.scit.xml.security.JwtTokenDetailsUtil;
import com.scit.xml.service.EmailService;
import com.scit.xml.service.PaperService;
import com.scit.xml.service.ReviewService;
import com.scit.xml.service.UserService;
import com.scit.xml.service.validator.dto.PaperDtoValidator;
import com.scit.xml.service.validator.dto.ReviewDtoValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;

@RestController
@RequestMapping(RestApiEndpoints.REVIEWS)
@RequiredArgsConstructor
public class ReviewsController {

    private final ReviewService reviewService;
    private final ReviewDtoValidator reviewDtoValidator;
    private final EmailService emailService;
    private final UserService userService;
    private final PaperService paperService;
    private final PaperDtoValidator paperDtoValidator;

    /**
     * POST api/v1/reviews
     * ACCESS LEVEL: Only authenticated users
     *
     * Creates a {@link Review} for a {@link Paper} instance
     * @param xml XML string representation of the reviewed {@link Paper}
     * @param paperId unique identifier of the created {@link Review} instance
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping(consumes = MediaType.APPLICATION_XML_VALUE,
                 produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> create(@RequestBody String xml) throws MessagingException {
        Paper paper = this.paperDtoValidator.validate(xml);
        this.reviewService.checkIsUserReviewingPaper(JwtTokenDetailsUtil.getCurrentUserId(), paper.getId());

        XmlWrapper paperWrapper = new XmlWrapper(xml);
        String reviewId = this.reviewService.createReview(paperWrapper, paper.getId(), JwtTokenDetailsUtil.getCurrentUserId());

        String editorEmail =  XmlExtractorUtil.extractUserEmail(this.userService.findByUsername(Constants.EDITOR_USERNAME));
        String reviewerUsername = JwtTokenDetailsUtil.getCurrentUserUsername();
        byte[] pdf = ResourceUtils.convertResourceToByteArray(this.paperService.convertPaperToPdf(paperWrapper.getXml()));
        String html = ResourceUtils.convertResourceToString(this.paperService.convertPaperToHtml(paperWrapper.getXml()));
        this.emailService.sendPaperReviewedNotificationEmail(editorEmail, paper, reviewerUsername, pdf, html);

        String responseBody = XmlResponseUtils.wrapResponse(new XmlResponse(RestApiConstants.ID, reviewId));
        return ResponseEntity.ok(responseBody);
    }
}
