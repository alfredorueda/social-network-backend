package edu.socialnetwork.repository;

import edu.socialnetwork.domain.Chatroom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Chatroom entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChatroomRepository extends JpaRepository<Chatroom, Long>, JpaSpecificationExecutor<Chatroom> {

    @Query(value = "select distinct chatroom from Chatroom chatroom left join fetch chatroom.participants",
        countQuery = "select count(distinct chatroom) from Chatroom chatroom")
    Page<Chatroom> findAllWithEagerRelationships(Pageable pageable);

    @Query(value = "select distinct chatroom from Chatroom chatroom left join fetch chatroom.participants")
    List<Chatroom> findAllWithEagerRelationships();

    @Query("select chatroom from Chatroom chatroom left join fetch chatroom.participants where chatroom.id =:id")
    Optional<Chatroom> findOneWithEagerRelationships(@Param("id") Long id);

}
