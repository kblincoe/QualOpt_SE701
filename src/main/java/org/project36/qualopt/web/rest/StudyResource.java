package org.project36.qualopt.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;
import org.project36.qualopt.domain.Study;
import org.project36.qualopt.domain.User;
import org.project36.qualopt.repository.StudyRepository;
import org.project36.qualopt.repository.UserRepository;
import org.project36.qualopt.service.StudyService;
import org.project36.qualopt.service.dto.StudyInfoDTO;
import org.project36.qualopt.web.rest.util.HeaderUtil;
import org.project36.qualopt.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * REST controller for managing Study.
 */
@RestController
@RequestMapping("/api")
public class StudyResource {

    private final Logger log = LoggerFactory.getLogger(StudyResource.class);

    private static final String ENTITY_NAME = "study";

    private final StudyService studyService;

    private final UserRepository UserRepository;

    private final StudyRepository studyRepository;

    public StudyResource(StudyRepository studyRepository, StudyService studyService, UserRepository UserRepository) {
        this.studyRepository = studyRepository;
        this.studyService = studyService;
        this.UserRepository = UserRepository;
    }

    /**
     * POST  /studies : Create a new study.
     *
     * @param study the study to create
     * @return the ResponseEntity with status 201 (Created) and with body the new study, or with status 400 (Bad Request) if the study has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/studies")
    @Timed
    public ResponseEntity<Study> createStudy(@Valid @RequestBody Study study) throws URISyntaxException {
        log.debug("REST request to save Study : {}", study);
        if (study.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new study cannot already have an ID")).body(null);
        }
        User user = new User();
        user = UserRepository.findOneByLogin(getCurrentUserLogin()).get();
        study.setUser(user);
        Study result = studyRepository.save(study);
        return ResponseEntity.created(new URI("/api/studies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getName()))
            .body(result);
    }

    /**
     * PUT  /studies : Updates an existing study.
     *
     * @param study the study to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated study,
     * or with status 400 (Bad Request) if the study is not valid,
     * or with status 500 (Internal Server Error) if the study couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/studies")
    @Timed
    public ResponseEntity<Study> updateStudy(@Valid @RequestBody Study study) throws URISyntaxException {
        log.debug("REST request to update Study : {}", study);
        if (study.getId() == null) {
            return createStudy(study);
        }
        Study result = studyRepository.save(study);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, study.getName()))
            .body(result);
    }

    /**
     * GET  /studies : get all the studies.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of studies in body
     */
    @GetMapping("/studies")
    @Timed
    public ResponseEntity<List<Study>> getAllStudies(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Studies");
        Page<Study> page = studyRepository.findByUserIsCurrentUser(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/studies");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /studies/:id : get the "id" study.
     *
     * @param id the id of the study to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the study, or with status 404 (Not Found)
     */
    @GetMapping("/studies/{id}")
    @Timed
    public ResponseEntity<Study> getStudy(@PathVariable Long id) {
        log.debug("REST request to get Study : {}", id);
        Study study = studyRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(study));
    }

    /**
     * GET  /studies/info/:id : get the study info with the specified id.
     *
     * @param id the id of the study, from which the study info will be retrieved
     * @return the ResponseEntity with status 200 (OK) and with body the study, or with status 404 (Not Found)
     */
    @GetMapping("/studies/{id}/info")
    @Timed
    public ResponseEntity<StudyInfoDTO> getStudyInfo(@PathVariable Long id) {
        log.debug("REST request to get study info: {}", id);
        Study study = studyRepository.findOneWithEagerRelationships(id);
        if(study==null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        StudyInfoDTO studyInfo = new StudyInfoDTO(study);
        return new ResponseEntity<>(studyInfo, HttpStatus.OK);
    }

    /**
     * DELETE  /studies/:id : delete the "id" study.
     *
     * @param id the id of the study to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/studies/{id}")
    @Timed
    public ResponseEntity<Void> deleteStudy(@PathVariable Long id) {
        log.debug("REST request to delete Study : {}", id);
        studyRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    public String getCurrentUserLogin() {
        org.springframework.security.core.context.SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String login = null;
        if (authentication != null) {
            if (authentication.getPrincipal() instanceof UserDetails) {
                login = ((UserDetails) authentication.getPrincipal()).getUsername();
            }
            else if (authentication.getPrincipal() instanceof String) {
                login = (String) authentication.getPrincipal();
            }
        }
        return login;
    }

    /**
     * POST  /studies/send : send the study.
     *
     * @param study the study to send
     * @return the ResponseEntity with status 201 (Created) if the study was sent or 400 (Bad Request) if the study doesn't exist
     */
    @PostMapping(path = "/studies/send",
        produces={MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE})
    @Timed
    public ResponseEntity<Object> sendStudy(@Valid @RequestBody Study study) throws URISyntaxException {
        log.debug("REST request to send Study : {}", study);
        if (study == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Set<String> bouncedMail = studyService.sendInvitationEmail(study);
        return ResponseEntity
            .created(new URI("/api/studies/send"))
            .header("Sent: " + study.getName())
            .body(bouncedMail);
    }
}
