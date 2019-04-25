package edu.socialnetwork.web.rest;

import edu.socialnetwork.SocialNetworkBackendApp;

import edu.socialnetwork.domain.Gender;
import edu.socialnetwork.domain.Profile;
import edu.socialnetwork.repository.GenderRepository;
import edu.socialnetwork.service.GenderService;
import edu.socialnetwork.web.rest.errors.ExceptionTranslator;
import edu.socialnetwork.service.dto.GenderCriteria;
import edu.socialnetwork.service.GenderQueryService;

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
 * Test class for the GenderResource REST controller.
 *
 * @see GenderResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SocialNetworkBackendApp.class)
public class GenderResourceIntTest {

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    @Autowired
    private GenderRepository genderRepository;

    @Autowired
    private GenderService genderService;

    @Autowired
    private GenderQueryService genderQueryService;

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

    private MockMvc restGenderMockMvc;

    private Gender gender;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final GenderResource genderResource = new GenderResource(genderService, genderQueryService);
        this.restGenderMockMvc = MockMvcBuilders.standaloneSetup(genderResource)
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
    public static Gender createEntity(EntityManager em) {
        Gender gender = new Gender()
            .type(DEFAULT_TYPE);
        return gender;
    }

    @Before
    public void initTest() {
        gender = createEntity(em);
    }

    @Test
    @Transactional
    public void createGender() throws Exception {
        int databaseSizeBeforeCreate = genderRepository.findAll().size();

        // Create the Gender
        restGenderMockMvc.perform(post("/api/genders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(gender)))
            .andExpect(status().isCreated());

        // Validate the Gender in the database
        List<Gender> genderList = genderRepository.findAll();
        assertThat(genderList).hasSize(databaseSizeBeforeCreate + 1);
        Gender testGender = genderList.get(genderList.size() - 1);
        assertThat(testGender.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    public void createGenderWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = genderRepository.findAll().size();

        // Create the Gender with an existing ID
        gender.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restGenderMockMvc.perform(post("/api/genders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(gender)))
            .andExpect(status().isBadRequest());

        // Validate the Gender in the database
        List<Gender> genderList = genderRepository.findAll();
        assertThat(genderList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = genderRepository.findAll().size();
        // set the field null
        gender.setType(null);

        // Create the Gender, which fails.

        restGenderMockMvc.perform(post("/api/genders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(gender)))
            .andExpect(status().isBadRequest());

        List<Gender> genderList = genderRepository.findAll();
        assertThat(genderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllGenders() throws Exception {
        // Initialize the database
        genderRepository.saveAndFlush(gender);

        // Get all the genderList
        restGenderMockMvc.perform(get("/api/genders?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(gender.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }
    
    @Test
    @Transactional
    public void getGender() throws Exception {
        // Initialize the database
        genderRepository.saveAndFlush(gender);

        // Get the gender
        restGenderMockMvc.perform(get("/api/genders/{id}", gender.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(gender.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getAllGendersByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        genderRepository.saveAndFlush(gender);

        // Get all the genderList where type equals to DEFAULT_TYPE
        defaultGenderShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the genderList where type equals to UPDATED_TYPE
        defaultGenderShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllGendersByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        genderRepository.saveAndFlush(gender);

        // Get all the genderList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultGenderShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the genderList where type equals to UPDATED_TYPE
        defaultGenderShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllGendersByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        genderRepository.saveAndFlush(gender);

        // Get all the genderList where type is not null
        defaultGenderShouldBeFound("type.specified=true");

        // Get all the genderList where type is null
        defaultGenderShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    public void getAllGendersByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        Profile user = ProfileResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        gender.addUser(user);
        genderRepository.saveAndFlush(gender);
        Long userId = user.getId();

        // Get all the genderList where user equals to userId
        defaultGenderShouldBeFound("userId.equals=" + userId);

        // Get all the genderList where user equals to userId + 1
        defaultGenderShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultGenderShouldBeFound(String filter) throws Exception {
        restGenderMockMvc.perform(get("/api/genders?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(gender.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)));

        // Check, that the count call also returns 1
        restGenderMockMvc.perform(get("/api/genders/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultGenderShouldNotBeFound(String filter) throws Exception {
        restGenderMockMvc.perform(get("/api/genders?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restGenderMockMvc.perform(get("/api/genders/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingGender() throws Exception {
        // Get the gender
        restGenderMockMvc.perform(get("/api/genders/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGender() throws Exception {
        // Initialize the database
        genderService.save(gender);

        int databaseSizeBeforeUpdate = genderRepository.findAll().size();

        // Update the gender
        Gender updatedGender = genderRepository.findById(gender.getId()).get();
        // Disconnect from session so that the updates on updatedGender are not directly saved in db
        em.detach(updatedGender);
        updatedGender
            .type(UPDATED_TYPE);

        restGenderMockMvc.perform(put("/api/genders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedGender)))
            .andExpect(status().isOk());

        // Validate the Gender in the database
        List<Gender> genderList = genderRepository.findAll();
        assertThat(genderList).hasSize(databaseSizeBeforeUpdate);
        Gender testGender = genderList.get(genderList.size() - 1);
        assertThat(testGender.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingGender() throws Exception {
        int databaseSizeBeforeUpdate = genderRepository.findAll().size();

        // Create the Gender

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGenderMockMvc.perform(put("/api/genders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(gender)))
            .andExpect(status().isBadRequest());

        // Validate the Gender in the database
        List<Gender> genderList = genderRepository.findAll();
        assertThat(genderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteGender() throws Exception {
        // Initialize the database
        genderService.save(gender);

        int databaseSizeBeforeDelete = genderRepository.findAll().size();

        // Delete the gender
        restGenderMockMvc.perform(delete("/api/genders/{id}", gender.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Gender> genderList = genderRepository.findAll();
        assertThat(genderList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Gender.class);
        Gender gender1 = new Gender();
        gender1.setId(1L);
        Gender gender2 = new Gender();
        gender2.setId(gender1.getId());
        assertThat(gender1).isEqualTo(gender2);
        gender2.setId(2L);
        assertThat(gender1).isNotEqualTo(gender2);
        gender1.setId(null);
        assertThat(gender1).isNotEqualTo(gender2);
    }
}
