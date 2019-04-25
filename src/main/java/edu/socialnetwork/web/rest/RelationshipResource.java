package edu.socialnetwork.web.rest;
import edu.socialnetwork.domain.Relationship;
import edu.socialnetwork.service.RelationshipService;
import edu.socialnetwork.web.rest.errors.BadRequestAlertException;
import edu.socialnetwork.web.rest.util.HeaderUtil;
import edu.socialnetwork.web.rest.util.PaginationUtil;
import edu.socialnetwork.service.dto.RelationshipCriteria;
import edu.socialnetwork.service.RelationshipQueryService;
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
 * REST controller for managing Relationship.
 */
@RestController
@RequestMapping("/api")
public class RelationshipResource {

    private final Logger log = LoggerFactory.getLogger(RelationshipResource.class);

    private static final String ENTITY_NAME = "relationship";

    private final RelationshipService relationshipService;

    private final RelationshipQueryService relationshipQueryService;

    public RelationshipResource(RelationshipService relationshipService, RelationshipQueryService relationshipQueryService) {
        this.relationshipService = relationshipService;
        this.relationshipQueryService = relationshipQueryService;
    }

    /**
     * POST  /relationships : Create a new relationship.
     *
     * @param relationship the relationship to create
     * @return the ResponseEntity with status 201 (Created) and with body the new relationship, or with status 400 (Bad Request) if the relationship has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/relationships")
    public ResponseEntity<Relationship> createRelationship(@Valid @RequestBody Relationship relationship) throws URISyntaxException {
        log.debug("REST request to save Relationship : {}", relationship);
        if (relationship.getId() != null) {
            throw new BadRequestAlertException("A new relationship cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Relationship result = relationshipService.save(relationship);
        return ResponseEntity.created(new URI("/api/relationships/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /relationships : Updates an existing relationship.
     *
     * @param relationship the relationship to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated relationship,
     * or with status 400 (Bad Request) if the relationship is not valid,
     * or with status 500 (Internal Server Error) if the relationship couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/relationships")
    public ResponseEntity<Relationship> updateRelationship(@Valid @RequestBody Relationship relationship) throws URISyntaxException {
        log.debug("REST request to update Relationship : {}", relationship);
        if (relationship.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Relationship result = relationshipService.save(relationship);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, relationship.getId().toString()))
            .body(result);
    }

    /**
     * GET  /relationships : get all the relationships.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of relationships in body
     */
    @GetMapping("/relationships")
    public ResponseEntity<List<Relationship>> getAllRelationships(RelationshipCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Relationships by criteria: {}", criteria);
        Page<Relationship> page = relationshipQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/relationships");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /relationships/count : count all the relationships.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/relationships/count")
    public ResponseEntity<Long> countRelationships(RelationshipCriteria criteria) {
        log.debug("REST request to count Relationships by criteria: {}", criteria);
        return ResponseEntity.ok().body(relationshipQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /relationships/:id : get the "id" relationship.
     *
     * @param id the id of the relationship to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the relationship, or with status 404 (Not Found)
     */
    @GetMapping("/relationships/{id}")
    public ResponseEntity<Relationship> getRelationship(@PathVariable Long id) {
        log.debug("REST request to get Relationship : {}", id);
        Optional<Relationship> relationship = relationshipService.findOne(id);
        return ResponseUtil.wrapOrNotFound(relationship);
    }

    /**
     * DELETE  /relationships/:id : delete the "id" relationship.
     *
     * @param id the id of the relationship to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/relationships/{id}")
    public ResponseEntity<Void> deleteRelationship(@PathVariable Long id) {
        log.debug("REST request to delete Relationship : {}", id);
        relationshipService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
