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

import edu.socialnetwork.domain.Gender;
import edu.socialnetwork.domain.*; // for static metamodels
import edu.socialnetwork.repository.GenderRepository;
import edu.socialnetwork.service.dto.GenderCriteria;

/**
 * Service for executing complex queries for Gender entities in the database.
 * The main input is a {@link GenderCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Gender} or a {@link Page} of {@link Gender} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class GenderQueryService extends QueryService<Gender> {

    private final Logger log = LoggerFactory.getLogger(GenderQueryService.class);

    private final GenderRepository genderRepository;

    public GenderQueryService(GenderRepository genderRepository) {
        this.genderRepository = genderRepository;
    }

    /**
     * Return a {@link List} of {@link Gender} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Gender> findByCriteria(GenderCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Gender> specification = createSpecification(criteria);
        return genderRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Gender} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Gender> findByCriteria(GenderCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Gender> specification = createSpecification(criteria);
        return genderRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(GenderCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Gender> specification = createSpecification(criteria);
        return genderRepository.count(specification);
    }

    /**
     * Function to convert GenderCriteria to a {@link Specification}
     */
    private Specification<Gender> createSpecification(GenderCriteria criteria) {
        Specification<Gender> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Gender_.id));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getType(), Gender_.type));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildSpecification(criteria.getUserId(),
                    root -> root.join(Gender_.users, JoinType.LEFT).get(Profile_.id)));
            }
        }
        return specification;
    }
}
