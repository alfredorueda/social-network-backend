package edu.socialnetwork.repository;

import edu.socialnetwork.domain.Relationship;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Relationship entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RelationshipRepository extends JpaRepository<Relationship, Long>, JpaSpecificationExecutor<Relationship> {

}
