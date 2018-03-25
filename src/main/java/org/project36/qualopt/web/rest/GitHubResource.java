package org.project36.qualopt.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.project36.qualopt.service.github.GitHubAPIRequest;
import org.project36.qualopt.service.github.GitHubAPIService;
import org.project36.qualopt.repository.ParticipantRepository;
import org.project36.qualopt.web.rest.errors.GHRateLimitReachedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.kohsuke.github.*;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * REST controller for managing Participant.
 */
@RestController
@RequestMapping("/api")
public class GitHubResource {

    public static final String ERROR_MESSAGE_WHEN_CANNOT_CONNECT = "Error when connecting to the GitHub API - ";
    public static final String ERROR_MESSAGE_WHEN_GITHUB_FAIL = "Error when contacting the GitHub API - ";
    public static final String ERROR_MESSAGE_WHEN_API_LIMIT_EXCEEDED = "Reached GitHub API Rate Limit - ";

    private final Logger log = LoggerFactory.getLogger(GitHubResource.class);

    private GitHubAPIService gitHubAPIService;

    public GitHubResource(ParticipantRepository participantRepository) {
        this.gitHubAPIService = new GitHubAPIService(participantRepository);
    }
     
     /**
     * POST /gitHubQuery : Create a new GitHub User Search and populate the Participant repository
     *                       with representations of the results
     *
     * @param gitHubAPIRequest the infomration from the API form that the user submitted
     * @return the Response with status 201 (Created) if the results were successfully added, or with status 400 (Bad Request) if an error occured, with different strings
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/gitHubQuery")
    @Timed
    public ResponseEntity<String> createGitHubAPIUserSearch(@RequestBody GitHubAPIRequest gitHubAPIRequest) throws URISyntaxException {
        log.debug("REST request to send GitHub API user search : {}", gitHubAPIRequest.toString());
        try{
            this.gitHubAPIService.startGitHubAPIUserSearch(gitHubAPIRequest);
        }catch(IOException e){
            log.debug("Unable To Connect to the GitHub API");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ERROR_MESSAGE_WHEN_CANNOT_CONNECT + e.getMessage());
        }catch(GHException e) {
            log.debug("Error sending GitHub API Request, but was able to connect");
            return ResponseEntity.badRequest()
                .body(ERROR_MESSAGE_WHEN_GITHUB_FAIL + e.getMessage());
        }catch(GHRateLimitReachedException e){
            log.debug("Error contacting GitHub API - Reached API Rate Limit");
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .body(ERROR_MESSAGE_WHEN_API_LIMIT_EXCEEDED + e.getMessage());
        }
        log.debug("Succesfully completed GitHub API user search, sending an OK HTTP Request");
        return ResponseEntity.ok()
            .body(gitHubAPIRequest.toString());
    } 

    /**
     * Method used to promote testability - can mock GitHubAPIService and set it
     * with this method. Thus, when a POST call is made to the ResourceHandler we're able
     * to change the behaviour (ie. avoid making network calls).
     */
    public void setGitHubAPIServiceMock(GitHubAPIService gitHubAPIService){
        this.gitHubAPIService = gitHubAPIService;
    }
}