package org.project36.qualopt.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
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

    @Lob
    @Column(name = "incentive")
    private String incentive;

    @Column(name = "has_pay")
    private Boolean hasPay;

    @ManyToOne
    private User user;

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

    public Boolean isHasPay() {
        return hasPay;
    }

    public Study hasPay(Boolean hasPay) {
        this.hasPay = hasPay;
        return this;
    }

    public void setHasPay(Boolean hasPay) {
        this.hasPay = hasPay;
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
            ", hasPay='" + isHasPay() + "'" +
            "}";
    }
}
