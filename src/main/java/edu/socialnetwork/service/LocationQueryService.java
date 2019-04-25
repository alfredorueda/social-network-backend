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

import edu.socialnetwork.domain.Location;
import edu.socialnetwork.domain.*; // for static metamodels
import edu.socialnetwork.repository.LocationRepository;
import edu.socialnetwork.service.dto.LocationCriteria;

/**
 * Service for executing complex queries for Location entities in the database.
 * The main input is a {@link LocationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Location} or a {@link Page} of {@link Location} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LocationQueryService extends QueryService<Location> {

    private final Logger log = LoggerFactory.getLogger(LocationQueryService.class);

    private final LocationRepository locationRepository;

    public LocationQueryService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    /**
     * Return a {@link List} of {@link Location} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Location> findByCriteria(LocationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Location> specification = createSpecification(criteria);
        return locationRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Location} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Location> findByCriteria(LocationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Location> specification = createSpecification(criteria);
        return locationRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LocationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Location> specification = createSpecification(criteria);
        return locationRepository.count(specification);
    }

    /**
     * Function to convert LocationCriteria to a {@link Specification}
     */
    private Specification<Location> createSpecification(LocationCriteria criteria) {
        Specification<Location> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Location_.id));
            }
            if (criteria.getLatitude() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLatitude(), Location_.latitude));
            }
            if (criteria.getLongitude() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLongitude(), Location_.longitude));
            }
            if (criteria.getUrlGoogleMaps() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUrlGoogleMaps(), Location_.urlGoogleMaps));
            }
            if (criteria.getUrlOpenStreetMap() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUrlOpenStreetMap(), Location_.urlOpenStreetMap));
            }
            if (criteria.getAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAddress(), Location_.address));
            }
            if (criteria.getPostalCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPostalCode(), Location_.postalCode));
            }
            if (criteria.getCity() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCity(), Location_.city));
            }
            if (criteria.getStateProvice() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStateProvice(), Location_.stateProvice));
            }
            if (criteria.getCounty() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCounty(), Location_.county));
            }
            if (criteria.getCountry() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCountry(), Location_.country));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildSpecification(criteria.getUserId(),
                    root -> root.join(Location_.user, JoinType.LEFT).get(Profile_.id)));
            }
            if (criteria.getMessageId() != null) {
                specification = specification.and(buildSpecification(criteria.getMessageId(),
                    root -> root.join(Location_.message, JoinType.LEFT).get(Message_.id)));
            }
        }
        return specification;
    }
}
