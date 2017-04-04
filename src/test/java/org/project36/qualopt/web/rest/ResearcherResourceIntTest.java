package org.project36.qualopt.web.rest;

import org.project36.qualopt.QualOpt2App;

import org.project36.qualopt.domain.Researcher;
import org.project36.qualopt.repository.ResearcherRepository;
import org.project36.qualopt.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
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
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ResearcherResource REST controller.
 *
 * @see ResearcherResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = QualOpt2App.class)
public class ResearcherResourceIntTest {

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final String DEFAULT_PROFESSION = "AAAAAAAAAA";
    private static final String UPDATED_PROFESSION = "BBBBBBBBBB";

    private static final String DEFAULT_INSTITUTION = "AAAAAAAAAA";
    private static final String UPDATED_INSTITUTION = "BBBBBBBBBB";

    private static final String DEFAULT_MAIL_SERVER = "AAAAAAAAAA";
    private static final String UPDATED_MAIL_SERVER = "BBBBBBBBBB";

    @Autowired
    private ResearcherRepository researcherRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restResearcherMockMvc;

    private Researcher researcher;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ResearcherResource researcherResource = new ResearcherResource(researcherRepository);
        this.restResearcherMockMvc = MockMvcBuilders.standaloneSetup(researcherResource)
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
    public static Researcher createEntity(EntityManager em) {
        Researcher researcher = new Researcher()
            .email(DEFAULT_EMAIL)
            .password(DEFAULT_PASSWORD)
            .profession(DEFAULT_PROFESSION)
            .institution(DEFAULT_INSTITUTION)
            .mailServer(DEFAULT_MAIL_SERVER);
        return researcher;
    }

    @Before
    public void initTest() {
        researcher = createEntity(em);
    }

    @Test
    @Transactional
    public void createResearcher() throws Exception {
        int databaseSizeBeforeCreate = researcherRepository.findAll().size();

        // Create the Researcher
        restResearcherMockMvc.perform(post("/api/researchers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(researcher)))
            .andExpect(status().isCreated());

        // Validate the Researcher in the database
        List<Researcher> researcherList = researcherRepository.findAll();
        assertThat(researcherList).hasSize(databaseSizeBeforeCreate + 1);
        Researcher testResearcher = researcherList.get(researcherList.size() - 1);
        assertThat(testResearcher.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testResearcher.getPassword()).isEqualTo(DEFAULT_PASSWORD);
        assertThat(testResearcher.getProfession()).isEqualTo(DEFAULT_PROFESSION);
        assertThat(testResearcher.getInstitution()).isEqualTo(DEFAULT_INSTITUTION);
        assertThat(testResearcher.getMailServer()).isEqualTo(DEFAULT_MAIL_SERVER);
    }

    @Test
    @Transactional
    public void createResearcherWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = researcherRepository.findAll().size();

        // Create the Researcher with an existing ID
        researcher.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restResearcherMockMvc.perform(post("/api/researchers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(researcher)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Researcher> researcherList = researcherRepository.findAll();
        assertThat(researcherList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllResearchers() throws Exception {
        // Initialize the database
        researcherRepository.saveAndFlush(researcher);

        // Get all the researcherList
        restResearcherMockMvc.perform(get("/api/researchers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(researcher.getId().intValue())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD.toString())))
            .andExpect(jsonPath("$.[*].profession").value(hasItem(DEFAULT_PROFESSION.toString())))
            .andExpect(jsonPath("$.[*].institution").value(hasItem(DEFAULT_INSTITUTION.toString())))
            .andExpect(jsonPath("$.[*].mailServer").value(hasItem(DEFAULT_MAIL_SERVER.toString())));
    }

    @Test
    @Transactional
    public void getResearcher() throws Exception {
        // Initialize the database
        researcherRepository.saveAndFlush(researcher);

        // Get the researcher
        restResearcherMockMvc.perform(get("/api/researchers/{id}", researcher.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(researcher.getId().intValue()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD.toString()))
            .andExpect(jsonPath("$.profession").value(DEFAULT_PROFESSION.toString()))
            .andExpect(jsonPath("$.institution").value(DEFAULT_INSTITUTION.toString()))
            .andExpect(jsonPath("$.mailServer").value(DEFAULT_MAIL_SERVER.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingResearcher() throws Exception {
        // Get the researcher
        restResearcherMockMvc.perform(get("/api/researchers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateResearcher() throws Exception {
        // Initialize the database
        researcherRepository.saveAndFlush(researcher);
        int databaseSizeBeforeUpdate = researcherRepository.findAll().size();

        // Update the researcher
        Researcher updatedResearcher = researcherRepository.findOne(researcher.getId());
        updatedResearcher
            .email(UPDATED_EMAIL)
            .password(UPDATED_PASSWORD)
            .profession(UPDATED_PROFESSION)
            .institution(UPDATED_INSTITUTION)
            .mailServer(UPDATED_MAIL_SERVER);

        restResearcherMockMvc.perform(put("/api/researchers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedResearcher)))
            .andExpect(status().isOk());

        // Validate the Researcher in the database
        List<Researcher> researcherList = researcherRepository.findAll();
        assertThat(researcherList).hasSize(databaseSizeBeforeUpdate);
        Researcher testResearcher = researcherList.get(researcherList.size() - 1);
        assertThat(testResearcher.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testResearcher.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testResearcher.getProfession()).isEqualTo(UPDATED_PROFESSION);
        assertThat(testResearcher.getInstitution()).isEqualTo(UPDATED_INSTITUTION);
        assertThat(testResearcher.getMailServer()).isEqualTo(UPDATED_MAIL_SERVER);
    }

    @Test
    @Transactional
    public void updateNonExistingResearcher() throws Exception {
        int databaseSizeBeforeUpdate = researcherRepository.findAll().size();

        // Create the Researcher

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restResearcherMockMvc.perform(put("/api/researchers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(researcher)))
            .andExpect(status().isCreated());

        // Validate the Researcher in the database
        List<Researcher> researcherList = researcherRepository.findAll();
        assertThat(researcherList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteResearcher() throws Exception {
        // Initialize the database
        researcherRepository.saveAndFlush(researcher);
        int databaseSizeBeforeDelete = researcherRepository.findAll().size();

        // Get the researcher
        restResearcherMockMvc.perform(delete("/api/researchers/{id}", researcher.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Researcher> researcherList = researcherRepository.findAll();
        assertThat(researcherList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Researcher.class);
    }
}
