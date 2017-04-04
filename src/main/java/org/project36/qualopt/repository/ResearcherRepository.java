package org.project36.qualopt.repository;

import org.project36.qualopt.domain.Researcher;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Researcher entity.
 */
@SuppressWarnings("unused")
public interface ResearcherRepository extends JpaRepository<Researcher,Long> {

}
