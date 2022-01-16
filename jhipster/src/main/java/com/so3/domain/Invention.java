package com.so3.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Invention.
 */
@Entity
@Table(name = "invention")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Invention implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "cost")
    private Integer cost;

    @Column(name = "difficulty")
    private Integer difficulty;

    @OneToMany(mappedBy = "invention")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "disciplineSkills", "invention" }, allowSetters = true)
    private Set<Inventor> inventors = new HashSet<>();

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

    public Invention id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Invention name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCost() {
        return this.cost;
    }

    public Invention cost(Integer cost) {
        this.cost = cost;
        return this;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public Integer getDifficulty() {
        return this.difficulty;
    }

    public Invention difficulty(Integer difficulty) {
        this.difficulty = difficulty;
        return this;
    }

    public void setDifficulty(Integer difficulty) {
        this.difficulty = difficulty;
    }

    public Set<Inventor> getInventors() {
        return this.inventors;
    }

    public Invention inventors(Set<Inventor> inventors) {
        this.setInventors(inventors);
        return this;
    }

    public Invention addInventor(Inventor inventor) {
        this.inventors.add(inventor);
        inventor.setInvention(this);
        return this;
    }

    public Invention removeInventor(Inventor inventor) {
        this.inventors.remove(inventor);
        inventor.setInvention(null);
        return this;
    }

    public void setInventors(Set<Inventor> inventors) {
        if (this.inventors != null) {
            this.inventors.forEach(i -> i.setInvention(null));
        }
        if (inventors != null) {
            inventors.forEach(i -> i.setInvention(this));
        }
        this.inventors = inventors;
    }

    public Discipline getDiscipline() {
        return this.discipline;
    }

    public Invention discipline(Discipline discipline) {
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
        if (!(o instanceof Invention)) {
            return false;
        }
        return id != null && id.equals(((Invention) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Invention{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", cost=" + getCost() +
            ", difficulty=" + getDifficulty() +
            "}";
    }
}
