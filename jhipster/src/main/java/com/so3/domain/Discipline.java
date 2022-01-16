package com.so3.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Discipline.
 */
@Entity
@Table(name = "discipline")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Discipline implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "helper_device")
    private String helperDevice;

    @OneToMany(mappedBy = "discipline")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "inventor", "discipline" }, allowSetters = true)
    private Set<DisciplineSkill> disciplineSkills = new HashSet<>();

    @OneToMany(mappedBy = "discipline")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "inventors", "discipline" }, allowSetters = true)
    private Set<Invention> inventions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Discipline id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Discipline name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHelperDevice() {
        return this.helperDevice;
    }

    public Discipline helperDevice(String helperDevice) {
        this.helperDevice = helperDevice;
        return this;
    }

    public void setHelperDevice(String helperDevice) {
        this.helperDevice = helperDevice;
    }

    public Set<DisciplineSkill> getDisciplineSkills() {
        return this.disciplineSkills;
    }

    public Discipline disciplineSkills(Set<DisciplineSkill> disciplineSkills) {
        this.setDisciplineSkills(disciplineSkills);
        return this;
    }

    public Discipline addDisciplineSkill(DisciplineSkill disciplineSkill) {
        this.disciplineSkills.add(disciplineSkill);
        disciplineSkill.setDiscipline(this);
        return this;
    }

    public Discipline removeDisciplineSkill(DisciplineSkill disciplineSkill) {
        this.disciplineSkills.remove(disciplineSkill);
        disciplineSkill.setDiscipline(null);
        return this;
    }

    public void setDisciplineSkills(Set<DisciplineSkill> disciplineSkills) {
        if (this.disciplineSkills != null) {
            this.disciplineSkills.forEach(i -> i.setDiscipline(null));
        }
        if (disciplineSkills != null) {
            disciplineSkills.forEach(i -> i.setDiscipline(this));
        }
        this.disciplineSkills = disciplineSkills;
    }

    public Set<Invention> getInventions() {
        return this.inventions;
    }

    public Discipline inventions(Set<Invention> inventions) {
        this.setInventions(inventions);
        return this;
    }

    public Discipline addInvention(Invention invention) {
        this.inventions.add(invention);
        invention.setDiscipline(this);
        return this;
    }

    public Discipline removeInvention(Invention invention) {
        this.inventions.remove(invention);
        invention.setDiscipline(null);
        return this;
    }

    public void setInventions(Set<Invention> inventions) {
        if (this.inventions != null) {
            this.inventions.forEach(i -> i.setDiscipline(null));
        }
        if (inventions != null) {
            inventions.forEach(i -> i.setDiscipline(this));
        }
        this.inventions = inventions;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Discipline)) {
            return false;
        }
        return id != null && id.equals(((Discipline) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Discipline{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", helperDevice='" + getHelperDevice() + "'" +
            "}";
    }
}
