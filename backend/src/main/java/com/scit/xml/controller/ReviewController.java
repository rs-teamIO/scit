package com.scit.xml.controller;

import com.scit.xml.common.Constants;
import com.scit.xml.common.api.RestApiEndpoints;
import com.scit.xml.common.api.RestApiRequestParameters;
import com.scit.xml.common.util.XmlExtractorUtil;
import com.scit.xml.security.JwtTokenDetailsUtil;
import com.scit.xml.service.EmailService;
import com.scit.xml.service.PaperService;
import com.scit.xml.service.ReviewService;
import com.scit.xml.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;

@RestController
@RequestMapping(RestApiEndpoints.REVIEW)
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final EmailService emailService;
    private final UserService userService;
    private final PaperService paperService;

    /**
     * PUT api/v1/review/accept
     *
     * Endpoint for accepting a review request for {@link Paper} with given ID
     * @param paperId unique identifier of the {@link Paper}
     */
    @PreAuthorize("isAuthenticated()")
    @PutMapping(value = RestApiEndpoints.ACCEPT,
                produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity acceptReviewRequest(@RequestParam(RestApiRequestParameters.PAPER_ID) String paperId) throws MessagingException {
        String userId = JwtTokenDetailsUtil.getCurrentUserId();
        this.reviewService.acceptReviewRequest(userId, paperId);

        String editorEmail =  XmlExtractorUtil.extractUserEmail(this.userService.findByUsername(Constants.EDITOR_USERNAME));
        String paperTitle = XmlExtractorUtil.extractPaperTitle(this.paperService.findById(paperId));
        this.emailService.sendReviewAcceptedNotificationEmail(editorEmail, JwtTokenDetailsUtil.getCurrentUserUsername(), paperTitle);

        return ResponseEntity.ok().build();
    }

    /**
     * PUT api/v1/review/decline
     *
     * Endpoint for declining a review request for {@link Paper} with given ID
     * @param paperId unique identifier of the {@link Paper}
     */
    @PreAuthorize("isAuthenticated()")
    @PutMapping(value = RestApiEndpoints.DECLINE,
                produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity declineReviewRequest(@RequestParam(RestApiRequestParameters.PAPER_ID) String paperId) throws MessagingException {
        String userId = JwtTokenDetailsUtil.getCurrentUserId();
        this.reviewService.declineReviewRequest(userId, paperId);

        String editorEmail =  XmlExtractorUtil.extractUserEmail(this.userService.findByUsername(Constants.EDITOR_USERNAME));
        String paperTitle = XmlExtractorUtil.extractPaperTitle(this.paperService.findById(paperId));
        this.emailService.sendReviewDeclinedNotificationEmail(editorEmail, JwtTokenDetailsUtil.getCurrentUserUsername(), paperTitle);

        return ResponseEntity.ok().build();
    }
}
