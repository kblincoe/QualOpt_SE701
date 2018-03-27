package org.project36.qualopt.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.project36.qualopt.domain.EmailTemplate;
import org.project36.qualopt.repository.EmailTemplateRepository;
import org.project36.qualopt.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

/**
 * REST controller for managing Email Templates.
 * 
 * Supported operation:
 * 
 * POST: 		create an email template
 * PUT:			update an email template
 * GET:			gets all the email template
 * GET/login:	gets all the email template for a specific user login
 * DELETE:		delete an email template
 */
@RestController
@RequestMapping("/api")
public class EmailTemplateResource {

    private final Logger log = LoggerFactory.getLogger(EmailTemplateResource.class);
    
    private static final String ENTITY_NAME = "email_template";

    private final EmailTemplateRepository emailTemplateRepository;

    public EmailTemplateResource(EmailTemplateRepository emailRepository) {
        this.emailTemplateRepository = emailRepository;
    }

    /**
     * POST  /emailTemplates : Create a new email Template.
     *
     * @param emailTemplate the email template to create
     * @return the ResponseEntity with status 201 (Created) and with body the new email template, 
     * 			or with status 400 (Bad Request) if the email has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/emailTemplates")
    @Timed
    public ResponseEntity<EmailTemplate> createEmail(@RequestBody EmailTemplate emailTemplate) throws URISyntaxException {
        log.debug("REST request to save Email template: {}", emailTemplate);
        if (emailTemplate.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new email template cannot already have an ID")).body(null);
        }
        EmailTemplate result = emailTemplateRepository.save(emailTemplate);
        return ResponseEntity.created(new URI("/api/emailTemplates/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /emailTemplates : Updates an existing email.
     *
     * @param emailTemplate the email template to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated email,
     * 			status 400 (Bad Request) if the name of the template is missing.
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/emailTemplates")
    @Timed
    public ResponseEntity<EmailTemplate> updateEmail(@RequestBody EmailTemplate emailTemplate) throws URISyntaxException {
        log.debug("REST request to update EmailTemplate : {}", emailTemplate);
        if (emailTemplate.getId() == null) {
            return createEmail(emailTemplate);
        }
        
        if (emailTemplate.getName() == null){
        	return ResponseEntity.badRequest().build();
        }
        
        
        EmailTemplate result = emailTemplateRepository.save(emailTemplate);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, emailTemplate.getId().toString()))
            .body(result);
    }

    /**
     * GET  /emailTemplates : get all the email templates
     *
     * @return the ResponseEntity with status 200 (OK) and the list of email templates in body
     */
    @GetMapping("/emailTemplates")
    @Timed
    public List<EmailTemplate> getAllEmailTemplates() {
        log.debug("REST request to get all Email Templates");
        return emailTemplateRepository.findAll();
    }

    /**
     * GET  /emailTemplates/:id : get the email templates associated with the user.
     *
     * @param userLogin the login of the user account
     * @return the ResponseEntity with status 200 (OK) and with body the email template, or with status 404 (Not Found)
     */
    @GetMapping("/emailTemplates/{userLogin}")
    @Timed
    public List<EmailTemplate> getEmail(@PathVariable String userLogin) {
        log.debug("REST request to get Email Template with userId: {}", userLogin);
        List<EmailTemplate> templates = emailTemplateRepository.findAllByUserLogin((userLogin));
        log.debug("Retrieved " + templates.size() + " templates");
        return templates;
    }

    /**
     * DELETE  /emailTemplates/:id : delete the "id" email.
     *
     * @param id the id of the email to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/emailTemplates/{id}")
    @Timed
    public ResponseEntity<Void> deleteEmail(@PathVariable Long id) {
        log.debug("REST request to delete Email Template: {}", id);
        emailTemplateRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
