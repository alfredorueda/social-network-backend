package edu.socialnetwork.repository;

import edu.socialnetwork.domain.Block;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Block entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BlockRepository extends JpaRepository<Block, Long>, JpaSpecificationExecutor<Block> {

}
