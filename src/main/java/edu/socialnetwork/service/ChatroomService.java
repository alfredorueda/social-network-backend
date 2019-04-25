package edu.socialnetwork.service;

import edu.socialnetwork.domain.Chatroom;
import edu.socialnetwork.repository.ChatroomRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing Chatroom.
 */
@Service
@Transactional
public class ChatroomService {

    private final Logger log = LoggerFactory.getLogger(ChatroomService.class);

    private final ChatroomRepository chatroomRepository;

    public ChatroomService(ChatroomRepository chatroomRepository) {
        this.chatroomRepository = chatroomRepository;
    }

    /**
     * Save a chatroom.
     *
     * @param chatroom the entity to save
     * @return the persisted entity
     */
    public Chatroom save(Chatroom chatroom) {
        log.debug("Request to save Chatroom : {}", chatroom);
        return chatroomRepository.save(chatroom);
    }

    /**
     * Get all the chatrooms.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Chatroom> findAll(Pageable pageable) {
        log.debug("Request to get all Chatrooms");
        return chatroomRepository.findAll(pageable);
    }

    /**
     * Get all the Chatroom with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    public Page<Chatroom> findAllWithEagerRelationships(Pageable pageable) {
        return chatroomRepository.findAllWithEagerRelationships(pageable);
    }
    

    /**
     * Get one chatroom by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<Chatroom> findOne(Long id) {
        log.debug("Request to get Chatroom : {}", id);
        return chatroomRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the chatroom by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Chatroom : {}", id);
        chatroomRepository.deleteById(id);
    }
}
