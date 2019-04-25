package edu.socialnetwork.service;

import edu.socialnetwork.domain.Location;
import edu.socialnetwork.repository.LocationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Service Implementation for managing Location.
 */
@Service
@Transactional
public class LocationService {

    private final Logger log = LoggerFactory.getLogger(LocationService.class);

    private final LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    /**
     * Save a location.
     *
     * @param location the entity to save
     * @return the persisted entity
     */
    public Location save(Location location) {
        log.debug("Request to save Location : {}", location);
        return locationRepository.save(location);
    }

    /**
     * Get all the locations.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Location> findAll(Pageable pageable) {
        log.debug("Request to get all Locations");
        return locationRepository.findAll(pageable);
    }



    /**
     *  get all the locations where User is null.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<Location> findAllWhereUserIsNull() {
        log.debug("Request to get all locations where User is null");
        return StreamSupport
            .stream(locationRepository.findAll().spliterator(), false)
            .filter(location -> location.getUser() == null)
            .collect(Collectors.toList());
    }


    /**
     *  get all the locations where Message is null.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<Location> findAllWhereMessageIsNull() {
        log.debug("Request to get all locations where Message is null");
        return StreamSupport
            .stream(locationRepository.findAll().spliterator(), false)
            .filter(location -> location.getMessage() == null)
            .collect(Collectors.toList());
    }

    /**
     * Get one location by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<Location> findOne(Long id) {
        log.debug("Request to get Location : {}", id);
        return locationRepository.findById(id);
    }

    /**
     * Delete the location by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Location : {}", id);
        locationRepository.deleteById(id);
    }
}
