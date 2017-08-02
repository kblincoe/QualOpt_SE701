package org.project36.qualopt.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
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
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "occupation")
    private String occupation;

    @Column(name = "institute")
    private String institute;

    @ManyToOne
    private Study study;

    @OneToOne
    @MapsId
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public Researcher email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public Study getStudy() {
        return study;
    }

    public Researcher study(Study study) {
        this.study = study;
        return this;
    }

    public void setStudy(Study study) {
        this.study = study;
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
            ", email='" + getEmail() + "'" +
            ", occupation='" + getOccupation() + "'" +
            ", institute='" + getInstitute() + "'" +
            "}";
    }
}
