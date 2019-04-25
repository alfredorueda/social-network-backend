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

import edu.socialnetwork.domain.Ethnicity;
import edu.socialnetwork.domain.*; // for static metamodels
import edu.socialnetwork.repository.EthnicityRepository;
import edu.socialnetwork.service.dto.EthnicityCriteria;

/**
 * Service for executing complex queries for Ethnicity entities in the database.
 * The main input is a {@link EthnicityCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Ethnicity} or a {@link Page} of {@link Ethnicity} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EthnicityQueryService extends QueryService<Ethnicity> {

    private final Logger log = LoggerFactory.getLogger(EthnicityQueryService.class);

    private final EthnicityRepository ethnicityRepository;

    public EthnicityQueryService(EthnicityRepository ethnicityRepository) {
        this.ethnicityRepository = ethnicityRepository;
    }

    /**
     * Return a {@link List} of {@link Ethnicity} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Ethnicity> findByCriteria(EthnicityCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Ethnicity> specification = createSpecification(criteria);
        return ethnicityRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Ethnicity} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Ethnicity> findByCriteria(EthnicityCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Ethnicity> specification = createSpecification(criteria);
        return ethnicityRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EthnicityCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Ethnicity> specification = createSpecification(criteria);
        return ethnicityRepository.count(specification);
    }

    /**
     * Function to convert EthnicityCriteria to a {@link Specification}
     */
    private Specification<Ethnicity> createSpecification(EthnicityCriteria criteria) {
        Specification<Ethnicity> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Ethnicity_.id));
            }
            if (criteria.getEthnicity() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEthnicity(), Ethnicity_.ethnicity));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildSpecification(criteria.getUserId(),
                    root -> root.join(Ethnicity_.users, JoinType.LEFT).get(Profile_.id)));
            }
        }
        return specification;
    }
}
