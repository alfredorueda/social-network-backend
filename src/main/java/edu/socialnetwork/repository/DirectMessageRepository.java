package edu.socialnetwork.repository;

import edu.socialnetwork.domain.DirectMessage;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the DirectMessage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DirectMessageRepository extends JpaRepository<DirectMessage, Long>, JpaSpecificationExecutor<DirectMessage> {

}
