package edu.socialnetwork.web.rest;

import edu.socialnetwork.domain.Location;
import edu.socialnetwork.domain.Profile;
import edu.socialnetwork.repository.ProfileRepository;
import edu.socialnetwork.security.SecurityUtils;
import edu.socialnetwork.service.ProfileQueryService;
import edu.socialnetwork.service.ProfileService;
import edu.socialnetwork.service.dto.ProfileCriteria;
import edu.socialnetwork.service.dto.ProfileDistanceDTO;
import edu.socialnetwork.web.rest.errors.BadRequestAlertException;
import edu.socialnetwork.web.rest.util.HeaderUtil;
import edu.socialnetwork.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing Profile.
 */
@RestController
@RequestMapping("/api")
public class ProfileResource {

    private final Logger log = LoggerFactory.getLogger(ProfileResource.class);

    private static final String ENTITY_NAME = "profile";

    private final ProfileService profileService;

    private final ProfileQueryService profileQueryService;

    private final ProfileRepository profileRepository;

    public ProfileResource(ProfileService profileService, ProfileQueryService profileQueryService, ProfileRepository profileRepository) {
        this.profileService = profileService;
        this.profileQueryService = profileQueryService;
        this.profileRepository = profileRepository;
    }

    /**
     * POST  /profiles : Create a new profile.
     *
     * @param profile the profile to create
     * @return the ResponseEntity with status 201 (Created) and with body the new profile, or with status 400 (Bad Request) if the profile has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/profiles")
    public ResponseEntity<Profile> createProfile(@Valid @RequestBody Profile profile) throws URISyntaxException {
        log.debug("REST request to save Profile : {}", profile);
        if (profile.getId() != null) {
            throw new BadRequestAlertException("A new profile cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Profile result = profileService.save(profile);
        return ResponseEntity.created(new URI("/api/profiles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /profiles : Updates an existing profile.
     *
     * @param profile the profile to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated profile,
     * or with status 400 (Bad Request) if the profile is not valid,
     * or with status 500 (Internal Server Error) if the profile couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/profiles")
    public ResponseEntity<Profile> updateProfile(@Valid @RequestBody Profile profile) throws URISyntaxException {
        log.debug("REST request to update Profile : {}", profile);
        if (profile.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Profile result = profileService.save(profile);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, profile.getId().toString()))
            .body(result);
    }

    @GetMapping("/my-profile")
    public ResponseEntity<Profile> getCurrentUserProfile() throws URISyntaxException {
        Optional<Profile> currentProfile = profileRepository.findByUserLogin(SecurityUtils.getCurrentUserLogin().get());

        return ResponseUtil.wrapOrNotFound(currentProfile);
    }

    @PutMapping("/my-profile")
    public ResponseEntity<Profile> updateCurrentUserProfile(@Valid @RequestBody Profile profile) throws URISyntaxException {
        log.debug("REST request to update Profile : {}", profile);

        Optional<Profile> currentProfileOptional = profileRepository.findByUserLogin(SecurityUtils.getCurrentUserLogin().get());

        if (!currentProfileOptional.isPresent()) {
            return ResponseEntity
                .notFound()
                .headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "profileNotExist", "User does not have a profile"))
                .build();
        }

        profile.setId(currentProfileOptional.get().getId());
        profile.setUser(currentProfileOptional.get().getUser());
        Profile result = profileService.save(profile);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, profile.getId().toString()))
            .body(result);
    }


    /**
     * GET  /profiles : get all the profiles.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of profiles in body
     */
    @GetMapping("/profiles")
    public ResponseEntity<List<Profile>> getAllProfiles(ProfileCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Profiles by criteria: {}", criteria);
        Page<Profile> page = profileQueryService.findByCriteria(criteria, pageable);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/profiles");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /profiles/count : count all the profiles.
     *
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the count in body
     */
    @GetMapping("/profiles/count")
    public ResponseEntity<Long> countProfiles(ProfileCriteria criteria) {
        log.debug("REST request to count Profiles by criteria: {}", criteria);
        return ResponseEntity.ok().body(profileQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /profiles/:id : get the "id" profile.
     *
     * @param id the id of the profile to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the profile, or with status 404 (Not Found)
     */
    @GetMapping("/profiles/{id}")
    public ResponseEntity<Profile> getProfile(@PathVariable Long id) {
        log.debug("REST request to get Profile : {}", id);
        Optional<Profile> profile = profileService.findOne(id);
        return ResponseUtil.wrapOrNotFound(profile);
    }

    /**
     * DELETE  /profiles/:id : delete the "id" profile.
     *
     * @param id the id of the profile to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/profiles/{id}")
    public ResponseEntity<Void> deleteProfile(@PathVariable Long id) {
        log.debug("REST request to delete Profile : {}", id);
        profileService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    //https://planetcalc.com/7042/
    //https://www.latlong.net/
    @GetMapping("/profiles/by-location")
    public List<ProfileDistanceDTO> getEventsByLocationNear(
        @RequestParam("lat") Double latitude,
        @RequestParam("long") Double longitude,
        @RequestParam("d") Double distance) {

        final double[] d = new double[1];
        return profileRepository.findAll().stream()
            .filter(profile -> {
                log.debug("/profiles/by-location - filtering profile by location : {}", profile.getDisplayName());
                Location location = profile.getLocation();
                if (location == null) return false;
                d[0] = distance(location.getLatitude(), location.getLongitude(), latitude, longitude, "K");
                log.debug("/profiles/by-location - distance : {}", d[0]);
                if (d[0] <= distance) return true;
                else return false;
            })
            .map(profile -> new ProfileDistanceDTO(profile, d[0]))
            .sorted(Comparator.comparing(ProfileDistanceDTO::getDistance))
            .collect(Collectors.toList());
    }

    private static double distance(double lat1, double lon1, double lat2, double lon2, String unit) {
        if ((lat1 == lat2) && (lon1 == lon2)) {
            return 0;
        } else {
            double theta = lon1 - lon2;
            double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) +
                          Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
            dist = Math.acos(dist);
            dist = Math.toDegrees(dist);
            dist = dist * 60 * 1.1515;
            if (unit == "K") {
                dist = dist * 1.609344;
            } else if (unit == "N") {
                dist = dist * 0.8684;
            }
            return (dist);
        }
    }

}
