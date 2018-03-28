package org.project36.qualopt.service;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.CharEncoding;
import org.project36.qualopt.domain.Participant;
import org.project36.qualopt.domain.Study;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.project36.qualopt.config.MailConfiguration.USER_EMAIL;
import static org.project36.qualopt.config.MailConfiguration.USER_PASSWORD;

/**
 * Service for sending and processing bounced mail for study invitations.
 * <p>
 * The current implementation for sending and processing received email is specific to Gmail.
 */
@Service
public class StudyService {

    // Gmail specific filters for processing bounced email.
    static final String GMAIL_SERVICE_ADDRESS = "Mail Delivery Subsystem <mailer-daemon@googlemail.com>";
    static final String BOUNCED_MAIL_SUBJECT_FILTER = "(Failure)";
    static final String BOUNCED_MAIL_FILTER_START = "<b>";
    static final String BOUNCED_MAIL_FILTER_END = "</b>";
    private static final int BOUNCED_MAIL_PART_INDEX = 0;

    private static final Logger log = LoggerFactory.getLogger(StudyService.class);

    public static final String CUSTOM_LAST_NAME = "--lastName";
    public static final String CUSTOM_FIRST_NAME = "--firstName";
    public static final String CUSTOM_LOCATION = "--location";
    public static final String CUSTOM_OCCUPATION = "--occupation";
    public static final String CUSTOM_PROGRAMMING_LANGUAGE = "--programmingLanguage";
    public static final String CUSTOM_NUMBER_OF_CONTRIBUTIONS = "--numberOfContributions";
    public static final String CUSTOM_NUMBER_OF_REPOSITORIES = "--numberOfRepositories";

    private final JavaMailSenderImpl javaMailSender;

    private final Session authenticatedUserSmtpSession;

    private final Session userPop3Session;

    public StudyService(JavaMailSenderImpl javaMailSender, Session authenticatedUserSmtpSession, Session userPop3Session) {
        this.javaMailSender = javaMailSender;
        this.authenticatedUserSmtpSession = authenticatedUserSmtpSession;
        this.userPop3Session = userPop3Session;
    }

    /**
     * Attempts to send a study invitation out to all participants as a single email (i.e. with multiple recipients).
     * Then returns a Set of any addresses that bounced back.
     */
    public ImmutableSet<String> sendInvitationEmail(Study study) {
        log.debug("Sending invitation email for study '{}'.", study.getName());
        javaMailSender.setSession(authenticatedUserSmtpSession);
        study.getParticipants().forEach(p -> sendStudy(p, study));
        ImmutableSet<String> bouncedAddresses = readAndCheckForBouncedMail();
        bouncedAddresses.forEach(address -> log.debug("Bounced: '{}'", address));
        ImmutableSet<String> participantAddresses = study.getParticipants().stream()
            .map(Participant::getEmail)
            .collect(Collectors.collectingAndThen(Collectors.toSet(), ImmutableSet::copyOf));
        // Only care about bounced emails from the participants of this study
        return Sets.intersection(participantAddresses, bouncedAddresses).immutableCopy();
    }

    private void sendStudy(Participant participant, Study study) {
        try {
            String subject = study.getEmailSubject() == null ? "" : study.getEmailSubject();
            String content = study.getEmailBody() == null ? "" : study.getEmailBody();
            String userEmail = study.getUser().getEmail();
            MimeMessage message = javaMailSender.createMimeMessage();
            message.setFrom(new InternetAddress(userEmail));
            InternetAddress participantEmailAddress;
            participantEmailAddress = new InternetAddress(participant.getEmail());
            message.addRecipient(Message.RecipientType.TO, participantEmailAddress);
            String customisedContent = customiseEmailText(participant, content);
            String customisedSubject = customiseEmailText(participant, subject);
            message.setSubject(customisedSubject, CharEncoding.UTF_8);
            message.setText(customisedContent, CharEncoding.UTF_8);
            javaMailSender.send(message);
            log.debug("Sent invitation email for study '{}' to '{}'", study, participant);
        } catch (MessagingException e) {
            log.error("Failed to create internet address from participant email", e);
        }
    }

    /**
     * This method goes through text and customises it with the participants personal details.
     */
    private String customiseEmailText(Participant participant, String content) {
        return content.replaceAll(CUSTOM_FIRST_NAME, participant.getFirstName())
            .replaceAll(CUSTOM_LAST_NAME, participant.getLastName())
            .replaceAll(CUSTOM_LOCATION, participant.getLocation())
            .replaceAll(CUSTOM_OCCUPATION, participant.getOccupation())
            .replaceAll(CUSTOM_PROGRAMMING_LANGUAGE, participant.getProgrammingLanguage())
            .replaceAll(CUSTOM_NUMBER_OF_CONTRIBUTIONS, participant.getNumberOfContributions() + "")
            .replaceAll(CUSTOM_NUMBER_OF_REPOSITORIES, participant.getNumberOfRepositories() + "");
    }

    private ImmutableSet<String> readAndCheckForBouncedMail() {
        try {
            Store store = userPop3Session.getStore("pop3s");
            store.connect("pop.gmail.com", USER_EMAIL, USER_PASSWORD);
            Folder folder = store.getFolder("INBOX");
            folder.open(Folder.READ_ONLY);
            Message[] messages = folder.getMessages();
            ImmutableSet<String> bouncedMail = checkMessagesForBouncedMail(messages);
            folder.close(true);
            store.close();
            return bouncedMail;
        } catch (MessagingException e) {
            log.error("Failed to get bounced mail.", e);
            return ImmutableSet.of();
        }
    }

    /**
     * Checks messages for bounced mail. This is specific to the gmail service.
     *
     * When an email sent from a gmail account fails to send gmail will immediately return an email with a subject
     * such as "(Failure) Address not found" or "(Failure) Message not delivered".
     * The contents of the email will then contain an html attachment with the bounced email address in bold; hence this
     * method simply extracts the text between the first sequence of <b> and </b>.
     *
     * Gmail sends an individual message for each bounce; hence the argument is an array and the same routine used.
     */
    private ImmutableSet<String> checkMessagesForBouncedMail(Message[] messages) {
        log.debug("Searching for bounced mail.");
        return Arrays.stream(messages)
            .filter(message -> {
                try {
                    return message.getSubject().contains(BOUNCED_MAIL_SUBJECT_FILTER)
                        && message.getFrom()[0].toString().equals(GMAIL_SERVICE_ADDRESS);
                } catch (MessagingException e) {
                    return false;
                }
            })
            .map(message -> {
                try {
                    Multipart multipart = (Multipart) message.getContent();
                    Part failedAttachmentPart = multipart.getBodyPart(BOUNCED_MAIL_PART_INDEX);
                    String text = getText(failedAttachmentPart).orElse("");
                    return (text.contains(BOUNCED_MAIL_FILTER_START) && text.contains(BOUNCED_MAIL_FILTER_END)) ?
                        text.substring(text.indexOf(BOUNCED_MAIL_FILTER_START) + BOUNCED_MAIL_FILTER_START.length(),
                            text.indexOf(BOUNCED_MAIL_FILTER_END)) : null;
                } catch (IOException | MessagingException e) {
                    log.error("Failed to read Multipart message content.", e);
                    return null;
                }
            })
            .filter(Objects::nonNull)
            .collect(Collectors.collectingAndThen(Collectors.toSet(), ImmutableSet::copyOf));
    }

    /**
     * Based on https://javaee.github.io/javamail/FAQ#mainbody
     * Extracts the text from a Multipart message favouring html attachments over plain text.
     */
    private Optional<String> getText(Part part) throws MessagingException, IOException {
        if (part.isMimeType("text/*")) {
            return Optional.of(part.getContent().toString());
        } else if (part.isMimeType("multipart/alternative")) {
            // Prefer html over plain text
            Multipart multipart = (Multipart) part.getContent();
            Optional<String> text = Optional.empty();
            for (int i = 0; i < multipart.getCount(); i++) {
                Part bodyPart = multipart.getBodyPart(i);
                if (bodyPart.isMimeType("text/plain")) {
                    if (!text.isPresent()) {
                        text = getText(bodyPart);
                    }
                } else if (bodyPart.isMimeType("text/html")) {
                    Optional<String> bodyText = getText(bodyPart);
                    if (bodyText.isPresent()) {
                        return bodyText;
                    }
                } else {
                    return getText(bodyPart);
                }
            }
            return text;
        } else if (part.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart) part.getContent();
            for (int i = 0; i < multipart.getCount(); i++) {
                Optional<String> text = getText(multipart.getBodyPart(i));
                if (text.isPresent()) {
                    return text;
                }
            }
        }
        return Optional.empty();
    }
}
