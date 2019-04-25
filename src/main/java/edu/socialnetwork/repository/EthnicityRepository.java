package edu.socialnetwork.repository;

import edu.socialnetwork.domain.Ethnicity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Ethnicity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EthnicityRepository extends JpaRepository<Ethnicity, Long>, JpaSpecificationExecutor<Ethnicity> {

}
