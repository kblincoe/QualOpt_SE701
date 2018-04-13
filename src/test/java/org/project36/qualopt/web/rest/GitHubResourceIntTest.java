package org.project36.qualopt.web.rest;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kohsuke.github.GHException;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.project36.qualopt.QualOptApp;
import org.project36.qualopt.repository.ParticipantRepository;
import org.project36.qualopt.service.github.GitHubAPIRequest;
import org.project36.qualopt.service.github.GitHubAPIService;
import org.project36.qualopt.service.github.GitHubUserType;
import org.project36.qualopt.web.rest.errors.ExceptionTranslator;
import org.project36.qualopt.web.rest.errors.GHRateLimitReachedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

/**
 * Test class for the GitHubResource REST controller.
 * 
 * These unit test insert a mock GitHubAPIService object to the REST controller, allowing control on the methods that 
 * make network calls. In doing so, we ensure that these tests do not make the actual API calls, as this would be 
 * potentially non-determinant and would be too long for a unit test. 
 * 
 * Note: This class is used to test the REST Controller, not the GitHub API domain model used - that has
 * it's own set of tests which can be found on its website. Further, this does not test the creation of making
 * the participants that would be returned from the GitHub API - as this is tested by the ParticipantResourceIntTest series
 * of tests.
 *
 * @see GitHubResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = QualOptApp.class)
public class GitHubResourceIntTest {

    private static final GitHubUserType DEFAULT_USER_TYPE = GitHubUserType.NOPREFERENCE;

    private static final String DEFAULT_KEYWORD = "AAAAAAAAAA";

    private static final String DEFAULT_NUMBER_OF_REPOSITORIES = "AAAA";

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";

    private static final String DEFAULT_JOINED_BEFORE_DATE = "AAAAAAAAAA";

    private static final String DEFAULT_PROGRAMMING_LANGUAGE = "C#";

    private static final String DEFAULT_NUMBER_OF_FOLLOWERS = "AAAAAAAAAAAAA";

    private static final boolean DEFAULT_REQUEST_CONTRIBUTION_COUNT = false;

    private static final int DEAFULT_MAX_NUMBER_OF_USER_LOOKUPS = 0;
   
    @Autowired
    private ParticipantRepository participantRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restGitHubAPIMockMvc;

    @InjectMocks
    private GitHubResource gitHubResource;

    /**
     * Enum to pass in to define what the GitHubAPIService mock should exhibit
     */
    private enum GitHubAPIServiceMockBehaviour {
        SUCCESS,
        NETWORK_FAIL,
        GITHUB_FAIL,
        RATE_LIMIT_EXCEEDED
    }

    /**
     * Setup method to produce a MockMVC of the GitHubResource REST controller
     * 
     * NOTE: each test must also place in its own implementation specifc mock, this method
     * simply places a deafult "success" mock in case future tests forget to add a mock (don't 
     * ever want to do a network call while testing)
     */
    @Before
    public void setupWithMock() throws Exception{
        MockitoAnnotations.initMocks(this);
        this.gitHubResource = new GitHubResource(participantRepository);
        this.gitHubResource.setGitHubAPIServiceMock(createMockGitHubAPIService(GitHubAPIServiceMockBehaviour.SUCCESS));
        this.restGitHubAPIMockMvc = MockMvcBuilders.standaloneSetup(this.gitHubResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create a default GitHubAPIRequest
     * 
     * Should be used to simply send to the resource and NOT used to actually query the API - this
     * would not turn any reasonable results. This should be used for testing alongside mocking
     * the GitHubAPIService object.
     */
    public static GitHubAPIRequest createGitHubAPIRequest() {
        GitHubAPIRequest gitHubAPIRequest = new GitHubAPIRequest()
            .userType(DEFAULT_USER_TYPE)
            .keyword(DEFAULT_KEYWORD)
            .numberOfRepositories(DEFAULT_NUMBER_OF_REPOSITORIES)
            .location(DEFAULT_LOCATION)
            .joinedBeforeDate(DEFAULT_JOINED_BEFORE_DATE)
            .programmingLanguage(DEFAULT_PROGRAMMING_LANGUAGE)
            .numberOfFollowers(DEFAULT_NUMBER_OF_FOLLOWERS)
            .requestedContributionCount(DEFAULT_REQUEST_CONTRIBUTION_COUNT)
            .maxNumberOfUserLookups(DEAFULT_MAX_NUMBER_OF_USER_LOOKUPS);
        gitHubAPIRequest.setMuleId(1);
        return gitHubAPIRequest;
    }

    /**
     * Helper method to return a mock that exhibits testable behaviour without having to perform
     * the network call. This mock must be inserted into the GitHubResource REST controller to allow
     * for it to exhibit the behaviour.
     * 
     * Mocking the object is facilitated through Mockito - the framework makes a spy (partial mock) and 
     * then mocksout the sendGitHubAPIUserSearch() method. It will vary the behavior of this method 
     * depending on the type of behavior that should be tested (represented by the enum). The resource 
     * handler is mocked up by the setup method.
     */
    public GitHubAPIService createMockGitHubAPIService(GitHubAPIServiceMockBehaviour mockBehaviour) throws Exception{
        GitHubAPIService gitHubAPIServiceMock = spy(new GitHubAPIService(participantRepository));
        
        // Add the mock behavior, currently just mocking out the one method responsible for the network 
        // call, it will instead just "completes" the method by do nothing, or throws an exception to 
        // simulate some kind of failure
        switch(mockBehaviour){
            case SUCCESS:
                doNothing().when(gitHubAPIServiceMock).sendGitHubAPIUserSearch();
                break;
            case NETWORK_FAIL:
                doThrow(new IOException()).when(gitHubAPIServiceMock).sendGitHubAPIUserSearch();
                break;
            case GITHUB_FAIL:
                doThrow(new GHException("")).when(gitHubAPIServiceMock).sendGitHubAPIUserSearch();
                break;
            case RATE_LIMIT_EXCEEDED:
                doThrow(new GHRateLimitReachedException("")).when(gitHubAPIServiceMock).sendGitHubAPIUserSearch();
                break;
            default:
                doNothing().when(gitHubAPIServiceMock).sendGitHubAPIUserSearch();
                break;
        }
        return gitHubAPIServiceMock;
    }

    /**
     * Test for basic behavior, when there is no failure or rate limit exceeded
     */
    @Test
    @Transactional
    public void sendGitHubRequestBasic() throws Exception {
        GitHubAPIRequest testRequest = GitHubResourceIntTest.createGitHubAPIRequest();

        this.gitHubResource.setGitHubAPIServiceMock(createMockGitHubAPIService(GitHubAPIServiceMockBehaviour.SUCCESS));
        
        restGitHubAPIMockMvc.perform(post("/api/gitHubQuery")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(testRequest)))
            .andExpect(status().isOk());
    }

    /**
     * Test for when a network failure occurs during sending the GitHub API request
     */
    @Test
    @Transactional
    public void sendGitHubRequestAndNetworkFail() throws Exception {
        GitHubAPIRequest testRequest = GitHubResourceIntTest.createGitHubAPIRequest();

        this.gitHubResource.setGitHubAPIServiceMock(createMockGitHubAPIService(GitHubAPIServiceMockBehaviour.NETWORK_FAIL));
        
        MvcResult result = restGitHubAPIMockMvc.perform(post("/api/gitHubQuery")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(testRequest)))
            .andExpect(status().isUnauthorized())
            .andReturn();
        assertTrue(result.getResponse().getContentAsString().contains(GitHubResource.ERROR_MESSAGE_WHEN_CANNOT_CONNECT));
    }

    /**
     * Test for when there is a failure with the GitHub API, but no network failure occurs
     */
    @Test
    @Transactional
    public void sendGitHubRequestAndGHFail() throws Exception {
        GitHubAPIRequest testRequest = GitHubResourceIntTest.createGitHubAPIRequest();

        this.gitHubResource.setGitHubAPIServiceMock(createMockGitHubAPIService(GitHubAPIServiceMockBehaviour.GITHUB_FAIL));
        
        MvcResult result = restGitHubAPIMockMvc.perform(post("/api/gitHubQuery")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(testRequest)))
            .andExpect(status().isBadRequest())
            .andReturn();
        assertTrue(result.getResponse().getContentAsString().contains(GitHubResource.ERROR_MESSAGE_WHEN_GITHUB_FAIL));
    }

    /**
     * Test for when the rate limit is exceeded while sending a GitHub API request
     */
    @Test
    @Transactional
    public void sendGitHubRequestAndRateLimitExceeded() throws Exception {
        GitHubAPIRequest testRequest = GitHubResourceIntTest.createGitHubAPIRequest();

        this.gitHubResource.setGitHubAPIServiceMock(createMockGitHubAPIService(GitHubAPIServiceMockBehaviour.RATE_LIMIT_EXCEEDED));
        
        MvcResult result = restGitHubAPIMockMvc.perform(post("/api/gitHubQuery")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(testRequest)))
            .andExpect(status().isTooManyRequests())
            .andReturn();
        assertTrue(result.getResponse().getContentAsString().contains(GitHubResource.ERROR_MESSAGE_WHEN_API_LIMIT_EXCEEDED));
    }
}
