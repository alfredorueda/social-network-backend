package edu.socialnetwork.web.rest;

import edu.socialnetwork.SocialNetworkBackendApp;

import edu.socialnetwork.domain.Ethnicity;
import edu.socialnetwork.domain.Profile;
import edu.socialnetwork.repository.EthnicityRepository;
import edu.socialnetwork.service.EthnicityService;
import edu.socialnetwork.web.rest.errors.ExceptionTranslator;
import edu.socialnetwork.service.dto.EthnicityCriteria;
import edu.socialnetwork.service.EthnicityQueryService;

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
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;


import static edu.socialnetwork.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the EthnicityResource REST controller.
 *
 * @see EthnicityResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SocialNetworkBackendApp.class)
public class EthnicityResourceIntTest {

    private static final String DEFAULT_ETHNICITY = "AAAAAAAAAA";
    private static final String UPDATED_ETHNICITY = "BBBBBBBBBB";

    @Autowired
    private EthnicityRepository ethnicityRepository;

    @Autowired
    private EthnicityService ethnicityService;

    @Autowired
    private EthnicityQueryService ethnicityQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restEthnicityMockMvc;

    private Ethnicity ethnicity;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EthnicityResource ethnicityResource = new EthnicityResource(ethnicityService, ethnicityQueryService);
        this.restEthnicityMockMvc = MockMvcBuilders.standaloneSetup(ethnicityResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ethnicity createEntity(EntityManager em) {
        Ethnicity ethnicity = new Ethnicity()
            .ethnicity(DEFAULT_ETHNICITY);
        return ethnicity;
    }

    @Before
    public void initTest() {
        ethnicity = createEntity(em);
    }

    @Test
    @Transactional
    public void createEthnicity() throws Exception {
        int databaseSizeBeforeCreate = ethnicityRepository.findAll().size();

        // Create the Ethnicity
        restEthnicityMockMvc.perform(post("/api/ethnicities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ethnicity)))
            .andExpect(status().isCreated());

        // Validate the Ethnicity in the database
        List<Ethnicity> ethnicityList = ethnicityRepository.findAll();
        assertThat(ethnicityList).hasSize(databaseSizeBeforeCreate + 1);
        Ethnicity testEthnicity = ethnicityList.get(ethnicityList.size() - 1);
        assertThat(testEthnicity.getEthnicity()).isEqualTo(DEFAULT_ETHNICITY);
    }

    @Test
    @Transactional
    public void createEthnicityWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = ethnicityRepository.findAll().size();

        // Create the Ethnicity with an existing ID
        ethnicity.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEthnicityMockMvc.perform(post("/api/ethnicities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ethnicity)))
            .andExpect(status().isBadRequest());

        // Validate the Ethnicity in the database
        List<Ethnicity> ethnicityList = ethnicityRepository.findAll();
        assertThat(ethnicityList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkEthnicityIsRequired() throws Exception {
        int databaseSizeBeforeTest = ethnicityRepository.findAll().size();
        // set the field null
        ethnicity.setEthnicity(null);

        // Create the Ethnicity, which fails.

        restEthnicityMockMvc.perform(post("/api/ethnicities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ethnicity)))
            .andExpect(status().isBadRequest());

        List<Ethnicity> ethnicityList = ethnicityRepository.findAll();
        assertThat(ethnicityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEthnicities() throws Exception {
        // Initialize the database
        ethnicityRepository.saveAndFlush(ethnicity);

        // Get all the ethnicityList
        restEthnicityMockMvc.perform(get("/api/ethnicities?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ethnicity.getId().intValue())))
            .andExpect(jsonPath("$.[*].ethnicity").value(hasItem(DEFAULT_ETHNICITY.toString())));
    }
    
    @Test
    @Transactional
    public void getEthnicity() throws Exception {
        // Initialize the database
        ethnicityRepository.saveAndFlush(ethnicity);

        // Get the ethnicity
        restEthnicityMockMvc.perform(get("/api/ethnicities/{id}", ethnicity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(ethnicity.getId().intValue()))
            .andExpect(jsonPath("$.ethnicity").value(DEFAULT_ETHNICITY.toString()));
    }

    @Test
    @Transactional
    public void getAllEthnicitiesByEthnicityIsEqualToSomething() throws Exception {
        // Initialize the database
        ethnicityRepository.saveAndFlush(ethnicity);

        // Get all the ethnicityList where ethnicity equals to DEFAULT_ETHNICITY
        defaultEthnicityShouldBeFound("ethnicity.equals=" + DEFAULT_ETHNICITY);

        // Get all the ethnicityList where ethnicity equals to UPDATED_ETHNICITY
        defaultEthnicityShouldNotBeFound("ethnicity.equals=" + UPDATED_ETHNICITY);
    }

    @Test
    @Transactional
    public void getAllEthnicitiesByEthnicityIsInShouldWork() throws Exception {
        // Initialize the database
        ethnicityRepository.saveAndFlush(ethnicity);

        // Get all the ethnicityList where ethnicity in DEFAULT_ETHNICITY or UPDATED_ETHNICITY
        defaultEthnicityShouldBeFound("ethnicity.in=" + DEFAULT_ETHNICITY + "," + UPDATED_ETHNICITY);

        // Get all the ethnicityList where ethnicity equals to UPDATED_ETHNICITY
        defaultEthnicityShouldNotBeFound("ethnicity.in=" + UPDATED_ETHNICITY);
    }

    @Test
    @Transactional
    public void getAllEthnicitiesByEthnicityIsNullOrNotNull() throws Exception {
        // Initialize the database
        ethnicityRepository.saveAndFlush(ethnicity);

        // Get all the ethnicityList where ethnicity is not null
        defaultEthnicityShouldBeFound("ethnicity.specified=true");

        // Get all the ethnicityList where ethnicity is null
        defaultEthnicityShouldNotBeFound("ethnicity.specified=false");
    }

    @Test
    @Transactional
    public void getAllEthnicitiesByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        Profile user = ProfileResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        ethnicity.addUser(user);
        ethnicityRepository.saveAndFlush(ethnicity);
        Long userId = user.getId();

        // Get all the ethnicityList where user equals to userId
        defaultEthnicityShouldBeFound("userId.equals=" + userId);

        // Get all the ethnicityList where user equals to userId + 1
        defaultEthnicityShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultEthnicityShouldBeFound(String filter) throws Exception {
        restEthnicityMockMvc.perform(get("/api/ethnicities?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ethnicity.getId().intValue())))
            .andExpect(jsonPath("$.[*].ethnicity").value(hasItem(DEFAULT_ETHNICITY)));

        // Check, that the count call also returns 1
        restEthnicityMockMvc.perform(get("/api/ethnicities/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultEthnicityShouldNotBeFound(String filter) throws Exception {
        restEthnicityMockMvc.perform(get("/api/ethnicities?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEthnicityMockMvc.perform(get("/api/ethnicities/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingEthnicity() throws Exception {
        // Get the ethnicity
        restEthnicityMockMvc.perform(get("/api/ethnicities/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEthnicity() throws Exception {
        // Initialize the database
        ethnicityService.save(ethnicity);

        int databaseSizeBeforeUpdate = ethnicityRepository.findAll().size();

        // Update the ethnicity
        Ethnicity updatedEthnicity = ethnicityRepository.findById(ethnicity.getId()).get();
        // Disconnect from session so that the updates on updatedEthnicity are not directly saved in db
        em.detach(updatedEthnicity);
        updatedEthnicity
            .ethnicity(UPDATED_ETHNICITY);

        restEthnicityMockMvc.perform(put("/api/ethnicities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedEthnicity)))
            .andExpect(status().isOk());

        // Validate the Ethnicity in the database
        List<Ethnicity> ethnicityList = ethnicityRepository.findAll();
        assertThat(ethnicityList).hasSize(databaseSizeBeforeUpdate);
        Ethnicity testEthnicity = ethnicityList.get(ethnicityList.size() - 1);
        assertThat(testEthnicity.getEthnicity()).isEqualTo(UPDATED_ETHNICITY);
    }

    @Test
    @Transactional
    public void updateNonExistingEthnicity() throws Exception {
        int databaseSizeBeforeUpdate = ethnicityRepository.findAll().size();

        // Create the Ethnicity

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEthnicityMockMvc.perform(put("/api/ethnicities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ethnicity)))
            .andExpect(status().isBadRequest());

        // Validate the Ethnicity in the database
        List<Ethnicity> ethnicityList = ethnicityRepository.findAll();
        assertThat(ethnicityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteEthnicity() throws Exception {
        // Initialize the database
        ethnicityService.save(ethnicity);

        int databaseSizeBeforeDelete = ethnicityRepository.findAll().size();

        // Delete the ethnicity
        restEthnicityMockMvc.perform(delete("/api/ethnicities/{id}", ethnicity.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Ethnicity> ethnicityList = ethnicityRepository.findAll();
        assertThat(ethnicityList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Ethnicity.class);
        Ethnicity ethnicity1 = new Ethnicity();
        ethnicity1.setId(1L);
        Ethnicity ethnicity2 = new Ethnicity();
        ethnicity2.setId(ethnicity1.getId());
        assertThat(ethnicity1).isEqualTo(ethnicity2);
        ethnicity2.setId(2L);
        assertThat(ethnicity1).isNotEqualTo(ethnicity2);
        ethnicity1.setId(null);
        assertThat(ethnicity1).isNotEqualTo(ethnicity2);
    }
}
