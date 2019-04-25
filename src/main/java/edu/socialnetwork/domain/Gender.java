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
 * A Gender.
 */
@Entity
@Table(name = "gender")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Gender implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "jhi_type", nullable = false)
    private String type;

    @OneToMany(mappedBy = "gender")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Profile> users = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public Gender type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Set<Profile> getUsers() {
        return users;
    }

    public Gender users(Set<Profile> profiles) {
        this.users = profiles;
        return this;
    }

    public Gender addUser(Profile profile) {
        this.users.add(profile);
        profile.setGender(this);
        return this;
    }

    public Gender removeUser(Profile profile) {
        this.users.remove(profile);
        profile.setGender(null);
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
        Gender gender = (Gender) o;
        if (gender.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), gender.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Gender{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            "}";
    }
}
