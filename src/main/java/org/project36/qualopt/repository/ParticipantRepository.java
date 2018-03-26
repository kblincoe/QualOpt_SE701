package org.project36.qualopt.repository;

import org.project36.qualopt.domain.Participant;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Participant entity.
 */
// @SuppressWarnings("unused")
@Repository
public interface ParticipantRepository extends JpaRepository<Participant,Long> {
    
}
