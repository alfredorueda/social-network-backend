package edu.socialnetwork.web.rest;
import edu.socialnetwork.domain.Gender;
import edu.socialnetwork.service.GenderService;
import edu.socialnetwork.web.rest.errors.BadRequestAlertException;
import edu.socialnetwork.web.rest.util.HeaderUtil;
import edu.socialnetwork.web.rest.util.PaginationUtil;
import edu.socialnetwork.service.dto.GenderCriteria;
import edu.socialnetwork.service.GenderQueryService;
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

/**
 * REST controller for managing Gender.
 */
@RestController
@RequestMapping("/api")
public class GenderResource {

    private final Logger log = LoggerFactory.getLogger(GenderResource.class);

    private static final String ENTITY_NAME = "gender";

    private final GenderService genderService;

    private final GenderQueryService genderQueryService;

    public GenderResource(GenderService genderService, GenderQueryService genderQueryService) {
        this.genderService = genderService;
        this.genderQueryService = genderQueryService;
    }

    /**
     * POST  /genders : Create a new gender.
     *
     * @param gender the gender to create
     * @return the ResponseEntity with status 201 (Created) and with body the new gender, or with status 400 (Bad Request) if the gender has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/genders")
    public ResponseEntity<Gender> createGender(@Valid @RequestBody Gender gender) throws URISyntaxException {
        log.debug("REST request to save Gender : {}", gender);
        if (gender.getId() != null) {
            throw new BadRequestAlertException("A new gender cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Gender result = genderService.save(gender);
        return ResponseEntity.created(new URI("/api/genders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /genders : Updates an existing gender.
     *
     * @param gender the gender to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated gender,
     * or with status 400 (Bad Request) if the gender is not valid,
     * or with status 500 (Internal Server Error) if the gender couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/genders")
    public ResponseEntity<Gender> updateGender(@Valid @RequestBody Gender gender) throws URISyntaxException {
        log.debug("REST request to update Gender : {}", gender);
        if (gender.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Gender result = genderService.save(gender);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, gender.getId().toString()))
            .body(result);
    }

    /**
     * GET  /genders : get all the genders.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of genders in body
     */
    @GetMapping("/genders")
    public ResponseEntity<List<Gender>> getAllGenders(GenderCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Genders by criteria: {}", criteria);
        Page<Gender> page = genderQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/genders");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /genders/count : count all the genders.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/genders/count")
    public ResponseEntity<Long> countGenders(GenderCriteria criteria) {
        log.debug("REST request to count Genders by criteria: {}", criteria);
        return ResponseEntity.ok().body(genderQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /genders/:id : get the "id" gender.
     *
     * @param id the id of the gender to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the gender, or with status 404 (Not Found)
     */
    @GetMapping("/genders/{id}")
    public ResponseEntity<Gender> getGender(@PathVariable Long id) {
        log.debug("REST request to get Gender : {}", id);
        Optional<Gender> gender = genderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(gender);
    }

    /**
     * DELETE  /genders/:id : delete the "id" gender.
     *
     * @param id the id of the gender to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/genders/{id}")
    public ResponseEntity<Void> deleteGender(@PathVariable Long id) {
        log.debug("REST request to delete Gender : {}", id);
        genderService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
