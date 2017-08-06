package org.project36.qualopt.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Researcher.
 */
@Entity
@Table(name = "researcher")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Researcher implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email_address")
    private String emailAddress;

    @Column(name = "occupation")
    private String occupation;

    @Column(name = "institute")
    private String institute;

    @OneToMany(mappedBy = "researcher")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Study> studies = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public Researcher emailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
        return this;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getOccupation() {
        return occupation;
    }

    public Researcher occupation(String occupation) {
        this.occupation = occupation;
        return this;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getInstitute() {
        return institute;
    }

    public Researcher institute(String institute) {
        this.institute = institute;
        return this;
    }

    public void setInstitute(String institute) {
        this.institute = institute;
    }

    public Set<Study> getStudies() {
        return studies;
    }

    public Researcher studies(Set<Study> studies) {
        this.studies = studies;
        return this;
    }

    public Researcher addStudy(Study study) {
        this.studies.add(study);
        study.setResearcher(this);
        return this;
    }

    public Researcher removeStudy(Study study) {
        this.studies.remove(study);
        study.setResearcher(null);
        return this;
    }

    public void setStudies(Set<Study> studies) {
        this.studies = studies;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Researcher researcher = (Researcher) o;
        if (researcher.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), researcher.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Researcher{" +
            "id=" + getId() +
            ", emailAddress='" + getEmailAddress() + "'" +
            ", occupation='" + getOccupation() + "'" +
            ", institute='" + getInstitute() + "'" +
            "}";
    }
}
