package edu.socialnetwork.repository;

import edu.socialnetwork.domain.Invitation;
import edu.socialnetwork.domain.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the Invitation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InvitationRepository extends JpaRepository<Invitation, Long>, JpaSpecificationExecutor<Invitation> {

    Page<Invitation> findByAcceptedIsNullAndReceivedUserLogin(String login, Pageable pageable);

    List<Invitation> findBySentUserLoginAndReceivedUserLogin(String sender, String receiver);

    @Query("SELECT invitation FROM Invitation invitation WHERE invitation.accepted=true AND " +
        "(invitation.received=:user OR invitation.sent=:user) ")
    Page<Invitation> findAcceptedInvitations(@Param("user") Profile profile, Pageable pageable);
}
