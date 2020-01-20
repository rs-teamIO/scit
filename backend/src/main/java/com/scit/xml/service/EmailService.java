package com.scit.xml.service;

import com.scit.xml.model.paper.Paper;
import com.scit.xml.model.user.User;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.CharEncoding;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static org.springframework.http.MediaType.APPLICATION_PDF_VALUE;
import static org.springframework.http.MediaType.TEXT_HTML_VALUE;

/**
 * E-mail generation and delivery service implementation.
 */
@Service
@RequiredArgsConstructor
public class EmailService {

    @Value("${spring.mail.username}")
    private String email;

    private final JavaMailSender mailSender;

    /**
     * Sends a paper submission notiication e-mail to the recipient.
     * In case a messaging error on the SMTP server occurs, a {@link MessagingException} is thrown.
     *
     * @param recipient e-mail address of the recipient
     * @param paper     {@link Paper} instance to be attached in PDF and HTML format in the e-mail
     * @throws MessagingException Exception thrown in case an error on the SMTP server occurs
     */
    @Async
    public void sendPaperSubmissionNotificationEmail(String recipient, Paper paper, byte[] pdf, String html) throws MessagingException {
        String currentDate = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
        String fileName = String.format("%s_%s", paper.getTitle(), currentDate);

        MimeBodyPart textBodyPart = new MimeBodyPart();
        textBodyPart.setContent(String.format("A new paper named <b>%s</b> has been submitted.", paper.getTitle()), TEXT_HTML_VALUE);

        MimeBodyPart pdfAttachmentBodyPart = new MimeBodyPart();
        pdfAttachmentBodyPart.setContent(pdf, APPLICATION_PDF_VALUE);
        pdfAttachmentBodyPart.setFileName(fileName + ".pdf");

        MimeBodyPart htmlAttachmentBodyPart = new MimeBodyPart();
        htmlAttachmentBodyPart.setContent(html, TEXT_HTML_VALUE);
        htmlAttachmentBodyPart.setFileName(fileName + ".html");

        Multipart multipartContent = new MimeMultipart();
        multipartContent.addBodyPart(textBodyPart);
        multipartContent.addBodyPart(pdfAttachmentBodyPart);
        multipartContent.addBodyPart(htmlAttachmentBodyPart);

        this.sendEmail(recipient, "New paper has been submitted", multipartContent);
    }

    /**
     * Sends an email.
     * In case an messaging error on the SMTP server occurs, a {@link MessagingException} is thrown.
     *
     * @param emailAddress Recipient's email address
     * @param subject      Email subject
     * @param content      Email content
     * @throws MessagingException Exception thrown in case an error on the SMTP server occurs
     */
    @Async
    void sendEmail(String emailAddress, String subject, String content) throws MessagingException {
        final MimeMessage mailMessage = this.mailSender.createMimeMessage();
        final MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage, CharEncoding.UTF_8);

        messageHelper.setFrom(this.email);
        messageHelper.setTo(emailAddress);
        messageHelper.setSubject(subject);
        mailMessage.setContent(content, "text/html");

        this.mailSender.send(mailMessage);
    }

    /**
     * Sends an email with multipart content.
     * In case an messaging error on the SMTP server occurs, a {@link MessagingException} is thrown.
     *
     * @param emailAddress Recipient's email address
     * @param subject      Email subject
     * @param content      Email content
     * @throws MessagingException Exception thrown in case an error on the SMTP server occurs
     */
    @Async
    void sendEmail(String emailAddress, String subject, Multipart content) throws MessagingException {
        final MimeMessage mailMessage = this.mailSender.createMimeMessage();
        final MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage, CharEncoding.UTF_8);

        messageHelper.setFrom(this.email);
        messageHelper.setTo(emailAddress);
        messageHelper.setSubject(subject);
        mailMessage.setContent(content);

        this.mailSender.send(mailMessage);
    }
}
