package com.olacabs.jackhammer.common;

public class EmailTemplate {
    public static final String RESET_PASSWORD_INSTRUCTIONS_MAIL_BODY = String.join(
            System.getProperty("line.separator"),
            "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n",
            "\n",
            "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n",
            "\n",
            "<head>\n",
            "\n",
            "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n",
            "\n",
            "    <style>\n",
            "\n",
            "        body {\n",
            "\n",
            "            background-color: #FFFFFF; padding: 0; margin: 0;\n",
            "\n",
            "        }\n",
            "\n",
            "    </style>\n",
            "\n",
            "</head>\n",
            "\n",
            "<body style=\"background-color: #FFFFFF; padding: 0; margin: 0;\">\n",
            "\n",
            "<table border=\"0\" cellpadding=\"0\" cellspacing=\"10\" height=\"100%\" bgcolor=\"#FFFFFF\" width=\"100%\" style=\"max-width: 650px;\" id=\"bodyTable\">\n",
            "\n",
            "    <tr>\n",
            "       <td align=\"left\" valign=\"top\">\n",
            "        Hi {{userName}},\n",
            "       </td>",
            "     </tr>",
            "    <tr>\n",
            "\n",
            "        <td align=\"center\" valign=\"top\">\n",
            "\n",
            "            <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" id=\"emailContainer\" style=\"font-family:Arial; color: #333333;\">\n",
            "\n",
            "\n",
            "\n",
            "\n",
            "                <tr>\n",
            "\n",
            "                    <td align=\"left\" valign=\"top\" colspan=\"2\" style=\"padding-top: 10px;\">\n",
            "\n",
            "                        <span style=\"font-size: 12px; line-height: 1.5; color: #333333;\">\n",
            "\n",
            "                            You have requested a password reset, please follow the link below to reset your password.\n",
            "\n",
            "                            <br/><br/>\n",
            "                            Please ignore this email if you did not request a password change.\n",
            "\n",
            "                            To reset your password,please click <a href=\"{{resetPasswordLink}}\">here</a>\n",
            "\n",
            "                            <br/><br/>\n",
            "                        </span>\n",
            "\n",
            "                    </td>\n",
            "\n",
            "                </tr>\n",
            "\n",
            "            </table>\n",
            "\n",
            "        </td>\n",
            "\n",
            "    </tr>\n",
            "\n",
            "</table>\n",
            "\n",
            "</body>\n",
            "\n",
            "</html>\n",
            "\n"
    );

    public static final String RESET_PASSWORD_INSTRUCTIONS_MAIL_SUBJECT = "Resetting your password for  Jackhammer";
    public static final String TRUE = "true";
    public static final String SMTP = "smtp";
    public static final String MAIL_PROTOCOL = "mail.transport.protocol";
    public static final String MAIL_PORT = "mail.smtp.port";
    public static final String MAIL_STAR_TTLS = "mail.smtp.starttls.enable";
    public static final String MAIL_AUTH = "mail.smtp.auth";
    public static final String RESET_PASSWORD_URL = "/reset_password";
    public static final String PASSWORD_TOKEN_PARAM = "?password_token=";
    public static final String USER_NAME = "{{userName}}";
    public static final String RESET_PASSWORD_LINK = "{{resetPasswordLink}}";
}
