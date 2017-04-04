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

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "profession")
    private String profession;

    @Column(name = "institution")
    private String institution;

    @Column(name = "mail_server")
    private String mailServer;

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

    public String getPassword() {
        return password;
    }

    public Researcher password(String password) {
        this.password = password;
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfession() {
        return profession;
    }

    public Researcher profession(String profession) {
        this.profession = profession;
        return this;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getInstitution() {
        return institution;
    }

    public Researcher institution(String institution) {
        this.institution = institution;
        return this;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getMailServer() {
        return mailServer;
    }

    public Researcher mailServer(String mailServer) {
        this.mailServer = mailServer;
        return this;
    }

    public void setMailServer(String mailServer) {
        this.mailServer = mailServer;
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
        if (researcher.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, researcher.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Researcher{" +
            "id=" + id +
            ", email='" + email + "'" +
            ", password='" + password + "'" +
            ", profession='" + profession + "'" +
            ", institution='" + institution + "'" +
            ", mailServer='" + mailServer + "'" +
            '}';
    }
}
