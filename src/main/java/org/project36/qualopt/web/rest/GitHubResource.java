package org.project36.qualopt.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.project36.qualopt.domain.Participant;
import org.project36.qualopt.service.github.GitHubAPIRequest;
import org.project36.qualopt.repository.ParticipantRepository;
import org.project36.qualopt.web.rest.errors.GHRateLimitReachedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.kohsuke.github.*;
import java.io.IOException;
import java.lang.Integer;
import java.net.URISyntaxException;
import java.util.List;

/**
 * REST controller for managing Participant.
 */
@RestController
@RequestMapping("/api")
public class GitHubResource {

    private final Logger log = LoggerFactory.getLogger(GitHubResource.class);

    private final ParticipantRepository participantRepository;

    public GitHubResource(ParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
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
            sendGitHubAPIUserSearch(gitHubAPIRequest);
        }catch(IOException e){
            log.debug("Unable To Connect to the GitHub API");
            return ResponseEntity.badRequest()
                .body("Error when contacting the GitHub API - " + e.getMessage());
        }catch(GHException e) {
            log.debug("Error sending GitHub API Request, but was able to connect");
            return ResponseEntity.badRequest()
                .body("Error when contacting the GitHub API - " + e.getMessage());
        }catch(GHRateLimitReachedException e){
            log.debug("Error contacting GitHub API - Reached API Rate Limit");
            return ResponseEntity.badRequest()
                .body("Error contacting GitHub API - Reached API Rate Limit");
        }
        log.debug("Succesfully completed GitHub API user searcgh, sending an OK HTTP Request");
        return ResponseEntity.ok()
            .body(gitHubAPIRequest.toString());
    }

    /**
     * Helper method to connect to the GitHub API and subsequently send the user search request. Uses
     * the kohsuke.github maven repository in order to provide an object model for the different GitHub
     * resources. Please refer to http://github-api.kohsuke.org/apidocs/index.html for more information.
     * 
     * TODO: Add a timeout in this method/surrounding this method to make the call more robust in case of network failure
     * TODO: Add ability to connect using logged in credentials to increase API calls per hour
     * 
     * @param gitHubAPIRequest the infomration from the API form that the user submitted
     * @throws IOException if connection unable to be established
     * @throws GHException if request was rejected by the API
     * @throws GHRateLimitReachedException custom exception thrown if API call limit reached for this hour.
     */
    private void sendGitHubAPIUserSearch(GitHubAPIRequest gitHubAPIRequest) throws IOException, GHException, GHRateLimitReachedException {
        GitHub github = GitHub.connectAnonymously();
        log.debug("Connected to GitHub Anonymously");

        int currentRateLimit = github.getRateLimit().remaining;
        currentRateLimit = checkRateLimit(currentRateLimit);
        log.debug("Rate Limit after connecting is underestimated at = " + currentRateLimit);

        GHUserSearchBuilder search = github.searchUsers();
        buildUserSearch(search, gitHubAPIRequest);
     
        // Code for allowing for modifiable pagination; change this if it starts to page the search 
        // results for some reason.
        // search.withPageSize(pageSize);

        // Performs the actual API query and then checks the limit
        PagedSearchIterable<GHUser> searchResults = search.list();
        currentRateLimit = checkRateLimit(currentRateLimit);

        String totalNumberResults = Integer.toString(searchResults.getTotalCount());
        log.debug("Total Number of Users found for current request = " + totalNumberResults);

        int userLimiter = 0;
        int maxUserLookups = gitHubAPIRequest.getMaxNumberOfUserLookups(); // will break out of for loop if exceeded
        for(GHUser currentUser : searchResults){
            userLimiter++;
            currentRateLimit = checkRateLimit(currentRateLimit);
            if(userLimiter > maxUserLookups){
                log.debug("Reached the specified max user lookup from the GitHub API Form - Exiting now");
                break;
            }
            try{
                // Take the GHUser and convert them to a Participant reprenesentation and store.
                Participant convertedParticipant = convertToParticipant(currentUser, gitHubAPIRequest, currentRateLimit);
                participantRepository.save(convertedParticipant);
            }catch(IOException e){
                log.debug("Unable to convert participant, skipping");
            }
        }
    }

     /**
     * Helper method to keep track of the estimated rate limit. Uses and returns int instead of querying
     * the API every time to check the rate limit (too many network calls).
     * 
     * @param currentRateLimit cached int value representing the allotted API calls remaining
     * @throws GHRateLimitReachedException custom exception thrown if API call limit reached for this hour.
     */
    private int checkRateLimit(int currentRateLimit) throws GHRateLimitReachedException {
        // Want to be conservative so even if the counter says that the rate limit isn't 0, we want
        // to terminate early in order to avoid getting stuck waiting for a API request when out of allocated calls. 
        if(currentRateLimit <= 5){
            throw new GHRateLimitReachedException("Rate Limit nearly reached! Prematurely aborting the request");
        }
        return currentRateLimit--;
    }

    /**
     * Helper method to add the correct information to the GHUserSearchBuilder object, which
     * is responsible for maintaining the URL used to query GitHub.
     * 
     * @param search the GHUserSearchBuilder used to construct the query string
     * @param gitHubAPIRequest the infomration from the API form that the user submitted
     */
    private void buildUserSearch(GHUserSearchBuilder search, GitHubAPIRequest gitHubAPIRequest) {
        switch(gitHubAPIRequest.getUserType()){
            case USER:
                search.type("user");
                break;
            case ORG:
                search.type("org");
                break;
            case NOPREFERENCE:
                // Don't need to add anything to the search term
                break;
        }

        if(gitHubAPIRequest.getKeyword() != null){
            search.q(gitHubAPIRequest.getKeyword());
        }

        if(gitHubAPIRequest.getNumberOfRepositories() != null){
            search.repos(gitHubAPIRequest.getNumberOfRepositories());
        }

        if(gitHubAPIRequest.getLocation() != null){
            search.location(gitHubAPIRequest.getLocation());
        }

        if(gitHubAPIRequest.getJoinedBeforeDate() != null){
            search.created(gitHubAPIRequest.getJoinedBeforeDate());
        }

        if(gitHubAPIRequest.getProgrammingLanguage() != null){
            search.language(gitHubAPIRequest.getProgrammingLanguage());
        }

        if(gitHubAPIRequest.getNumberOfFollowers() != null){
            search.followers(gitHubAPIRequest.getNumberOfFollowers());
        }
	}

    /**
     * Helper method to convert a GitHub user representation to a Participant.
     * Encapsulates any changes to the Participant class - simply add extra function calls
     * on the convertedParticipant object within this function
     * 
     * TODO: IMPROVE THE CONTRIBUTION COUNT CHECK - currently does far too many calls as a result of REST limitations 
     *       solution = switch to GraphQL?
     * TODO: Currently just setting fake email address for safety. To change this to produce participants with real emails:
     *      convertedParticipant.email(ghUser.getEmail()) - but check the kohsuke.github javadocs for most up to date implementation.
     * 
     * @param ghUser the current user to build a particpat out of
     * @param gitHubAPIRequest the infomration from the API form that the user submitted
     * @param currentRateLimit cached int value representing the allotted API calls remaining
     * @throws IOException if network problem occurs
     */
	private Participant convertToParticipant(GHUser ghUser, GitHubAPIRequest gitHubAPIRequest, int currentRateLimit) throws IOException {
        Participant convertedParticipant = new Participant();

        // Set all the basic fields possible
        convertedParticipant
            .email(ghUser.getLogin()+"@fake.email.co.nz")
            .occupation(ghUser.getCompany())
            .location(ghUser.getLocation())
            .numberOfRepositories(ghUser.getPublicRepoCount());
        
        // Currently, will set the page size and do a single REST request to get a number of repositories
        // and set the programming language to the first language its able to find.
        PagedIterable<GHRepository> allRepos = ghUser.listRepositories();
        int numberOfReposToCheck = 10; // Arbitrary number to check - defines the number of repos obtained per page! 
        PagedIterator<GHRepository> repoIterator = allRepos.withPageSize(numberOfReposToCheck).iterator();
        String firstProgrammingLanguageFound = null;
        for(int i = 0; i < numberOfReposToCheck; i++){
            if(firstProgrammingLanguageFound == null){
                if(repoIterator.hasNext()){
                    GHRepository checkRepo = repoIterator.next();
                    firstProgrammingLanguageFound = checkRepo.getLanguage();
                }
            }else{
                // the checkRepo variable also has #listLanguages() to get multiple languages if we wanted that
                convertedParticipant.programmingLanguage(firstProgrammingLanguageFound);
                break;
            }
        }
        
        // User must explicitly specify they want Contribution Count on form, else this is skipped.
        if(gitHubAPIRequest.getRequestedContributionCount()){
            log.debug("BEGINNING CONTRIBUTION COUNT - EXTREMELY NETWORK INTENSIVE!!!");
            allRepos = ghUser.listRepositories(ghUser.getPublicRepoCount()); // Set page size to number of repos for reducing request count
            int contributionCounter = 0;

            // This is the area with unfortunate consequences - the REST API only exposes repositories, meaning
            // you must iterate through each of the repos that a user has to get their total contribution count, 
            // and you then must find them as a contributor in each repository. This is EXTREMELY inefficient -
            // an unfortunate consequence of using a pure REST API architecture.
            // TODO: Could submit a feature request/query to the maintainer of the GitHub API maven repoistory being used OR switch to GraphQL
            for(GHRepository repo : allRepos){
                // Check rate limit EACH time we try a new repo
                currentRateLimit = checkRateLimit(currentRateLimit);

                // Large page sizes (1000 contributors per page, but each contributor is very small) have been used, 
                // to avoid having to page a single request for contributors over and over
                PagedIterable<GHRepository.Contributor> contributors = repo.listContributors().withPageSize(1000);
                List<GHRepository.Contributor> contributorsList = contributors.withPageSize(1000).asList();
                int indexCheck = contributorsList.indexOf(ghUser); // Have to index in the contributor list
                if(indexCheck >= 0){
                    GHRepository.Contributor currentUserAsContributor = contributorsList.get(indexCheck); // Look up the same contributor here
                    int userContributionsToCurrentRepo = currentUserAsContributor.getContributions();
                    log.debug("Found this many contributions for this repo: "+userContributionsToCurrentRepo);
                    contributionCounter += userContributionsToCurrentRepo;
                }
            }
            convertedParticipant.numberOfContributions(contributionCounter);
        }
        return convertedParticipant;
    }
}