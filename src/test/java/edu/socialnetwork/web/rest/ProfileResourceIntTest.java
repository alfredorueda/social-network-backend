package edu.socialnetwork.web.rest;

import edu.socialnetwork.SocialNetworkBackendApp;

import edu.socialnetwork.domain.Profile;
import edu.socialnetwork.domain.Location;
import edu.socialnetwork.domain.User;
import edu.socialnetwork.domain.Relationship;
import edu.socialnetwork.domain.Gender;
import edu.socialnetwork.domain.Ethnicity;
import edu.socialnetwork.domain.Invitation;
import edu.socialnetwork.domain.Block;
import edu.socialnetwork.domain.Message;
import edu.socialnetwork.domain.Chatroom;
import edu.socialnetwork.repository.ProfileRepository;
import edu.socialnetwork.service.ProfileService;
import edu.socialnetwork.web.rest.errors.ExceptionTranslator;
import edu.socialnetwork.service.dto.ProfileCriteria;
import edu.socialnetwork.service.ProfileQueryService;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;


import static edu.socialnetwork.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import edu.socialnetwork.domain.enumeration.UnitSystem;
/**
 * Test class for the ProfileResource REST controller.
 *
 * @see ProfileResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SocialNetworkBackendApp.class)
public class ProfileResourceIntTest {

    private static final LocalDate DEFAULT_BIRTH_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BIRTH_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final byte[] DEFAULT_PICTURE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PICTURE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PICTURE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PICTURE_CONTENT_TYPE = "image/png";

    private static final Double DEFAULT_HEIGHT = 1D;
    private static final Double UPDATED_HEIGHT = 2D;

    private static final Double DEFAULT_WEIGHT = 1D;
    private static final Double UPDATED_WEIGHT = 2D;

    private static final UnitSystem DEFAULT_UNIT_SYSTEM = UnitSystem.IMPERIAL;
    private static final UnitSystem UPDATED_UNIT_SYSTEM = UnitSystem.METRIC;

    private static final String DEFAULT_ABOUT_ME = "AAAAAAAAAA";
    private static final String UPDATED_ABOUT_ME = "BBBBBBBBBB";

    private static final String DEFAULT_DISPLAY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DISPLAY_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_SHOW_AGE = false;
    private static final Boolean UPDATED_SHOW_AGE = true;

    private static final Boolean DEFAULT_BANNED = false;
    private static final Boolean UPDATED_BANNED = true;

    private static final String DEFAULT_FILTER_PREFERENCES = "AAAAAAAAAA";
    private static final String UPDATED_FILTER_PREFERENCES = "BBBBBBBBBB";

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private ProfileQueryService profileQueryService;

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

    private MockMvc restProfileMockMvc;

    private Profile profile;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProfileResource profileResource = new ProfileResource(profileService, profileQueryService, profileRepository);
        this.restProfileMockMvc = MockMvcBuilders.standaloneSetup(profileResource)
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
    public static Profile createEntity(EntityManager em) {
        Profile profile = new Profile()
            .birthDate(DEFAULT_BIRTH_DATE)
            .picture(DEFAULT_PICTURE)
            .pictureContentType(DEFAULT_PICTURE_CONTENT_TYPE)
            .height(DEFAULT_HEIGHT)
            .weight(DEFAULT_WEIGHT)
            .unitSystem(DEFAULT_UNIT_SYSTEM)
            .aboutMe(DEFAULT_ABOUT_ME)
            .displayName(DEFAULT_DISPLAY_NAME)
            .showAge(DEFAULT_SHOW_AGE)
            .banned(DEFAULT_BANNED)
            .filterPreferences(DEFAULT_FILTER_PREFERENCES);
        return profile;
    }

    @Before
    public void initTest() {
        profile = createEntity(em);
    }

    @Test
    @Transactional
    public void createProfile() throws Exception {
        int databaseSizeBeforeCreate = profileRepository.findAll().size();

        // Create the Profile
        restProfileMockMvc.perform(post("/api/profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(profile)))
            .andExpect(status().isCreated());

        // Validate the Profile in the database
        List<Profile> profileList = profileRepository.findAll();
        assertThat(profileList).hasSize(databaseSizeBeforeCreate + 1);
        Profile testProfile = profileList.get(profileList.size() - 1);
        assertThat(testProfile.getBirthDate()).isEqualTo(DEFAULT_BIRTH_DATE);
        assertThat(testProfile.getPicture()).isEqualTo(DEFAULT_PICTURE);
        assertThat(testProfile.getPictureContentType()).isEqualTo(DEFAULT_PICTURE_CONTENT_TYPE);
        assertThat(testProfile.getHeight()).isEqualTo(DEFAULT_HEIGHT);
        assertThat(testProfile.getWeight()).isEqualTo(DEFAULT_WEIGHT);
        assertThat(testProfile.getUnitSystem()).isEqualTo(DEFAULT_UNIT_SYSTEM);
        assertThat(testProfile.getAboutMe()).isEqualTo(DEFAULT_ABOUT_ME);
        assertThat(testProfile.getDisplayName()).isEqualTo(DEFAULT_DISPLAY_NAME);
        assertThat(testProfile.isShowAge()).isEqualTo(DEFAULT_SHOW_AGE);
        assertThat(testProfile.isBanned()).isEqualTo(DEFAULT_BANNED);
        assertThat(testProfile.getFilterPreferences()).isEqualTo(DEFAULT_FILTER_PREFERENCES);
    }

    @Test
    @Transactional
    public void createProfileWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = profileRepository.findAll().size();

        // Create the Profile with an existing ID
        profile.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProfileMockMvc.perform(post("/api/profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(profile)))
            .andExpect(status().isBadRequest());

        // Validate the Profile in the database
        List<Profile> profileList = profileRepository.findAll();
        assertThat(profileList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDisplayNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = profileRepository.findAll().size();
        // set the field null
        profile.setDisplayName(null);

        // Create the Profile, which fails.

        restProfileMockMvc.perform(post("/api/profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(profile)))
            .andExpect(status().isBadRequest());

        List<Profile> profileList = profileRepository.findAll();
        assertThat(profileList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProfiles() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList
        restProfileMockMvc.perform(get("/api/profiles?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(profile.getId().intValue())))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE.toString())))
            .andExpect(jsonPath("$.[*].pictureContentType").value(hasItem(DEFAULT_PICTURE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].picture").value(hasItem(Base64Utils.encodeToString(DEFAULT_PICTURE))))
            .andExpect(jsonPath("$.[*].height").value(hasItem(DEFAULT_HEIGHT.doubleValue())))
            .andExpect(jsonPath("$.[*].weight").value(hasItem(DEFAULT_WEIGHT.doubleValue())))
            .andExpect(jsonPath("$.[*].unitSystem").value(hasItem(DEFAULT_UNIT_SYSTEM.toString())))
            .andExpect(jsonPath("$.[*].aboutMe").value(hasItem(DEFAULT_ABOUT_ME.toString())))
            .andExpect(jsonPath("$.[*].displayName").value(hasItem(DEFAULT_DISPLAY_NAME.toString())))
            .andExpect(jsonPath("$.[*].showAge").value(hasItem(DEFAULT_SHOW_AGE.booleanValue())))
            .andExpect(jsonPath("$.[*].banned").value(hasItem(DEFAULT_BANNED.booleanValue())))
            .andExpect(jsonPath("$.[*].filterPreferences").value(hasItem(DEFAULT_FILTER_PREFERENCES.toString())));
    }
    
    @Test
    @Transactional
    public void getProfile() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get the profile
        restProfileMockMvc.perform(get("/api/profiles/{id}", profile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(profile.getId().intValue()))
            .andExpect(jsonPath("$.birthDate").value(DEFAULT_BIRTH_DATE.toString()))
            .andExpect(jsonPath("$.pictureContentType").value(DEFAULT_PICTURE_CONTENT_TYPE))
            .andExpect(jsonPath("$.picture").value(Base64Utils.encodeToString(DEFAULT_PICTURE)))
            .andExpect(jsonPath("$.height").value(DEFAULT_HEIGHT.doubleValue()))
            .andExpect(jsonPath("$.weight").value(DEFAULT_WEIGHT.doubleValue()))
            .andExpect(jsonPath("$.unitSystem").value(DEFAULT_UNIT_SYSTEM.toString()))
            .andExpect(jsonPath("$.aboutMe").value(DEFAULT_ABOUT_ME.toString()))
            .andExpect(jsonPath("$.displayName").value(DEFAULT_DISPLAY_NAME.toString()))
            .andExpect(jsonPath("$.showAge").value(DEFAULT_SHOW_AGE.booleanValue()))
            .andExpect(jsonPath("$.banned").value(DEFAULT_BANNED.booleanValue()))
            .andExpect(jsonPath("$.filterPreferences").value(DEFAULT_FILTER_PREFERENCES.toString()));
    }

    @Test
    @Transactional
    public void getAllProfilesByBirthDateIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where birthDate equals to DEFAULT_BIRTH_DATE
        defaultProfileShouldBeFound("birthDate.equals=" + DEFAULT_BIRTH_DATE);

        // Get all the profileList where birthDate equals to UPDATED_BIRTH_DATE
        defaultProfileShouldNotBeFound("birthDate.equals=" + UPDATED_BIRTH_DATE);
    }

    @Test
    @Transactional
    public void getAllProfilesByBirthDateIsInShouldWork() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where birthDate in DEFAULT_BIRTH_DATE or UPDATED_BIRTH_DATE
        defaultProfileShouldBeFound("birthDate.in=" + DEFAULT_BIRTH_DATE + "," + UPDATED_BIRTH_DATE);

        // Get all the profileList where birthDate equals to UPDATED_BIRTH_DATE
        defaultProfileShouldNotBeFound("birthDate.in=" + UPDATED_BIRTH_DATE);
    }

    @Test
    @Transactional
    public void getAllProfilesByBirthDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where birthDate is not null
        defaultProfileShouldBeFound("birthDate.specified=true");

        // Get all the profileList where birthDate is null
        defaultProfileShouldNotBeFound("birthDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllProfilesByBirthDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where birthDate greater than or equals to DEFAULT_BIRTH_DATE
        defaultProfileShouldBeFound("birthDate.greaterOrEqualThan=" + DEFAULT_BIRTH_DATE);

        // Get all the profileList where birthDate greater than or equals to UPDATED_BIRTH_DATE
        defaultProfileShouldNotBeFound("birthDate.greaterOrEqualThan=" + UPDATED_BIRTH_DATE);
    }

    @Test
    @Transactional
    public void getAllProfilesByBirthDateIsLessThanSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where birthDate less than or equals to DEFAULT_BIRTH_DATE
        defaultProfileShouldNotBeFound("birthDate.lessThan=" + DEFAULT_BIRTH_DATE);

        // Get all the profileList where birthDate less than or equals to UPDATED_BIRTH_DATE
        defaultProfileShouldBeFound("birthDate.lessThan=" + UPDATED_BIRTH_DATE);
    }


    @Test
    @Transactional
    public void getAllProfilesByHeightIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where height equals to DEFAULT_HEIGHT
        defaultProfileShouldBeFound("height.equals=" + DEFAULT_HEIGHT);

        // Get all the profileList where height equals to UPDATED_HEIGHT
        defaultProfileShouldNotBeFound("height.equals=" + UPDATED_HEIGHT);
    }

    @Test
    @Transactional
    public void getAllProfilesByHeightIsInShouldWork() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where height in DEFAULT_HEIGHT or UPDATED_HEIGHT
        defaultProfileShouldBeFound("height.in=" + DEFAULT_HEIGHT + "," + UPDATED_HEIGHT);

        // Get all the profileList where height equals to UPDATED_HEIGHT
        defaultProfileShouldNotBeFound("height.in=" + UPDATED_HEIGHT);
    }

    @Test
    @Transactional
    public void getAllProfilesByHeightIsNullOrNotNull() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where height is not null
        defaultProfileShouldBeFound("height.specified=true");

        // Get all the profileList where height is null
        defaultProfileShouldNotBeFound("height.specified=false");
    }

    @Test
    @Transactional
    public void getAllProfilesByWeightIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where weight equals to DEFAULT_WEIGHT
        defaultProfileShouldBeFound("weight.equals=" + DEFAULT_WEIGHT);

        // Get all the profileList where weight equals to UPDATED_WEIGHT
        defaultProfileShouldNotBeFound("weight.equals=" + UPDATED_WEIGHT);
    }

    @Test
    @Transactional
    public void getAllProfilesByWeightIsInShouldWork() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where weight in DEFAULT_WEIGHT or UPDATED_WEIGHT
        defaultProfileShouldBeFound("weight.in=" + DEFAULT_WEIGHT + "," + UPDATED_WEIGHT);

        // Get all the profileList where weight equals to UPDATED_WEIGHT
        defaultProfileShouldNotBeFound("weight.in=" + UPDATED_WEIGHT);
    }

    @Test
    @Transactional
    public void getAllProfilesByWeightIsNullOrNotNull() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where weight is not null
        defaultProfileShouldBeFound("weight.specified=true");

        // Get all the profileList where weight is null
        defaultProfileShouldNotBeFound("weight.specified=false");
    }

    @Test
    @Transactional
    public void getAllProfilesByUnitSystemIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where unitSystem equals to DEFAULT_UNIT_SYSTEM
        defaultProfileShouldBeFound("unitSystem.equals=" + DEFAULT_UNIT_SYSTEM);

        // Get all the profileList where unitSystem equals to UPDATED_UNIT_SYSTEM
        defaultProfileShouldNotBeFound("unitSystem.equals=" + UPDATED_UNIT_SYSTEM);
    }

    @Test
    @Transactional
    public void getAllProfilesByUnitSystemIsInShouldWork() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where unitSystem in DEFAULT_UNIT_SYSTEM or UPDATED_UNIT_SYSTEM
        defaultProfileShouldBeFound("unitSystem.in=" + DEFAULT_UNIT_SYSTEM + "," + UPDATED_UNIT_SYSTEM);

        // Get all the profileList where unitSystem equals to UPDATED_UNIT_SYSTEM
        defaultProfileShouldNotBeFound("unitSystem.in=" + UPDATED_UNIT_SYSTEM);
    }

    @Test
    @Transactional
    public void getAllProfilesByUnitSystemIsNullOrNotNull() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where unitSystem is not null
        defaultProfileShouldBeFound("unitSystem.specified=true");

        // Get all the profileList where unitSystem is null
        defaultProfileShouldNotBeFound("unitSystem.specified=false");
    }

    @Test
    @Transactional
    public void getAllProfilesByAboutMeIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where aboutMe equals to DEFAULT_ABOUT_ME
        defaultProfileShouldBeFound("aboutMe.equals=" + DEFAULT_ABOUT_ME);

        // Get all the profileList where aboutMe equals to UPDATED_ABOUT_ME
        defaultProfileShouldNotBeFound("aboutMe.equals=" + UPDATED_ABOUT_ME);
    }

    @Test
    @Transactional
    public void getAllProfilesByAboutMeIsInShouldWork() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where aboutMe in DEFAULT_ABOUT_ME or UPDATED_ABOUT_ME
        defaultProfileShouldBeFound("aboutMe.in=" + DEFAULT_ABOUT_ME + "," + UPDATED_ABOUT_ME);

        // Get all the profileList where aboutMe equals to UPDATED_ABOUT_ME
        defaultProfileShouldNotBeFound("aboutMe.in=" + UPDATED_ABOUT_ME);
    }

    @Test
    @Transactional
    public void getAllProfilesByAboutMeIsNullOrNotNull() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where aboutMe is not null
        defaultProfileShouldBeFound("aboutMe.specified=true");

        // Get all the profileList where aboutMe is null
        defaultProfileShouldNotBeFound("aboutMe.specified=false");
    }

    @Test
    @Transactional
    public void getAllProfilesByDisplayNameIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where displayName equals to DEFAULT_DISPLAY_NAME
        defaultProfileShouldBeFound("displayName.equals=" + DEFAULT_DISPLAY_NAME);

        // Get all the profileList where displayName equals to UPDATED_DISPLAY_NAME
        defaultProfileShouldNotBeFound("displayName.equals=" + UPDATED_DISPLAY_NAME);
    }

    @Test
    @Transactional
    public void getAllProfilesByDisplayNameIsInShouldWork() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where displayName in DEFAULT_DISPLAY_NAME or UPDATED_DISPLAY_NAME
        defaultProfileShouldBeFound("displayName.in=" + DEFAULT_DISPLAY_NAME + "," + UPDATED_DISPLAY_NAME);

        // Get all the profileList where displayName equals to UPDATED_DISPLAY_NAME
        defaultProfileShouldNotBeFound("displayName.in=" + UPDATED_DISPLAY_NAME);
    }

    @Test
    @Transactional
    public void getAllProfilesByDisplayNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where displayName is not null
        defaultProfileShouldBeFound("displayName.specified=true");

        // Get all the profileList where displayName is null
        defaultProfileShouldNotBeFound("displayName.specified=false");
    }

    @Test
    @Transactional
    public void getAllProfilesByShowAgeIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where showAge equals to DEFAULT_SHOW_AGE
        defaultProfileShouldBeFound("showAge.equals=" + DEFAULT_SHOW_AGE);

        // Get all the profileList where showAge equals to UPDATED_SHOW_AGE
        defaultProfileShouldNotBeFound("showAge.equals=" + UPDATED_SHOW_AGE);
    }

    @Test
    @Transactional
    public void getAllProfilesByShowAgeIsInShouldWork() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where showAge in DEFAULT_SHOW_AGE or UPDATED_SHOW_AGE
        defaultProfileShouldBeFound("showAge.in=" + DEFAULT_SHOW_AGE + "," + UPDATED_SHOW_AGE);

        // Get all the profileList where showAge equals to UPDATED_SHOW_AGE
        defaultProfileShouldNotBeFound("showAge.in=" + UPDATED_SHOW_AGE);
    }

    @Test
    @Transactional
    public void getAllProfilesByShowAgeIsNullOrNotNull() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where showAge is not null
        defaultProfileShouldBeFound("showAge.specified=true");

        // Get all the profileList where showAge is null
        defaultProfileShouldNotBeFound("showAge.specified=false");
    }

    @Test
    @Transactional
    public void getAllProfilesByBannedIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where banned equals to DEFAULT_BANNED
        defaultProfileShouldBeFound("banned.equals=" + DEFAULT_BANNED);

        // Get all the profileList where banned equals to UPDATED_BANNED
        defaultProfileShouldNotBeFound("banned.equals=" + UPDATED_BANNED);
    }

    @Test
    @Transactional
    public void getAllProfilesByBannedIsInShouldWork() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where banned in DEFAULT_BANNED or UPDATED_BANNED
        defaultProfileShouldBeFound("banned.in=" + DEFAULT_BANNED + "," + UPDATED_BANNED);

        // Get all the profileList where banned equals to UPDATED_BANNED
        defaultProfileShouldNotBeFound("banned.in=" + UPDATED_BANNED);
    }

    @Test
    @Transactional
    public void getAllProfilesByBannedIsNullOrNotNull() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where banned is not null
        defaultProfileShouldBeFound("banned.specified=true");

        // Get all the profileList where banned is null
        defaultProfileShouldNotBeFound("banned.specified=false");
    }

    @Test
    @Transactional
    public void getAllProfilesByFilterPreferencesIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where filterPreferences equals to DEFAULT_FILTER_PREFERENCES
        defaultProfileShouldBeFound("filterPreferences.equals=" + DEFAULT_FILTER_PREFERENCES);

        // Get all the profileList where filterPreferences equals to UPDATED_FILTER_PREFERENCES
        defaultProfileShouldNotBeFound("filterPreferences.equals=" + UPDATED_FILTER_PREFERENCES);
    }

    @Test
    @Transactional
    public void getAllProfilesByFilterPreferencesIsInShouldWork() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where filterPreferences in DEFAULT_FILTER_PREFERENCES or UPDATED_FILTER_PREFERENCES
        defaultProfileShouldBeFound("filterPreferences.in=" + DEFAULT_FILTER_PREFERENCES + "," + UPDATED_FILTER_PREFERENCES);

        // Get all the profileList where filterPreferences equals to UPDATED_FILTER_PREFERENCES
        defaultProfileShouldNotBeFound("filterPreferences.in=" + UPDATED_FILTER_PREFERENCES);
    }

    @Test
    @Transactional
    public void getAllProfilesByFilterPreferencesIsNullOrNotNull() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where filterPreferences is not null
        defaultProfileShouldBeFound("filterPreferences.specified=true");

        // Get all the profileList where filterPreferences is null
        defaultProfileShouldNotBeFound("filterPreferences.specified=false");
    }

    @Test
    @Transactional
    public void getAllProfilesByLocationIsEqualToSomething() throws Exception {
        // Initialize the database
        Location location = LocationResourceIntTest.createEntity(em);
        em.persist(location);
        em.flush();
        profile.setLocation(location);
        profileRepository.saveAndFlush(profile);
        Long locationId = location.getId();

        // Get all the profileList where location equals to locationId
        defaultProfileShouldBeFound("locationId.equals=" + locationId);

        // Get all the profileList where location equals to locationId + 1
        defaultProfileShouldNotBeFound("locationId.equals=" + (locationId + 1));
    }


    @Test
    @Transactional
    public void getAllProfilesByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        profile.setUser(user);
        profileRepository.saveAndFlush(profile);
        Long userId = user.getId();

        // Get all the profileList where user equals to userId
        defaultProfileShouldBeFound("userId.equals=" + userId);

        // Get all the profileList where user equals to userId + 1
        defaultProfileShouldNotBeFound("userId.equals=" + (userId + 1));
    }


    @Test
    @Transactional
    public void getAllProfilesByRelationshipIsEqualToSomething() throws Exception {
        // Initialize the database
        Relationship relationship = RelationshipResourceIntTest.createEntity(em);
        em.persist(relationship);
        em.flush();
        profile.setRelationship(relationship);
        profileRepository.saveAndFlush(profile);
        Long relationshipId = relationship.getId();

        // Get all the profileList where relationship equals to relationshipId
        defaultProfileShouldBeFound("relationshipId.equals=" + relationshipId);

        // Get all the profileList where relationship equals to relationshipId + 1
        defaultProfileShouldNotBeFound("relationshipId.equals=" + (relationshipId + 1));
    }


    @Test
    @Transactional
    public void getAllProfilesByGenderIsEqualToSomething() throws Exception {
        // Initialize the database
        Gender gender = GenderResourceIntTest.createEntity(em);
        em.persist(gender);
        em.flush();
        profile.setGender(gender);
        profileRepository.saveAndFlush(profile);
        Long genderId = gender.getId();

        // Get all the profileList where gender equals to genderId
        defaultProfileShouldBeFound("genderId.equals=" + genderId);

        // Get all the profileList where gender equals to genderId + 1
        defaultProfileShouldNotBeFound("genderId.equals=" + (genderId + 1));
    }


    @Test
    @Transactional
    public void getAllProfilesByEthnicityIsEqualToSomething() throws Exception {
        // Initialize the database
        Ethnicity ethnicity = EthnicityResourceIntTest.createEntity(em);
        em.persist(ethnicity);
        em.flush();
        profile.setEthnicity(ethnicity);
        profileRepository.saveAndFlush(profile);
        Long ethnicityId = ethnicity.getId();

        // Get all the profileList where ethnicity equals to ethnicityId
        defaultProfileShouldBeFound("ethnicityId.equals=" + ethnicityId);

        // Get all the profileList where ethnicity equals to ethnicityId + 1
        defaultProfileShouldNotBeFound("ethnicityId.equals=" + (ethnicityId + 1));
    }


    @Test
    @Transactional
    public void getAllProfilesBySentInvitationIsEqualToSomething() throws Exception {
        // Initialize the database
        Invitation sentInvitation = InvitationResourceIntTest.createEntity(em);
        em.persist(sentInvitation);
        em.flush();
        profile.addSentInvitation(sentInvitation);
        profileRepository.saveAndFlush(profile);
        Long sentInvitationId = sentInvitation.getId();

        // Get all the profileList where sentInvitation equals to sentInvitationId
        defaultProfileShouldBeFound("sentInvitationId.equals=" + sentInvitationId);

        // Get all the profileList where sentInvitation equals to sentInvitationId + 1
        defaultProfileShouldNotBeFound("sentInvitationId.equals=" + (sentInvitationId + 1));
    }


    @Test
    @Transactional
    public void getAllProfilesByReceivedInvitationIsEqualToSomething() throws Exception {
        // Initialize the database
        Invitation receivedInvitation = InvitationResourceIntTest.createEntity(em);
        em.persist(receivedInvitation);
        em.flush();
        profile.addReceivedInvitation(receivedInvitation);
        profileRepository.saveAndFlush(profile);
        Long receivedInvitationId = receivedInvitation.getId();

        // Get all the profileList where receivedInvitation equals to receivedInvitationId
        defaultProfileShouldBeFound("receivedInvitationId.equals=" + receivedInvitationId);

        // Get all the profileList where receivedInvitation equals to receivedInvitationId + 1
        defaultProfileShouldNotBeFound("receivedInvitationId.equals=" + (receivedInvitationId + 1));
    }


    @Test
    @Transactional
    public void getAllProfilesBySentBlockIsEqualToSomething() throws Exception {
        // Initialize the database
        Block sentBlock = BlockResourceIntTest.createEntity(em);
        em.persist(sentBlock);
        em.flush();
        profile.addSentBlock(sentBlock);
        profileRepository.saveAndFlush(profile);
        Long sentBlockId = sentBlock.getId();

        // Get all the profileList where sentBlock equals to sentBlockId
        defaultProfileShouldBeFound("sentBlockId.equals=" + sentBlockId);

        // Get all the profileList where sentBlock equals to sentBlockId + 1
        defaultProfileShouldNotBeFound("sentBlockId.equals=" + (sentBlockId + 1));
    }


    @Test
    @Transactional
    public void getAllProfilesByReceivedBlockIsEqualToSomething() throws Exception {
        // Initialize the database
        Block receivedBlock = BlockResourceIntTest.createEntity(em);
        em.persist(receivedBlock);
        em.flush();
        profile.addReceivedBlock(receivedBlock);
        profileRepository.saveAndFlush(profile);
        Long receivedBlockId = receivedBlock.getId();

        // Get all the profileList where receivedBlock equals to receivedBlockId
        defaultProfileShouldBeFound("receivedBlockId.equals=" + receivedBlockId);

        // Get all the profileList where receivedBlock equals to receivedBlockId + 1
        defaultProfileShouldNotBeFound("receivedBlockId.equals=" + (receivedBlockId + 1));
    }


    @Test
    @Transactional
    public void getAllProfilesBySentMessageIsEqualToSomething() throws Exception {
        // Initialize the database
        Message sentMessage = MessageResourceIntTest.createEntity(em);
        em.persist(sentMessage);
        em.flush();
        profile.addSentMessage(sentMessage);
        profileRepository.saveAndFlush(profile);
        Long sentMessageId = sentMessage.getId();

        // Get all the profileList where sentMessage equals to sentMessageId
        defaultProfileShouldBeFound("sentMessageId.equals=" + sentMessageId);

        // Get all the profileList where sentMessage equals to sentMessageId + 1
        defaultProfileShouldNotBeFound("sentMessageId.equals=" + (sentMessageId + 1));
    }


    @Test
    @Transactional
    public void getAllProfilesByAdminChatroomIsEqualToSomething() throws Exception {
        // Initialize the database
        Chatroom adminChatroom = ChatroomResourceIntTest.createEntity(em);
        em.persist(adminChatroom);
        em.flush();
        profile.addAdminChatroom(adminChatroom);
        profileRepository.saveAndFlush(profile);
        Long adminChatroomId = adminChatroom.getId();

        // Get all the profileList where adminChatroom equals to adminChatroomId
        defaultProfileShouldBeFound("adminChatroomId.equals=" + adminChatroomId);

        // Get all the profileList where adminChatroom equals to adminChatroomId + 1
        defaultProfileShouldNotBeFound("adminChatroomId.equals=" + (adminChatroomId + 1));
    }


    @Test
    @Transactional
    public void getAllProfilesByJoinedChatroomIsEqualToSomething() throws Exception {
        // Initialize the database
        Chatroom joinedChatroom = ChatroomResourceIntTest.createEntity(em);
        em.persist(joinedChatroom);
        em.flush();
        profile.addJoinedChatroom(joinedChatroom);
        profileRepository.saveAndFlush(profile);
        Long joinedChatroomId = joinedChatroom.getId();

        // Get all the profileList where joinedChatroom equals to joinedChatroomId
        defaultProfileShouldBeFound("joinedChatroomId.equals=" + joinedChatroomId);

        // Get all the profileList where joinedChatroom equals to joinedChatroomId + 1
        defaultProfileShouldNotBeFound("joinedChatroomId.equals=" + (joinedChatroomId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultProfileShouldBeFound(String filter) throws Exception {
        restProfileMockMvc.perform(get("/api/profiles?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(profile.getId().intValue())))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE.toString())))
            .andExpect(jsonPath("$.[*].pictureContentType").value(hasItem(DEFAULT_PICTURE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].picture").value(hasItem(Base64Utils.encodeToString(DEFAULT_PICTURE))))
            .andExpect(jsonPath("$.[*].height").value(hasItem(DEFAULT_HEIGHT.doubleValue())))
            .andExpect(jsonPath("$.[*].weight").value(hasItem(DEFAULT_WEIGHT.doubleValue())))
            .andExpect(jsonPath("$.[*].unitSystem").value(hasItem(DEFAULT_UNIT_SYSTEM.toString())))
            .andExpect(jsonPath("$.[*].aboutMe").value(hasItem(DEFAULT_ABOUT_ME)))
            .andExpect(jsonPath("$.[*].displayName").value(hasItem(DEFAULT_DISPLAY_NAME)))
            .andExpect(jsonPath("$.[*].showAge").value(hasItem(DEFAULT_SHOW_AGE.booleanValue())))
            .andExpect(jsonPath("$.[*].banned").value(hasItem(DEFAULT_BANNED.booleanValue())))
            .andExpect(jsonPath("$.[*].filterPreferences").value(hasItem(DEFAULT_FILTER_PREFERENCES)));

        // Check, that the count call also returns 1
        restProfileMockMvc.perform(get("/api/profiles/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultProfileShouldNotBeFound(String filter) throws Exception {
        restProfileMockMvc.perform(get("/api/profiles?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProfileMockMvc.perform(get("/api/profiles/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingProfile() throws Exception {
        // Get the profile
        restProfileMockMvc.perform(get("/api/profiles/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProfile() throws Exception {
        // Initialize the database
        profileService.save(profile);

        int databaseSizeBeforeUpdate = profileRepository.findAll().size();

        // Update the profile
        Profile updatedProfile = profileRepository.findById(profile.getId()).get();
        // Disconnect from session so that the updates on updatedProfile are not directly saved in db
        em.detach(updatedProfile);
        updatedProfile
            .birthDate(UPDATED_BIRTH_DATE)
            .picture(UPDATED_PICTURE)
            .pictureContentType(UPDATED_PICTURE_CONTENT_TYPE)
            .height(UPDATED_HEIGHT)
            .weight(UPDATED_WEIGHT)
            .unitSystem(UPDATED_UNIT_SYSTEM)
            .aboutMe(UPDATED_ABOUT_ME)
            .displayName(UPDATED_DISPLAY_NAME)
            .showAge(UPDATED_SHOW_AGE)
            .banned(UPDATED_BANNED)
            .filterPreferences(UPDATED_FILTER_PREFERENCES);

        restProfileMockMvc.perform(put("/api/profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedProfile)))
            .andExpect(status().isOk());

        // Validate the Profile in the database
        List<Profile> profileList = profileRepository.findAll();
        assertThat(profileList).hasSize(databaseSizeBeforeUpdate);
        Profile testProfile = profileList.get(profileList.size() - 1);
        assertThat(testProfile.getBirthDate()).isEqualTo(UPDATED_BIRTH_DATE);
        assertThat(testProfile.getPicture()).isEqualTo(UPDATED_PICTURE);
        assertThat(testProfile.getPictureContentType()).isEqualTo(UPDATED_PICTURE_CONTENT_TYPE);
        assertThat(testProfile.getHeight()).isEqualTo(UPDATED_HEIGHT);
        assertThat(testProfile.getWeight()).isEqualTo(UPDATED_WEIGHT);
        assertThat(testProfile.getUnitSystem()).isEqualTo(UPDATED_UNIT_SYSTEM);
        assertThat(testProfile.getAboutMe()).isEqualTo(UPDATED_ABOUT_ME);
        assertThat(testProfile.getDisplayName()).isEqualTo(UPDATED_DISPLAY_NAME);
        assertThat(testProfile.isShowAge()).isEqualTo(UPDATED_SHOW_AGE);
        assertThat(testProfile.isBanned()).isEqualTo(UPDATED_BANNED);
        assertThat(testProfile.getFilterPreferences()).isEqualTo(UPDATED_FILTER_PREFERENCES);
    }

    @Test
    @Transactional
    public void updateNonExistingProfile() throws Exception {
        int databaseSizeBeforeUpdate = profileRepository.findAll().size();

        // Create the Profile

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProfileMockMvc.perform(put("/api/profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(profile)))
            .andExpect(status().isBadRequest());

        // Validate the Profile in the database
        List<Profile> profileList = profileRepository.findAll();
        assertThat(profileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteProfile() throws Exception {
        // Initialize the database
        profileService.save(profile);

        int databaseSizeBeforeDelete = profileRepository.findAll().size();

        // Delete the profile
        restProfileMockMvc.perform(delete("/api/profiles/{id}", profile.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Profile> profileList = profileRepository.findAll();
        assertThat(profileList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Profile.class);
        Profile profile1 = new Profile();
        profile1.setId(1L);
        Profile profile2 = new Profile();
        profile2.setId(profile1.getId());
        assertThat(profile1).isEqualTo(profile2);
        profile2.setId(2L);
        assertThat(profile1).isNotEqualTo(profile2);
        profile1.setId(null);
        assertThat(profile1).isNotEqualTo(profile2);
    }
}
