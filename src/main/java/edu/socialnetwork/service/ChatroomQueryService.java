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

import edu.socialnetwork.domain.Chatroom;
import edu.socialnetwork.domain.*; // for static metamodels
import edu.socialnetwork.repository.ChatroomRepository;
import edu.socialnetwork.service.dto.ChatroomCriteria;

/**
 * Service for executing complex queries for Chatroom entities in the database.
 * The main input is a {@link ChatroomCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Chatroom} or a {@link Page} of {@link Chatroom} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ChatroomQueryService extends QueryService<Chatroom> {

    private final Logger log = LoggerFactory.getLogger(ChatroomQueryService.class);

    private final ChatroomRepository chatroomRepository;

    public ChatroomQueryService(ChatroomRepository chatroomRepository) {
        this.chatroomRepository = chatroomRepository;
    }

    /**
     * Return a {@link List} of {@link Chatroom} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Chatroom> findByCriteria(ChatroomCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Chatroom> specification = createSpecification(criteria);
        return chatroomRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Chatroom} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Chatroom> findByCriteria(ChatroomCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Chatroom> specification = createSpecification(criteria);
        return chatroomRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ChatroomCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Chatroom> specification = createSpecification(criteria);
        return chatroomRepository.count(specification);
    }

    /**
     * Function to convert ChatroomCriteria to a {@link Specification}
     */
    private Specification<Chatroom> createSpecification(ChatroomCriteria criteria) {
        Specification<Chatroom> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Chatroom_.id));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), Chatroom_.createdDate));
            }
            if (criteria.getTopic() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTopic(), Chatroom_.topic));
            }
            if (criteria.getAdminId() != null) {
                specification = specification.and(buildSpecification(criteria.getAdminId(),
                    root -> root.join(Chatroom_.admin, JoinType.LEFT).get(Profile_.id)));
            }
            if (criteria.getParticipantId() != null) {
                specification = specification.and(buildSpecification(criteria.getParticipantId(),
                    root -> root.join(Chatroom_.participants, JoinType.LEFT).get(Profile_.id)));
            }
            if (criteria.getMessageId() != null) {
                specification = specification.and(buildSpecification(criteria.getMessageId(),
                    root -> root.join(Chatroom_.messages, JoinType.LEFT).get(Message_.id)));
            }
        }
        return specification;
    }
}
