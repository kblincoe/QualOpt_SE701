package org.project36.qualopt.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
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

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "incentive")
    private String incentive;

    @Column(name = "has_pay")
    private Boolean hasPay;

    @ManyToOne
    private Researcher researcher;

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

    public Researcher getResearcher() {
        return researcher;
    }

    public Study researcher(Researcher researcher) {
        this.researcher = researcher;
        return this;
    }

    public void setResearcher(Researcher researcher) {
        this.researcher = researcher;
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
        if (study.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, study.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Study{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", description='" + description + "'" +
            ", incentive='" + incentive + "'" +
            ", hasPay='" + hasPay + "'" +
            '}';
    }
}
