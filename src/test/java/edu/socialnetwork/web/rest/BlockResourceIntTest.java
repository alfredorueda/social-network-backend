package edu.socialnetwork.web.rest;

import edu.socialnetwork.SocialNetworkBackendApp;

import edu.socialnetwork.domain.Block;
import edu.socialnetwork.domain.Profile;
import edu.socialnetwork.repository.BlockRepository;
import edu.socialnetwork.service.BlockService;
import edu.socialnetwork.web.rest.errors.ExceptionTranslator;
import edu.socialnetwork.service.dto.BlockCriteria;
import edu.socialnetwork.service.BlockQueryService;

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
import java.util.List;


import static edu.socialnetwork.web.rest.TestUtil.sameInstant;
import static edu.socialnetwork.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the BlockResource REST controller.
 *
 * @see BlockResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SocialNetworkBackendApp.class)
public class BlockResourceIntTest {

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private BlockRepository blockRepository;

    @Autowired
    private BlockService blockService;

    @Autowired
    private BlockQueryService blockQueryService;

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

    private MockMvc restBlockMockMvc;

    private Block block;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BlockResource blockResource = new BlockResource(blockService, blockQueryService);
        this.restBlockMockMvc = MockMvcBuilders.standaloneSetup(blockResource)
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
    public static Block createEntity(EntityManager em) {
        Block block = new Block()
            .createdDate(DEFAULT_CREATED_DATE);
        return block;
    }

    @Before
    public void initTest() {
        block = createEntity(em);
    }

    @Test
    @Transactional
    public void createBlock() throws Exception {
        int databaseSizeBeforeCreate = blockRepository.findAll().size();

        // Create the Block
        restBlockMockMvc.perform(post("/api/blocks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(block)))
            .andExpect(status().isCreated());

        // Validate the Block in the database
        List<Block> blockList = blockRepository.findAll();
        assertThat(blockList).hasSize(databaseSizeBeforeCreate + 1);
        Block testBlock = blockList.get(blockList.size() - 1);
        assertThat(testBlock.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
    }

    @Test
    @Transactional
    public void createBlockWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = blockRepository.findAll().size();

        // Create the Block with an existing ID
        block.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBlockMockMvc.perform(post("/api/blocks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(block)))
            .andExpect(status().isBadRequest());

        // Validate the Block in the database
        List<Block> blockList = blockRepository.findAll();
        assertThat(blockList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = blockRepository.findAll().size();
        // set the field null
        block.setCreatedDate(null);

        // Create the Block, which fails.

        restBlockMockMvc.perform(post("/api/blocks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(block)))
            .andExpect(status().isBadRequest());

        List<Block> blockList = blockRepository.findAll();
        assertThat(blockList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBlocks() throws Exception {
        // Initialize the database
        blockRepository.saveAndFlush(block);

        // Get all the blockList
        restBlockMockMvc.perform(get("/api/blocks?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(block.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))));
    }
    
    @Test
    @Transactional
    public void getBlock() throws Exception {
        // Initialize the database
        blockRepository.saveAndFlush(block);

        // Get the block
        restBlockMockMvc.perform(get("/api/blocks/{id}", block.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(block.getId().intValue()))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)));
    }

    @Test
    @Transactional
    public void getAllBlocksByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        blockRepository.saveAndFlush(block);

        // Get all the blockList where createdDate equals to DEFAULT_CREATED_DATE
        defaultBlockShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);

        // Get all the blockList where createdDate equals to UPDATED_CREATED_DATE
        defaultBlockShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllBlocksByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        blockRepository.saveAndFlush(block);

        // Get all the blockList where createdDate in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
        defaultBlockShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);

        // Get all the blockList where createdDate equals to UPDATED_CREATED_DATE
        defaultBlockShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllBlocksByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        blockRepository.saveAndFlush(block);

        // Get all the blockList where createdDate is not null
        defaultBlockShouldBeFound("createdDate.specified=true");

        // Get all the blockList where createdDate is null
        defaultBlockShouldNotBeFound("createdDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllBlocksByCreatedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        blockRepository.saveAndFlush(block);

        // Get all the blockList where createdDate greater than or equals to DEFAULT_CREATED_DATE
        defaultBlockShouldBeFound("createdDate.greaterOrEqualThan=" + DEFAULT_CREATED_DATE);

        // Get all the blockList where createdDate greater than or equals to UPDATED_CREATED_DATE
        defaultBlockShouldNotBeFound("createdDate.greaterOrEqualThan=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllBlocksByCreatedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        blockRepository.saveAndFlush(block);

        // Get all the blockList where createdDate less than or equals to DEFAULT_CREATED_DATE
        defaultBlockShouldNotBeFound("createdDate.lessThan=" + DEFAULT_CREATED_DATE);

        // Get all the blockList where createdDate less than or equals to UPDATED_CREATED_DATE
        defaultBlockShouldBeFound("createdDate.lessThan=" + UPDATED_CREATED_DATE);
    }


    @Test
    @Transactional
    public void getAllBlocksBySentIsEqualToSomething() throws Exception {
        // Initialize the database
        Profile sent = ProfileResourceIntTest.createEntity(em);
        em.persist(sent);
        em.flush();
        block.setSent(sent);
        blockRepository.saveAndFlush(block);
        Long sentId = sent.getId();

        // Get all the blockList where sent equals to sentId
        defaultBlockShouldBeFound("sentId.equals=" + sentId);

        // Get all the blockList where sent equals to sentId + 1
        defaultBlockShouldNotBeFound("sentId.equals=" + (sentId + 1));
    }


    @Test
    @Transactional
    public void getAllBlocksByReceivedIsEqualToSomething() throws Exception {
        // Initialize the database
        Profile received = ProfileResourceIntTest.createEntity(em);
        em.persist(received);
        em.flush();
        block.setReceived(received);
        blockRepository.saveAndFlush(block);
        Long receivedId = received.getId();

        // Get all the blockList where received equals to receivedId
        defaultBlockShouldBeFound("receivedId.equals=" + receivedId);

        // Get all the blockList where received equals to receivedId + 1
        defaultBlockShouldNotBeFound("receivedId.equals=" + (receivedId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultBlockShouldBeFound(String filter) throws Exception {
        restBlockMockMvc.perform(get("/api/blocks?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(block.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))));

        // Check, that the count call also returns 1
        restBlockMockMvc.perform(get("/api/blocks/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultBlockShouldNotBeFound(String filter) throws Exception {
        restBlockMockMvc.perform(get("/api/blocks?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBlockMockMvc.perform(get("/api/blocks/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingBlock() throws Exception {
        // Get the block
        restBlockMockMvc.perform(get("/api/blocks/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBlock() throws Exception {
        // Initialize the database
        blockService.save(block);

        int databaseSizeBeforeUpdate = blockRepository.findAll().size();

        // Update the block
        Block updatedBlock = blockRepository.findById(block.getId()).get();
        // Disconnect from session so that the updates on updatedBlock are not directly saved in db
        em.detach(updatedBlock);
        updatedBlock
            .createdDate(UPDATED_CREATED_DATE);

        restBlockMockMvc.perform(put("/api/blocks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedBlock)))
            .andExpect(status().isOk());

        // Validate the Block in the database
        List<Block> blockList = blockRepository.findAll();
        assertThat(blockList).hasSize(databaseSizeBeforeUpdate);
        Block testBlock = blockList.get(blockList.size() - 1);
        assertThat(testBlock.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingBlock() throws Exception {
        int databaseSizeBeforeUpdate = blockRepository.findAll().size();

        // Create the Block

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBlockMockMvc.perform(put("/api/blocks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(block)))
            .andExpect(status().isBadRequest());

        // Validate the Block in the database
        List<Block> blockList = blockRepository.findAll();
        assertThat(blockList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteBlock() throws Exception {
        // Initialize the database
        blockService.save(block);

        int databaseSizeBeforeDelete = blockRepository.findAll().size();

        // Delete the block
        restBlockMockMvc.perform(delete("/api/blocks/{id}", block.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Block> blockList = blockRepository.findAll();
        assertThat(blockList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Block.class);
        Block block1 = new Block();
        block1.setId(1L);
        Block block2 = new Block();
        block2.setId(block1.getId());
        assertThat(block1).isEqualTo(block2);
        block2.setId(2L);
        assertThat(block1).isNotEqualTo(block2);
        block1.setId(null);
        assertThat(block1).isNotEqualTo(block2);
    }
}
