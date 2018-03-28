package org.project36.qualopt.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

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

    @Column(name = "occupation")
    private String occupation;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "location")
    private String location;

    @Column(name = "programming_language")
    private String programmingLanguage;

    @Column(name = "number_of_contributions")
    private Integer numberOfContributions;

    @Column(name = "number_of_repositories")
    private Integer numberOfRepositories;

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

    public String getOccupation() {
        return getStringNonNull(occupation);
    }

    public Participant occupation(String occupation) {
        this.occupation = occupation;
        return this;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getLocation() {
        return getStringNonNull(location);
    }

    public Participant location(String location) {
        this.location = location;
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getProgrammingLanguage() {
        return getStringNonNull(programmingLanguage);
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

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return getStringNonNull(firstName);
    }

    public String getLastName() {
        return getStringNonNull(lastName);
    }

    private String getStringNonNull(String s) {
        return Objects.isNull(s) ? "" : s;
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
            "id=" + getId() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", email='" + getEmail() + "'" +
            ", occupation='" + getOccupation() + "'" +
            ", location='" + getLocation() + "'" +
            ", programmingLanguage='" + getProgrammingLanguage() + "'" +
            ", numberOfContributions='" + getNumberOfContributions() + "'" +
            ", numberOfRepositories='" + getNumberOfRepositories() + "'" +
            ", optedIn='" + getOptedIn() + "'" +
            ", numberOfInvitationsRequested='" + getNumberOfInvitationsRequested() + "'" +
            ", incentiveChoice='" + getIncentiveChoice() + "'" +
            ", incentiveDetails='" + getIncentiveDetails() + "'" +
            "}";
    }
}
