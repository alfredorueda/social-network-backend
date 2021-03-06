package edu.socialnetwork.service;

import edu.socialnetwork.domain.Relationship;
import edu.socialnetwork.repository.RelationshipRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing Relationship.
 */
@Service
@Transactional
public class RelationshipService {

    private final Logger log = LoggerFactory.getLogger(RelationshipService.class);

    private final RelationshipRepository relationshipRepository;

    public RelationshipService(RelationshipRepository relationshipRepository) {
        this.relationshipRepository = relationshipRepository;
    }

    /**
     * Save a relationship.
     *
     * @param relationship the entity to save
     * @return the persisted entity
     */
    public Relationship save(Relationship relationship) {
        log.debug("Request to save Relationship : {}", relationship);
        return relationshipRepository.save(relationship);
    }

    /**
     * Get all the relationships.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Relationship> findAll(Pageable pageable) {
        log.debug("Request to get all Relationships");
        return relationshipRepository.findAll(pageable);
    }


    /**
     * Get one relationship by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<Relationship> findOne(Long id) {
        log.debug("Request to get Relationship : {}", id);
        return relationshipRepository.findById(id);
    }

    /**
     * Delete the relationship by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Relationship : {}", id);
        relationshipRepository.deleteById(id);
    }
}
