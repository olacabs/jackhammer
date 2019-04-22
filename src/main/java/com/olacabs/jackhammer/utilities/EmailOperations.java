package com.olacabs.jackhammer.utilities;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.olacabs.jackhammer.common.Constants;
import com.olacabs.jackhammer.common.EmailTemplate;
import com.olacabs.jackhammer.common.ScanMailTemplate;
import com.olacabs.jackhammer.configuration.JackhammerConfiguration;
import com.olacabs.jackhammer.db.FindingDAO;
import com.olacabs.jackhammer.db.SMTPDetailDAO;
import com.olacabs.jackhammer.models.*;
import com.olacabs.jackhammer.security.AES;
import com.opencsv.CSVWriter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.mail.*;
import javax.mail.internet.*;
import javax.ws.rs.core.MediaType;

@Slf4j
public class EmailOperations {

    @Inject
    @Named(Constants.SMTP_DETAIL_DAO)
    SMTPDetailDAO smtpDetailDAO;


    @Inject
    @Named(Constants.FINDING_DAO)
    FindingDAO findingDAO;

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
            String body = EmailTemplate.RESET_PASSWORD_INSTRUCTIONS_MAIL_BODY.replace(EmailTemplate.USER_NAME, user.getName());
            body = body.replace(EmailTemplate.RESET_PASSWORD_LINK, resetPasswordLink);
            MimeMessage msg = getMimeMessage(session, EmailTemplate.RESET_PASSWORD_INSTRUCTIONS_MAIL_SUBJECT, body, recipients, smtpDetail);
            String smtpUserName = smtpDetail.getSmtpUserName();
            String smtpPassword = AES.decrypt(smtpDetail.getSmtpPassword(), jackhammerConfiguration.getJwtConfiguration().getTokenSigningKey());
            log.info("Sending password instructions...");
            transport.connect(smtpDetail.getSmtpHost(), smtpUserName, smtpPassword);
            transport.sendMessage(msg, msg.getAllRecipients());
            log.info("Password instructions has sent!");
        } catch (Exception ex) {
            log.error("Password instructions was not sent!", ex);
            log.error("Error message: ", ex.getMessage());
        } finally {
            // Close and terminate the connection.
            try {
                transport.close();
            } catch (MessagingException me) {

            }
        }
    }

    public void sendAlertMail(Scan scan, String scanTypeTitle) {
        String rows = StringUtils.EMPTY;
        List<Finding> findingList = findingDAO.getAllWithNoLimit(scan.getId());
        int findingCount = 0;
        for (Finding finding : findingList) {
            if (findingCount > 10) break;
            rows += getRow(finding);
            findingCount += 1;
        }
        String mailTemplate = ScanMailTemplate.emailTempalte;
        mailTemplate = mailTemplate.replace(ScanMailTemplate.TARGET, scan.getTarget());
        mailTemplate = mailTemplate.replace(ScanMailTemplate.TBODY, rows);
        Transport transport = null;
        SMTPDetail smtpDetail = smtpDetailDAO.get();
        Properties props = getSMTPProperties(smtpDetail.getSmtpPort());
        Session session = Session.getDefaultInstance(props);

        try {
            transport = session.getTransport();
            Address[] recipients = getRecipients();
            StringBuilder stringBulilderSubject = new StringBuilder();
            stringBulilderSubject.append(scanTypeTitle);
            stringBulilderSubject.append(Constants.SCAN);
            stringBulilderSubject.append(Constants.HYPEN);
            stringBulilderSubject.append(ScanMailTemplate.JACKHAMMER_SCAN_RESULTS);
            String mailSubject = stringBulilderSubject.toString();
            MimeMessage msg = getMimeMessage(session, mailSubject, mailTemplate, recipients, smtpDetail);
            if (findingList.size() > 10) {
                msg = getMultipartMsg(msg, mailTemplate, findingList);
            }
            String smtpUserName = smtpDetail.getSmtpUserName();
            String smtpPassword = AES.decrypt(smtpDetail.getSmtpPassword(), jackhammerConfiguration.getJwtConfiguration().getTokenSigningKey());
            log.info("Sending scan alert...");
            transport.connect(smtpDetail.getSmtpHost(), smtpUserName, smtpPassword);
            transport.sendMessage(msg, msg.getAllRecipients());
            log.info("scan alert has sent!");
        } catch (Exception ex) {
            log.error("scan alert was not sent!", ex);
            log.error("Error message: ", ex.getMessage());
        } finally {
            // Close and terminate the connection.
            try {
                transport.close();
            } catch (MessagingException me) {
                log.error("transport.close(): ", me.getMessage());
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

    private String getRow(Finding finding) {
        StringBuilder findingBuilder = new StringBuilder();
        findingBuilder.append("<tr>");
        findingBuilder.append("<td>");
        findingBuilder.append(finding.getSeverity());
        findingBuilder.append("</td>");
        findingBuilder.append("<td>");
        findingBuilder.append(finding.getName());
        findingBuilder.append("</td>");
        findingBuilder.append("<td>");
        findingBuilder.append(finding.getDescription() == null ? Constants.STRING_SPACER : finding.getDescription());
        findingBuilder.append("</td>");
        findingBuilder.append("</tr>");
        return findingBuilder.toString();
    }

    private File writeDataToCsv(List<Finding> findings) {
        File tempFile = null;
        try {
            tempFile = File.createTempFile(Constants.JACKHAMMER_RESULT, Constants.CSV_EXTENSION);
            FileWriter olaOutputFile = new FileWriter(tempFile);
            CSVWriter writer = new CSVWriter(olaOutputFile);
            String[] headers = {"Severity","Bug Type","Description","External Link","CVE Code",
                    "Solution","Line Number","File Name","code","Tool Name","Cvss Score","Location","Port","State"};
            writer.writeNext(headers);
            for (Finding finding : findings) {
                String[] findingSet = {finding.getSeverity(), finding.getName(), finding.getDescription(),
                        finding.getExternalLink(),finding.getCveCode(),finding.getSolution(),finding.getLineNumber(),
                finding.getFileName(),finding.getCode(),finding.getToolName(),finding.getCvssScore(),finding.getLocation()
                ,finding.getPort(),finding.getState()};
                writer.writeNext(findingSet);
            }
            writer.close();
        } catch (IOException io) {
            log.error("IOException while creating temp file", io);
        }
        return tempFile;
    }

    private Address[] getRecipients() throws AddressException {
        String[] mailIds = jackhammerConfiguration.getScanMangerConfiguration().getAlertMails().split(Constants.COMMA);
        final List<Address> to = new ArrayList<Address>();
        for (final String address : mailIds) {
            to.add(new InternetAddress(address.trim()));
        }
        return to.toArray(new InternetAddress[0]);
    }

    private MimeMessage getMultipartMsg(MimeMessage mimeMessage, String body, List<Finding> findings) {
        Multipart multipart = new MimeMultipart();
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        try {
            messageBodyPart.setContent(body, MediaType.TEXT_HTML);
            multipart.addBodyPart(messageBodyPart);
            MimeBodyPart attachPart = new MimeBodyPart();
            try {
                attachPart.attachFile(writeDataToCsv(findings));
                multipart.addBodyPart(attachPart);
            } catch (IOException io) {
                log.error("IOException while attaching files", io);
            }
            mimeMessage.setContent(multipart);
        } catch (MessagingException me) {
            log.error("MessagingException while getting multipart...");
        }
        return mimeMessage;
    }
}
