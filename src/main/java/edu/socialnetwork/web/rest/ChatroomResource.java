package edu.socialnetwork.web.rest;
import edu.socialnetwork.domain.Chatroom;
import edu.socialnetwork.service.ChatroomService;
import edu.socialnetwork.web.rest.errors.BadRequestAlertException;
import edu.socialnetwork.web.rest.util.HeaderUtil;
import edu.socialnetwork.web.rest.util.PaginationUtil;
import edu.socialnetwork.service.dto.ChatroomCriteria;
import edu.socialnetwork.service.ChatroomQueryService;
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
 * REST controller for managing Chatroom.
 */
@RestController
@RequestMapping("/api")
public class ChatroomResource {

    private final Logger log = LoggerFactory.getLogger(ChatroomResource.class);

    private static final String ENTITY_NAME = "chatroom";

    private final ChatroomService chatroomService;

    private final ChatroomQueryService chatroomQueryService;

    public ChatroomResource(ChatroomService chatroomService, ChatroomQueryService chatroomQueryService) {
        this.chatroomService = chatroomService;
        this.chatroomQueryService = chatroomQueryService;
    }

    /**
     * POST  /chatrooms : Create a new chatroom.
     *
     * @param chatroom the chatroom to create
     * @return the ResponseEntity with status 201 (Created) and with body the new chatroom, or with status 400 (Bad Request) if the chatroom has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/chatrooms")
    public ResponseEntity<Chatroom> createChatroom(@Valid @RequestBody Chatroom chatroom) throws URISyntaxException {
        log.debug("REST request to save Chatroom : {}", chatroom);
        if (chatroom.getId() != null) {
            throw new BadRequestAlertException("A new chatroom cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Chatroom result = chatroomService.save(chatroom);
        return ResponseEntity.created(new URI("/api/chatrooms/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /chatrooms : Updates an existing chatroom.
     *
     * @param chatroom the chatroom to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated chatroom,
     * or with status 400 (Bad Request) if the chatroom is not valid,
     * or with status 500 (Internal Server Error) if the chatroom couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/chatrooms")
    public ResponseEntity<Chatroom> updateChatroom(@Valid @RequestBody Chatroom chatroom) throws URISyntaxException {
        log.debug("REST request to update Chatroom : {}", chatroom);
        if (chatroom.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Chatroom result = chatroomService.save(chatroom);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, chatroom.getId().toString()))
            .body(result);
    }

    /**
     * GET  /chatrooms : get all the chatrooms.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of chatrooms in body
     */
    @GetMapping("/chatrooms")
    public ResponseEntity<List<Chatroom>> getAllChatrooms(ChatroomCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Chatrooms by criteria: {}", criteria);
        Page<Chatroom> page = chatroomQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/chatrooms");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /chatrooms/count : count all the chatrooms.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/chatrooms/count")
    public ResponseEntity<Long> countChatrooms(ChatroomCriteria criteria) {
        log.debug("REST request to count Chatrooms by criteria: {}", criteria);
        return ResponseEntity.ok().body(chatroomQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /chatrooms/:id : get the "id" chatroom.
     *
     * @param id the id of the chatroom to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the chatroom, or with status 404 (Not Found)
     */
    @GetMapping("/chatrooms/{id}")
    public ResponseEntity<Chatroom> getChatroom(@PathVariable Long id) {
        log.debug("REST request to get Chatroom : {}", id);
        Optional<Chatroom> chatroom = chatroomService.findOne(id);
        return ResponseUtil.wrapOrNotFound(chatroom);
    }

    /**
     * DELETE  /chatrooms/:id : delete the "id" chatroom.
     *
     * @param id the id of the chatroom to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/chatrooms/{id}")
    public ResponseEntity<Void> deleteChatroom(@PathVariable Long id) {
        log.debug("REST request to delete Chatroom : {}", id);
        chatroomService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
