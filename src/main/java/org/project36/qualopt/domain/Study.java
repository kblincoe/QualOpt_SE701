package org.project36.qualopt.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Study.
 */
@Entity
@Table(name = "study")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Study implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Lob
    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status = Status.Inactive;

    @Lob
    @Column(name = "incentive_type", columnDefinition = "blob")
    private IncentiveType incentiveType;

    @Lob
    @Column(name = "incentive_detail")
    private String incentiveDetail;

    @NotNull
    @Column(name = "email_subject", nullable = false)
    private String emailSubject;

    @Lob
    @Column(name = "email_body")
    private String emailBody;

    @Column(name = "faq")
    private String faq;

    @ManyToOne
    private User user;

    @Column(name = "bounced_mail")
    private String bouncedMail;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "study_participant",
               joinColumns = @JoinColumn(name="studies_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="participants_id", referencedColumnName="id"))
    private Set<Participant> participants = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "study_id")
    private Set<Document> documents = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Study name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Status getStatus() {
        return status;
    }

    public Study status(Status status) {
        this.status = status;
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public Study description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public IncentiveType getIncentiveType() {
        return incentiveType;
    }

    public Study incentiveType(IncentiveType incentiveType) {
        this.incentiveType = incentiveType;
        return this;
    }

    public void setIncentiveType(IncentiveType incentiveType) {
        this.incentiveType = incentiveType;
    }

    public String getIncentiveDetail() {
        return incentiveDetail;
    }

    public Study incentiveDetail(String incentiveDetail) {
        this.incentiveDetail = incentiveDetail;
        return this;
    }

    public void setIncentiveDetail(String incentiveDetail) {
        this.incentiveDetail = incentiveDetail;
    }

    public String getEmailSubject() {
        return emailSubject;
    }

    public Study emailSubject(String emailSubject) {
        this.emailSubject = emailSubject;
        return this;
    }

    public void setEmailSubject(String emailSubject) {
        this.emailSubject = emailSubject;
    }

    public String getEmailBody() {
        return emailBody;
    }

    public String getFaq() {
        return faq;
    }

    public Study faq(String faq) {
        this.faq = faq;
        return this;
    }

    public void setFaq(String faq) {
        this.faq = faq;
    }

    public Study emailBody(String emailBody) {
        this.emailBody = emailBody;
        return this;
    }

    public void setEmailBody(String emailBody) {
        this.emailBody = emailBody;
    }

    public User getUser() {
        return user;
    }

    public Study user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Participant> getParticipants() {
        return participants;
    }

    public Study participants(Set<Participant> participants) {
        this.participants = participants;
        return this;
    }

    public Study addParticipant(Participant participant) {
        this.participants.add(participant);
        participant.getStudies().add(this);
        return this;
    }

    public Study removeParticipant(Participant participant) {
        this.participants.remove(participant);
        participant.getStudies().remove(this);
        return this;
    }

    public void setParticipants(Set<Participant> participants) {
        this.participants = participants;
    }

    public Set<Document> getDocuments() {
        return documents;
    }

    public Study documents(Set<Document> documents) {
        this.documents = documents;
        return this;
    }

    public Study addDocument(Document document) {
        this.documents.add(document);
        return this;
    }

    public Study removeDocument(Document document) {
        this.documents.remove(document);
        return this;
    }

    public void setDocuments(Set<Document> documents) {
        this.documents = documents;
    }

    public String getBouncedMail() {
        return bouncedMail;
    }

    public void setBouncedMail(String bouncedMail) {
        this.bouncedMail = bouncedMail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Study study = (Study) o;
        if (study.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), study.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Study{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", incentiveType='" + getIncentiveType() + "'" +
            ", incentiveDetails='" + getIncentiveDetail() + "'" +
            ", status='" + getStatus() + "'" +
            ", emailSubject='" + getEmailSubject() + "'" +
            ", emailBody='" + getEmailBody() + "'" +
            ", bouncedMail='" + getBouncedMail() + "'" +
            "}";
    }
}
