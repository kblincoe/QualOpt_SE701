package org.project36.qualopt.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.project36.qualopt.domain.Researcher;

import org.project36.qualopt.repository.ResearcherRepository;
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
 * REST controller for managing Researcher.
 */
@RestController
@RequestMapping("/api")
public class ResearcherResource {

    private final Logger log = LoggerFactory.getLogger(ResearcherResource.class);

    private static final String ENTITY_NAME = "researcher";

    private final ResearcherRepository researcherRepository;

    public ResearcherResource(ResearcherRepository researcherRepository) {
        this.researcherRepository = researcherRepository;
    }

    /**
     * POST  /researchers : Create a new researcher.
     *
     * @param researcher the researcher to create
     * @return the ResponseEntity with status 201 (Created) and with body the new researcher, or with status 400 (Bad Request) if the researcher has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/researchers")
    @Timed
    public ResponseEntity<Researcher> createResearcher(@RequestBody Researcher researcher) throws URISyntaxException {
        log.debug("REST request to save Researcher : {}", researcher);
        if (researcher.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new researcher cannot already have an ID")).body(null);
        }
        Researcher result = researcherRepository.save(researcher);
        return ResponseEntity.created(new URI("/api/researchers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /researchers : Updates an existing researcher.
     *
     * @param researcher the researcher to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated researcher,
     * or with status 400 (Bad Request) if the researcher is not valid,
     * or with status 500 (Internal Server Error) if the researcher couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/researchers")
    @Timed
    public ResponseEntity<Researcher> updateResearcher(@RequestBody Researcher researcher) throws URISyntaxException {
        log.debug("REST request to update Researcher : {}", researcher);
        if (researcher.getId() == null) {
            return createResearcher(researcher);
        }
        Researcher result = researcherRepository.save(researcher);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, researcher.getId().toString()))
            .body(result);
    }

    /**
     * GET  /researchers : get all the researchers.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of researchers in body
     */
    @GetMapping("/researchers")
    @Timed
    public List<Researcher> getAllResearchers() {
        log.debug("REST request to get all Researchers");
        return researcherRepository.findAll();
    }

    /**
     * GET  /researchers/:id : get the "id" researcher.
     *
     * @param id the id of the researcher to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the researcher, or with status 404 (Not Found)
     */
    @GetMapping("/researchers/{id}")
    @Timed
    public ResponseEntity<Researcher> getResearcher(@PathVariable Long id) {
        log.debug("REST request to get Researcher : {}", id);
        Researcher researcher = researcherRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(researcher));
    }

    /**
     * DELETE  /researchers/:id : delete the "id" researcher.
     *
     * @param id the id of the researcher to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/researchers/{id}")
    @Timed
    public ResponseEntity<Void> deleteResearcher(@PathVariable Long id) {
        log.debug("REST request to delete Researcher : {}", id);
        researcherRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
