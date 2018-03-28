package org.project36.qualopt.repository;

import org.project36.qualopt.domain.Email;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Email entity.
 */
// @SuppressWarnings("unused")
@Repository
public interface EmailRepository extends JpaRepository<Email,Long> {
    
}
