package org.project36.qualopt.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.project36.qualopt.QualOptApp;
import org.project36.qualopt.domain.Email;
import org.project36.qualopt.domain.EmailTemplate;
import org.project36.qualopt.domain.User;
import org.project36.qualopt.repository.EmailTemplateRepository;
import org.project36.qualopt.repository.UserRepository;
import org.project36.qualopt.web.rest.errors.ExceptionTranslator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

/**
 * Test class for the EmailResource REST controller.
 *
 * @see EmailTemplateResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = QualOptApp.class)
public class EmailTemplateResourceIntTest {

    @Autowired
    private EmailTemplateRepository emailTemplateRepository;
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restEmailMockMvc;

    private EmailTemplate emailTemplate;
    private User defaultUser ;

    private static final String BADUSERLOGIN = "0";
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EmailTemplateResource emailResource = new EmailTemplateResource(emailTemplateRepository);
        this.restEmailMockMvc = MockMvcBuilders.standaloneSetup(emailResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     * 
     * Associate the email template to user 4;
     */
    public static EmailTemplate createEntity(EntityManager em) {
        EmailTemplate emailTemplate = new EmailTemplate();
        //Email template name cannot be null.
        emailTemplate.setName("testTemplate");
        
        return emailTemplate;
    }
    
    /**
     * Create a default user for testing purpose.
     * 
     * The user has all of its mandatory field set.
     * @param id
     * @return
     */
    public static User createDefaultUser() {
    	User user = new User();
    	user.setLogin("AAAA");
    	//the password field is a hash of 60 characters. 
    	user.setPassword("$2a$10$VEjxo0jq2YG9Rbk2HmX9S.k1uZBGYUHdUcid3g/vfiEl7lwWgOH/K");
    	user.setActivated(true);
    	
    	return user;
    }

    @Before
    public void initTest() {
        emailTemplate = createEntity(em);
        
    	User user = createDefaultUser();
    	defaultUser = userRepository.saveAndFlush(user);
        emailTemplate.setUser(defaultUser);
    }

    @Test
    @Transactional
    public void createEmailTemplate() throws Exception {
        int databaseSizeBeforeCreate = emailTemplateRepository.findAll().size();

        // Create the Email Template
        restEmailMockMvc.perform(post("/api/emailTemplates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(emailTemplate)))
            .andExpect(status().isCreated());

        // Validate the Email in the database
        List<EmailTemplate> emailTemplateList = emailTemplateRepository.findAll();
        assertThat(emailTemplateList).hasSize(databaseSizeBeforeCreate + 1);
    }

    @Test
    @Transactional
    public void createEmailTemplateWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = emailTemplateRepository.findAll().size();

        // Create the Email with an existing ID
        emailTemplate.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmailMockMvc.perform(post("/api/emailTemplates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(emailTemplate)))
            .andExpect(status().isBadRequest());

        // Validate the email template in the database
        List<EmailTemplate> emailList = emailTemplateRepository.findAll();
        assertThat(emailList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllEmailTemplates() throws Exception {
        // Initialize the database
        emailTemplateRepository.saveAndFlush(emailTemplate);

        // Get all the email template List
        restEmailMockMvc.perform(get("/api/emailTemplates?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(emailTemplate.getId().intValue())));
    }

    @Test
    @Transactional
    public void getEmailTemplateByUserId() throws Exception {
        // Initialize the database
        emailTemplateRepository.saveAndFlush(emailTemplate);

        String userLogin = emailTemplateRepository.findAll().iterator().next().getUser().getLogin();
        
        // Get the email template list
        restEmailMockMvc.perform(get("/api/emailTemplates/{userLogin}", userLogin))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(emailTemplate.getId().intValue())));
        
        List<EmailTemplate> emailTemplateListForAnotherUser = emailTemplateRepository.findAllByUserLogin(BADUSERLOGIN);
        List<EmailTemplate> emailTemplateListForOnlyUser = emailTemplateRepository.findAllByUserLogin(userLogin);
        assertThat(emailTemplateListForAnotherUser).hasSize(0);
        assertThat(emailTemplateListForOnlyUser).hasSize(1);
        assertEquals(emailTemplateListForOnlyUser.get(0).getUser(), defaultUser);
    }

    @Test
    @Transactional
    public void getNonExistingEmail() throws Exception {
        // Get the email template list
        restEmailMockMvc.perform(get("/api/emailTemplates/{userLogin}", BADUSERLOGIN))
            .andExpect(status().isOk());
        assertThat(emailTemplateRepository.findAllByUserLogin(BADUSERLOGIN)).hasSize(0);
    }

    @Test
    @Transactional
    public void updateEmailTemplate() throws Exception {
        // Initialize the database
        emailTemplateRepository.saveAndFlush(emailTemplate);
        int databaseSizeBeforeUpdate = emailTemplateRepository.findAll().size();

        // Update the email
        EmailTemplate updatedEmailTemplate = emailTemplateRepository.getOne(emailTemplate.getId());

        restEmailMockMvc.perform(put("/api/emailTemplates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedEmailTemplate)))
            .andExpect(status().isOk());

        // Validate the Email in the database
        List<EmailTemplate> emailTemplateList = emailTemplateRepository.findAll();
        assertThat(emailTemplateList).hasSize(databaseSizeBeforeUpdate);
        EmailTemplate testEmailTemplate = emailTemplateList.get(emailTemplateList.size() - 1);
        assertEquals(testEmailTemplate, emailTemplate);
    }

    @Test
    @Transactional
    public void updateNonExistingEmailTemplate() throws Exception {
        int databaseSizeBeforeUpdate = emailTemplateRepository.findAll().size();

        // Create the Email Template

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restEmailMockMvc.perform(put("/api/emailTemplates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(emailTemplate)))
            .andExpect(status().isCreated());

        // Validate the Email in the database
        List<EmailTemplate> emailTemplateList = emailTemplateRepository.findAll();
        assertThat(emailTemplateList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteEmailTemplate() throws Exception {
        // Initialize the database
        emailTemplateRepository.saveAndFlush(emailTemplate);
        int databaseSizeBeforeDelete = emailTemplateRepository.findAll().size();

        // Get the email
        restEmailMockMvc.perform(delete("/api/emailTemplates/{id}", emailTemplate.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<EmailTemplate> emailTemplateList = emailTemplateRepository.findAll();
        assertThat(emailTemplateList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Email.class);
        EmailTemplate emailTemplate1 = new EmailTemplate();
        emailTemplate1.setId(1L);
        EmailTemplate emailTemplate2 = new EmailTemplate();
        emailTemplate2.setId(emailTemplate1.getId());
        assertThat(emailTemplate1).isEqualTo(emailTemplate2);
        emailTemplate2.setId(2L);
        assertThat(emailTemplate1).isNotEqualTo(emailTemplate2);
        emailTemplate1.setId(null);
        assertThat(emailTemplate1).isNotEqualTo(emailTemplate2);
    }
}
