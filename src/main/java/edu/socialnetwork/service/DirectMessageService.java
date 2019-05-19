package edu.socialnetwork.service;

import edu.socialnetwork.domain.DirectMessage;
import edu.socialnetwork.repository.DirectMessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing DirectMessage.
 */
@Service
@Transactional
public class DirectMessageService {

    private final Logger log = LoggerFactory.getLogger(DirectMessageService.class);

    private final DirectMessageRepository directMessageRepository;

    public DirectMessageService(DirectMessageRepository directMessageRepository) {
        this.directMessageRepository = directMessageRepository;
    }

    /**
     * Save a directMessage.
     *
     * @param directMessage the entity to save
     * @return the persisted entity
     */
    public DirectMessage save(DirectMessage directMessage) {
        log.debug("Request to save DirectMessage : {}", directMessage);
        return directMessageRepository.save(directMessage);
    }

    /**
     * Get all the directMessages.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<DirectMessage> findAll(Pageable pageable) {
        log.debug("Request to get all DirectMessages");
        return directMessageRepository.findAll(pageable);
    }


    /**
     * Get one directMessage by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<DirectMessage> findOne(Long id) {
        log.debug("Request to get DirectMessage : {}", id);
        return directMessageRepository.findById(id);
    }

    /**
     * Delete the directMessage by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete DirectMessage : {}", id);
        directMessageRepository.deleteById(id);
    }
}
