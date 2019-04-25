package edu.socialnetwork.web.rest;

import edu.socialnetwork.SocialNetworkBackendApp;

import edu.socialnetwork.domain.Location;
import edu.socialnetwork.domain.Profile;
import edu.socialnetwork.domain.Message;
import edu.socialnetwork.repository.LocationRepository;
import edu.socialnetwork.repository.ProfileRepository;
import edu.socialnetwork.service.LocationService;
import edu.socialnetwork.web.rest.errors.ExceptionTranslator;
import edu.socialnetwork.service.dto.LocationCriteria;
import edu.socialnetwork.service.LocationQueryService;

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
 * Test class for the LocationResource REST controller.
 *
 * @see LocationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SocialNetworkBackendApp.class)
public class LocationResourceIntTest {

    private static final Double DEFAULT_LATITUDE = 1D;
    private static final Double UPDATED_LATITUDE = 2D;

    private static final Double DEFAULT_LONGITUDE = 1D;
    private static final Double UPDATED_LONGITUDE = 2D;

    private static final String DEFAULT_URL_GOOGLE_MAPS = "AAAAAAAAAA";
    private static final String UPDATED_URL_GOOGLE_MAPS = "BBBBBBBBBB";

    private static final String DEFAULT_URL_OPEN_STREET_MAP = "AAAAAAAAAA";
    private static final String UPDATED_URL_OPEN_STREET_MAP = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_POSTAL_CODE = "AAAAAAAAAA";
    private static final String UPDATED_POSTAL_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_STATE_PROVICE = "AAAAAAAAAA";
    private static final String UPDATED_STATE_PROVICE = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTY = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private LocationService locationService;

    @Autowired
    private LocationQueryService locationQueryService;

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

    private MockMvc restLocationMockMvc;

    private Location location;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final LocationResource locationResource = new LocationResource(locationService, locationQueryService, profileRepository);
        this.restLocationMockMvc = MockMvcBuilders.standaloneSetup(locationResource)
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
    public static Location createEntity(EntityManager em) {
        Location location = new Location()
            .latitude(DEFAULT_LATITUDE)
            .longitude(DEFAULT_LONGITUDE)
            .urlGoogleMaps(DEFAULT_URL_GOOGLE_MAPS)
            .urlOpenStreetMap(DEFAULT_URL_OPEN_STREET_MAP)
            .address(DEFAULT_ADDRESS)
            .postalCode(DEFAULT_POSTAL_CODE)
            .city(DEFAULT_CITY)
            .stateProvice(DEFAULT_STATE_PROVICE)
            .county(DEFAULT_COUNTY)
            .country(DEFAULT_COUNTRY);
        return location;
    }

    @Before
    public void initTest() {
        location = createEntity(em);
    }

    @Test
    @Transactional
    public void createLocation() throws Exception {
        int databaseSizeBeforeCreate = locationRepository.findAll().size();

        // Create the Location
        restLocationMockMvc.perform(post("/api/locations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(location)))
            .andExpect(status().isCreated());

        // Validate the Location in the database
        List<Location> locationList = locationRepository.findAll();
        assertThat(locationList).hasSize(databaseSizeBeforeCreate + 1);
        Location testLocation = locationList.get(locationList.size() - 1);
        assertThat(testLocation.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
        assertThat(testLocation.getLongitude()).isEqualTo(DEFAULT_LONGITUDE);
        assertThat(testLocation.getUrlGoogleMaps()).isEqualTo(DEFAULT_URL_GOOGLE_MAPS);
        assertThat(testLocation.getUrlOpenStreetMap()).isEqualTo(DEFAULT_URL_OPEN_STREET_MAP);
        assertThat(testLocation.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testLocation.getPostalCode()).isEqualTo(DEFAULT_POSTAL_CODE);
        assertThat(testLocation.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testLocation.getStateProvice()).isEqualTo(DEFAULT_STATE_PROVICE);
        assertThat(testLocation.getCounty()).isEqualTo(DEFAULT_COUNTY);
        assertThat(testLocation.getCountry()).isEqualTo(DEFAULT_COUNTRY);
    }

    @Test
    @Transactional
    public void createLocationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = locationRepository.findAll().size();

        // Create the Location with an existing ID
        location.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLocationMockMvc.perform(post("/api/locations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(location)))
            .andExpect(status().isBadRequest());

        // Validate the Location in the database
        List<Location> locationList = locationRepository.findAll();
        assertThat(locationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkLatitudeIsRequired() throws Exception {
        int databaseSizeBeforeTest = locationRepository.findAll().size();
        // set the field null
        location.setLatitude(null);

        // Create the Location, which fails.

        restLocationMockMvc.perform(post("/api/locations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(location)))
            .andExpect(status().isBadRequest());

        List<Location> locationList = locationRepository.findAll();
        assertThat(locationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLongitudeIsRequired() throws Exception {
        int databaseSizeBeforeTest = locationRepository.findAll().size();
        // set the field null
        location.setLongitude(null);

        // Create the Location, which fails.

        restLocationMockMvc.perform(post("/api/locations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(location)))
            .andExpect(status().isBadRequest());

        List<Location> locationList = locationRepository.findAll();
        assertThat(locationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLocations() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList
        restLocationMockMvc.perform(get("/api/locations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(location.getId().intValue())))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].urlGoogleMaps").value(hasItem(DEFAULT_URL_GOOGLE_MAPS.toString())))
            .andExpect(jsonPath("$.[*].urlOpenStreetMap").value(hasItem(DEFAULT_URL_OPEN_STREET_MAP.toString())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].postalCode").value(hasItem(DEFAULT_POSTAL_CODE.toString())))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
            .andExpect(jsonPath("$.[*].stateProvice").value(hasItem(DEFAULT_STATE_PROVICE.toString())))
            .andExpect(jsonPath("$.[*].county").value(hasItem(DEFAULT_COUNTY.toString())))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY.toString())));
    }
    
    @Test
    @Transactional
    public void getLocation() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get the location
        restLocationMockMvc.perform(get("/api/locations/{id}", location.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(location.getId().intValue()))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE.doubleValue()))
            .andExpect(jsonPath("$.longitude").value(DEFAULT_LONGITUDE.doubleValue()))
            .andExpect(jsonPath("$.urlGoogleMaps").value(DEFAULT_URL_GOOGLE_MAPS.toString()))
            .andExpect(jsonPath("$.urlOpenStreetMap").value(DEFAULT_URL_OPEN_STREET_MAP.toString()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS.toString()))
            .andExpect(jsonPath("$.postalCode").value(DEFAULT_POSTAL_CODE.toString()))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY.toString()))
            .andExpect(jsonPath("$.stateProvice").value(DEFAULT_STATE_PROVICE.toString()))
            .andExpect(jsonPath("$.county").value(DEFAULT_COUNTY.toString()))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY.toString()));
    }

    @Test
    @Transactional
    public void getAllLocationsByLatitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where latitude equals to DEFAULT_LATITUDE
        defaultLocationShouldBeFound("latitude.equals=" + DEFAULT_LATITUDE);

        // Get all the locationList where latitude equals to UPDATED_LATITUDE
        defaultLocationShouldNotBeFound("latitude.equals=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    public void getAllLocationsByLatitudeIsInShouldWork() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where latitude in DEFAULT_LATITUDE or UPDATED_LATITUDE
        defaultLocationShouldBeFound("latitude.in=" + DEFAULT_LATITUDE + "," + UPDATED_LATITUDE);

        // Get all the locationList where latitude equals to UPDATED_LATITUDE
        defaultLocationShouldNotBeFound("latitude.in=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    public void getAllLocationsByLatitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where latitude is not null
        defaultLocationShouldBeFound("latitude.specified=true");

        // Get all the locationList where latitude is null
        defaultLocationShouldNotBeFound("latitude.specified=false");
    }

    @Test
    @Transactional
    public void getAllLocationsByLongitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where longitude equals to DEFAULT_LONGITUDE
        defaultLocationShouldBeFound("longitude.equals=" + DEFAULT_LONGITUDE);

        // Get all the locationList where longitude equals to UPDATED_LONGITUDE
        defaultLocationShouldNotBeFound("longitude.equals=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    public void getAllLocationsByLongitudeIsInShouldWork() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where longitude in DEFAULT_LONGITUDE or UPDATED_LONGITUDE
        defaultLocationShouldBeFound("longitude.in=" + DEFAULT_LONGITUDE + "," + UPDATED_LONGITUDE);

        // Get all the locationList where longitude equals to UPDATED_LONGITUDE
        defaultLocationShouldNotBeFound("longitude.in=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    public void getAllLocationsByLongitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where longitude is not null
        defaultLocationShouldBeFound("longitude.specified=true");

        // Get all the locationList where longitude is null
        defaultLocationShouldNotBeFound("longitude.specified=false");
    }

    @Test
    @Transactional
    public void getAllLocationsByUrlGoogleMapsIsEqualToSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where urlGoogleMaps equals to DEFAULT_URL_GOOGLE_MAPS
        defaultLocationShouldBeFound("urlGoogleMaps.equals=" + DEFAULT_URL_GOOGLE_MAPS);

        // Get all the locationList where urlGoogleMaps equals to UPDATED_URL_GOOGLE_MAPS
        defaultLocationShouldNotBeFound("urlGoogleMaps.equals=" + UPDATED_URL_GOOGLE_MAPS);
    }

    @Test
    @Transactional
    public void getAllLocationsByUrlGoogleMapsIsInShouldWork() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where urlGoogleMaps in DEFAULT_URL_GOOGLE_MAPS or UPDATED_URL_GOOGLE_MAPS
        defaultLocationShouldBeFound("urlGoogleMaps.in=" + DEFAULT_URL_GOOGLE_MAPS + "," + UPDATED_URL_GOOGLE_MAPS);

        // Get all the locationList where urlGoogleMaps equals to UPDATED_URL_GOOGLE_MAPS
        defaultLocationShouldNotBeFound("urlGoogleMaps.in=" + UPDATED_URL_GOOGLE_MAPS);
    }

    @Test
    @Transactional
    public void getAllLocationsByUrlGoogleMapsIsNullOrNotNull() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where urlGoogleMaps is not null
        defaultLocationShouldBeFound("urlGoogleMaps.specified=true");

        // Get all the locationList where urlGoogleMaps is null
        defaultLocationShouldNotBeFound("urlGoogleMaps.specified=false");
    }

    @Test
    @Transactional
    public void getAllLocationsByUrlOpenStreetMapIsEqualToSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where urlOpenStreetMap equals to DEFAULT_URL_OPEN_STREET_MAP
        defaultLocationShouldBeFound("urlOpenStreetMap.equals=" + DEFAULT_URL_OPEN_STREET_MAP);

        // Get all the locationList where urlOpenStreetMap equals to UPDATED_URL_OPEN_STREET_MAP
        defaultLocationShouldNotBeFound("urlOpenStreetMap.equals=" + UPDATED_URL_OPEN_STREET_MAP);
    }

    @Test
    @Transactional
    public void getAllLocationsByUrlOpenStreetMapIsInShouldWork() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where urlOpenStreetMap in DEFAULT_URL_OPEN_STREET_MAP or UPDATED_URL_OPEN_STREET_MAP
        defaultLocationShouldBeFound("urlOpenStreetMap.in=" + DEFAULT_URL_OPEN_STREET_MAP + "," + UPDATED_URL_OPEN_STREET_MAP);

        // Get all the locationList where urlOpenStreetMap equals to UPDATED_URL_OPEN_STREET_MAP
        defaultLocationShouldNotBeFound("urlOpenStreetMap.in=" + UPDATED_URL_OPEN_STREET_MAP);
    }

    @Test
    @Transactional
    public void getAllLocationsByUrlOpenStreetMapIsNullOrNotNull() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where urlOpenStreetMap is not null
        defaultLocationShouldBeFound("urlOpenStreetMap.specified=true");

        // Get all the locationList where urlOpenStreetMap is null
        defaultLocationShouldNotBeFound("urlOpenStreetMap.specified=false");
    }

    @Test
    @Transactional
    public void getAllLocationsByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where address equals to DEFAULT_ADDRESS
        defaultLocationShouldBeFound("address.equals=" + DEFAULT_ADDRESS);

        // Get all the locationList where address equals to UPDATED_ADDRESS
        defaultLocationShouldNotBeFound("address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllLocationsByAddressIsInShouldWork() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where address in DEFAULT_ADDRESS or UPDATED_ADDRESS
        defaultLocationShouldBeFound("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS);

        // Get all the locationList where address equals to UPDATED_ADDRESS
        defaultLocationShouldNotBeFound("address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllLocationsByAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where address is not null
        defaultLocationShouldBeFound("address.specified=true");

        // Get all the locationList where address is null
        defaultLocationShouldNotBeFound("address.specified=false");
    }

    @Test
    @Transactional
    public void getAllLocationsByPostalCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where postalCode equals to DEFAULT_POSTAL_CODE
        defaultLocationShouldBeFound("postalCode.equals=" + DEFAULT_POSTAL_CODE);

        // Get all the locationList where postalCode equals to UPDATED_POSTAL_CODE
        defaultLocationShouldNotBeFound("postalCode.equals=" + UPDATED_POSTAL_CODE);
    }

    @Test
    @Transactional
    public void getAllLocationsByPostalCodeIsInShouldWork() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where postalCode in DEFAULT_POSTAL_CODE or UPDATED_POSTAL_CODE
        defaultLocationShouldBeFound("postalCode.in=" + DEFAULT_POSTAL_CODE + "," + UPDATED_POSTAL_CODE);

        // Get all the locationList where postalCode equals to UPDATED_POSTAL_CODE
        defaultLocationShouldNotBeFound("postalCode.in=" + UPDATED_POSTAL_CODE);
    }

    @Test
    @Transactional
    public void getAllLocationsByPostalCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where postalCode is not null
        defaultLocationShouldBeFound("postalCode.specified=true");

        // Get all the locationList where postalCode is null
        defaultLocationShouldNotBeFound("postalCode.specified=false");
    }

    @Test
    @Transactional
    public void getAllLocationsByCityIsEqualToSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where city equals to DEFAULT_CITY
        defaultLocationShouldBeFound("city.equals=" + DEFAULT_CITY);

        // Get all the locationList where city equals to UPDATED_CITY
        defaultLocationShouldNotBeFound("city.equals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    public void getAllLocationsByCityIsInShouldWork() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where city in DEFAULT_CITY or UPDATED_CITY
        defaultLocationShouldBeFound("city.in=" + DEFAULT_CITY + "," + UPDATED_CITY);

        // Get all the locationList where city equals to UPDATED_CITY
        defaultLocationShouldNotBeFound("city.in=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    public void getAllLocationsByCityIsNullOrNotNull() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where city is not null
        defaultLocationShouldBeFound("city.specified=true");

        // Get all the locationList where city is null
        defaultLocationShouldNotBeFound("city.specified=false");
    }

    @Test
    @Transactional
    public void getAllLocationsByStateProviceIsEqualToSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where stateProvice equals to DEFAULT_STATE_PROVICE
        defaultLocationShouldBeFound("stateProvice.equals=" + DEFAULT_STATE_PROVICE);

        // Get all the locationList where stateProvice equals to UPDATED_STATE_PROVICE
        defaultLocationShouldNotBeFound("stateProvice.equals=" + UPDATED_STATE_PROVICE);
    }

    @Test
    @Transactional
    public void getAllLocationsByStateProviceIsInShouldWork() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where stateProvice in DEFAULT_STATE_PROVICE or UPDATED_STATE_PROVICE
        defaultLocationShouldBeFound("stateProvice.in=" + DEFAULT_STATE_PROVICE + "," + UPDATED_STATE_PROVICE);

        // Get all the locationList where stateProvice equals to UPDATED_STATE_PROVICE
        defaultLocationShouldNotBeFound("stateProvice.in=" + UPDATED_STATE_PROVICE);
    }

    @Test
    @Transactional
    public void getAllLocationsByStateProviceIsNullOrNotNull() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where stateProvice is not null
        defaultLocationShouldBeFound("stateProvice.specified=true");

        // Get all the locationList where stateProvice is null
        defaultLocationShouldNotBeFound("stateProvice.specified=false");
    }

    @Test
    @Transactional
    public void getAllLocationsByCountyIsEqualToSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where county equals to DEFAULT_COUNTY
        defaultLocationShouldBeFound("county.equals=" + DEFAULT_COUNTY);

        // Get all the locationList where county equals to UPDATED_COUNTY
        defaultLocationShouldNotBeFound("county.equals=" + UPDATED_COUNTY);
    }

    @Test
    @Transactional
    public void getAllLocationsByCountyIsInShouldWork() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where county in DEFAULT_COUNTY or UPDATED_COUNTY
        defaultLocationShouldBeFound("county.in=" + DEFAULT_COUNTY + "," + UPDATED_COUNTY);

        // Get all the locationList where county equals to UPDATED_COUNTY
        defaultLocationShouldNotBeFound("county.in=" + UPDATED_COUNTY);
    }

    @Test
    @Transactional
    public void getAllLocationsByCountyIsNullOrNotNull() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where county is not null
        defaultLocationShouldBeFound("county.specified=true");

        // Get all the locationList where county is null
        defaultLocationShouldNotBeFound("county.specified=false");
    }

    @Test
    @Transactional
    public void getAllLocationsByCountryIsEqualToSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where country equals to DEFAULT_COUNTRY
        defaultLocationShouldBeFound("country.equals=" + DEFAULT_COUNTRY);

        // Get all the locationList where country equals to UPDATED_COUNTRY
        defaultLocationShouldNotBeFound("country.equals=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    public void getAllLocationsByCountryIsInShouldWork() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where country in DEFAULT_COUNTRY or UPDATED_COUNTRY
        defaultLocationShouldBeFound("country.in=" + DEFAULT_COUNTRY + "," + UPDATED_COUNTRY);

        // Get all the locationList where country equals to UPDATED_COUNTRY
        defaultLocationShouldNotBeFound("country.in=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    public void getAllLocationsByCountryIsNullOrNotNull() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where country is not null
        defaultLocationShouldBeFound("country.specified=true");

        // Get all the locationList where country is null
        defaultLocationShouldNotBeFound("country.specified=false");
    }

    @Test
    @Transactional
    public void getAllLocationsByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        Profile user = ProfileResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        location.setUser(user);
        user.setLocation(location);
        locationRepository.saveAndFlush(location);
        Long userId = user.getId();

        // Get all the locationList where user equals to userId
        defaultLocationShouldBeFound("userId.equals=" + userId);

        // Get all the locationList where user equals to userId + 1
        defaultLocationShouldNotBeFound("userId.equals=" + (userId + 1));
    }


    @Test
    @Transactional
    public void getAllLocationsByMessageIsEqualToSomething() throws Exception {
        // Initialize the database
        Message message = MessageResourceIntTest.createEntity(em);
        em.persist(message);
        em.flush();
        location.setMessage(message);
        message.setLocation(location);
        locationRepository.saveAndFlush(location);
        Long messageId = message.getId();

        // Get all the locationList where message equals to messageId
        defaultLocationShouldBeFound("messageId.equals=" + messageId);

        // Get all the locationList where message equals to messageId + 1
        defaultLocationShouldNotBeFound("messageId.equals=" + (messageId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultLocationShouldBeFound(String filter) throws Exception {
        restLocationMockMvc.perform(get("/api/locations?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(location.getId().intValue())))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].urlGoogleMaps").value(hasItem(DEFAULT_URL_GOOGLE_MAPS)))
            .andExpect(jsonPath("$.[*].urlOpenStreetMap").value(hasItem(DEFAULT_URL_OPEN_STREET_MAP)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].postalCode").value(hasItem(DEFAULT_POSTAL_CODE)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].stateProvice").value(hasItem(DEFAULT_STATE_PROVICE)))
            .andExpect(jsonPath("$.[*].county").value(hasItem(DEFAULT_COUNTY)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)));

        // Check, that the count call also returns 1
        restLocationMockMvc.perform(get("/api/locations/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultLocationShouldNotBeFound(String filter) throws Exception {
        restLocationMockMvc.perform(get("/api/locations?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLocationMockMvc.perform(get("/api/locations/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingLocation() throws Exception {
        // Get the location
        restLocationMockMvc.perform(get("/api/locations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLocation() throws Exception {
        // Initialize the database
        locationService.save(location);

        int databaseSizeBeforeUpdate = locationRepository.findAll().size();

        // Update the location
        Location updatedLocation = locationRepository.findById(location.getId()).get();
        // Disconnect from session so that the updates on updatedLocation are not directly saved in db
        em.detach(updatedLocation);
        updatedLocation
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .urlGoogleMaps(UPDATED_URL_GOOGLE_MAPS)
            .urlOpenStreetMap(UPDATED_URL_OPEN_STREET_MAP)
            .address(UPDATED_ADDRESS)
            .postalCode(UPDATED_POSTAL_CODE)
            .city(UPDATED_CITY)
            .stateProvice(UPDATED_STATE_PROVICE)
            .county(UPDATED_COUNTY)
            .country(UPDATED_COUNTRY);

        restLocationMockMvc.perform(put("/api/locations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedLocation)))
            .andExpect(status().isOk());

        // Validate the Location in the database
        List<Location> locationList = locationRepository.findAll();
        assertThat(locationList).hasSize(databaseSizeBeforeUpdate);
        Location testLocation = locationList.get(locationList.size() - 1);
        assertThat(testLocation.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testLocation.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testLocation.getUrlGoogleMaps()).isEqualTo(UPDATED_URL_GOOGLE_MAPS);
        assertThat(testLocation.getUrlOpenStreetMap()).isEqualTo(UPDATED_URL_OPEN_STREET_MAP);
        assertThat(testLocation.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testLocation.getPostalCode()).isEqualTo(UPDATED_POSTAL_CODE);
        assertThat(testLocation.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testLocation.getStateProvice()).isEqualTo(UPDATED_STATE_PROVICE);
        assertThat(testLocation.getCounty()).isEqualTo(UPDATED_COUNTY);
        assertThat(testLocation.getCountry()).isEqualTo(UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    public void updateNonExistingLocation() throws Exception {
        int databaseSizeBeforeUpdate = locationRepository.findAll().size();

        // Create the Location

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLocationMockMvc.perform(put("/api/locations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(location)))
            .andExpect(status().isBadRequest());

        // Validate the Location in the database
        List<Location> locationList = locationRepository.findAll();
        assertThat(locationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteLocation() throws Exception {
        // Initialize the database
        locationService.save(location);

        int databaseSizeBeforeDelete = locationRepository.findAll().size();

        // Delete the location
        restLocationMockMvc.perform(delete("/api/locations/{id}", location.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Location> locationList = locationRepository.findAll();
        assertThat(locationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Location.class);
        Location location1 = new Location();
        location1.setId(1L);
        Location location2 = new Location();
        location2.setId(location1.getId());
        assertThat(location1).isEqualTo(location2);
        location2.setId(2L);
        assertThat(location1).isNotEqualTo(location2);
        location1.setId(null);
        assertThat(location1).isNotEqualTo(location2);
    }
}
