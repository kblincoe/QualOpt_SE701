package org.project36.qualopt.repository;

import java.util.List;

import org.project36.qualopt.domain.EmailTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Email Template entity.
 */
@Repository
public interface EmailTemplateRepository extends JpaRepository<EmailTemplate, Long> {

	List<EmailTemplate> findAll();
	
	@Query("SELECT et FROM EmailTemplate et WHERE et.user.login = :login")
	List<EmailTemplate> findAllByUserLogin(@Param("login") String login);
}
