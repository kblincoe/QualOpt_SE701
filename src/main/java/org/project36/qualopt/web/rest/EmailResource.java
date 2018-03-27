package org.project36.qualopt.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.project36.qualopt.domain.Email;

import org.project36.qualopt.repository.EmailRepository;
import org.project36.qualopt.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Email.
 */
@RestController
@RequestMapping("/api")
public class EmailResource {

    private final Logger log = LoggerFactory.getLogger(EmailResource.class);

    private static final String ENTITY_NAME = "email";

    private final EmailRepository emailRepository;

    public EmailResource(EmailRepository emailRepository) {
        this.emailRepository = emailRepository;
    }

    /**
     * POST  /emails : Create a new email.
     *
     * @param email the email to create
     * @return the ResponseEntity with status 201 (Created) and with body the new email, or with status 400 (Bad Request) if the email has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/emails")
    @Timed
    public ResponseEntity<Email> createEmail(@RequestBody Email email) throws URISyntaxException {
        log.debug("REST request to save Email : {}", email);
        if (email.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new email cannot already have an ID")).body(null);
        }
        Email result = emailRepository.save(email);
        return ResponseEntity.created(new URI("/api/emails/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /emails : Updates an existing email.
     *
     * @param email the email to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated email,
     * or with status 400 (Bad Request) if the email is not valid,
     * or with status 500 (Internal Server Error) if the email couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/emails")
    @Timed
    public ResponseEntity<Email> updateEmail(@RequestBody Email email) throws URISyntaxException {
        log.debug("REST request to update Email : {}", email);
        if (email.getId() == null) {
            return createEmail(email);
        }
        Email result = emailRepository.save(email);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, email.getId().toString()))
            .body(result);
    }

    /**
     * GET  /emails : get all the emails.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of emails in body
     */
    @GetMapping("/emails")
    @Timed
    public List<Email> getAllEmails() {
        log.debug("REST request to get all Emails");
        return emailRepository.findAll();
    }

    /**
     * GET  /emails/:id : get the "id" email.
     *
     * @param id the id of the email to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the email, or with status 404 (Not Found)
     */
    @GetMapping("/emails/{id}")
    @Timed
    public ResponseEntity<Email> getEmail(@PathVariable Long id) {
        log.debug("REST request to get Email : {}", id);
        Optional<Email> email = emailRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(email);
    }

    /**
     * DELETE  /emails/:id : delete the "id" email.
     *
     * @param id the id of the email to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/emails/{id}")
    @Timed
    public ResponseEntity<Void> deleteEmail(@PathVariable Long id) {
        log.debug("REST request to delete Email : {}", id);
        emailRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
