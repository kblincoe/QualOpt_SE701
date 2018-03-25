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
    @Column(name = "incentive")
    private String incentive;

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

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "study_participant",
               joinColumns = @JoinColumn(name="studies_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="participants_id", referencedColumnName="id"))
    private Set<Participant> participants = new HashSet<>();
    
    //Save list of email addresses that have receive e-mail, 
    //so that the researcher can send to the email addresses that haven't recieve email only 
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name="study_invited",
            joinColumns=@JoinColumn(name="studies_id")
      )
      @Column(name="email")
    private Set<String> emailAddressesHaveInvited = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "study_id")
    private Set<Document> documents = new HashSet<>();

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

    public String getIncentive() {
        return incentive;
    }

    public Study incentive(String incentive) {
        this.incentive = incentive;
        return this;
    }

    public void setIncentive(String incentive) {
        this.incentive = incentive;
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
    
    public Set<String> getEmailAddressesHaveInvited(){
    	return emailAddressesHaveInvited;
    };
    
    public void setEmailAddressesHaveInvited(Set<String> listOfEmailAddress){
    	emailAddressesHaveInvited = listOfEmailAddress;
    };
    
    public void addToEmailAddressesHaveInvited(String emailAddress){
    	emailAddressesHaveInvited.add(emailAddress);	
    };
    
    public void removeFromEmailAddressesHaveInvited(String emailAddress){
    	emailAddressesHaveInvited.remove(emailAddress);
    };

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
            ", incentive='" + getIncentive() + "'" +
            ", status='" + getStatus() + "'" +
            ", faq='" + getFaq() + "'" +
            ", emailSubject='" + getEmailSubject() + "'" +
            ", emailBody='" + getEmailBody() + "'" +
            "}";
    }
}
