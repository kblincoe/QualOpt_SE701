package org.project36.qualopt.service.github;

import org.project36.qualopt.domain.Participant;
import org.project36.qualopt.service.github.GitHubAPIRequest;
import org.project36.qualopt.repository.ParticipantRepository;
import org.project36.qualopt.web.rest.errors.GHRateLimitReachedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.kohsuke.github.*;
import java.io.IOException;
import java.lang.Integer;
import java.util.List;


public class GitHubAPIService{

    private final Logger log = LoggerFactory.getLogger(GitHubAPIService.class);

    private final ParticipantRepository participantRepository;

	private GitHubAPIRequest gitHubAPIRequest;

    public GitHubAPIService(ParticipantRepository participantRepository){
        this.participantRepository = participantRepository;
    }
    
    /**
     * Method that is called by the Resource Handler in order to pass in the API request.
     * 
     * An extra layer of misdirection has been added to support mocking easier - we instead
     * mock the sendGitHubAPIUserSearch() method as this has no arguments, allowing it to
     * be mocked by Mockit much easier.
     */
    public void startGitHubAPIUserSearch(GitHubAPIRequest gitHubAPIRequest) throws IOException, GHException, GHRateLimitReachedException {
        this.gitHubAPIRequest = gitHubAPIRequest;
        sendGitHubAPIUserSearch(); // Uses the gitHubAPIRequest field, which can be mocked! 
    }

    /**
     * Method to connect to the GitHub API and subsequently send the user search request. Uses
     * the kohsuke.github maven repository in order to provide an object model for the different GitHub
     * resources. Please refer to http://github-api.kohsuke.org/apidocs/index.html for more information.
     * 
     * NOTE: THIS METHOD IS MOCKED DURING UNIT TESTING TO SIMPLY RETURN NOTHING OR A RELEVANT EXCEPTION.
     * Don't just merge this method into startGitHubAPIUserSearch().
     * 
     * TODO: Add a timeout in this method/surrounding this method to make the call more robust in case of network failure
     * TODO: Add ability to connect using logged in credentials to increase API calls per hour
     * 
     * @param gitHubAPIRequest the infomration from the API form that the user submitted
     * @throws IOException if connection unable to be established
     * @throws GHException if request was rejected by the API
     * @throws GHRateLimitReachedException custom exception thrown if API call limit reached for this hour.
     */
    public void sendGitHubAPIUserSearch() throws IOException, GHException, GHRateLimitReachedException {
        GitHub github = GitHub.connectAnonymously();
        log.debug("Connected to GitHub Anonymously");

        int currentRateLimit = github.getRateLimit().remaining;
        currentRateLimit = checkRateLimit(currentRateLimit);
        log.debug("Rate Limit after connecting is underestimated at = " + currentRateLimit);

        GHUserSearchBuilder search = github.searchUsers();
        buildUserSearch(search, this.gitHubAPIRequest);
     
        // Code for allowing for modifiable pagination; change this if it starts to page the search 
        // results for some reason.
        // search.withPageSize(pageSize);

        // Performs the actual API query and then checks the limit
        PagedSearchIterable<GHUser> searchResults = search.list();
        currentRateLimit = checkRateLimit(currentRateLimit);

        String totalNumberResults = Integer.toString(searchResults.getTotalCount());
        log.debug("Total Number of Users found for current request = " + totalNumberResults);

        int userLimiter = 0;
        int maxUserLookups = this.gitHubAPIRequest.getMaxNumberOfUserLookups(); // will break out of for loop if exceeded
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