package edu.socialnetwork.web.rest;
import edu.socialnetwork.domain.Location;
import edu.socialnetwork.domain.Profile;
import edu.socialnetwork.repository.ProfileRepository;
import edu.socialnetwork.security.SecurityUtils;
import edu.socialnetwork.service.LocationService;
import edu.socialnetwork.web.rest.errors.BadRequestAlertException;
import edu.socialnetwork.web.rest.util.HeaderUtil;
import edu.socialnetwork.web.rest.util.PaginationUtil;
import edu.socialnetwork.service.dto.LocationCriteria;
import edu.socialnetwork.service.LocationQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

/**
 * REST controller for managing Location.
 */
@RestController
@RequestMapping("/api")
public class LocationResource {

    private final Logger log = LoggerFactory.getLogger(LocationResource.class);

    private static final String ENTITY_NAME = "location";

    private final LocationService locationService;

    private final LocationQueryService locationQueryService;

    private final ProfileRepository profileRepository;

    public LocationResource(LocationService locationService, LocationQueryService locationQueryService, ProfileRepository profileRepository) {
        this.locationService = locationService;
        this.locationQueryService = locationQueryService;
        this.profileRepository = profileRepository;
    }

    /**
     * POST  /locations : Create a new location.
     *
     * @param location the location to create
     * @return the ResponseEntity with status 201 (Created) and with body the new location, or with status 400 (Bad Request) if the location has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/locations")
    public ResponseEntity<Location> createLocation(@Valid @RequestBody Location location) throws URISyntaxException {
        log.debug("REST request to save Location : {}", location);
        if (location.getId() != null) {
            throw new BadRequestAlertException("A new location cannot already have an ID", ENTITY_NAME, "idexists");
        }

        Location result = locationService.save(location);

        Profile profile = profileRepository.findByUserLogin(SecurityUtils.getCurrentUserLogin().get()).get();

        profile.setLocation(result);

        profileRepository.save(profile);

        return ResponseEntity.created(new URI("/api/locations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /locations : Updates an existing location.
     *
     * @param location the location to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated location,
     * or with status 400 (Bad Request) if the location is not valid,
     * or with status 500 (Internal Server Error) if the location couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/locations")
    public ResponseEntity<Location> updateLocation(@Valid @RequestBody Location location) throws URISyntaxException {
        log.debug("REST request to update Location : {}", location);
        if (location.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Location result = locationService.save(location);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, location.getId().toString()))
            .body(result);
    }

    /**
     * GET  /locations : get all the locations.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of locations in body
     */
    @GetMapping("/locations")
    public ResponseEntity<List<Location>> getAllLocations(LocationCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Locations by criteria: {}", criteria);
        Page<Location> page = locationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/locations");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /locations/count : count all the locations.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/locations/count")
    public ResponseEntity<Long> countLocations(LocationCriteria criteria) {
        log.debug("REST request to count Locations by criteria: {}", criteria);
        return ResponseEntity.ok().body(locationQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /locations/:id : get the "id" location.
     *
     * @param id the id of the location to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the location, or with status 404 (Not Found)
     */
    @GetMapping("/locations/{id}")
    public ResponseEntity<Location> getLocation(@PathVariable Long id) {
        log.debug("REST request to get Location : {}", id);
        Optional<Location> location = locationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(location);
    }

    /**
     * DELETE  /locations/:id : delete the "id" location.
     *
     * @param id the id of the location to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/locations/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable Long id) {
        log.debug("REST request to delete Location : {}", id);
        locationService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
