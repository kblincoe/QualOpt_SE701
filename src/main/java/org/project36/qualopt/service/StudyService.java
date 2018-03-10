package org.project36.qualopt.service;

import org.project36.qualopt.domain.Study;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Objects;
import java.util.Properties;

@Service
public class StudyService {

    private static final Logger log = LoggerFactory.getLogger(StudyService.class);

    @Async
    public void sendInvitationEmail(Study study){
        log.debug("Sending invitation email for study '{}'", study);

        String subject = study.getEmailSubject();
        String content = Objects.isNull(study.getEmailBody()) ? "" : study.getEmailBody();
        String userEmail = study.getUser().getEmail();
        try {
            MimeMessage message = new MimeMessage(getUserEmailSession());
            message.setFrom(new InternetAddress(userEmail));
            message.addRecipients(Message.RecipientType.TO, study
                .getParticipants()
                .stream()
                .map(participant -> {
                    try {
                        return new InternetAddress(participant.getEmail());
                    } catch (AddressException e) {
                        log.error("Failed to create internet address from participant email", e);
                        throw new RuntimeException(e);
                    }
                })
                .toArray(Address[]::new));
            message.setSubject(subject);
            message.setText(content);
            Transport.send(message);
            log.debug("Sent invitation email for study '{}'", study);
        } catch (MessagingException e) {
            log.error("Failed to send invitation email", e);
        }
    }

    private Session getUserEmailSession(){
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        // trust the host gmail; prevents antivirus from blocking emails being sent
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        return Session.getInstance(props,
            new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    // dev test gmail account for proof of concept
                    // TODO authenticate user email and use that instead. i.e. study.getUser().getEmail()
                    return new PasswordAuthentication("tt7199425@gmail.com", "testemail123");
                }
            });
    }
}
