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

import edu.socialnetwork.domain.Message;
import edu.socialnetwork.domain.*; // for static metamodels
import edu.socialnetwork.repository.MessageRepository;
import edu.socialnetwork.service.dto.MessageCriteria;

/**
 * Service for executing complex queries for Message entities in the database.
 * The main input is a {@link MessageCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Message} or a {@link Page} of {@link Message} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MessageQueryService extends QueryService<Message> {

    private final Logger log = LoggerFactory.getLogger(MessageQueryService.class);

    private final MessageRepository messageRepository;

    public MessageQueryService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    /**
     * Return a {@link List} of {@link Message} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Message> findByCriteria(MessageCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Message> specification = createSpecification(criteria);
        return messageRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Message} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Message> findByCriteria(MessageCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Message> specification = createSpecification(criteria);
        return messageRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MessageCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Message> specification = createSpecification(criteria);
        return messageRepository.count(specification);
    }

    /**
     * Function to convert MessageCriteria to a {@link Specification}
     */
    private Specification<Message> createSpecification(MessageCriteria criteria) {
        Specification<Message> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Message_.id));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), Message_.createdDate));
            }
            if (criteria.getMessage() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMessage(), Message_.message));
            }
            if (criteria.getUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUrl(), Message_.url));
            }
            if (criteria.getLocationId() != null) {
                specification = specification.and(buildSpecification(criteria.getLocationId(),
                    root -> root.join(Message_.location, JoinType.LEFT).get(Location_.id)));
            }
            if (criteria.getSenderId() != null) {
                specification = specification.and(buildSpecification(criteria.getSenderId(),
                    root -> root.join(Message_.sender, JoinType.LEFT).get(Profile_.id)));
            }
            if (criteria.getChatroomId() != null) {
                specification = specification.and(buildSpecification(criteria.getChatroomId(),
                    root -> root.join(Message_.chatroom, JoinType.LEFT).get(Chatroom_.id)));
            }
        }
        return specification;
    }
}
