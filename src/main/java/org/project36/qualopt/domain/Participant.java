package org.project36.qualopt.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Participant.
 */
@Entity
@Table(name = "participant")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Participant implements Serializable {

    private static final long serialVersionUID = 1L;

    //Default values for participant details
    private static final int DEFAULT_NUMBER_OF_INVITATIONS = 5;
    private static final String DEFAULT_INCENTIVE_CHOICE = "ONE_OFF";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "name")
    private String name;

    @Column(name = "occupation")
    private String occupation;

    @Column(name = "location")
    private String location;

    @Column(name = "programming_language")
    private String programmingLanguage;

    @Column(name = "number_of_contributions")
    private Integer numberOfContributions;

    @Column(name = "number_of_repositories")
    private Integer numberOfRepositories;

    @Column(name = "followers")
    private Integer followers;

    @Column(name = "following")
    private Integer following;

    @Column(name = "has_opted_in")
    private boolean hasOptedIn = true;

    @Column(name = "number_of_invitations_requested")
    private Integer numberOfInvitationsRequested = DEFAULT_NUMBER_OF_INVITATIONS;

    @Column(name = "incentive_choice")
    private String incentiveChoice = DEFAULT_INCENTIVE_CHOICE; //Currently a string, need to change to an enum when Issue 5 is pulled in

    @Column(name = "incentive_details")
    private String incentiveDetails;

    @ManyToMany(mappedBy = "participants")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Study> studies = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public Participant email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public Participant name(String name) {
        this.name = name;
        return this;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getOccupation() {
        return occupation;
    }

    public Participant occupation(String occupation) {
        this.occupation = occupation;
        return this;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getLocation() {
        return location;
    }

    public Participant location(String location) {
        this.location = location;
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getProgrammingLanguage() {
        return programmingLanguage;
    }

    public Participant programmingLanguage(String programmingLanguage) {
        this.programmingLanguage = programmingLanguage;
        return this;
    }

    public void setProgrammingLanguage(String programmingLanguage) {
        this.programmingLanguage = programmingLanguage;
    }

    public Integer getNumberOfContributions() {
        return numberOfContributions;
    }

    public Participant numberOfContributions(Integer numberOfContributions) {
        this.numberOfContributions = numberOfContributions;
        return this;
    }

    public void setNumberOfContributions(Integer numberOfContributions) {
        this.numberOfContributions = numberOfContributions;
    }

    public Integer getNumberOfRepositories() {
        return numberOfRepositories;
    }

    public Participant numberOfRepositories(Integer numberOfRepositories) {
        this.numberOfRepositories = numberOfRepositories;
        return this;
    }

    public void setNumberOfRepositories(Integer numberOfRepositories) {
        this.numberOfRepositories = numberOfRepositories;
    }

    public Integer getFollowers() {
        return followers;
    }

    public Participant followers(Integer followers) {
        this.followers = followers;
        return this;
    }

    public void setFollowers(Integer followers) {
        this.followers = followers;
    }

    public Integer getFollowing() {
        return following;
    }

    public Participant following(Integer following) {
        this.following = following;
        return this;
    }

    public void setFollowing(Integer following) {
        this.following = following;
    }

    public Set<Study> getStudies() {
        return studies;
    }

    public Participant studies(Set<Study> studies) {
        this.studies = studies;
        return this;
    }

    public Participant addStudy(Study study) {
        this.studies.add(study);
        study.getParticipants().add(this);
        return this;
    }

    public Participant removeStudy(Study study) {
        this.studies.remove(study);
        return this;
    }

    public void setStudies(Set<Study> studies) {
        this.studies = studies;
    }

    public boolean getOptedIn() {
        return this.hasOptedIn;
    }

    public void setOptedIn(boolean optedInStatus) {
        this.hasOptedIn = optedInStatus;
    }

    public Integer getNumberOfInvitationsRequested() {
        return this.numberOfInvitationsRequested;
    }

    public void setNumberOfInvitationsRequested(Integer number) {
       this.numberOfInvitationsRequested = number;
    }

    public String getIncentiveChoice() {
        return this.incentiveChoice;
    }

    public void setIncentiveChoice(String choice) {
       this.incentiveChoice = choice;
    }

    public String getIncentiveDetails() {
        return this.incentiveDetails;
    }

    public void setIncentiveDetails(String details) {
       this.incentiveDetails = details;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Participant participant = (Participant) o;
        if (participant.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), participant.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Participant{" +
            "id=" + getId() +
            ", email='" + getEmail() + "'" +
            ", name='" + getName() + "'" +
            ", occupation='" + getOccupation() + "'" +
            ", location='" + getLocation() + "'" +
            ", programmingLanguage='" + getProgrammingLanguage() + "'" +
            ", numberOfContributions='" + getNumberOfContributions() + "'" +
            ", numberOfRepositories='" + getNumberOfRepositories() + "'" +
            ", followers='" + getFollowers() + "'" +
            ", following='" + getFollowing() + "'" +
            ", optedIn='" + getOptedIn() + "'" +
            ", numberOfInvitationsRequested='" + getNumberOfInvitationsRequested() + "'" +
            ", incentiveChoice='" + getIncentiveChoice() + "'" +
            ", incentiveDetails='" + getIncentiveDetails() + "'" +
            "}";
    }
}
