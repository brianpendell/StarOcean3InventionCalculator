package com.so3.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A DisciplineSkill.
 */
@Entity
@Table(name = "discipline_skill")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class DisciplineSkill implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "skill_modifier")
    private Integer skillModifier;

    @ManyToOne
    @JsonIgnoreProperties(value = { "disciplineSkills", "invention" }, allowSetters = true)
    private Inventor inventor;

    @ManyToOne
    @JsonIgnoreProperties(value = { "disciplineSkills", "inventions" }, allowSetters = true)
    private Discipline discipline;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DisciplineSkill id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getSkillModifier() {
        return this.skillModifier;
    }

    public DisciplineSkill skillModifier(Integer skillModifier) {
        this.skillModifier = skillModifier;
        return this;
    }

    public void setSkillModifier(Integer skillModifier) {
        this.skillModifier = skillModifier;
    }

    public Inventor getInventor() {
        return this.inventor;
    }

    public DisciplineSkill inventor(Inventor inventor) {
        this.setInventor(inventor);
        return this;
    }

    public void setInventor(Inventor inventor) {
        this.inventor = inventor;
    }

    public Discipline getDiscipline() {
        return this.discipline;
    }

    public DisciplineSkill discipline(Discipline discipline) {
        this.setDiscipline(discipline);
        return this;
    }

    public void setDiscipline(Discipline discipline) {
        this.discipline = discipline;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DisciplineSkill)) {
            return false;
        }
        return id != null && id.equals(((DisciplineSkill) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DisciplineSkill{" +
            "id=" + getId() +
            ", skillModifier=" + getSkillModifier() +
            "}";
    }
}
