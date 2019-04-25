package edu.socialnetwork.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Invitation.
 */
@Entity
@Table(name = "invitation")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Invitation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "created_date", nullable = false)
    private ZonedDateTime createdDate;

    @Column(name = "accepted")
    private Boolean accepted;

    @ManyToOne
    @JsonIgnoreProperties(value = {
        "birthDate",
        "sentInvitations",
        "height",
        "weight",
        "unitSystem",
        "aboutMe",
        "showAge",
        "banned",
        "filterPreferences",
        "location",
        "user",
        "gender",
        "ethnicity",
        "relationship",
        "receivedInvitations",
        "sentBlocks",
        "receivedBlocks",
        "sentMessages",
        "adminChatrooms"
    })
    private Profile sent;

    @ManyToOne
    @JsonIgnoreProperties(value = {
        "birthDate",
        "sentInvitations",
        "height",
        "weight",
        "unitSystem",
        "aboutMe",
        "showAge",
        "banned",
        "filterPreferences",
        "location",
        "user",
        "gender",
        "ethnicity",
        "relationship",
        "receivedInvitations",
        "sentBlocks",
        "receivedBlocks",
        "sentMessages",
        "adminChatrooms"
    })
    private Profile received;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public Invitation createdDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public Boolean isAccepted() {
        return accepted;
    }

    public Invitation accepted(Boolean accepted) {
        this.accepted = accepted;
        return this;
    }

    public void setAccepted(Boolean accepted) {
        this.accepted = accepted;
    }

    public Profile getSent() {
        return sent;
    }

    public Invitation sent(Profile profile) {
        this.sent = profile;
        return this;
    }

    public void setSent(Profile profile) {
        this.sent = profile;
    }

    public Profile getReceived() {
        return received;
    }

    public Invitation received(Profile profile) {
        this.received = profile;
        return this;
    }

    public void setReceived(Profile profile) {
        this.received = profile;
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
        Invitation invitation = (Invitation) o;
        if (invitation.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), invitation.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Invitation{" +
            "id=" + getId() +
            ", createdDate='" + getCreatedDate() + "'" +
            ", accepted='" + isAccepted() + "'" +
            "}";
    }
}
