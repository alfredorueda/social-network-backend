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

import edu.socialnetwork.domain.DirectMessage;
import edu.socialnetwork.domain.*; // for static metamodels
import edu.socialnetwork.repository.DirectMessageRepository;
import edu.socialnetwork.service.dto.DirectMessageCriteria;

/**
 * Service for executing complex queries for DirectMessage entities in the database.
 * The main input is a {@link DirectMessageCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DirectMessage} or a {@link Page} of {@link DirectMessage} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DirectMessageQueryService extends QueryService<DirectMessage> {

    private final Logger log = LoggerFactory.getLogger(DirectMessageQueryService.class);

    private final DirectMessageRepository directMessageRepository;

    public DirectMessageQueryService(DirectMessageRepository directMessageRepository) {
        this.directMessageRepository = directMessageRepository;
    }

    /**
     * Return a {@link List} of {@link DirectMessage} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DirectMessage> findByCriteria(DirectMessageCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<DirectMessage> specification = createSpecification(criteria);
        return directMessageRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link DirectMessage} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DirectMessage> findByCriteria(DirectMessageCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DirectMessage> specification = createSpecification(criteria);
        return directMessageRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DirectMessageCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<DirectMessage> specification = createSpecification(criteria);
        return directMessageRepository.count(specification);
    }

    /**
     * Function to convert DirectMessageCriteria to a {@link Specification}
     */
    private Specification<DirectMessage> createSpecification(DirectMessageCriteria criteria) {
        Specification<DirectMessage> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), DirectMessage_.id));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), DirectMessage_.createdDate));
            }
            if (criteria.getMessage() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMessage(), DirectMessage_.message));
            }
            if (criteria.getUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUrl(), DirectMessage_.url));
            }
            if (criteria.getSenderId() != null) {
                specification = specification.and(buildSpecification(criteria.getSenderId(),
                    root -> root.join(DirectMessage_.sender, JoinType.LEFT).get(Profile_.id)));
            }
            if (criteria.getRecipientId() != null) {
                specification = specification.and(buildSpecification(criteria.getRecipientId(),
                    root -> root.join(DirectMessage_.recipient, JoinType.LEFT).get(Profile_.id)));
            }
        }
        return specification;
    }
}
