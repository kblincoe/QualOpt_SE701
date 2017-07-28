package org.project36.qualopt.repository;

import org.project36.qualopt.domain.Researcher;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Researcher entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ResearcherRepository extends JpaRepository<Researcher,Long> {
    
}
