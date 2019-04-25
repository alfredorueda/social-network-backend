package edu.socialnetwork.web.rest;

import edu.socialnetwork.domain.Invitation;
import edu.socialnetwork.domain.Profile;
import edu.socialnetwork.repository.InvitationRepository;
import edu.socialnetwork.repository.ProfileRepository;
import edu.socialnetwork.repository.UserRepository;
import edu.socialnetwork.security.SecurityUtils;
import edu.socialnetwork.service.InvitationService;
import edu.socialnetwork.web.rest.errors.BadRequestAlertException;
import edu.socialnetwork.web.rest.util.HeaderUtil;
import edu.socialnetwork.web.rest.util.PaginationUtil;
import edu.socialnetwork.service.dto.InvitationCriteria;
import edu.socialnetwork.service.InvitationQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * REST controller for managing Invitation.
 */
@RestController
@RequestMapping("/api")
public class InvitationResource {

    private final Logger log = LoggerFactory.getLogger(InvitationResource.class);

    private static final String ENTITY_NAME = "invitation";

    private final InvitationService invitationService;

    private final InvitationQueryService invitationQueryService;

    private final UserRepository userRepository;

    private final InvitationRepository invitationRepository;

    private final ProfileRepository profileRepository;

    public InvitationResource(InvitationService invitationService, InvitationQueryService invitationQueryService, UserRepository userRepository, InvitationRepository invitationRepository, ProfileRepository profileRepository) {
        this.invitationService = invitationService;
        this.invitationQueryService = invitationQueryService;
        this.userRepository = userRepository;
        this.invitationRepository = invitationRepository;
        this.profileRepository = profileRepository;
    }

    /**
     * POST  /invitations : Create a new invitation.
     *
     * @param invitation the invitation to create
     * @return the ResponseEntity with status 201 (Created) and with body the new invitation, or with status 400 (Bad Request) if the invitation has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/invitations")
    public ResponseEntity<Invitation> createInvitation(@RequestBody Invitation invitation) throws URISyntaxException {
        log.debug("REST request to save Invitation : {}", invitation);
        if (invitation.getId() != null) {
            throw new BadRequestAlertException("A new invitation cannot already have an ID", ENTITY_NAME, "idexists");
        }

        Invitation result = invitationService.save(invitation);
        return ResponseEntity.created(new URI("/api/invitations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /invitations : Updates an existing invitation.
     *
     * @param invitation the invitation to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated invitation,
     * or with status 400 (Bad Request) if the invitation is not valid,
     * or with status 500 (Internal Server Error) if the invitation couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/invitations")
    public ResponseEntity<Invitation> updateInvitation(@RequestBody Invitation invitation) throws URISyntaxException {
        log.debug("REST request to update Invitation : {}", invitation);
        if (invitation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        invitation.setCreatedDate(invitationService.findOne(invitation.getId()).get().getCreatedDate());

        Invitation result = invitationService.save(invitation);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, invitation.getId().toString()))
            .body(result);
    }

    /**
     * GET  /invitations : get all the invitations.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of invitations in body
     */
    @GetMapping("/invitations")
    public ResponseEntity<List<Invitation>> getAllInvitations(InvitationCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Invitations by criteria: {}", criteria);
        Page<Invitation> page = invitationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/invitations");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /invitations/count : count all the invitations.
     *
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the count in body
     */
    @GetMapping("/invitations/count")
    public ResponseEntity<Long> countInvitations(InvitationCriteria criteria) {
        log.debug("REST request to count Invitations by criteria: {}", criteria);
        return ResponseEntity.ok().body(invitationQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /invitations/:id : get the "id" invitation.
     *
     * @param id the id of the invitation to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the invitation, or with status 404 (Not Found)
     */
    @GetMapping("/invitations/{id}")
    public ResponseEntity<Invitation> getInvitation(@PathVariable Long id) {
        log.debug("REST request to get Invitation : {}", id);
        Optional<Invitation> invitation = invitationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(invitation);
    }

    /**
     * DELETE  /invitations/:id : delete the "id" invitation.
     *
     * @param id the id of the invitation to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/invitations/{id}")
    public ResponseEntity<Void> deleteInvitation(@PathVariable Long id) {
        log.debug("REST request to delete Invitation : {}", id);
        invitationService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }


    @GetMapping("/pending-invites")
    public ResponseEntity<List<Invitation>> getPendingInvites(Pageable pageable) {
        log.debug("REST request to get pending invitations: {}");
        Page<Invitation> invitations = invitationRepository.findByAcceptedIsNullAndReceivedUserLogin(
            SecurityUtils.getCurrentUserLogin().get(),
            pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(invitations, "/api/pending-invites");
        return ResponseEntity.ok().headers(headers).body(invitations.getContent());
    }

    @PostMapping("/invite/{userId}")
    public ResponseEntity<Invitation> createInvite(@PathVariable Long userId) throws URISyntaxException {
        String sender = SecurityUtils.getCurrentUserLogin().get();
        String receiver = userRepository.findOneWithAuthoritiesById(userId).get().getLogin();

        if (!invitationRepository.findBySentUserLoginAndReceivedUserLogin(sender, receiver).isEmpty()
            || !invitationRepository.findBySentUserLoginAndReceivedUserLogin(receiver, sender).isEmpty()) {
            throw new BadRequestAlertException("Invite already exists between " + sender + " and " + receiver, ENTITY_NAME, "inviteExist");
        }

        Invitation invitation = new Invitation();
        invitation.setCreatedDate(ZonedDateTime.now());
        invitation.setSent(profileRepository.findByUserLogin(sender).get());
        invitation.setReceived(profileRepository.findByUserLogin(receiver).get());

        log.debug("REST request to save Invitation : {}", invitation);

        Invitation result = invitationService.save(invitation);
        return ResponseEntity.created(new URI("/api/invite/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/invite/{id}/state/{state}")
    public ResponseEntity<Invitation> changeInviteState(Long id, boolean state) throws URISyntaxException {
        Optional<Invitation> invitationOptional = invitationRepository.findById(id);

        if(!invitationOptional.isPresent()) {
            throw new BadRequestAlertException("Invite "+ id +" does not exists ", ENTITY_NAME, "inviteDoesNotExist");
        }

        Invitation invitation = invitationOptional.get();
        String userLogin = SecurityUtils.getCurrentUserLogin().get();
        Profile profile = profileRepository.findByUserLogin(userLogin).get();

        if(!invitation.getReceived().equals(profile)) {
            throw new BadRequestAlertException("Invite "+ id +" is not addressed to " + userLogin, ENTITY_NAME, "inviteDoesNotBelongToUser");
        }

        invitation.setAccepted(state);

        Invitation result = invitationRepository.save(invitation);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, invitation.getId().toString()))
            .body(result);

    }

    @GetMapping("/accepted-invites")
    public ResponseEntity<List<Invitation>> getAcceptedInvites(Pageable pageable) {
        log.debug("REST request to get accepted invitations: {}");

        String userLogin = SecurityUtils.getCurrentUserLogin().get();
        Profile profile = profileRepository.findByUserLogin(userLogin).get();

        Page<Invitation> invitations = invitationRepository.findAcceptedInvitations(
            profile,
            pageable);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(invitations, "/api/accepted-invites");
        return ResponseEntity.ok().headers(headers).body(invitations.getContent());
    }

    @GetMapping("/my-friends")
    public ResponseEntity<List<Profile>> getMyFriends(Pageable pageable) {
        log.debug("REST request to get current user friends: {}");

        String userLogin = SecurityUtils.getCurrentUserLogin().get();
        Profile profile = profileRepository.findByUserLogin(userLogin).get();

        Page<Profile> friends = invitationRepository.findAcceptedInvitations(profile, pageable)

            .map( invitation -> {
                Profile friend;
                if (invitation.getReceived().equals(profile)) friend = invitation.getSent();
                else friend = invitation.getReceived();

                return friend;
            }
        );


        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(friends, "/api/my-friends");
        return ResponseEntity.ok().headers(headers).body(friends.getContent());
    }
}
