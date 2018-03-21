package org.project36.qualopt.service.github;

import org.project36.qualopt.service.github.GitHubUserType;

import java.io.Serializable;
import java.util.Objects;

/**
 * A GitHub API Request that contains the information entered from the front end GitHub API
 * User Search Form. The Springboot framework automatically marshalls this object when the
 * GitHubResource receives a HTTP Request
 */
public class GitHubAPIRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private int muleId;

    private GitHubUserType userType;

    private String keyword;

    private String numberOfRepositories;

    private String location;

    private String joinedBeforeDate;

    private String programmingLanguage;

    private String numberOfFollowers;

    private boolean requestedContributionCount;

    private int maxNumberOfUserLookups;

    public Integer getMuleId(){
        return muleId;
    }
    
    public GitHubUserType getUserType(){
        return userType;
    }

    public String getKeyword(){
        return keyword;
    }

    public String getNumberOfRepositories(){
        return numberOfRepositories;
    }
    
    public String getLocation(){
        return location;
    }
    
    public String getJoinedBeforeDate(){
        return joinedBeforeDate;
    }
    
    public String getProgrammingLanguage(){
        return programmingLanguage;
    }
    
    public String getNumberOfFollowers(){
        return numberOfFollowers;
    }

    public boolean getRequestedContributionCount(){
        return requestedContributionCount;
    }

    public int getMaxNumberOfUserLookups(){
        return maxNumberOfUserLookups;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        GitHubAPIRequest request = (GitHubAPIRequest) o;

        //Using toString() as this will compare ALL other fields.
        return !(request.toString().equals(o.toString()));
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getMuleId());
    }

    @Override
    public String toString() {
        return "GitHubUserSearchAPIRequest{" +
            "muleId='" + muleId + '\'' +
            ", userType='" + userType + '\'' +
            ", keyword='" + keyword + '\'' +
            ", numberOfRepositories='" + numberOfRepositories + '\'' +
            ", location='" + location + '\'' +
            ", joinedBeforeDate='" + joinedBeforeDate + '\'' +
            ", programmingLanguage='" + programmingLanguage + '\'' +
            ", numberOfFollowers='" + numberOfFollowers + '\'' +
            ", requestedContributionCount='" + requestedContributionCount + '\'' +
            ", maxNumberOfUserLookups='" + maxNumberOfUserLookups + '\'' +
            "}";
    }
}
