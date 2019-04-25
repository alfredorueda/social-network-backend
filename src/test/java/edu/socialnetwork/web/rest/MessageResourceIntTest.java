package edu.socialnetwork.web.rest;

import edu.socialnetwork.SocialNetworkBackendApp;

import edu.socialnetwork.domain.Message;
import edu.socialnetwork.domain.Location;
import edu.socialnetwork.domain.Profile;
import edu.socialnetwork.domain.Chatroom;
import edu.socialnetwork.repository.MessageRepository;
import edu.socialnetwork.service.MessageService;
import edu.socialnetwork.web.rest.errors.ExceptionTranslator;
import edu.socialnetwork.service.dto.MessageCriteria;
import edu.socialnetwork.service.MessageQueryService;

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
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;


import static edu.socialnetwork.web.rest.TestUtil.sameInstant;
import static edu.socialnetwork.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the MessageResource REST controller.
 *
 * @see MessageResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SocialNetworkBackendApp.class)
public class MessageResourceIntTest {

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
    private MessageRepository messageRepository;

    @Autowired
    private MessageService messageService;

    @Autowired
    private MessageQueryService messageQueryService;

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

    private MockMvc restMessageMockMvc;

    private Message message;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MessageResource messageResource = new MessageResource(messageService, messageQueryService);
        this.restMessageMockMvc = MockMvcBuilders.standaloneSetup(messageResource)
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
    public static Message createEntity(EntityManager em) {
        Message message = new Message()
            .createdDate(DEFAULT_CREATED_DATE)
            .message(DEFAULT_MESSAGE)
            .url(DEFAULT_URL)
            .picture(DEFAULT_PICTURE)
            .pictureContentType(DEFAULT_PICTURE_CONTENT_TYPE);
        return message;
    }

    @Before
    public void initTest() {
        message = createEntity(em);
    }

    @Test
    @Transactional
    public void createMessage() throws Exception {
        int databaseSizeBeforeCreate = messageRepository.findAll().size();

        // Create the Message
        restMessageMockMvc.perform(post("/api/messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(message)))
            .andExpect(status().isCreated());

        // Validate the Message in the database
        List<Message> messageList = messageRepository.findAll();
        assertThat(messageList).hasSize(databaseSizeBeforeCreate + 1);
        Message testMessage = messageList.get(messageList.size() - 1);
        assertThat(testMessage.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testMessage.getMessage()).isEqualTo(DEFAULT_MESSAGE);
        assertThat(testMessage.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testMessage.getPicture()).isEqualTo(DEFAULT_PICTURE);
        assertThat(testMessage.getPictureContentType()).isEqualTo(DEFAULT_PICTURE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void createMessageWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = messageRepository.findAll().size();

        // Create the Message with an existing ID
        message.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMessageMockMvc.perform(post("/api/messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(message)))
            .andExpect(status().isBadRequest());

        // Validate the Message in the database
        List<Message> messageList = messageRepository.findAll();
        assertThat(messageList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = messageRepository.findAll().size();
        // set the field null
        message.setCreatedDate(null);

        // Create the Message, which fails.

        restMessageMockMvc.perform(post("/api/messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(message)))
            .andExpect(status().isBadRequest());

        List<Message> messageList = messageRepository.findAll();
        assertThat(messageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMessages() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messageList
        restMessageMockMvc.perform(get("/api/messages?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(message.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE.toString())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())))
            .andExpect(jsonPath("$.[*].pictureContentType").value(hasItem(DEFAULT_PICTURE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].picture").value(hasItem(Base64Utils.encodeToString(DEFAULT_PICTURE))));
    }
    
    @Test
    @Transactional
    public void getMessage() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get the message
        restMessageMockMvc.perform(get("/api/messages/{id}", message.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(message.getId().intValue()))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
            .andExpect(jsonPath("$.message").value(DEFAULT_MESSAGE.toString()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL.toString()))
            .andExpect(jsonPath("$.pictureContentType").value(DEFAULT_PICTURE_CONTENT_TYPE))
            .andExpect(jsonPath("$.picture").value(Base64Utils.encodeToString(DEFAULT_PICTURE)));
    }

    @Test
    @Transactional
    public void getAllMessagesByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messageList where createdDate equals to DEFAULT_CREATED_DATE
        defaultMessageShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);

        // Get all the messageList where createdDate equals to UPDATED_CREATED_DATE
        defaultMessageShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllMessagesByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messageList where createdDate in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
        defaultMessageShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);

        // Get all the messageList where createdDate equals to UPDATED_CREATED_DATE
        defaultMessageShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllMessagesByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messageList where createdDate is not null
        defaultMessageShouldBeFound("createdDate.specified=true");

        // Get all the messageList where createdDate is null
        defaultMessageShouldNotBeFound("createdDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllMessagesByCreatedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messageList where createdDate greater than or equals to DEFAULT_CREATED_DATE
        defaultMessageShouldBeFound("createdDate.greaterOrEqualThan=" + DEFAULT_CREATED_DATE);

        // Get all the messageList where createdDate greater than or equals to UPDATED_CREATED_DATE
        defaultMessageShouldNotBeFound("createdDate.greaterOrEqualThan=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllMessagesByCreatedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messageList where createdDate less than or equals to DEFAULT_CREATED_DATE
        defaultMessageShouldNotBeFound("createdDate.lessThan=" + DEFAULT_CREATED_DATE);

        // Get all the messageList where createdDate less than or equals to UPDATED_CREATED_DATE
        defaultMessageShouldBeFound("createdDate.lessThan=" + UPDATED_CREATED_DATE);
    }


    @Test
    @Transactional
    public void getAllMessagesByMessageIsEqualToSomething() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messageList where message equals to DEFAULT_MESSAGE
        defaultMessageShouldBeFound("message.equals=" + DEFAULT_MESSAGE);

        // Get all the messageList where message equals to UPDATED_MESSAGE
        defaultMessageShouldNotBeFound("message.equals=" + UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    public void getAllMessagesByMessageIsInShouldWork() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messageList where message in DEFAULT_MESSAGE or UPDATED_MESSAGE
        defaultMessageShouldBeFound("message.in=" + DEFAULT_MESSAGE + "," + UPDATED_MESSAGE);

        // Get all the messageList where message equals to UPDATED_MESSAGE
        defaultMessageShouldNotBeFound("message.in=" + UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    public void getAllMessagesByMessageIsNullOrNotNull() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messageList where message is not null
        defaultMessageShouldBeFound("message.specified=true");

        // Get all the messageList where message is null
        defaultMessageShouldNotBeFound("message.specified=false");
    }

    @Test
    @Transactional
    public void getAllMessagesByUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messageList where url equals to DEFAULT_URL
        defaultMessageShouldBeFound("url.equals=" + DEFAULT_URL);

        // Get all the messageList where url equals to UPDATED_URL
        defaultMessageShouldNotBeFound("url.equals=" + UPDATED_URL);
    }

    @Test
    @Transactional
    public void getAllMessagesByUrlIsInShouldWork() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messageList where url in DEFAULT_URL or UPDATED_URL
        defaultMessageShouldBeFound("url.in=" + DEFAULT_URL + "," + UPDATED_URL);

        // Get all the messageList where url equals to UPDATED_URL
        defaultMessageShouldNotBeFound("url.in=" + UPDATED_URL);
    }

    @Test
    @Transactional
    public void getAllMessagesByUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messageList where url is not null
        defaultMessageShouldBeFound("url.specified=true");

        // Get all the messageList where url is null
        defaultMessageShouldNotBeFound("url.specified=false");
    }

    @Test
    @Transactional
    public void getAllMessagesByLocationIsEqualToSomething() throws Exception {
        // Initialize the database
        Location location = LocationResourceIntTest.createEntity(em);
        em.persist(location);
        em.flush();
        message.setLocation(location);
        messageRepository.saveAndFlush(message);
        Long locationId = location.getId();

        // Get all the messageList where location equals to locationId
        defaultMessageShouldBeFound("locationId.equals=" + locationId);

        // Get all the messageList where location equals to locationId + 1
        defaultMessageShouldNotBeFound("locationId.equals=" + (locationId + 1));
    }


    @Test
    @Transactional
    public void getAllMessagesBySenderIsEqualToSomething() throws Exception {
        // Initialize the database
        Profile sender = ProfileResourceIntTest.createEntity(em);
        em.persist(sender);
        em.flush();
        message.setSender(sender);
        messageRepository.saveAndFlush(message);
        Long senderId = sender.getId();

        // Get all the messageList where sender equals to senderId
        defaultMessageShouldBeFound("senderId.equals=" + senderId);

        // Get all the messageList where sender equals to senderId + 1
        defaultMessageShouldNotBeFound("senderId.equals=" + (senderId + 1));
    }


    @Test
    @Transactional
    public void getAllMessagesByChatroomIsEqualToSomething() throws Exception {
        // Initialize the database
        Chatroom chatroom = ChatroomResourceIntTest.createEntity(em);
        em.persist(chatroom);
        em.flush();
        message.setChatroom(chatroom);
        messageRepository.saveAndFlush(message);
        Long chatroomId = chatroom.getId();

        // Get all the messageList where chatroom equals to chatroomId
        defaultMessageShouldBeFound("chatroomId.equals=" + chatroomId);

        // Get all the messageList where chatroom equals to chatroomId + 1
        defaultMessageShouldNotBeFound("chatroomId.equals=" + (chatroomId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultMessageShouldBeFound(String filter) throws Exception {
        restMessageMockMvc.perform(get("/api/messages?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(message.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE)))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].pictureContentType").value(hasItem(DEFAULT_PICTURE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].picture").value(hasItem(Base64Utils.encodeToString(DEFAULT_PICTURE))));

        // Check, that the count call also returns 1
        restMessageMockMvc.perform(get("/api/messages/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultMessageShouldNotBeFound(String filter) throws Exception {
        restMessageMockMvc.perform(get("/api/messages?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMessageMockMvc.perform(get("/api/messages/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingMessage() throws Exception {
        // Get the message
        restMessageMockMvc.perform(get("/api/messages/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMessage() throws Exception {
        // Initialize the database
        messageService.save(message);

        int databaseSizeBeforeUpdate = messageRepository.findAll().size();

        // Update the message
        Message updatedMessage = messageRepository.findById(message.getId()).get();
        // Disconnect from session so that the updates on updatedMessage are not directly saved in db
        em.detach(updatedMessage);
        updatedMessage
            .createdDate(UPDATED_CREATED_DATE)
            .message(UPDATED_MESSAGE)
            .url(UPDATED_URL)
            .picture(UPDATED_PICTURE)
            .pictureContentType(UPDATED_PICTURE_CONTENT_TYPE);

        restMessageMockMvc.perform(put("/api/messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMessage)))
            .andExpect(status().isOk());

        // Validate the Message in the database
        List<Message> messageList = messageRepository.findAll();
        assertThat(messageList).hasSize(databaseSizeBeforeUpdate);
        Message testMessage = messageList.get(messageList.size() - 1);
        assertThat(testMessage.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testMessage.getMessage()).isEqualTo(UPDATED_MESSAGE);
        assertThat(testMessage.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testMessage.getPicture()).isEqualTo(UPDATED_PICTURE);
        assertThat(testMessage.getPictureContentType()).isEqualTo(UPDATED_PICTURE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingMessage() throws Exception {
        int databaseSizeBeforeUpdate = messageRepository.findAll().size();

        // Create the Message

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMessageMockMvc.perform(put("/api/messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(message)))
            .andExpect(status().isBadRequest());

        // Validate the Message in the database
        List<Message> messageList = messageRepository.findAll();
        assertThat(messageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMessage() throws Exception {
        // Initialize the database
        messageService.save(message);

        int databaseSizeBeforeDelete = messageRepository.findAll().size();

        // Delete the message
        restMessageMockMvc.perform(delete("/api/messages/{id}", message.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Message> messageList = messageRepository.findAll();
        assertThat(messageList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Message.class);
        Message message1 = new Message();
        message1.setId(1L);
        Message message2 = new Message();
        message2.setId(message1.getId());
        assertThat(message1).isEqualTo(message2);
        message2.setId(2L);
        assertThat(message1).isNotEqualTo(message2);
        message1.setId(null);
        assertThat(message1).isNotEqualTo(message2);
    }
}
