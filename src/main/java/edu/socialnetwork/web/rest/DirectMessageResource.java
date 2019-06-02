package edu.socialnetwork.web.rest;

import edu.socialnetwork.domain.DirectMessage;
import edu.socialnetwork.domain.Profile;
import edu.socialnetwork.repository.InvitationRepository;
import edu.socialnetwork.repository.ProfileRepository;
import edu.socialnetwork.security.SecurityUtils;
import edu.socialnetwork.service.DirectMessageQueryService;
import edu.socialnetwork.service.DirectMessageService;
import edu.socialnetwork.service.dto.DirectMessageCriteria;
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
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing DirectMessage.
 */
@RestController
@RequestMapping("/api")
public class DirectMessageResource {

    private final Logger log = LoggerFactory.getLogger(DirectMessageResource.class);

    private static final String ENTITY_NAME = "directMessage";

    private final DirectMessageService directMessageService;

    private final DirectMessageQueryService directMessageQueryService;

    private final ProfileRepository profileRepository;

    private final InvitationRepository invitationRepository;

    public DirectMessageResource(DirectMessageService directMessageService,
                                 DirectMessageQueryService directMessageQueryService,
                                 ProfileRepository profileRepository,
                                 InvitationRepository invitationRepository) {
        this.directMessageService = directMessageService;
        this.directMessageQueryService = directMessageQueryService;
        this.profileRepository = profileRepository;
        this.invitationRepository = invitationRepository;
    }

    /**
     * POST  /direct-messages : Create a new directMessage.
     *
     * @param directMessage the directMessage to create
     * @return the ResponseEntity with status 201 (Created) and with body the new directMessage, or with status 400 (Bad Request) if the directMessage has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/direct-messages")
    public ResponseEntity<DirectMessage> createDirectMessage(@RequestBody DirectMessage directMessage) throws URISyntaxException {
        log.debug("REST request to save DirectMessage : {}", directMessage);
        if (directMessage.getId() != null) {
            throw new BadRequestAlertException("A new directMessage cannot already have an ID", ENTITY_NAME, "idexists");
        }

        if (directMessage.getRecipient() == null) {
            throw new BadRequestAlertException("Profile recipient is null", ENTITY_NAME, "recipientisnull");
        }

        if (!profileRepository.findById(directMessage.getRecipient().getId()).isPresent()) {
            throw new BadRequestAlertException("Profile recipient id is invalid", ENTITY_NAME, "recipientIdInvalid");
        }

        DirectMessage newDM = new DirectMessage();
        String userLogin = SecurityUtils.getCurrentUserLogin().get();

        Optional<Profile> optionalProfile = profileRepository.findByUserLogin(userLogin);

        if (!optionalProfile.isPresent()) {
            throw new BadRequestAlertException("username " + userLogin + " has no profile" , ENTITY_NAME, "profileNotFound");
        }

        Profile profile = optionalProfile.get();
        newDM.setSender(profile);
        newDM.setRecipient(directMessage.getRecipient());
        newDM.setCreatedDate(ZonedDateTime.now(ZoneId.systemDefault()));
        newDM.setPicture(directMessage.getPicture());
        newDM.setPictureContentType(directMessage.getPictureContentType());
        newDM.setMessage(directMessage.getMessage());
        newDM.setUrl(directMessage.getUrl());

        List<Profile> friends = invitationRepository.findAcceptedInvitations(profile)
            .stream().map( invitation -> {
                    Profile friend;
                    if (invitation.getReceived().equals(profile)) friend = invitation.getSent();
                    else friend = invitation.getReceived();

                    return friend;
                }
            ).collect(Collectors.toList());

        boolean theyAreFriends = friends.contains(newDM.getRecipient());

        if(!theyAreFriends) {
            throw new BadRequestAlertException("You are not a friend of " + newDM.getRecipient().getDisplayName(), ENTITY_NAME, "noFriendWithRecipient");
        }

        DirectMessage result = directMessageService.save(newDM);
        return ResponseEntity.created(new URI("/api/direct-messages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /direct-messages : Updates an existing directMessage.
     *
     * @param directMessage the directMessage to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated directMessage,
     * or with status 400 (Bad Request) if the directMessage is not valid,
     * or with status 500 (Internal Server Error) if the directMessage couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/direct-messages")
    public ResponseEntity<DirectMessage> updateDirectMessage(@Valid @RequestBody DirectMessage directMessage) throws URISyntaxException {
        log.debug("REST request to update DirectMessage : {}", directMessage);
        if (directMessage.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DirectMessage result = directMessageService.save(directMessage);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, directMessage.getId().toString()))
            .body(result);
    }

    /**
     * GET  /direct-messages : get all the directMessages.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of directMessages in body
     */
    @GetMapping("/direct-messages")
    public ResponseEntity<List<DirectMessage>> getAllDirectMessages(DirectMessageCriteria criteria, Pageable pageable) {
        log.debug("REST request to get DirectMessages by criteria: {}", criteria);
        Page<DirectMessage> page = directMessageQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/direct-messages");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /direct-messages/count : count all the directMessages.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/direct-messages/count")
    public ResponseEntity<Long> countDirectMessages(DirectMessageCriteria criteria) {
        log.debug("REST request to count DirectMessages by criteria: {}", criteria);
        return ResponseEntity.ok().body(directMessageQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /direct-messages/:id : get the "id" directMessage.
     *
     * @param id the id of the directMessage to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the directMessage, or with status 404 (Not Found)
     */
    @GetMapping("/direct-messages/{id}")
    public ResponseEntity<DirectMessage> getDirectMessage(@PathVariable Long id) {
        log.debug("REST request to get DirectMessage : {}", id);
        Optional<DirectMessage> directMessage = directMessageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(directMessage);
    }

    /**
     * DELETE  /direct-messages/:id : delete the "id" directMessage.
     *
     * @param id the id of the directMessage to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/direct-messages/{id}")
    public ResponseEntity<Void> deleteDirectMessage(@PathVariable Long id) {
        log.debug("REST request to delete DirectMessage : {}", id);
        directMessageService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
