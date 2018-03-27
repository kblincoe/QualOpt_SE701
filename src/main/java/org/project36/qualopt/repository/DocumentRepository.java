package org.project36.qualopt.repository;

import org.project36.qualopt.domain.Document;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Email entity.
 */
@Repository
public interface DocumentRepository extends JpaRepository<Document,Long> {
    
}
