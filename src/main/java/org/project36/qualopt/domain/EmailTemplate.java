package org.project36.qualopt.domain;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * An Email Template entity. 
 * 
 * Email Templates are stored against a user.
 * A user can have 0 to many email templates.
 * 
 * An email template contains: name, subject and body. 
 */
@Entity
@Table(name = "email_template")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class EmailTemplate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;
    
    @Lob
    @Column(name = "email_subject")
    private String emailSubject;

    @Lob
    @Column(name = "email_body")
    private String emailBody;
    
    @ManyToOne
    private User user;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EmailTemplate emailTemplate = (EmailTemplate) o;
        if (emailTemplate.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), emailTemplate.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "EmailTemplate{" +
            "id=" + getId() +
            "}";
    }

	public String getSubject() {
		return emailSubject;
	}

	public void setSubject(String subject) {
		this.emailSubject = subject;
	}

	public String getBody() {
		return emailBody;
	}

	public void setBody(String body) {
		this.emailBody = body;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
