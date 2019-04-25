package edu.socialnetwork.web.rest;

import edu.socialnetwork.SocialNetworkBackendApp;

import edu.socialnetwork.domain.Relationship;
import edu.socialnetwork.domain.Profile;
import edu.socialnetwork.repository.RelationshipRepository;
import edu.socialnetwork.service.RelationshipService;
import edu.socialnetwork.web.rest.errors.ExceptionTranslator;
import edu.socialnetwork.service.dto.RelationshipCriteria;
import edu.socialnetwork.service.RelationshipQueryService;

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
 * Test class for the RelationshipResource REST controller.
 *
 * @see RelationshipResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SocialNetworkBackendApp.class)
public class RelationshipResourceIntTest {

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    @Autowired
    private RelationshipRepository relationshipRepository;

    @Autowired
    private RelationshipService relationshipService;

    @Autowired
    private RelationshipQueryService relationshipQueryService;

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

    private MockMvc restRelationshipMockMvc;

    private Relationship relationship;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RelationshipResource relationshipResource = new RelationshipResource(relationshipService, relationshipQueryService);
        this.restRelationshipMockMvc = MockMvcBuilders.standaloneSetup(relationshipResource)
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
    public static Relationship createEntity(EntityManager em) {
        Relationship relationship = new Relationship()
            .status(DEFAULT_STATUS);
        return relationship;
    }

    @Before
    public void initTest() {
        relationship = createEntity(em);
    }

    @Test
    @Transactional
    public void createRelationship() throws Exception {
        int databaseSizeBeforeCreate = relationshipRepository.findAll().size();

        // Create the Relationship
        restRelationshipMockMvc.perform(post("/api/relationships")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(relationship)))
            .andExpect(status().isCreated());

        // Validate the Relationship in the database
        List<Relationship> relationshipList = relationshipRepository.findAll();
        assertThat(relationshipList).hasSize(databaseSizeBeforeCreate + 1);
        Relationship testRelationship = relationshipList.get(relationshipList.size() - 1);
        assertThat(testRelationship.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createRelationshipWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = relationshipRepository.findAll().size();

        // Create the Relationship with an existing ID
        relationship.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRelationshipMockMvc.perform(post("/api/relationships")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(relationship)))
            .andExpect(status().isBadRequest());

        // Validate the Relationship in the database
        List<Relationship> relationshipList = relationshipRepository.findAll();
        assertThat(relationshipList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = relationshipRepository.findAll().size();
        // set the field null
        relationship.setStatus(null);

        // Create the Relationship, which fails.

        restRelationshipMockMvc.perform(post("/api/relationships")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(relationship)))
            .andExpect(status().isBadRequest());

        List<Relationship> relationshipList = relationshipRepository.findAll();
        assertThat(relationshipList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRelationships() throws Exception {
        // Initialize the database
        relationshipRepository.saveAndFlush(relationship);

        // Get all the relationshipList
        restRelationshipMockMvc.perform(get("/api/relationships?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(relationship.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }
    
    @Test
    @Transactional
    public void getRelationship() throws Exception {
        // Initialize the database
        relationshipRepository.saveAndFlush(relationship);

        // Get the relationship
        restRelationshipMockMvc.perform(get("/api/relationships/{id}", relationship.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(relationship.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getAllRelationshipsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        relationshipRepository.saveAndFlush(relationship);

        // Get all the relationshipList where status equals to DEFAULT_STATUS
        defaultRelationshipShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the relationshipList where status equals to UPDATED_STATUS
        defaultRelationshipShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllRelationshipsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        relationshipRepository.saveAndFlush(relationship);

        // Get all the relationshipList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultRelationshipShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the relationshipList where status equals to UPDATED_STATUS
        defaultRelationshipShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllRelationshipsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        relationshipRepository.saveAndFlush(relationship);

        // Get all the relationshipList where status is not null
        defaultRelationshipShouldBeFound("status.specified=true");

        // Get all the relationshipList where status is null
        defaultRelationshipShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    public void getAllRelationshipsByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        Profile user = ProfileResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        relationship.addUser(user);
        relationshipRepository.saveAndFlush(relationship);
        Long userId = user.getId();

        // Get all the relationshipList where user equals to userId
        defaultRelationshipShouldBeFound("userId.equals=" + userId);

        // Get all the relationshipList where user equals to userId + 1
        defaultRelationshipShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultRelationshipShouldBeFound(String filter) throws Exception {
        restRelationshipMockMvc.perform(get("/api/relationships?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(relationship.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));

        // Check, that the count call also returns 1
        restRelationshipMockMvc.perform(get("/api/relationships/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultRelationshipShouldNotBeFound(String filter) throws Exception {
        restRelationshipMockMvc.perform(get("/api/relationships?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRelationshipMockMvc.perform(get("/api/relationships/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingRelationship() throws Exception {
        // Get the relationship
        restRelationshipMockMvc.perform(get("/api/relationships/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRelationship() throws Exception {
        // Initialize the database
        relationshipService.save(relationship);

        int databaseSizeBeforeUpdate = relationshipRepository.findAll().size();

        // Update the relationship
        Relationship updatedRelationship = relationshipRepository.findById(relationship.getId()).get();
        // Disconnect from session so that the updates on updatedRelationship are not directly saved in db
        em.detach(updatedRelationship);
        updatedRelationship
            .status(UPDATED_STATUS);

        restRelationshipMockMvc.perform(put("/api/relationships")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRelationship)))
            .andExpect(status().isOk());

        // Validate the Relationship in the database
        List<Relationship> relationshipList = relationshipRepository.findAll();
        assertThat(relationshipList).hasSize(databaseSizeBeforeUpdate);
        Relationship testRelationship = relationshipList.get(relationshipList.size() - 1);
        assertThat(testRelationship.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingRelationship() throws Exception {
        int databaseSizeBeforeUpdate = relationshipRepository.findAll().size();

        // Create the Relationship

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRelationshipMockMvc.perform(put("/api/relationships")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(relationship)))
            .andExpect(status().isBadRequest());

        // Validate the Relationship in the database
        List<Relationship> relationshipList = relationshipRepository.findAll();
        assertThat(relationshipList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRelationship() throws Exception {
        // Initialize the database
        relationshipService.save(relationship);

        int databaseSizeBeforeDelete = relationshipRepository.findAll().size();

        // Delete the relationship
        restRelationshipMockMvc.perform(delete("/api/relationships/{id}", relationship.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Relationship> relationshipList = relationshipRepository.findAll();
        assertThat(relationshipList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Relationship.class);
        Relationship relationship1 = new Relationship();
        relationship1.setId(1L);
        Relationship relationship2 = new Relationship();
        relationship2.setId(relationship1.getId());
        assertThat(relationship1).isEqualTo(relationship2);
        relationship2.setId(2L);
        assertThat(relationship1).isNotEqualTo(relationship2);
        relationship1.setId(null);
        assertThat(relationship1).isNotEqualTo(relationship2);
    }
}
