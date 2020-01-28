package com.scit.xml.service;

import com.scit.xml.model.cover_letter.CoverLetter;
import com.scit.xml.model.evaluation_form.EvaluationForm;
import com.scit.xml.model.paper.Paper;
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
     * @param paper {@link Paper} instance to be attached in PDF and HTML format in the e-mail
     * @param html HTML representation of the {@link Paper} instance
     * @param pdf PDF representation of the {@link Paper} instance
     * @throws MessagingException Exception thrown in case an error on the SMTP server occurs
     */
    @Async
    public void sendPaperSubmissionNotificationEmail(String recipient, Paper paper, byte[] pdf, String html) throws MessagingException {
        final String subject = String.format("New paper titled \"%s\" has been submitted", paper.getTitle());
        final String text = String.format("A new paper titled \"<b>%s</b>\" has been submitted.<br><br>The files can be found in the attachment.", paper.getTitle());

        this.sendEmailWithAttachments(recipient, subject, text, paper.getTitle(), pdf, html);
    }

    /**
     * Sends a cover letter submission notiication e-mail to the recipient.
     * In case a messaging error on the SMTP server occurs, a {@link MessagingException} is thrown.
     *
     * @param recipient e-mail address of the recipient
     * @param paper {@link CoverLetter} instance to be attached in PDF and HTML format in the e-mail
     * @param html HTML representation of the {@link CoverLetter} instance
     * @param pdf PDF representation of the {@link CoverLetter} instance
     * @throws MessagingException Exception thrown in case an error on the SMTP server occurs
     */
    @Async
    public void sendCoverLetterSubmissionNotificationEmail(String recipient, CoverLetter coverLetter, String paperTitle, byte[] pdf, String html) throws MessagingException {
        final String subject = String.format("Cover letter for paper \"%s\" has been submitted", paperTitle);
        final String text = String.format("A new cover letter for paper with title \"<b>%s</b>\" has been submitted.<br><br>The files can be found in the attachment.", paperTitle);

        this.sendEmailWithAttachments(recipient, subject, text, paperTitle, pdf, html);
    }

    /**
     * Sends a evaluation form submission notiication e-mail to the recipient.
     * In case a messaging error on the SMTP server occurs, a {@link MessagingException} is thrown.
     *
     * @param recipient e-mail address of the recipient
     * @param paper {@link EvaluationForm} instance to be attached in PDF and HTML format in the e-mail
     * @param html HTML representation of the {@link EvaluationForm} instance
     * @param pdf PDF representation of the {@link EvaluationForm} instance
     * @throws MessagingException Exception thrown in case an error on the SMTP server occurs
     */
    @Async
    public void sendEvaluationFormSubmissionNotificationEmail(String recipient, EvaluationForm evaluationForm, String paperTitle, byte[] pdf, String html) throws MessagingException {
        final String subject = String.format("Evaluation form for paper \"%s\" has been submitted", paperTitle);
        final String text = String.format("A new evaluation form for paper with title \"<b>%s</b>\" has been submitted.<br><br>The files can be found in the attachment.", paperTitle);

        this.sendEmailWithAttachments(recipient, subject, text, paperTitle, pdf, html);
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

    /**
     * Sends an email with given content and attachments to the recipient.
     *
     * In case a messaging error on the SMTP server occurs, a {@link MessagingException} is thrown.
     * @param recipient Recipient's email address
     * @param subject Email subject
     * @param text Email content
     * @param fileName Name of the attachment file
     * @param pdf PDF file
     * @param html HTML file
     * @throws MessagingException Exception thrown in case an error on the SMTP server occurs
     */
    @Async
    void sendEmailWithAttachments(String recipient, String subject, String text, String fileName, byte[] pdf, String html) throws MessagingException {

        final String currentDate = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
        final String newFileName = String.format("%s_%s", fileName, currentDate);

        MimeBodyPart textBodyPart = new MimeBodyPart();
        textBodyPart.setContent(text, TEXT_HTML_VALUE);

        MimeBodyPart pdfAttachmentBodyPart = new MimeBodyPart();
        pdfAttachmentBodyPart.setContent(pdf, APPLICATION_PDF_VALUE);
        pdfAttachmentBodyPart.setFileName(newFileName + ".pdf");

        MimeBodyPart htmlAttachmentBodyPart = new MimeBodyPart();
        htmlAttachmentBodyPart.setContent(html, TEXT_HTML_VALUE);
        htmlAttachmentBodyPart.setFileName(newFileName + ".html");

        Multipart multipartContent = new MimeMultipart();
        multipartContent.addBodyPart(textBodyPart);
        multipartContent.addBodyPart(pdfAttachmentBodyPart);
        multipartContent.addBodyPart(htmlAttachmentBodyPart);

        this.sendEmail(recipient, subject, multipartContent);
    }
}
