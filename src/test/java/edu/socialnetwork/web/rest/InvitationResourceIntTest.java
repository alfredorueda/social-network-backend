package edu.socialnetwork.web.rest;

import edu.socialnetwork.SocialNetworkBackendApp;

import edu.socialnetwork.domain.Invitation;
import edu.socialnetwork.domain.Profile;
import edu.socialnetwork.domain.User;
import edu.socialnetwork.repository.InvitationRepository;
import edu.socialnetwork.repository.ProfileRepository;
import edu.socialnetwork.repository.UserRepository;
import edu.socialnetwork.service.InvitationService;
import edu.socialnetwork.web.rest.errors.ExceptionTranslator;
import edu.socialnetwork.service.dto.InvitationCriteria;
import edu.socialnetwork.service.InvitationQueryService;

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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;


import static edu.socialnetwork.web.rest.TestUtil.sameInstant;
import static edu.socialnetwork.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the InvitationResource REST controller.
 *
 * @see InvitationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SocialNetworkBackendApp.class)
public class InvitationResourceIntTest {

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Boolean DEFAULT_ACCEPTED = false;
    private static final Boolean UPDATED_ACCEPTED = true;

    @Autowired
    private InvitationRepository invitationRepository;

    @Autowired
    private InvitationService invitationService;

    @Autowired
    private InvitationQueryService invitationQueryService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileRepository profileRepository;

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

    private MockMvc restInvitationMockMvc;

    private Invitation invitation;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final InvitationResource invitationResource = new InvitationResource(invitationService, invitationQueryService, userRepository, invitationRepository, profileRepository);
        this.restInvitationMockMvc = MockMvcBuilders.standaloneSetup(invitationResource)
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
    public static Invitation createEntity(EntityManager em) {
        Invitation invitation = new Invitation()
            .createdDate(DEFAULT_CREATED_DATE)
            .accepted(DEFAULT_ACCEPTED);
        return invitation;
    }

    @Before
    public void initTest() {
        invitation = createEntity(em);
    }

    @Test
    @Transactional
    public void createInvitation() throws Exception {
        int databaseSizeBeforeCreate = invitationRepository.findAll().size();

        // Create the Invitation
        restInvitationMockMvc.perform(post("/api/invitations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invitation)))
            .andExpect(status().isCreated());

        // Validate the Invitation in the database
        List<Invitation> invitationList = invitationRepository.findAll();
        assertThat(invitationList).hasSize(databaseSizeBeforeCreate + 1);
        Invitation testInvitation = invitationList.get(invitationList.size() - 1);
        assertThat(testInvitation.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testInvitation.isAccepted()).isEqualTo(DEFAULT_ACCEPTED);
    }

    @Test
    @Transactional
    public void createInvitationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = invitationRepository.findAll().size();

        // Create the Invitation with an existing ID
        invitation.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInvitationMockMvc.perform(post("/api/invitations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invitation)))
            .andExpect(status().isBadRequest());

        // Validate the Invitation in the database
        List<Invitation> invitationList = invitationRepository.findAll();
        assertThat(invitationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = invitationRepository.findAll().size();
        // set the field null
        invitation.setCreatedDate(null);

        // Create the Invitation, which fails.

        restInvitationMockMvc.perform(post("/api/invitations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invitation)))
            .andExpect(status().isBadRequest());

        List<Invitation> invitationList = invitationRepository.findAll();
        assertThat(invitationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllInvitations() throws Exception {
        // Initialize the database
        invitationRepository.saveAndFlush(invitation);

        // Get all the invitationList
        restInvitationMockMvc.perform(get("/api/invitations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invitation.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].accepted").value(hasItem(DEFAULT_ACCEPTED.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getInvitation() throws Exception {
        // Initialize the database
        invitationRepository.saveAndFlush(invitation);

        // Get the invitation
        restInvitationMockMvc.perform(get("/api/invitations/{id}", invitation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(invitation.getId().intValue()))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
            .andExpect(jsonPath("$.accepted").value(DEFAULT_ACCEPTED.booleanValue()));
    }

    @Test
    @Transactional
    public void getAllInvitationsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        invitationRepository.saveAndFlush(invitation);

        // Get all the invitationList where createdDate equals to DEFAULT_CREATED_DATE
        defaultInvitationShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);

        // Get all the invitationList where createdDate equals to UPDATED_CREATED_DATE
        defaultInvitationShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllInvitationsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        invitationRepository.saveAndFlush(invitation);

        // Get all the invitationList where createdDate in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
        defaultInvitationShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);

        // Get all the invitationList where createdDate equals to UPDATED_CREATED_DATE
        defaultInvitationShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllInvitationsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        invitationRepository.saveAndFlush(invitation);

        // Get all the invitationList where createdDate is not null
        defaultInvitationShouldBeFound("createdDate.specified=true");

        // Get all the invitationList where createdDate is null
        defaultInvitationShouldNotBeFound("createdDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllInvitationsByCreatedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        invitationRepository.saveAndFlush(invitation);

        // Get all the invitationList where createdDate greater than or equals to DEFAULT_CREATED_DATE
        defaultInvitationShouldBeFound("createdDate.greaterOrEqualThan=" + DEFAULT_CREATED_DATE);

        // Get all the invitationList where createdDate greater than or equals to UPDATED_CREATED_DATE
        defaultInvitationShouldNotBeFound("createdDate.greaterOrEqualThan=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllInvitationsByCreatedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        invitationRepository.saveAndFlush(invitation);

        // Get all the invitationList where createdDate less than or equals to DEFAULT_CREATED_DATE
        defaultInvitationShouldNotBeFound("createdDate.lessThan=" + DEFAULT_CREATED_DATE);

        // Get all the invitationList where createdDate less than or equals to UPDATED_CREATED_DATE
        defaultInvitationShouldBeFound("createdDate.lessThan=" + UPDATED_CREATED_DATE);
    }


    @Test
    @Transactional
    public void getAllInvitationsByAcceptedIsEqualToSomething() throws Exception {
        // Initialize the database
        invitationRepository.saveAndFlush(invitation);

        // Get all the invitationList where accepted equals to DEFAULT_ACCEPTED
        defaultInvitationShouldBeFound("accepted.equals=" + DEFAULT_ACCEPTED);

        // Get all the invitationList where accepted equals to UPDATED_ACCEPTED
        defaultInvitationShouldNotBeFound("accepted.equals=" + UPDATED_ACCEPTED);
    }

    @Test
    @Transactional
    public void getAllInvitationsByAcceptedIsInShouldWork() throws Exception {
        // Initialize the database
        invitationRepository.saveAndFlush(invitation);

        // Get all the invitationList where accepted in DEFAULT_ACCEPTED or UPDATED_ACCEPTED
        defaultInvitationShouldBeFound("accepted.in=" + DEFAULT_ACCEPTED + "," + UPDATED_ACCEPTED);

        // Get all the invitationList where accepted equals to UPDATED_ACCEPTED
        defaultInvitationShouldNotBeFound("accepted.in=" + UPDATED_ACCEPTED);
    }

    @Test
    @Transactional
    public void getAllInvitationsByAcceptedIsNullOrNotNull() throws Exception {
        // Initialize the database
        invitationRepository.saveAndFlush(invitation);

        // Get all the invitationList where accepted is not null
        defaultInvitationShouldBeFound("accepted.specified=true");

        // Get all the invitationList where accepted is null
        defaultInvitationShouldNotBeFound("accepted.specified=false");
    }

    @Test
    @Transactional
    public void getAllInvitationsBySentIsEqualToSomething() throws Exception {
        // Initialize the database
        Profile sent = ProfileResourceIntTest.createEntity(em);
        em.persist(sent);
        em.flush();
        invitation.setSent(sent);
        invitationRepository.saveAndFlush(invitation);
        Long sentId = sent.getId();

        // Get all the invitationList where sent equals to sentId
        defaultInvitationShouldBeFound("sentId.equals=" + sentId);

        // Get all the invitationList where sent equals to sentId + 1
        defaultInvitationShouldNotBeFound("sentId.equals=" + (sentId + 1));
    }


    @Test
    @Transactional
    public void getAllInvitationsByReceivedIsEqualToSomething() throws Exception {
        // Initialize the database
        Profile received = ProfileResourceIntTest.createEntity(em);
        em.persist(received);
        em.flush();
        invitation.setReceived(received);
        invitationRepository.saveAndFlush(invitation);
        Long receivedId = received.getId();

        // Get all the invitationList where received equals to receivedId
        defaultInvitationShouldBeFound("receivedId.equals=" + receivedId);

        // Get all the invitationList where received equals to receivedId + 1
        defaultInvitationShouldNotBeFound("receivedId.equals=" + (receivedId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultInvitationShouldBeFound(String filter) throws Exception {
        restInvitationMockMvc.perform(get("/api/invitations?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invitation.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].accepted").value(hasItem(DEFAULT_ACCEPTED.booleanValue())));

        // Check, that the count call also returns 1
        restInvitationMockMvc.perform(get("/api/invitations/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultInvitationShouldNotBeFound(String filter) throws Exception {
        restInvitationMockMvc.perform(get("/api/invitations?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restInvitationMockMvc.perform(get("/api/invitations/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingInvitation() throws Exception {
        // Get the invitation
        restInvitationMockMvc.perform(get("/api/invitations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInvitation() throws Exception {
        // Initialize the database
        invitationService.save(invitation);

        int databaseSizeBeforeUpdate = invitationRepository.findAll().size();

        // Update the invitation
        Invitation updatedInvitation = invitationRepository.findById(invitation.getId()).get();
        // Disconnect from session so that the updates on updatedInvitation are not directly saved in db
        em.detach(updatedInvitation);
        updatedInvitation
            .createdDate(UPDATED_CREATED_DATE)
            .accepted(UPDATED_ACCEPTED);

        restInvitationMockMvc.perform(put("/api/invitations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedInvitation)))
            .andExpect(status().isOk());

        // Validate the Invitation in the database
        List<Invitation> invitationList = invitationRepository.findAll();
        assertThat(invitationList).hasSize(databaseSizeBeforeUpdate);
        Invitation testInvitation = invitationList.get(invitationList.size() - 1);
        assertThat(testInvitation.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testInvitation.isAccepted()).isEqualTo(UPDATED_ACCEPTED);
    }

    @Test
    @Transactional
    public void updateNonExistingInvitation() throws Exception {
        int databaseSizeBeforeUpdate = invitationRepository.findAll().size();

        // Create the Invitation

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvitationMockMvc.perform(put("/api/invitations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invitation)))
            .andExpect(status().isBadRequest());

        // Validate the Invitation in the database
        List<Invitation> invitationList = invitationRepository.findAll();
        assertThat(invitationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteInvitation() throws Exception {
        // Initialize the database
        invitationService.save(invitation);

        int databaseSizeBeforeDelete = invitationRepository.findAll().size();

        // Delete the invitation
        restInvitationMockMvc.perform(delete("/api/invitations/{id}", invitation.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Invitation> invitationList = invitationRepository.findAll();
        assertThat(invitationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Invitation.class);
        Invitation invitation1 = new Invitation();
        invitation1.setId(1L);
        Invitation invitation2 = new Invitation();
        invitation2.setId(invitation1.getId());
        assertThat(invitation1).isEqualTo(invitation2);
        invitation2.setId(2L);
        assertThat(invitation1).isNotEqualTo(invitation2);
        invitation1.setId(null);
        assertThat(invitation1).isNotEqualTo(invitation2);
    }

//    @Test
//    @Transactional
//    public void createInvite() throws Exception {
//        User userSender = UserResourceIntTest.createEntity(em);
//        User userReceiver = UserResourceIntTest.createEntity(em);
//        String loginSender = userSender.getLogin();
//        String loginReceiver = userReceiver.getLogin();
//        Profile profileSender = ProfileResourceIntTest.createEntity(em);
//        Profile profileReceiver = ProfileResourceIntTest.createEntity(em);
//
//        profileSender.setUser(userSender);
//        profileReceiver.setUser(userReceiver);
//
//        userRepository.save(userSender);
//        userRepository.save(userReceiver);
//        profileRepository.save(profileSender);
//        profileRepository.save(profileReceiver);
//
//        invitation.setSent(profileSender);
//        invitation.setReceived(profileReceiver);
////
////
////        // Validate the Invitation in the database
////        List<Invitation> invitationList = invitationRepository.findAll();
//        assertThat(invitationRepository.findBySentUserLoginAndReceivedUserLogin(loginSender, loginReceiver)).isEmpty();
//        assertThat(invitationRepository.findBySentUserLoginAndReceivedUserLogin(loginReceiver, loginSender)).isEmpty();
//
//
//
////        int databaseSizeBeforeCreate = invitationRepository.findAll().size();
////        assertThat(invitationList).hasSize(databaseSizeBeforeCreate + 1);
////        Invitation testInvitation = invitationList.get(invitationList.size() - 1);
////        assertThat(testInvitation.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
////        assertThat(testInvitation.isAccepted()).isEqualTo(DEFAULT_ACCEPTED);
//
//
//
//
//        // Create the Invitation
//        restInvitationMockMvc.perform(
//            post("/api/invite/"+userReceiver.getId())
//            .headers()
//        )
//            .andExpect(status().isCreated());
//
//    }
}
