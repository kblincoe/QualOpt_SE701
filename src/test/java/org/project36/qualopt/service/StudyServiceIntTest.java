package org.project36.qualopt.service;

import com.google.common.collect.ImmutableSet;
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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.context.junit4.SpringRunner;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = QualOptApp.class)
public class StudyServiceIntTest {

    private StudyService studyService;

    @Spy
    private JavaMailSenderImpl javaMailSender;

    @Captor
    private ArgumentCaptor messageCaptor;

    @Before
    public void setup() throws MessagingException {
        MockitoAnnotations.initMocks(this);
        doNothing().when(javaMailSender).send(any(MimeMessage.class));
        studyService = new StudyService(javaMailSender);
    }

    @Test
    public void testSendInvitationEmail() throws Exception {
        User user = new User();
        user.setEmail("user@email.com");
        studyService.sendInvitationEmail(new Study()
            .user(user)
            .emailSubject("testSubject")
            .emailBody("testContent")
            .participants(ImmutableSet.of(new Participant().email("participant@email.com"))));
        verify(javaMailSender).send((MimeMessage) messageCaptor.capture());
        MimeMessage message = (MimeMessage) messageCaptor.getValue();
        assertThat(message.getSubject()).isEqualTo("testSubject");
        assertThat(message.getAllRecipients()[0].toString()).isEqualTo("participant@email.com");
        assertThat(message.getFrom()[0].toString()).isEqualTo("user@email.com");
        assertThat(message.getContent()).isInstanceOf(String.class);
        assertThat(message.getContent().toString()).isEqualTo("testContent");
        assertThat(message.getDataHandler().getContentType()).isEqualTo("text/plain; charset=UTF-8");
    }
}
