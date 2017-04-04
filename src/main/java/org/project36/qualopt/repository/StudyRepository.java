package org.project36.qualopt.repository;

import org.project36.qualopt.domain.Study;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Study entity.
 */
@SuppressWarnings("unused")
public interface StudyRepository extends JpaRepository<Study,Long> {

}
