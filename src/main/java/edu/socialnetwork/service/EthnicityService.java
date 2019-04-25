package edu.socialnetwork.service;

import edu.socialnetwork.domain.Ethnicity;
import edu.socialnetwork.repository.EthnicityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing Ethnicity.
 */
@Service
@Transactional
public class EthnicityService {

    private final Logger log = LoggerFactory.getLogger(EthnicityService.class);

    private final EthnicityRepository ethnicityRepository;

    public EthnicityService(EthnicityRepository ethnicityRepository) {
        this.ethnicityRepository = ethnicityRepository;
    }

    /**
     * Save a ethnicity.
     *
     * @param ethnicity the entity to save
     * @return the persisted entity
     */
    public Ethnicity save(Ethnicity ethnicity) {
        log.debug("Request to save Ethnicity : {}", ethnicity);
        return ethnicityRepository.save(ethnicity);
    }

    /**
     * Get all the ethnicities.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Ethnicity> findAll(Pageable pageable) {
        log.debug("Request to get all Ethnicities");
        return ethnicityRepository.findAll(pageable);
    }


    /**
     * Get one ethnicity by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<Ethnicity> findOne(Long id) {
        log.debug("Request to get Ethnicity : {}", id);
        return ethnicityRepository.findById(id);
    }

    /**
     * Delete the ethnicity by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Ethnicity : {}", id);
        ethnicityRepository.deleteById(id);
    }
}
