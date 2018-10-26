package com.olacabs.jackhammer.utilities;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.olacabs.jackhammer.common.Constants;
import com.olacabs.jackhammer.common.EmailTemplate;
import com.olacabs.jackhammer.configuration.JackhammerConfiguration;
import com.olacabs.jackhammer.db.SMTPDetailDAO;
import com.olacabs.jackhammer.models.SMTPDetail;
import com.olacabs.jackhammer.models.User;
import com.olacabs.jackhammer.security.AES;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.ws.rs.core.MediaType;

@Slf4j
public class EmailOperations {

    @Inject
    @Named(Constants.SMTP_DETAIL_DAO)
    SMTPDetailDAO smtpDetailDAO;

    @Inject
    JackhammerConfiguration jackhammerConfiguration;

    private Matcher matcher;
    private Pattern emailPattern = Pattern.compile(Constants.EMAIL_PATTREN, Pattern.CASE_INSENSITIVE);

    public void sendPasswordInstructions(User user) {
        SMTPDetail smtpDetail = smtpDetailDAO.get();
        Properties props = getSMTPProperties(smtpDetail.getSmtpPort());
        Session session = Session.getDefaultInstance(props);
        Transport transport = null;
        try {
            transport = session.getTransport();
            Address[] recipients = new Address[]{new InternetAddress(user.getEmail())};
            String resetPasswordLink = getResetPasswordLink(user, smtpDetail);
            String body = EmailTemplate.RESET_PASSWORD_INSTRUCTIONS_MAIL_BODY.replace(EmailTemplate.USER_NAME,user.getName());
            body = body.replace(EmailTemplate.RESET_PASSWORD_LINK,resetPasswordLink);
            MimeMessage msg = getMimeMessage(session, EmailTemplate.RESET_PASSWORD_INSTRUCTIONS_MAIL_SUBJECT, body, recipients, smtpDetail);
            String smtpUserName = smtpDetail.getSmtpUserName();
            String smtpPassword = AES.decrypt(smtpDetail.getSmtpPassword(),jackhammerConfiguration.getJwtConfiguration().getTokenSigningKey());
            log.info("Sending password instructions...");
            transport.connect(smtpDetail.getSmtpHost(), smtpUserName, smtpPassword);
            transport.sendMessage(msg, msg.getAllRecipients());
            log.info("Password instructions has sent!");
        } catch (Exception ex) {
            log.error("Password instructions was not sent!",ex);
            log.error("Error message: ", ex.getMessage());
        } finally {
            // Close and terminate the connection.
            try {
                transport.close();
            } catch (MessagingException me) {

            }
        }
    }

    public void isValidEmail(String email) {
        matcher = emailPattern.matcher(email);
        Preconditions.checkState(matcher.matches());
    }
    private Properties getSMTPProperties(int port) {
        Properties props = System.getProperties();
        props.put(EmailTemplate.MAIL_PROTOCOL, EmailTemplate.SMTP);
        props.put(EmailTemplate.MAIL_PORT, port);
        props.put(EmailTemplate.MAIL_STAR_TTLS, EmailTemplate.TRUE);
        props.put(EmailTemplate.MAIL_AUTH, EmailTemplate.TRUE);
        return props;
    }

    private MimeMessage getMimeMessage(Session session, String subject, String body, Address[] recipients, SMTPDetail smtpDetail) throws MessagingException, UnsupportedEncodingException {
        MimeMessage msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(smtpDetail.getSmtpUserName(), Constants.JACKHAMMER));
        msg.setRecipients(Message.RecipientType.TO, recipients);
        msg.setSubject(subject);
        msg.setContent(body, MediaType.TEXT_HTML);
        return msg;
    }

    private String getResetPasswordLink(User user, SMTPDetail smtpDetail) {
        StringBuilder passwordLink = new StringBuilder();
        passwordLink.append(smtpDetail.getApplicationUrl());
        passwordLink.append(EmailTemplate.RESET_PASSWORD_URL);
        passwordLink.append(EmailTemplate.PASSWORD_TOKEN_PARAM);
        passwordLink.append(user.getUserToken());
        return passwordLink.toString();
    }
}
