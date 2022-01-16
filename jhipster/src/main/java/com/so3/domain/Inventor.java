package com.so3.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Inventor.
 */
@Entity
@Table(name = "inventor")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Inventor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "time_modifer")
    private Integer timeModifer;

    @Column(name = "cost_modifier")
    private Integer costModifier;

    @OneToMany(mappedBy = "inventor")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "inventor", "discipline" }, allowSetters = true)
    private Set<DisciplineSkill> disciplineSkills = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "inventors", "discipline" }, allowSetters = true)
    private Invention invention;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Inventor id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Inventor name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTimeModifer() {
        return this.timeModifer;
    }

    public Inventor timeModifer(Integer timeModifer) {
        this.timeModifer = timeModifer;
        return this;
    }

    public void setTimeModifer(Integer timeModifer) {
        this.timeModifer = timeModifer;
    }

    public Integer getCostModifier() {
        return this.costModifier;
    }

    public Inventor costModifier(Integer costModifier) {
        this.costModifier = costModifier;
        return this;
    }

    public void setCostModifier(Integer costModifier) {
        this.costModifier = costModifier;
    }

    public Set<DisciplineSkill> getDisciplineSkills() {
        return this.disciplineSkills;
    }

    public Inventor disciplineSkills(Set<DisciplineSkill> disciplineSkills) {
        this.setDisciplineSkills(disciplineSkills);
        return this;
    }

    public Inventor addDisciplineSkill(DisciplineSkill disciplineSkill) {
        this.disciplineSkills.add(disciplineSkill);
        disciplineSkill.setInventor(this);
        return this;
    }

    public Inventor removeDisciplineSkill(DisciplineSkill disciplineSkill) {
        this.disciplineSkills.remove(disciplineSkill);
        disciplineSkill.setInventor(null);
        return this;
    }

    public void setDisciplineSkills(Set<DisciplineSkill> disciplineSkills) {
        if (this.disciplineSkills != null) {
            this.disciplineSkills.forEach(i -> i.setInventor(null));
        }
        if (disciplineSkills != null) {
            disciplineSkills.forEach(i -> i.setInventor(this));
        }
        this.disciplineSkills = disciplineSkills;
    }

    public Invention getInvention() {
        return this.invention;
    }

    public Inventor invention(Invention invention) {
        this.setInvention(invention);
        return this;
    }

    public void setInvention(Invention invention) {
        this.invention = invention;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Inventor)) {
            return false;
        }
        return id != null && id.equals(((Inventor) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Inventor{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", timeModifer=" + getTimeModifer() +
            ", costModifier=" + getCostModifier() +
            "}";
    }
}
