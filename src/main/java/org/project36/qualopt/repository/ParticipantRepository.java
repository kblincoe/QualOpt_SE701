package org.project36.qualopt.repository;

import org.project36.qualopt.domain.Participant;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

/**
 * Spring Data JPA repository for the Participant entity.
 */
// @SuppressWarnings("unused")
@Repository
public interface ParticipantRepository extends JpaRepository<Participant,Long> {

    //This query will eagerly fetch the participant with all of their corresponding studies
    @Query("select participant from Participant participant left join fetch participant.studies where participant.email =:email")
    Participant findOneByEmail(@Param("email") String email);
}
