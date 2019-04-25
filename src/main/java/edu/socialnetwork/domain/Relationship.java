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
 * A Relationship.
 */
@Entity
@Table(name = "relationship")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Relationship implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "status", nullable = false)
    private String status;

    @OneToMany(mappedBy = "relationship")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Profile> users = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public Relationship status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Set<Profile> getUsers() {
        return users;
    }

    public Relationship users(Set<Profile> profiles) {
        this.users = profiles;
        return this;
    }

    public Relationship addUser(Profile profile) {
        this.users.add(profile);
        profile.setRelationship(this);
        return this;
    }

    public Relationship removeUser(Profile profile) {
        this.users.remove(profile);
        profile.setRelationship(null);
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
        Relationship relationship = (Relationship) o;
        if (relationship.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), relationship.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Relationship{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
