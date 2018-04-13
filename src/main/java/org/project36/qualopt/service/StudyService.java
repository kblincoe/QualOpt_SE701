package org.project36.qualopt.service;

import org.project36.qualopt.domain.EmailScheduler;
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
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.quartz.SchedulerFactory;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.DateBuilder.IntervalUnit;

import static org.quartz.DateBuilder.futureDate;
import static org.quartz.TriggerBuilder.newTrigger;
import static org.quartz.JobBuilder.newJob;

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
    public ImmutableSet<String> sendInvitationEmail(Study study, int delay) {
        log.debug("Sending invitation email for study '{}'.", study.getName());
        String subject = study.getEmailSubject() == null ? "" : study.getEmailSubject();
        String content = study.getEmailBody() == null ? "" : study.getEmailBody();
        String userEmail = study.getUser().getEmail();
        try {
            javaMailSender.setSession(authenticatedUserSmtpSession);
            MimeMessage message = javaMailSender.createMimeMessage();
            message.setFrom(new InternetAddress(userEmail));
            ImmutableSet<String> participantAddresses = study.getParticipants().stream()
                .map(Participant::getEmail)
                .collect(Collectors.collectingAndThen(Collectors.toSet(), ImmutableSet::copyOf));
            message.addRecipients(Message.RecipientType.TO, participantAddresses.stream()
                .map(participantEmail -> {
                    try {
                        return new InternetAddress(participantEmail);
                    } catch (AddressException e) {
                        log.error("Failed to create internet address from participant email.", e);
                        throw new RuntimeException(e);
                    }
                })
                .toArray(Address[]::new));
            message.setSubject(subject, CharEncoding.UTF_8);
            message.setText(content, CharEncoding.UTF_8);
            if (delay > 0){
                try {
                    scheduleEmailJob(message, delay);
                } catch (SchedulerException se){
                    log.error("Failed to schedule email", se);
                    se.printStackTrace();
                }
                return ImmutableSet.of();
            } else {
                javaMailSender.send(message);
                log.debug("Sent invitation email for study '{}'.", study.getName());
                ImmutableSet<String> bouncedAddresses = readAndCheckForBouncedMail();
                bouncedAddresses.forEach(address -> log.debug("Bounced: '{}'", address));
                // Only care about bounced emails from the participants of this study
                return Sets.intersection(participantAddresses, bouncedAddresses).immutableCopy();
            }

        } catch (MessagingException e) {
            log.error("Failed to send invitation email.", e);
            return ImmutableSet.of();
        }
    }

    private ImmutableSet<String> readAndCheckForBouncedMail() throws MessagingException {
        Store store = userPop3Session.getStore("pop3s");
        store.connect("pop.gmail.com", USER_EMAIL, USER_PASSWORD);
        Folder folder = store.getFolder("INBOX");
        folder.open(Folder.READ_ONLY);
        Message[] messages = folder.getMessages();
        ImmutableSet<String> bouncedMail = checkMessagesForBouncedMail(messages);
        folder.close(true);
        store.close();
        return bouncedMail;
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
    /**
     * Method is used to create a schedule for a job: invitation email to be registered
     * with the the Quartz Scheduler.
     */
    private void scheduleEmailJob(MimeMessage message, int delay) throws SchedulerException {
        
        SchedulerFactory schedFact = new StdSchedulerFactory();
        Scheduler sched = schedFact.getScheduler();

        sched.start();
        JobDataMap messageMap = new JobDataMap();
        messageMap.put("message", message);

        JobDetail job = newJob(EmailScheduler.class)
            .withIdentity("myJob", "group1")
            .usingJobData(messageMap)
            .build();

        // Trigger the job to run after a certain delay
        Trigger trigger = newTrigger().withIdentity("myTrigger", "group1")
            .startAt(futureDate(delay,IntervalUnit.SECOND))
            .forJob(job)
            .build();

        // Tell quartz to schedule the job using our trigger
        sched.scheduleJob(job, trigger);
    }
}
