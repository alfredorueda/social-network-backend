package edu.socialnetwork.web.rest;

import edu.socialnetwork.SocialNetworkBackendApp;

import edu.socialnetwork.domain.Chatroom;
import edu.socialnetwork.domain.Profile;
import edu.socialnetwork.domain.Message;
import edu.socialnetwork.repository.ChatroomRepository;
import edu.socialnetwork.service.ChatroomService;
import edu.socialnetwork.web.rest.errors.ExceptionTranslator;
import edu.socialnetwork.service.dto.ChatroomCriteria;
import edu.socialnetwork.service.ChatroomQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ChatroomResource REST controller.
 *
 * @see ChatroomResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SocialNetworkBackendApp.class)
public class ChatroomResourceIntTest {

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_TOPIC = "AAAAAAAAAA";
    private static final String UPDATED_TOPIC = "BBBBBBBBBB";

    @Autowired
    private ChatroomRepository chatroomRepository;

    @Mock
    private ChatroomRepository chatroomRepositoryMock;

    @Mock
    private ChatroomService chatroomServiceMock;

    @Autowired
    private ChatroomService chatroomService;

    @Autowired
    private ChatroomQueryService chatroomQueryService;

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

    private MockMvc restChatroomMockMvc;

    private Chatroom chatroom;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ChatroomResource chatroomResource = new ChatroomResource(chatroomService, chatroomQueryService);
        this.restChatroomMockMvc = MockMvcBuilders.standaloneSetup(chatroomResource)
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
    public static Chatroom createEntity(EntityManager em) {
        Chatroom chatroom = new Chatroom()
            .createdDate(DEFAULT_CREATED_DATE)
            .topic(DEFAULT_TOPIC);
        return chatroom;
    }

    @Before
    public void initTest() {
        chatroom = createEntity(em);
    }

    @Test
    @Transactional
    public void createChatroom() throws Exception {
        int databaseSizeBeforeCreate = chatroomRepository.findAll().size();

        // Create the Chatroom
        restChatroomMockMvc.perform(post("/api/chatrooms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chatroom)))
            .andExpect(status().isCreated());

        // Validate the Chatroom in the database
        List<Chatroom> chatroomList = chatroomRepository.findAll();
        assertThat(chatroomList).hasSize(databaseSizeBeforeCreate + 1);
        Chatroom testChatroom = chatroomList.get(chatroomList.size() - 1);
        assertThat(testChatroom.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testChatroom.getTopic()).isEqualTo(DEFAULT_TOPIC);
    }

    @Test
    @Transactional
    public void createChatroomWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = chatroomRepository.findAll().size();

        // Create the Chatroom with an existing ID
        chatroom.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restChatroomMockMvc.perform(post("/api/chatrooms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chatroom)))
            .andExpect(status().isBadRequest());

        // Validate the Chatroom in the database
        List<Chatroom> chatroomList = chatroomRepository.findAll();
        assertThat(chatroomList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = chatroomRepository.findAll().size();
        // set the field null
        chatroom.setCreatedDate(null);

        // Create the Chatroom, which fails.

        restChatroomMockMvc.perform(post("/api/chatrooms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chatroom)))
            .andExpect(status().isBadRequest());

        List<Chatroom> chatroomList = chatroomRepository.findAll();
        assertThat(chatroomList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTopicIsRequired() throws Exception {
        int databaseSizeBeforeTest = chatroomRepository.findAll().size();
        // set the field null
        chatroom.setTopic(null);

        // Create the Chatroom, which fails.

        restChatroomMockMvc.perform(post("/api/chatrooms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chatroom)))
            .andExpect(status().isBadRequest());

        List<Chatroom> chatroomList = chatroomRepository.findAll();
        assertThat(chatroomList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllChatrooms() throws Exception {
        // Initialize the database
        chatroomRepository.saveAndFlush(chatroom);

        // Get all the chatroomList
        restChatroomMockMvc.perform(get("/api/chatrooms?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(chatroom.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].topic").value(hasItem(DEFAULT_TOPIC.toString())));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllChatroomsWithEagerRelationshipsIsEnabled() throws Exception {
        ChatroomResource chatroomResource = new ChatroomResource(chatroomServiceMock, chatroomQueryService);
        when(chatroomServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restChatroomMockMvc = MockMvcBuilders.standaloneSetup(chatroomResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restChatroomMockMvc.perform(get("/api/chatrooms?eagerload=true"))
        .andExpect(status().isOk());

        verify(chatroomServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllChatroomsWithEagerRelationshipsIsNotEnabled() throws Exception {
        ChatroomResource chatroomResource = new ChatroomResource(chatroomServiceMock, chatroomQueryService);
            when(chatroomServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restChatroomMockMvc = MockMvcBuilders.standaloneSetup(chatroomResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restChatroomMockMvc.perform(get("/api/chatrooms?eagerload=true"))
        .andExpect(status().isOk());

            verify(chatroomServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getChatroom() throws Exception {
        // Initialize the database
        chatroomRepository.saveAndFlush(chatroom);

        // Get the chatroom
        restChatroomMockMvc.perform(get("/api/chatrooms/{id}", chatroom.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(chatroom.getId().intValue()))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
            .andExpect(jsonPath("$.topic").value(DEFAULT_TOPIC.toString()));
    }

    @Test
    @Transactional
    public void getAllChatroomsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        chatroomRepository.saveAndFlush(chatroom);

        // Get all the chatroomList where createdDate equals to DEFAULT_CREATED_DATE
        defaultChatroomShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);

        // Get all the chatroomList where createdDate equals to UPDATED_CREATED_DATE
        defaultChatroomShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllChatroomsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        chatroomRepository.saveAndFlush(chatroom);

        // Get all the chatroomList where createdDate in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
        defaultChatroomShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);

        // Get all the chatroomList where createdDate equals to UPDATED_CREATED_DATE
        defaultChatroomShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllChatroomsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        chatroomRepository.saveAndFlush(chatroom);

        // Get all the chatroomList where createdDate is not null
        defaultChatroomShouldBeFound("createdDate.specified=true");

        // Get all the chatroomList where createdDate is null
        defaultChatroomShouldNotBeFound("createdDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllChatroomsByCreatedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        chatroomRepository.saveAndFlush(chatroom);

        // Get all the chatroomList where createdDate greater than or equals to DEFAULT_CREATED_DATE
        defaultChatroomShouldBeFound("createdDate.greaterOrEqualThan=" + DEFAULT_CREATED_DATE);

        // Get all the chatroomList where createdDate greater than or equals to UPDATED_CREATED_DATE
        defaultChatroomShouldNotBeFound("createdDate.greaterOrEqualThan=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllChatroomsByCreatedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        chatroomRepository.saveAndFlush(chatroom);

        // Get all the chatroomList where createdDate less than or equals to DEFAULT_CREATED_DATE
        defaultChatroomShouldNotBeFound("createdDate.lessThan=" + DEFAULT_CREATED_DATE);

        // Get all the chatroomList where createdDate less than or equals to UPDATED_CREATED_DATE
        defaultChatroomShouldBeFound("createdDate.lessThan=" + UPDATED_CREATED_DATE);
    }


    @Test
    @Transactional
    public void getAllChatroomsByTopicIsEqualToSomething() throws Exception {
        // Initialize the database
        chatroomRepository.saveAndFlush(chatroom);

        // Get all the chatroomList where topic equals to DEFAULT_TOPIC
        defaultChatroomShouldBeFound("topic.equals=" + DEFAULT_TOPIC);

        // Get all the chatroomList where topic equals to UPDATED_TOPIC
        defaultChatroomShouldNotBeFound("topic.equals=" + UPDATED_TOPIC);
    }

    @Test
    @Transactional
    public void getAllChatroomsByTopicIsInShouldWork() throws Exception {
        // Initialize the database
        chatroomRepository.saveAndFlush(chatroom);

        // Get all the chatroomList where topic in DEFAULT_TOPIC or UPDATED_TOPIC
        defaultChatroomShouldBeFound("topic.in=" + DEFAULT_TOPIC + "," + UPDATED_TOPIC);

        // Get all the chatroomList where topic equals to UPDATED_TOPIC
        defaultChatroomShouldNotBeFound("topic.in=" + UPDATED_TOPIC);
    }

    @Test
    @Transactional
    public void getAllChatroomsByTopicIsNullOrNotNull() throws Exception {
        // Initialize the database
        chatroomRepository.saveAndFlush(chatroom);

        // Get all the chatroomList where topic is not null
        defaultChatroomShouldBeFound("topic.specified=true");

        // Get all the chatroomList where topic is null
        defaultChatroomShouldNotBeFound("topic.specified=false");
    }

    @Test
    @Transactional
    public void getAllChatroomsByAdminIsEqualToSomething() throws Exception {
        // Initialize the database
        Profile admin = ProfileResourceIntTest.createEntity(em);
        em.persist(admin);
        em.flush();
        chatroom.setAdmin(admin);
        chatroomRepository.saveAndFlush(chatroom);
        Long adminId = admin.getId();

        // Get all the chatroomList where admin equals to adminId
        defaultChatroomShouldBeFound("adminId.equals=" + adminId);

        // Get all the chatroomList where admin equals to adminId + 1
        defaultChatroomShouldNotBeFound("adminId.equals=" + (adminId + 1));
    }


    @Test
    @Transactional
    public void getAllChatroomsByParticipantIsEqualToSomething() throws Exception {
        // Initialize the database
        Profile participant = ProfileResourceIntTest.createEntity(em);
        em.persist(participant);
        em.flush();
        chatroom.addParticipant(participant);
        chatroomRepository.saveAndFlush(chatroom);
        Long participantId = participant.getId();

        // Get all the chatroomList where participant equals to participantId
        defaultChatroomShouldBeFound("participantId.equals=" + participantId);

        // Get all the chatroomList where participant equals to participantId + 1
        defaultChatroomShouldNotBeFound("participantId.equals=" + (participantId + 1));
    }


    @Test
    @Transactional
    public void getAllChatroomsByMessageIsEqualToSomething() throws Exception {
        // Initialize the database
        Message message = MessageResourceIntTest.createEntity(em);
        em.persist(message);
        em.flush();
        chatroom.addMessage(message);
        chatroomRepository.saveAndFlush(chatroom);
        Long messageId = message.getId();

        // Get all the chatroomList where message equals to messageId
        defaultChatroomShouldBeFound("messageId.equals=" + messageId);

        // Get all the chatroomList where message equals to messageId + 1
        defaultChatroomShouldNotBeFound("messageId.equals=" + (messageId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultChatroomShouldBeFound(String filter) throws Exception {
        restChatroomMockMvc.perform(get("/api/chatrooms?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(chatroom.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].topic").value(hasItem(DEFAULT_TOPIC)));

        // Check, that the count call also returns 1
        restChatroomMockMvc.perform(get("/api/chatrooms/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultChatroomShouldNotBeFound(String filter) throws Exception {
        restChatroomMockMvc.perform(get("/api/chatrooms?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restChatroomMockMvc.perform(get("/api/chatrooms/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingChatroom() throws Exception {
        // Get the chatroom
        restChatroomMockMvc.perform(get("/api/chatrooms/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateChatroom() throws Exception {
        // Initialize the database
        chatroomService.save(chatroom);

        int databaseSizeBeforeUpdate = chatroomRepository.findAll().size();

        // Update the chatroom
        Chatroom updatedChatroom = chatroomRepository.findById(chatroom.getId()).get();
        // Disconnect from session so that the updates on updatedChatroom are not directly saved in db
        em.detach(updatedChatroom);
        updatedChatroom
            .createdDate(UPDATED_CREATED_DATE)
            .topic(UPDATED_TOPIC);

        restChatroomMockMvc.perform(put("/api/chatrooms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedChatroom)))
            .andExpect(status().isOk());

        // Validate the Chatroom in the database
        List<Chatroom> chatroomList = chatroomRepository.findAll();
        assertThat(chatroomList).hasSize(databaseSizeBeforeUpdate);
        Chatroom testChatroom = chatroomList.get(chatroomList.size() - 1);
        assertThat(testChatroom.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testChatroom.getTopic()).isEqualTo(UPDATED_TOPIC);
    }

    @Test
    @Transactional
    public void updateNonExistingChatroom() throws Exception {
        int databaseSizeBeforeUpdate = chatroomRepository.findAll().size();

        // Create the Chatroom

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChatroomMockMvc.perform(put("/api/chatrooms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chatroom)))
            .andExpect(status().isBadRequest());

        // Validate the Chatroom in the database
        List<Chatroom> chatroomList = chatroomRepository.findAll();
        assertThat(chatroomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteChatroom() throws Exception {
        // Initialize the database
        chatroomService.save(chatroom);

        int databaseSizeBeforeDelete = chatroomRepository.findAll().size();

        // Delete the chatroom
        restChatroomMockMvc.perform(delete("/api/chatrooms/{id}", chatroom.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Chatroom> chatroomList = chatroomRepository.findAll();
        assertThat(chatroomList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Chatroom.class);
        Chatroom chatroom1 = new Chatroom();
        chatroom1.setId(1L);
        Chatroom chatroom2 = new Chatroom();
        chatroom2.setId(chatroom1.getId());
        assertThat(chatroom1).isEqualTo(chatroom2);
        chatroom2.setId(2L);
        assertThat(chatroom1).isNotEqualTo(chatroom2);
        chatroom1.setId(null);
        assertThat(chatroom1).isNotEqualTo(chatroom2);
    }
}
