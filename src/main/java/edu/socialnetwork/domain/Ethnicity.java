package edu.socialnetwork.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Ethnicity.
 */
@Entity
@Table(name = "ethnicity")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Ethnicity implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "ethnicity", nullable = false)
    private String ethnicity;

    @OneToMany(mappedBy = "ethnicity")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Profile> users = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEthnicity() {
        return ethnicity;
    }

    public Ethnicity ethnicity(String ethnicity) {
        this.ethnicity = ethnicity;
        return this;
    }

    public void setEthnicity(String ethnicity) {
        this.ethnicity = ethnicity;
    }

    public Set<Profile> getUsers() {
        return users;
    }

    public Ethnicity users(Set<Profile> profiles) {
        this.users = profiles;
        return this;
    }

    public Ethnicity addUser(Profile profile) {
        this.users.add(profile);
        profile.setEthnicity(this);
        return this;
    }

    public Ethnicity removeUser(Profile profile) {
        this.users.remove(profile);
        profile.setEthnicity(null);
        return this;
    }

    public void setUsers(Set<Profile> profiles) {
        this.users = profiles;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Ethnicity ethnicity = (Ethnicity) o;
        if (ethnicity.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), ethnicity.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Ethnicity{" +
            "id=" + getId() +
            ", ethnicity='" + getEthnicity() + "'" +
            "}";
    }
}
