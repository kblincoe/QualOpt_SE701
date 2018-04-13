package org.project36.qualopt.service.github;

import org.hibernate.usertype.UserType;
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
    
    public void setMuleId(int id){
        this.muleId = id;
    }

    public GitHubUserType getUserType(){
        return userType;
    }

    public GitHubAPIRequest userType(GitHubUserType userType){
        this.userType = userType;
        return this;
    }

    public void setUserType(GitHubUserType userType){
        this.userType = userType;
    }

    public String getKeyword(){
        return keyword;
    }

    public GitHubAPIRequest keyword(String keyword){
        this.keyword = keyword;
        return this;
    }

    public void setKeyword(String keyword){
        this.keyword = keyword;
    }

    public String getNumberOfRepositories(){
        return numberOfRepositories;
    }

    public GitHubAPIRequest numberOfRepositories(String numberOfRepositories){
        this.numberOfRepositories = numberOfRepositories;
        return this;
    }

    public void setNumberOfRepositories(String numberOfRepositories){
        this.numberOfRepositories = numberOfRepositories;
    }
    
    public String getLocation(){
        return location;
    }

    public GitHubAPIRequest location(String location){
        this.location = location;
        return this;
    }

    public void setLocation(String location){
        this.location = location;
    }
    
    public String getJoinedBeforeDate(){
        return joinedBeforeDate;
    }

    public GitHubAPIRequest joinedBeforeDate(String joinedBeforeDate){
        this.joinedBeforeDate = joinedBeforeDate;
        return this;
    }

    public void setJoinedBeforeDate(String joinedBeforeDate){
        this.joinedBeforeDate = joinedBeforeDate;
    }
    
    public String getProgrammingLanguage(){
        return programmingLanguage;
    }

    public GitHubAPIRequest programmingLanguage(String programmingLanguage){
        this.programmingLanguage = programmingLanguage;
        return this;
    }

    public void setProgrammingLanguage(String programmingLanguage){
        this.programmingLanguage = programmingLanguage;
    }
    
    public String getNumberOfFollowers(){
        return numberOfFollowers;
    }

    public GitHubAPIRequest numberOfFollowers(String numberOfFollowers){
        this.numberOfFollowers = numberOfFollowers;
        return this;
    }

    public void setNumberOfFollowers(String numberOfFollowers){
        this.numberOfFollowers = numberOfFollowers;
    }

    public boolean getRequestedContributionCount(){
        return requestedContributionCount;
    }

    public GitHubAPIRequest requestedContributionCount(boolean requestedContributionCount){
        this.requestedContributionCount = requestedContributionCount;
        return this;
    }

    public void setRequestedContributionCount(boolean requestedContributionCount){
        this.requestedContributionCount = requestedContributionCount;
    }

    public int getMaxNumberOfUserLookups(){
        return maxNumberOfUserLookups;
    }

    public GitHubAPIRequest maxNumberOfUserLookups(int maxNumberOfUserLookups){
        this.maxNumberOfUserLookups = maxNumberOfUserLookups;
        return this;
    }

    public void setMaxNumberOfUserLookups(int maxNumberOfUserLookups){
        this.maxNumberOfUserLookups = maxNumberOfUserLookups;
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
