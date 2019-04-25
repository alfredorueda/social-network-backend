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

import edu.socialnetwork.domain.Invitation;
import edu.socialnetwork.domain.*; // for static metamodels
import edu.socialnetwork.repository.InvitationRepository;
import edu.socialnetwork.service.dto.InvitationCriteria;

/**
 * Service for executing complex queries for Invitation entities in the database.
 * The main input is a {@link InvitationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Invitation} or a {@link Page} of {@link Invitation} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InvitationQueryService extends QueryService<Invitation> {

    private final Logger log = LoggerFactory.getLogger(InvitationQueryService.class);

    private final InvitationRepository invitationRepository;

    public InvitationQueryService(InvitationRepository invitationRepository) {
        this.invitationRepository = invitationRepository;
    }

    /**
     * Return a {@link List} of {@link Invitation} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Invitation> findByCriteria(InvitationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Invitation> specification = createSpecification(criteria);
        return invitationRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Invitation} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Invitation> findByCriteria(InvitationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Invitation> specification = createSpecification(criteria);
        return invitationRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(InvitationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Invitation> specification = createSpecification(criteria);
        return invitationRepository.count(specification);
    }

    /**
     * Function to convert InvitationCriteria to a {@link Specification}
     */
    private Specification<Invitation> createSpecification(InvitationCriteria criteria) {
        Specification<Invitation> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Invitation_.id));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), Invitation_.createdDate));
            }
            if (criteria.getAccepted() != null) {
                specification = specification.and(buildSpecification(criteria.getAccepted(), Invitation_.accepted));
            }
            if (criteria.getSentId() != null) {
                specification = specification.and(buildSpecification(criteria.getSentId(),
                    root -> root.join(Invitation_.sent, JoinType.LEFT).get(Profile_.id)));
            }
            if (criteria.getReceivedId() != null) {
                specification = specification.and(buildSpecification(criteria.getReceivedId(),
                    root -> root.join(Invitation_.received, JoinType.LEFT).get(Profile_.id)));
            }
        }
        return specification;
    }
}
