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

import edu.socialnetwork.domain.Block;
import edu.socialnetwork.domain.*; // for static metamodels
import edu.socialnetwork.repository.BlockRepository;
import edu.socialnetwork.service.dto.BlockCriteria;

/**
 * Service for executing complex queries for Block entities in the database.
 * The main input is a {@link BlockCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Block} or a {@link Page} of {@link Block} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BlockQueryService extends QueryService<Block> {

    private final Logger log = LoggerFactory.getLogger(BlockQueryService.class);

    private final BlockRepository blockRepository;

    public BlockQueryService(BlockRepository blockRepository) {
        this.blockRepository = blockRepository;
    }

    /**
     * Return a {@link List} of {@link Block} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Block> findByCriteria(BlockCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Block> specification = createSpecification(criteria);
        return blockRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Block} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Block> findByCriteria(BlockCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Block> specification = createSpecification(criteria);
        return blockRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BlockCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Block> specification = createSpecification(criteria);
        return blockRepository.count(specification);
    }

    /**
     * Function to convert BlockCriteria to a {@link Specification}
     */
    private Specification<Block> createSpecification(BlockCriteria criteria) {
        Specification<Block> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Block_.id));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), Block_.createdDate));
            }
            if (criteria.getSentId() != null) {
                specification = specification.and(buildSpecification(criteria.getSentId(),
                    root -> root.join(Block_.sent, JoinType.LEFT).get(Profile_.id)));
            }
            if (criteria.getReceivedId() != null) {
                specification = specification.and(buildSpecification(criteria.getReceivedId(),
                    root -> root.join(Block_.received, JoinType.LEFT).get(Profile_.id)));
            }
        }
        return specification;
    }
}
