package edu.socialnetwork.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import edu.socialnetwork.domain.Profile;
import edu.socialnetwork.domain.*; // for static metamodels
import edu.socialnetwork.repository.ProfileRepository;
import edu.socialnetwork.service.dto.ProfileCriteria;

/**
 * Service for executing complex queries for Profile entities in the database.
 * The main input is a {@link ProfileCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Profile} or a {@link Page} of {@link Profile} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProfileQueryService extends QueryService<Profile> {

    private final Logger log = LoggerFactory.getLogger(ProfileQueryService.class);

    private final ProfileRepository profileRepository;

    public ProfileQueryService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    /**
     * Return a {@link List} of {@link Profile} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Profile> findByCriteria(ProfileCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Profile> specification = createSpecification(criteria);
        return profileRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Profile} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Profile> findByCriteria(ProfileCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Profile> specification = createSpecification(criteria);
        return profileRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProfileCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Profile> specification = createSpecification(criteria);
        return profileRepository.count(specification);
    }

    /**
     * Function to convert ProfileCriteria to a {@link Specification}
     */
    private Specification<Profile> createSpecification(ProfileCriteria criteria) {
        Specification<Profile> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Profile_.id));
            }
            if (criteria.getBirthDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBirthDate(), Profile_.birthDate));
            }
            if (criteria.getHeight() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getHeight(), Profile_.height));
            }
            if (criteria.getWeight() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getWeight(), Profile_.weight));
            }
            if (criteria.getUnitSystem() != null) {
                specification = specification.and(buildSpecification(criteria.getUnitSystem(), Profile_.unitSystem));
            }
            if (criteria.getAboutMe() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAboutMe(), Profile_.aboutMe));
            }
            if (criteria.getDisplayName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDisplayName(), Profile_.displayName));
            }
            if (criteria.getShowAge() != null) {
                specification = specification.and(buildSpecification(criteria.getShowAge(), Profile_.showAge));
            }
            if (criteria.getBanned() != null) {
                specification = specification.and(buildSpecification(criteria.getBanned(), Profile_.banned));
            }
            if (criteria.getFilterPreferences() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFilterPreferences(), Profile_.filterPreferences));
            }
            if (criteria.getLocationId() != null) {
                specification = specification.and(buildSpecification(criteria.getLocationId(),
                    root -> root.join(Profile_.location, JoinType.LEFT).get(Location_.id)));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildSpecification(criteria.getUserId(),
                    root -> root.join(Profile_.user, JoinType.LEFT).get(User_.id)));
            }
            if (criteria.getRelationshipId() != null) {
                specification = specification.and(buildSpecification(criteria.getRelationshipId(),
                    root -> root.join(Profile_.relationship, JoinType.LEFT).get(Relationship_.id)));
            }
            if (criteria.getGenderId() != null) {
                specification = specification.and(buildSpecification(criteria.getGenderId(),
                    root -> root.join(Profile_.gender, JoinType.LEFT).get(Gender_.id)));
            }
            if (criteria.getEthnicityId() != null) {
                specification = specification.and(buildSpecification(criteria.getEthnicityId(),
                    root -> root.join(Profile_.ethnicity, JoinType.LEFT).get(Ethnicity_.id)));
            }
            if (criteria.getSentInvitationId() != null) {
                specification = specification.and(buildSpecification(criteria.getSentInvitationId(),
                    root -> root.join(Profile_.sentInvitations, JoinType.LEFT).get(Invitation_.id)));
            }
            if (criteria.getReceivedInvitationId() != null) {
                specification = specification.and(buildSpecification(criteria.getReceivedInvitationId(),
                    root -> root.join(Profile_.receivedInvitations, JoinType.LEFT).get(Invitation_.id)));
            }
            if (criteria.getSentBlockId() != null) {
                specification = specification.and(buildSpecification(criteria.getSentBlockId(),
                    root -> root.join(Profile_.sentBlocks, JoinType.LEFT).get(Block_.id)));
            }
            if (criteria.getReceivedBlockId() != null) {
                specification = specification.and(buildSpecification(criteria.getReceivedBlockId(),
                    root -> root.join(Profile_.receivedBlocks, JoinType.LEFT).get(Block_.id)));
            }
            if (criteria.getSentMessageId() != null) {
                specification = specification.and(buildSpecification(criteria.getSentMessageId(),
                    root -> root.join(Profile_.sentMessages, JoinType.LEFT).get(Message_.id)));
            }
            if (criteria.getSentDirectMessageId() != null) {
                specification = specification.and(buildSpecification(criteria.getSentDirectMessageId(),
                    root -> root.join(Profile_.sentDirectMessages, JoinType.LEFT).get(DirectMessage_.id)));
            }
            if (criteria.getReceivedDirectMessageId() != null) {
                specification = specification.and(buildSpecification(criteria.getReceivedDirectMessageId(),
                    root -> root.join(Profile_.receivedDirectMessages, JoinType.LEFT).get(DirectMessage_.id)));
            }
            if (criteria.getAdminChatroomId() != null) {
                specification = specification.and(buildSpecification(criteria.getAdminChatroomId(),
                    root -> root.join(Profile_.adminChatrooms, JoinType.LEFT).get(Chatroom_.id)));
            }
            if (criteria.getJoinedChatroomId() != null) {
                specification = specification.and(buildSpecification(criteria.getJoinedChatroomId(),
                    root -> root.join(Profile_.joinedChatrooms, JoinType.LEFT).get(Chatroom_.id)));
            }
        }
        return specification;
    }
}
