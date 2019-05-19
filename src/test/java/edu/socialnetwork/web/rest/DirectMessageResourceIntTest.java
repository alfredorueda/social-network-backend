package edu.socialnetwork.web.rest;

import edu.socialnetwork.SocialNetworkBackendApp;
import edu.socialnetwork.domain.DirectMessage;
import edu.socialnetwork.domain.Profile;
import edu.socialnetwork.repository.DirectMessageRepository;
import edu.socialnetwork.repository.InvitationRepository;
import edu.socialnetwork.repository.ProfileRepository;
import edu.socialnetwork.service.DirectMessageQueryService;
import edu.socialnetwork.service.DirectMessageService;
import edu.socialnetwork.web.rest.errors.ExceptionTranslator;
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
import org.springframework.util.Base64Utils;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

import static edu.socialnetwork.web.rest.TestUtil.createFormattingConversionService;
import static edu.socialnetwork.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the DirectMessageResource REST controller.
 *
 * @see DirectMessageResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SocialNetworkBackendApp.class)
public class DirectMessageResourceIntTest {

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_MESSAGE = "BBBBBBBBBB";

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final byte[] DEFAULT_PICTURE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PICTURE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PICTURE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PICTURE_CONTENT_TYPE = "image/png";

    @Autowired
    private DirectMessageRepository directMessageRepository;

    @Autowired
    private DirectMessageService directMessageService;

    @Autowired
    private DirectMessageQueryService directMessageQueryService;

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

    private MockMvc restDirectMessageMockMvc;

    private DirectMessage directMessage;

    private ProfileRepository profileRepository;

    private InvitationRepository invitationRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DirectMessageResource directMessageResource = new DirectMessageResource(directMessageService, directMessageQueryService, profileRepository, invitationRepository);
        this.restDirectMessageMockMvc = MockMvcBuilders.standaloneSetup(directMessageResource)
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
    public static DirectMessage createEntity(EntityManager em) {
        DirectMessage directMessage = new DirectMessage()
            .createdDate(DEFAULT_CREATED_DATE)
            .message(DEFAULT_MESSAGE)
            .url(DEFAULT_URL)
            .picture(DEFAULT_PICTURE)
            .pictureContentType(DEFAULT_PICTURE_CONTENT_TYPE);
        return directMessage;
    }

    @Before
    public void initTest() {
        directMessage = createEntity(em);
    }

    @Test
    @Transactional
    public void createDirectMessage() throws Exception {
        int databaseSizeBeforeCreate = directMessageRepository.findAll().size();

        // Create the DirectMessage
        restDirectMessageMockMvc.perform(post("/api/direct-messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(directMessage)))
            .andExpect(status().isCreated());

        // Validate the DirectMessage in the database
        List<DirectMessage> directMessageList = directMessageRepository.findAll();
        assertThat(directMessageList).hasSize(databaseSizeBeforeCreate + 1);
        DirectMessage testDirectMessage = directMessageList.get(directMessageList.size() - 1);
        assertThat(testDirectMessage.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testDirectMessage.getMessage()).isEqualTo(DEFAULT_MESSAGE);
        assertThat(testDirectMessage.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testDirectMessage.getPicture()).isEqualTo(DEFAULT_PICTURE);
        assertThat(testDirectMessage.getPictureContentType()).isEqualTo(DEFAULT_PICTURE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void createDirectMessageWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = directMessageRepository.findAll().size();

        // Create the DirectMessage with an existing ID
        directMessage.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDirectMessageMockMvc.perform(post("/api/direct-messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(directMessage)))
            .andExpect(status().isBadRequest());

        // Validate the DirectMessage in the database
        List<DirectMessage> directMessageList = directMessageRepository.findAll();
        assertThat(directMessageList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = directMessageRepository.findAll().size();
        // set the field null
        directMessage.setCreatedDate(null);

        // Create the DirectMessage, which fails.

        restDirectMessageMockMvc.perform(post("/api/direct-messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(directMessage)))
            .andExpect(status().isBadRequest());

        List<DirectMessage> directMessageList = directMessageRepository.findAll();
        assertThat(directMessageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDirectMessages() throws Exception {
        // Initialize the database
        directMessageRepository.saveAndFlush(directMessage);

        // Get all the directMessageList
        restDirectMessageMockMvc.perform(get("/api/direct-messages?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(directMessage.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE.toString())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())))
            .andExpect(jsonPath("$.[*].pictureContentType").value(hasItem(DEFAULT_PICTURE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].picture").value(hasItem(Base64Utils.encodeToString(DEFAULT_PICTURE))));
    }

    @Test
    @Transactional
    public void getDirectMessage() throws Exception {
        // Initialize the database
        directMessageRepository.saveAndFlush(directMessage);

        // Get the directMessage
        restDirectMessageMockMvc.perform(get("/api/direct-messages/{id}", directMessage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(directMessage.getId().intValue()))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
            .andExpect(jsonPath("$.message").value(DEFAULT_MESSAGE.toString()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL.toString()))
            .andExpect(jsonPath("$.pictureContentType").value(DEFAULT_PICTURE_CONTENT_TYPE))
            .andExpect(jsonPath("$.picture").value(Base64Utils.encodeToString(DEFAULT_PICTURE)));
    }

    @Test
    @Transactional
    public void getAllDirectMessagesByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        directMessageRepository.saveAndFlush(directMessage);

        // Get all the directMessageList where createdDate equals to DEFAULT_CREATED_DATE
        defaultDirectMessageShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);

        // Get all the directMessageList where createdDate equals to UPDATED_CREATED_DATE
        defaultDirectMessageShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllDirectMessagesByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        directMessageRepository.saveAndFlush(directMessage);

        // Get all the directMessageList where createdDate in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
        defaultDirectMessageShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);

        // Get all the directMessageList where createdDate equals to UPDATED_CREATED_DATE
        defaultDirectMessageShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllDirectMessagesByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        directMessageRepository.saveAndFlush(directMessage);

        // Get all the directMessageList where createdDate is not null
        defaultDirectMessageShouldBeFound("createdDate.specified=true");

        // Get all the directMessageList where createdDate is null
        defaultDirectMessageShouldNotBeFound("createdDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllDirectMessagesByCreatedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        directMessageRepository.saveAndFlush(directMessage);

        // Get all the directMessageList where createdDate greater than or equals to DEFAULT_CREATED_DATE
        defaultDirectMessageShouldBeFound("createdDate.greaterOrEqualThan=" + DEFAULT_CREATED_DATE);

        // Get all the directMessageList where createdDate greater than or equals to UPDATED_CREATED_DATE
        defaultDirectMessageShouldNotBeFound("createdDate.greaterOrEqualThan=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllDirectMessagesByCreatedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        directMessageRepository.saveAndFlush(directMessage);

        // Get all the directMessageList where createdDate less than or equals to DEFAULT_CREATED_DATE
        defaultDirectMessageShouldNotBeFound("createdDate.lessThan=" + DEFAULT_CREATED_DATE);

        // Get all the directMessageList where createdDate less than or equals to UPDATED_CREATED_DATE
        defaultDirectMessageShouldBeFound("createdDate.lessThan=" + UPDATED_CREATED_DATE);
    }


    @Test
    @Transactional
    public void getAllDirectMessagesByMessageIsEqualToSomething() throws Exception {
        // Initialize the database
        directMessageRepository.saveAndFlush(directMessage);

        // Get all the directMessageList where message equals to DEFAULT_MESSAGE
        defaultDirectMessageShouldBeFound("message.equals=" + DEFAULT_MESSAGE);

        // Get all the directMessageList where message equals to UPDATED_MESSAGE
        defaultDirectMessageShouldNotBeFound("message.equals=" + UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    public void getAllDirectMessagesByMessageIsInShouldWork() throws Exception {
        // Initialize the database
        directMessageRepository.saveAndFlush(directMessage);

        // Get all the directMessageList where message in DEFAULT_MESSAGE or UPDATED_MESSAGE
        defaultDirectMessageShouldBeFound("message.in=" + DEFAULT_MESSAGE + "," + UPDATED_MESSAGE);

        // Get all the directMessageList where message equals to UPDATED_MESSAGE
        defaultDirectMessageShouldNotBeFound("message.in=" + UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    public void getAllDirectMessagesByMessageIsNullOrNotNull() throws Exception {
        // Initialize the database
        directMessageRepository.saveAndFlush(directMessage);

        // Get all the directMessageList where message is not null
        defaultDirectMessageShouldBeFound("message.specified=true");

        // Get all the directMessageList where message is null
        defaultDirectMessageShouldNotBeFound("message.specified=false");
    }

    @Test
    @Transactional
    public void getAllDirectMessagesByUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        directMessageRepository.saveAndFlush(directMessage);

        // Get all the directMessageList where url equals to DEFAULT_URL
        defaultDirectMessageShouldBeFound("url.equals=" + DEFAULT_URL);

        // Get all the directMessageList where url equals to UPDATED_URL
        defaultDirectMessageShouldNotBeFound("url.equals=" + UPDATED_URL);
    }

    @Test
    @Transactional
    public void getAllDirectMessagesByUrlIsInShouldWork() throws Exception {
        // Initialize the database
        directMessageRepository.saveAndFlush(directMessage);

        // Get all the directMessageList where url in DEFAULT_URL or UPDATED_URL
        defaultDirectMessageShouldBeFound("url.in=" + DEFAULT_URL + "," + UPDATED_URL);

        // Get all the directMessageList where url equals to UPDATED_URL
        defaultDirectMessageShouldNotBeFound("url.in=" + UPDATED_URL);
    }

    @Test
    @Transactional
    public void getAllDirectMessagesByUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        directMessageRepository.saveAndFlush(directMessage);

        // Get all the directMessageList where url is not null
        defaultDirectMessageShouldBeFound("url.specified=true");

        // Get all the directMessageList where url is null
        defaultDirectMessageShouldNotBeFound("url.specified=false");
    }

    @Test
    @Transactional
    public void getAllDirectMessagesBySenderIsEqualToSomething() throws Exception {
        // Initialize the database
        Profile sender = ProfileResourceIntTest.createEntity(em);
        em.persist(sender);
        em.flush();
        directMessage.setSender(sender);
        directMessageRepository.saveAndFlush(directMessage);
        Long senderId = sender.getId();

        // Get all the directMessageList where sender equals to senderId
        defaultDirectMessageShouldBeFound("senderId.equals=" + senderId);

        // Get all the directMessageList where sender equals to senderId + 1
        defaultDirectMessageShouldNotBeFound("senderId.equals=" + (senderId + 1));
    }


    @Test
    @Transactional
    public void getAllDirectMessagesByRecipientIsEqualToSomething() throws Exception {
        // Initialize the database
        Profile recipient = ProfileResourceIntTest.createEntity(em);
        em.persist(recipient);
        em.flush();
        directMessage.setRecipient(recipient);
        directMessageRepository.saveAndFlush(directMessage);
        Long recipientId = recipient.getId();

        // Get all the directMessageList where recipient equals to recipientId
        defaultDirectMessageShouldBeFound("recipientId.equals=" + recipientId);

        // Get all the directMessageList where recipient equals to recipientId + 1
        defaultDirectMessageShouldNotBeFound("recipientId.equals=" + (recipientId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultDirectMessageShouldBeFound(String filter) throws Exception {
        restDirectMessageMockMvc.perform(get("/api/direct-messages?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(directMessage.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE)))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].pictureContentType").value(hasItem(DEFAULT_PICTURE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].picture").value(hasItem(Base64Utils.encodeToString(DEFAULT_PICTURE))));

        // Check, that the count call also returns 1
        restDirectMessageMockMvc.perform(get("/api/direct-messages/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultDirectMessageShouldNotBeFound(String filter) throws Exception {
        restDirectMessageMockMvc.perform(get("/api/direct-messages?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDirectMessageMockMvc.perform(get("/api/direct-messages/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingDirectMessage() throws Exception {
        // Get the directMessage
        restDirectMessageMockMvc.perform(get("/api/direct-messages/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDirectMessage() throws Exception {
        // Initialize the database
        directMessageService.save(directMessage);

        int databaseSizeBeforeUpdate = directMessageRepository.findAll().size();

        // Update the directMessage
        DirectMessage updatedDirectMessage = directMessageRepository.findById(directMessage.getId()).get();
        // Disconnect from session so that the updates on updatedDirectMessage are not directly saved in db
        em.detach(updatedDirectMessage);
        updatedDirectMessage
            .createdDate(UPDATED_CREATED_DATE)
            .message(UPDATED_MESSAGE)
            .url(UPDATED_URL)
            .picture(UPDATED_PICTURE)
            .pictureContentType(UPDATED_PICTURE_CONTENT_TYPE);

        restDirectMessageMockMvc.perform(put("/api/direct-messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDirectMessage)))
            .andExpect(status().isOk());

        // Validate the DirectMessage in the database
        List<DirectMessage> directMessageList = directMessageRepository.findAll();
        assertThat(directMessageList).hasSize(databaseSizeBeforeUpdate);
        DirectMessage testDirectMessage = directMessageList.get(directMessageList.size() - 1);
        assertThat(testDirectMessage.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testDirectMessage.getMessage()).isEqualTo(UPDATED_MESSAGE);
        assertThat(testDirectMessage.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testDirectMessage.getPicture()).isEqualTo(UPDATED_PICTURE);
        assertThat(testDirectMessage.getPictureContentType()).isEqualTo(UPDATED_PICTURE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingDirectMessage() throws Exception {
        int databaseSizeBeforeUpdate = directMessageRepository.findAll().size();

        // Create the DirectMessage

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDirectMessageMockMvc.perform(put("/api/direct-messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(directMessage)))
            .andExpect(status().isBadRequest());

        // Validate the DirectMessage in the database
        List<DirectMessage> directMessageList = directMessageRepository.findAll();
        assertThat(directMessageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteDirectMessage() throws Exception {
        // Initialize the database
        directMessageService.save(directMessage);

        int databaseSizeBeforeDelete = directMessageRepository.findAll().size();

        // Delete the directMessage
        restDirectMessageMockMvc.perform(delete("/api/direct-messages/{id}", directMessage.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<DirectMessage> directMessageList = directMessageRepository.findAll();
        assertThat(directMessageList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DirectMessage.class);
        DirectMessage directMessage1 = new DirectMessage();
        directMessage1.setId(1L);
        DirectMessage directMessage2 = new DirectMessage();
        directMessage2.setId(directMessage1.getId());
        assertThat(directMessage1).isEqualTo(directMessage2);
        directMessage2.setId(2L);
        assertThat(directMessage1).isNotEqualTo(directMessage2);
        directMessage1.setId(null);
        assertThat(directMessage1).isNotEqualTo(directMessage2);
    }
}
