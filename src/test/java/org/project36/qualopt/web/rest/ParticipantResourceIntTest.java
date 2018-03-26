package org.project36.qualopt.web.rest;

import org.project36.qualopt.QualOptApp;

import org.project36.qualopt.domain.Participant;
import org.project36.qualopt.repository.ParticipantRepository;
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
 * Test class for the ParticipantResource REST controller.
 *
 * @see ParticipantResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = QualOptApp.class)
public class ParticipantResourceIntTest {

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_OCCUPATION = "AAAAAAAAAA";
    private static final String UPDATED_OCCUPATION = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final String DEFAULT_PROGRAMMING_LANGUAGE = "AAAAAAAAAA";
    private static final String UPDATED_PROGRAMMING_LANGUAGE = "BBBBBBBBBB";

    private static final Integer DEFAULT_NUMBER_OF_CONTRIBUTIONS = 1;
    private static final Integer UPDATED_NUMBER_OF_CONTRIBUTIONS = 2;

    private static final Integer DEFAULT_NUMBER_OF_REPOSITORIES = 1;
    private static final Integer UPDATED_NUMBER_OF_REPOSITORIES = 2;

    @Autowired
    private ParticipantRepository participantRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restParticipantMockMvc;

    private Participant participant;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ParticipantResource participantResource = new ParticipantResource(participantRepository);
        this.restParticipantMockMvc = MockMvcBuilders.standaloneSetup(participantResource)
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
    public static Participant createEntity(EntityManager em) {
        Participant participant = new Participant()
            .email(DEFAULT_EMAIL)
            .occupation(DEFAULT_OCCUPATION)
            .location(DEFAULT_LOCATION)
            .programmingLanguage(DEFAULT_PROGRAMMING_LANGUAGE)
            .numberOfContributions(DEFAULT_NUMBER_OF_CONTRIBUTIONS)
            .numberOfRepositories(DEFAULT_NUMBER_OF_REPOSITORIES);
        return participant;
    }

    @Before
    public void initTest() {
        participant = createEntity(em);
    }

    @Test
    @Transactional
    public void createParticipant() throws Exception {
        int databaseSizeBeforeCreate = participantRepository.findAll().size();

        // Create the Participant
        restParticipantMockMvc.perform(post("/api/participants")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(participant)))
            .andExpect(status().isCreated());

        // Validate the Participant in the database
        List<Participant> participantList = participantRepository.findAll();
        assertThat(participantList).hasSize(databaseSizeBeforeCreate + 1);
        Participant testParticipant = participantList.get(participantList.size() - 1);
        assertThat(testParticipant.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testParticipant.getOccupation()).isEqualTo(DEFAULT_OCCUPATION);
        assertThat(testParticipant.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testParticipant.getProgrammingLanguage()).isEqualTo(DEFAULT_PROGRAMMING_LANGUAGE);
        assertThat(testParticipant.getNumberOfContributions()).isEqualTo(DEFAULT_NUMBER_OF_CONTRIBUTIONS);
        assertThat(testParticipant.getNumberOfRepositories()).isEqualTo(DEFAULT_NUMBER_OF_REPOSITORIES);
    }

    @Test
    @Transactional
    public void createParticipantWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = participantRepository.findAll().size();

        // Create the Participant with an existing ID
        participant.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restParticipantMockMvc.perform(post("/api/participants")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(participant)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Participant> participantList = participantRepository.findAll();
        assertThat(participantList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = participantRepository.findAll().size();
        // set the field null
        participant.setEmail(null);

        // Create the Participant, which fails.

        restParticipantMockMvc.perform(post("/api/participants")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(participant)))
            .andExpect(status().isBadRequest());

        List<Participant> participantList = participantRepository.findAll();
        assertThat(participantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllParticipants() throws Exception {
        // Initialize the database
        participantRepository.saveAndFlush(participant);

        // Get all the participantList
        restParticipantMockMvc.perform(get("/api/participants?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(participant.getId().intValue())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].occupation").value(hasItem(DEFAULT_OCCUPATION.toString())))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION.toString())))
            .andExpect(jsonPath("$.[*].programmingLanguage").value(hasItem(DEFAULT_PROGRAMMING_LANGUAGE.toString())))
            .andExpect(jsonPath("$.[*].numberOfContributions").value(hasItem(DEFAULT_NUMBER_OF_CONTRIBUTIONS)))
            .andExpect(jsonPath("$.[*].numberOfRepositories").value(hasItem(DEFAULT_NUMBER_OF_REPOSITORIES)));
    }

    @Test
    @Transactional
    public void getParticipant() throws Exception {
        // Initialize the database
        participantRepository.saveAndFlush(participant);

        // Get the participant
        restParticipantMockMvc.perform(get("/api/participants/{id}", participant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(participant.getId().intValue()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.occupation").value(DEFAULT_OCCUPATION.toString()))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION.toString()))
            .andExpect(jsonPath("$.programmingLanguage").value(DEFAULT_PROGRAMMING_LANGUAGE.toString()))
            .andExpect(jsonPath("$.numberOfContributions").value(DEFAULT_NUMBER_OF_CONTRIBUTIONS))
            .andExpect(jsonPath("$.numberOfRepositories").value(DEFAULT_NUMBER_OF_REPOSITORIES));
    }

    @Test
    @Transactional
    public void getNonExistingParticipant() throws Exception {
        // Get the participant
        restParticipantMockMvc.perform(get("/api/participants/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateParticipant() throws Exception {
        // Initialize the database
        participantRepository.saveAndFlush(participant);
        int databaseSizeBeforeUpdate = participantRepository.findAll().size();

        // Update the participant
        Participant updatedParticipant = participantRepository.getOne(participant.getId());
        updatedParticipant
            .email(UPDATED_EMAIL)
            .occupation(UPDATED_OCCUPATION)
            .location(UPDATED_LOCATION)
            .programmingLanguage(UPDATED_PROGRAMMING_LANGUAGE)
            .numberOfContributions(UPDATED_NUMBER_OF_CONTRIBUTIONS)
            .numberOfRepositories(UPDATED_NUMBER_OF_REPOSITORIES);

        restParticipantMockMvc.perform(put("/api/participants")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedParticipant)))
            .andExpect(status().isOk());

        // Validate the Participant in the database
        List<Participant> participantList = participantRepository.findAll();
        assertThat(participantList).hasSize(databaseSizeBeforeUpdate);
        Participant testParticipant = participantList.get(participantList.size() - 1);
        assertThat(testParticipant.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testParticipant.getOccupation()).isEqualTo(UPDATED_OCCUPATION);
        assertThat(testParticipant.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testParticipant.getProgrammingLanguage()).isEqualTo(UPDATED_PROGRAMMING_LANGUAGE);
        assertThat(testParticipant.getNumberOfContributions()).isEqualTo(UPDATED_NUMBER_OF_CONTRIBUTIONS);
        assertThat(testParticipant.getNumberOfRepositories()).isEqualTo(UPDATED_NUMBER_OF_REPOSITORIES);
    }

    @Test
    @Transactional
    public void updateNonExistingParticipant() throws Exception {
        int databaseSizeBeforeUpdate = participantRepository.findAll().size();

        // Create the Participant

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restParticipantMockMvc.perform(put("/api/participants")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(participant)))
            .andExpect(status().isCreated());

        // Validate the Participant in the database
        List<Participant> participantList = participantRepository.findAll();
        assertThat(participantList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteParticipant() throws Exception {
        // Initialize the database
        participantRepository.saveAndFlush(participant);
        int databaseSizeBeforeDelete = participantRepository.findAll().size();

        // Get the participant
        restParticipantMockMvc.perform(delete("/api/participants/{id}", participant.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Participant> participantList = participantRepository.findAll();
        assertThat(participantList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Participant.class);
        Participant participant1 = new Participant();
        participant1.setId(1L);
        Participant participant2 = new Participant();
        participant2.setId(participant1.getId());
        assertThat(participant1).isEqualTo(participant2);
        participant2.setId(2L);
        assertThat(participant1).isNotEqualTo(participant2);
        participant1.setId(null);
        assertThat(participant1).isNotEqualTo(participant2);
    }
}
