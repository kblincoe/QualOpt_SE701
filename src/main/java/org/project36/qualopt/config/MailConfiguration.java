package org.project36.qualopt.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Properties;

/**
 * Email configs for StudyService and MailService.
 */
@Configuration
public class MailConfiguration {

    private static final Logger log = LoggerFactory.getLogger(MailConfiguration.class);

    // Dev test gmail account for proof of concept.
    public static final String USER_EMAIL = "tt7199425@gmail.com";
    public static final String USER_PASSWORD = "testemail123";

    /**
     * Authenticated session for sending study invitation emails.
     */
    @Bean
    public Session authenticatedUserSmtpSession() {
        log.debug("Creating user SMTP session.");
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        return authenticateAndGetSession(props);
    }

    private Session authenticateAndGetSession(Properties properties){
        return Session.getInstance(properties,
            new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(USER_EMAIL, USER_PASSWORD);
                }
            });
    }

    /**
     * Session for receiving emails for processing bounced study invitation emails.
     * Need to authenticate the store used to read emails. I.e. session.getStore().connect(username, password).
     */
    @Bean
    public Session userPop3Session() {
        log.debug("Creating user POP3 session.");
        Properties props = new Properties();
        props.put("mail.pop3.auth", "true");
        props.put("mail.pop3.starttls.enable", "true");
        props.put("mail.pop3.host", "pop.gmail.com");
        props.put("mail.pop3.port", "995");
        props.put("mail.pop3.ssl.trust", "pop.gmail.com");
        return Session.getDefaultInstance(props);
    }

    /**
     * Spring is trying to wire a Session bean to MailService JavaMailSender when two exist.
     * Need this bean to explicitly wire the JavaMailSender in MailService else the application fails to start.
     * I think it's a bug that it confuses a Session with a JavaMailSender.
     * JavaMailSenderImpl is used for the setSession() method.
     */
    @Bean
    public JavaMailSenderImpl javaMailSender(){
        return new JavaMailSenderImpl();
    }

}
