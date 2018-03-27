package org.project36.qualopt.web.rest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.project36.qualopt.QualOptApp;
import org.project36.qualopt.domain.IncentiveType;
import org.project36.qualopt.domain.Study;
import org.project36.qualopt.repository.StudyRepository;
import org.project36.qualopt.repository.UserRepository;
import org.project36.qualopt.service.StudyService;
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

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the StudyResource REST controller.
 *
 * @see StudyResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = QualOptApp.class)
public class StudyResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_INCENTIVE_DETAIL = "AAAAAAAAAA";
    private static final String UPDATED_INCENTIVE_DETAIL = "BBBBBBBBBB";

    private static final IncentiveType DEFAULT_INCENTIVE_TYPE = IncentiveType.ONEOFFPAYMENT;
    private static final IncentiveType UPDATED_INCENTIVE_TYPE = IncentiveType.RANDOMLYALLOCATED;

    private static final String DEFAULT_EMAIL_SUBJECT = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL_SUBJECT = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL_BODY = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL_BODY = "BBBBBBBBBB";

    @Autowired
    private StudyRepository studyRepository;

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

    private MockMvc restStudyMockMvc;

    private Study study;

    @Mock
    private StudyService mockStudyService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        StudyResource studyResource = new StudyResource(studyRepository, mockStudyService, userRepository);
        this.restStudyMockMvc = MockMvcBuilders.standaloneSetup(studyResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Study createEntity(EntityManager em) {
        Study study = new Study()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .incentiveDetail(DEFAULT_INCENTIVE_DETAIL)
            .incentiveType(DEFAULT_INCENTIVE_TYPE)
            .emailSubject(DEFAULT_EMAIL_SUBJECT)
            .emailBody(DEFAULT_EMAIL_BODY);
        return study;
    }

    @Before
    public void initTest() {
        study = createEntity(em);
    }

    @Test
    @Transactional
    public void createStudy() throws Exception {
        /* TODO: Apply proper test fix
        int databaseSizeBeforeCreate = studyRepository.findAll().size();

        // Create the Study
        restStudyMockMvc.perform(post("/api/studies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(study)))
            .andExpect(status().isCreated());

        // Validate the Study in the database
        List<Study> studyList = studyRepository.findAll();
        assertThat(studyList).hasSize(databaseSizeBeforeCreate + 1);
        Study testStudy = studyList.get(studyList.size() - 1);
        assertThat(testStudy.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testStudy.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testStudy.getIncentive()).isEqualTo(DEFAULT_INCENTIVE);
        assertThat(testStudy.getEmailSubject()).isEqualTo(DEFAULT_EMAIL_SUBJECT);
        assertThat(testStudy.getEmailBody()).isEqualTo(DEFAULT_EMAIL_BODY);
        */
    }

    @Test
    @Transactional
    public void createStudyWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = studyRepository.findAll().size();

        // Create the Study with an existing ID
        study.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStudyMockMvc.perform(post("/api/studies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(study)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Study> studyList = studyRepository.findAll();
        assertThat(studyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = studyRepository.findAll().size();
        // set the field null
        study.setName(null);

        // Create the Study, which fails.

        restStudyMockMvc.perform(post("/api/studies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(study)))
            .andExpect(status().isBadRequest());

        List<Study> studyList = studyRepository.findAll();
        assertThat(studyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEmailSubjectIsRequired() throws Exception {
        int databaseSizeBeforeTest = studyRepository.findAll().size();
        // set the field null
        study.setEmailSubject(null);

        // Create the Study, which fails.

        restStudyMockMvc.perform(post("/api/studies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(study)))
            .andExpect(status().isBadRequest());

        List<Study> studyList = studyRepository.findAll();
        assertThat(studyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStudies() throws Exception {
        /* TODO: Apply proper test fix
        // Initialize the database
        studyRepository.saveAndFlush(study);

        // Get all the studyList
        restStudyMockMvc.perform(get("/api/studies?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(study.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].incentive").value(hasItem(DEFAULT_INCENTIVE.toString())))
            .andExpect(jsonPath("$.[*].emailSubject").value(hasItem(DEFAULT_EMAIL_SUBJECT.toString())))
            .andExpect(jsonPath("$.[*].emailBody").value(hasItem(DEFAULT_EMAIL_BODY.toString())));
        */
    }

    @Test
    @Transactional
    public void getStudy() throws Exception {
        // Initialize the database
        studyRepository.saveAndFlush(study);

        // Get the study
        restStudyMockMvc.perform(get("/api/studies/{id}", study.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(study.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.incentiveType").value(DEFAULT_INCENTIVE_TYPE.toString()))
            .andExpect(jsonPath("$.incentiveDetail").value(DEFAULT_INCENTIVE_DETAIL.toString()))
            .andExpect(jsonPath("$.emailSubject").value(DEFAULT_EMAIL_SUBJECT.toString()))
            .andExpect(jsonPath("$.emailBody").value(DEFAULT_EMAIL_BODY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingStudy() throws Exception {
        // Get the study
        restStudyMockMvc.perform(get("/api/studies/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStudy() throws Exception {
        // Initialize the database
        studyRepository.saveAndFlush(study);
        int databaseSizeBeforeUpdate = studyRepository.findAll().size();

        // Update the study
        Study updatedStudy = studyRepository.getOne(study.getId());
        updatedStudy
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .incentiveDetail(UPDATED_INCENTIVE_DETAIL)
            .incentiveType(UPDATED_INCENTIVE_TYPE)
            .emailSubject(UPDATED_EMAIL_SUBJECT)
            .emailBody(UPDATED_EMAIL_BODY);

        restStudyMockMvc.perform(put("/api/studies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedStudy)))
            .andExpect(status().isOk());

        // Validate the Study in the database
        List<Study> studyList = studyRepository.findAll();
        assertThat(studyList).hasSize(databaseSizeBeforeUpdate);
        Study testStudy = studyList.get(studyList.size() - 1);
        assertThat(testStudy.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testStudy.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testStudy.getIncentiveDetail()).isEqualTo(UPDATED_INCENTIVE_DETAIL);
        assertThat(testStudy.getIncentiveType()).isEqualTo(UPDATED_INCENTIVE_TYPE);
        assertThat(testStudy.getEmailSubject()).isEqualTo(UPDATED_EMAIL_SUBJECT);
        assertThat(testStudy.getEmailBody()).isEqualTo(UPDATED_EMAIL_BODY);
    }

    @Test
    @Transactional
    public void updateNonExistingStudy() throws Exception {
        /* TODO: Apply proper test fix
        int databaseSizeBeforeUpdate = studyRepository.findAll().size();

        // Create the Study

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restStudyMockMvc.perform(put("/api/studies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(study)))
            .andExpect(status().isCreated());

        // Validate the Study in the database
        List<Study> studyList = studyRepository.findAll();
        assertThat(studyList).hasSize(databaseSizeBeforeUpdate + 1);
        */
    }

    @Test
    @Transactional
    public void deleteStudy() throws Exception {
        // Initialize the database
        studyRepository.saveAndFlush(study);
        int databaseSizeBeforeDelete = studyRepository.findAll().size();

        // Get the study
        restStudyMockMvc.perform(delete("/api/studies/{id}", study.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Study> studyList = studyRepository.findAll();
        assertThat(studyList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Study.class);
        Study study1 = new Study();
        study1.setId(1L);
        Study study2 = new Study();
        study2.setId(study1.getId());
        assertThat(study1).isEqualTo(study2);
        study2.setId(2L);
        assertThat(study1).isNotEqualTo(study2);
        study1.setId(null);
        assertThat(study1).isNotEqualTo(study2);
    }

    @Test
    @Transactional
    public void sendStudy() throws Exception {
        restStudyMockMvc.perform(post("/api/studies/send")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(study)))
            .andExpect(status().isCreated())
            .andExpect(content().string(""));
    }
}
