package edu.socialnetwork.web.rest;
import edu.socialnetwork.domain.Ethnicity;
import edu.socialnetwork.service.EthnicityService;
import edu.socialnetwork.web.rest.errors.BadRequestAlertException;
import edu.socialnetwork.web.rest.util.HeaderUtil;
import edu.socialnetwork.web.rest.util.PaginationUtil;
import edu.socialnetwork.service.dto.EthnicityCriteria;
import edu.socialnetwork.service.EthnicityQueryService;
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
 * REST controller for managing Ethnicity.
 */
@RestController
@RequestMapping("/api")
public class EthnicityResource {

    private final Logger log = LoggerFactory.getLogger(EthnicityResource.class);

    private static final String ENTITY_NAME = "ethnicity";

    private final EthnicityService ethnicityService;

    private final EthnicityQueryService ethnicityQueryService;

    public EthnicityResource(EthnicityService ethnicityService, EthnicityQueryService ethnicityQueryService) {
        this.ethnicityService = ethnicityService;
        this.ethnicityQueryService = ethnicityQueryService;
    }

    /**
     * POST  /ethnicities : Create a new ethnicity.
     *
     * @param ethnicity the ethnicity to create
     * @return the ResponseEntity with status 201 (Created) and with body the new ethnicity, or with status 400 (Bad Request) if the ethnicity has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/ethnicities")
    public ResponseEntity<Ethnicity> createEthnicity(@Valid @RequestBody Ethnicity ethnicity) throws URISyntaxException {
        log.debug("REST request to save Ethnicity : {}", ethnicity);
        if (ethnicity.getId() != null) {
            throw new BadRequestAlertException("A new ethnicity cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Ethnicity result = ethnicityService.save(ethnicity);
        return ResponseEntity.created(new URI("/api/ethnicities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /ethnicities : Updates an existing ethnicity.
     *
     * @param ethnicity the ethnicity to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated ethnicity,
     * or with status 400 (Bad Request) if the ethnicity is not valid,
     * or with status 500 (Internal Server Error) if the ethnicity couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/ethnicities")
    public ResponseEntity<Ethnicity> updateEthnicity(@Valid @RequestBody Ethnicity ethnicity) throws URISyntaxException {
        log.debug("REST request to update Ethnicity : {}", ethnicity);
        if (ethnicity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Ethnicity result = ethnicityService.save(ethnicity);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, ethnicity.getId().toString()))
            .body(result);
    }

    /**
     * GET  /ethnicities : get all the ethnicities.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of ethnicities in body
     */
    @GetMapping("/ethnicities")
    public ResponseEntity<List<Ethnicity>> getAllEthnicities(EthnicityCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Ethnicities by criteria: {}", criteria);
        Page<Ethnicity> page = ethnicityQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/ethnicities");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /ethnicities/count : count all the ethnicities.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/ethnicities/count")
    public ResponseEntity<Long> countEthnicities(EthnicityCriteria criteria) {
        log.debug("REST request to count Ethnicities by criteria: {}", criteria);
        return ResponseEntity.ok().body(ethnicityQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /ethnicities/:id : get the "id" ethnicity.
     *
     * @param id the id of the ethnicity to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the ethnicity, or with status 404 (Not Found)
     */
    @GetMapping("/ethnicities/{id}")
    public ResponseEntity<Ethnicity> getEthnicity(@PathVariable Long id) {
        log.debug("REST request to get Ethnicity : {}", id);
        Optional<Ethnicity> ethnicity = ethnicityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ethnicity);
    }

    /**
     * DELETE  /ethnicities/:id : delete the "id" ethnicity.
     *
     * @param id the id of the ethnicity to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/ethnicities/{id}")
    public ResponseEntity<Void> deleteEthnicity(@PathVariable Long id) {
        log.debug("REST request to delete Ethnicity : {}", id);
        ethnicityService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
