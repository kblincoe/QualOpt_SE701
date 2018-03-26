package org.project36.qualopt.service;

import com.google.common.collect.ImmutableSet;
import de.saly.javamail.mock2.MailboxFolder;
import de.saly.javamail.mock2.MockMailbox;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.project36.qualopt.QualOptApp;
import org.project36.qualopt.domain.Participant;
import org.project36.qualopt.domain.Study;
import org.project36.qualopt.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.context.junit4.SpringRunner;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.*;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.project36.qualopt.config.MailConfiguration.USER_EMAIL;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = QualOptApp.class)
public class StudyServiceIntTest {

    private StudyService studyService;

    private MailboxFolder mailboxFolder;

    @Spy
    private JavaMailSenderImpl javaMailSender;

    @Captor
    private ArgumentCaptor<MimeMessage> messageCaptor;

    @Autowired
    private Session authenticatedUserSmtpSession;

    @Autowired
    private Session userPop3Session;

    @Before
    public void setup() throws AddressException {
        MockitoAnnotations.initMocks(this);
        doNothing().when(javaMailSender).send(any(MimeMessage.class));
        studyService = new StudyService(javaMailSender, authenticatedUserSmtpSession, userPop3Session);
        mailboxFolder = MockMailbox.get(USER_EMAIL).getInbox();
    }

    @After
    public void cleanup() throws MessagingException {
        Arrays.stream(mailboxFolder.getMessages()).forEach(m -> {
            try {
                mailboxFolder.markMessageAsDeleted(m);
            } catch (MessagingException ignored) {
            }
        });
        mailboxFolder.expunge();
    }

    private User mockUser(){
        User user = new User();
        user.setEmail("user@email.com");
        return user;
    }

    private Study mockStudy(){
        return new Study()
            .user(mockUser())
            .emailSubject("testSubject")
            .emailBody("testContent")
            .participants(ImmutableSet.of(new Participant().email("participant@email.com")));
    }

    /**
     * Ensures the StudyService correctly creates and sends an email from a Study object.
     */
    @Test
    public void testSendInvitationEmail() throws Exception {
        Set<String> bouncedMail = studyService.sendInvitationEmail(mockStudy());
        verify(javaMailSender).send((MimeMessage) messageCaptor.capture());
        MimeMessage message = (MimeMessage) messageCaptor.getValue();
        assertThat(message.getSubject()).isEqualTo("testSubject");
        assertThat(message.getAllRecipients()[0].toString()).isEqualTo("participant@email.com");
        assertThat(message.getFrom()[0].toString()).isEqualTo("user@email.com");
        assertThat(message.getContent()).isInstanceOf(String.class);
        assertThat(message.getContent().toString()).isEqualTo("testContent");
        assertThat(message.getDataHandler().getContentType()).isEqualTo("text/plain; charset=UTF-8");
        assertThat(bouncedMail.size()).isEqualTo(0);
    }

    /**
     * Mocking the Gmail service: A message saying an email address has bounced.
     */
    private MimeMessage mockGmailServiceMessage(String bouncedAddress) throws MessagingException {
        final MimeMessage mockMessage = new MimeMessage((Session) null);
        MimeBodyPart part = new MimeBodyPart();
        part.setText(String.format("%s%s%s", StudyService.BOUNCED_MAIL_FILTER_START, bouncedAddress,
            StudyService.BOUNCED_MAIL_FILTER_END), "utf-8", "html");
        Multipart multiPart = new MimeMultipart();
        multiPart.addBodyPart(part);
        mockMessage.setSubject(StudyService.BOUNCED_MAIL_SUBJECT_FILTER);
        mockMessage.setFrom(StudyService.GMAIL_SERVICE_ADDRESS);
        mockMessage.setContent(multiPart);
        mockMessage.setSentDate(Date.from(Instant.now()));
        mockMessage.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(USER_EMAIL));
        return mockMessage;
    }

    /**
     * Ensures the StudyService correctly processes the users inbox for bounced mail from participants.
     */
    @Test
    public void testSendInvitationWithBouncedMail() throws Exception {
        mailboxFolder.add(mockGmailServiceMessage("participant1@email.com"));
        mailboxFolder.add(mockGmailServiceMessage("participant2@gmail.com"));
        mailboxFolder.add(mockGmailServiceMessage("participant3@hmail.com"));
        ImmutableSet<Participant> participants = ImmutableSet.of(new Participant().email("participant1@email.com"),
            new Participant().email("participant2@gmail.com"), new Participant().email("participant3@hmail.com"));
        Set<String> bouncedMail = studyService.sendInvitationEmail(mockStudy().participants(participants));
        assertThat(bouncedMail.size()).isEqualTo(3);
        assertThat(bouncedMail).isEqualTo(participants.stream().map(Participant::getEmail).collect(Collectors.toSet()));
    }

    /**
     * Ensures the StudyService correctly processes the users inbox for bounced mail from participants and filters
     * out bounced mail from other addresses.
     */
    @Test
    public void testSendInvitationWithBouncedMailNotFromParticipants() throws Exception {
        mailboxFolder.add(mockGmailServiceMessage("participant1@email.com"));
        mailboxFolder.add(mockGmailServiceMessage("participant2@gmail.com"));
        mailboxFolder.add(mockGmailServiceMessage("other@email.com"));
        ImmutableSet<Participant> participants = ImmutableSet.of(new Participant().email("participant2@gmail.com"),
            new Participant().email("participant1@email.com"));
        Set<String> bouncedMail = studyService.sendInvitationEmail(mockStudy().participants(participants));
        assertThat(bouncedMail.size()).isEqualTo(2);
        assertThat(bouncedMail).isEqualTo(participants.stream().map(Participant::getEmail).collect(Collectors.toSet()));
    }
}
