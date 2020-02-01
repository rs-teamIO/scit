package com.scit.xml.controller;

import com.scit.xml.common.Constants;
import com.scit.xml.common.api.RestApiConstants;
import com.scit.xml.common.api.RestApiEndpoints;
import com.scit.xml.common.util.ResourceUtils;
import com.scit.xml.common.util.XmlResponseUtils;
import com.scit.xml.common.util.XmlWrapper;
import com.scit.xml.dto.XmlResponse;
import com.scit.xml.exception.BadRequestException;
import com.scit.xml.model.paper.Paper;
import com.scit.xml.model.review.Review;
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

    @PreAuthorize("isAuthenticated()")
    @PostMapping(consumes = MediaType.APPLICATION_XML_VALUE,
            produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> create(@RequestBody String xml) throws MessagingException {

        Paper paper = paperDtoValidator.validate(xml);
        paperService.checkIsUserReviewingPaper(JwtTokenDetailsUtil.getCurrentUserId(), paper.getId());

        XmlWrapper paperWrapper = new XmlWrapper(xml);
        String reviewId = this.reviewService.create(paperWrapper, paper.getId(), JwtTokenDetailsUtil.getCurrentUserId());

        String editorEmail = this.userService.getUserEmail(Constants.EDITOR_USERNAME);
        String reviewerUsername = JwtTokenDetailsUtil.getCurrentUserUsername();
        byte[] pdf = ResourceUtils.convertResourceToByteArray(this.paperService.convertToPdf(paperWrapper.getXml()));
        String html = ResourceUtils.convertResourceToString(this.paperService.convertToHtml(paperWrapper.getXml()));
        this.emailService.sendPaperReviewedNotificationEmail(editorEmail, paper, reviewerUsername, pdf, html);

        String responseBody = XmlResponseUtils.toXmlString(new XmlResponse(RestApiConstants.ID, reviewId));
        return ResponseEntity.ok(responseBody);
    }
}
