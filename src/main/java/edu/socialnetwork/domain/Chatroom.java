package edu.socialnetwork.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Chatroom.
 */
@Entity
@Table(name = "chatroom")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Chatroom implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "created_date", nullable = false)
    private ZonedDateTime createdDate;

    @NotNull
    @Column(name = "topic", nullable = false)
    private String topic;

    @ManyToOne
    @JsonIgnoreProperties("adminChatrooms")
    private Profile admin;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "chatroom_participant",
               joinColumns = @JoinColumn(name = "chatroom_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "participant_id", referencedColumnName = "id"))
    private Set<Profile> participants = new HashSet<>();

    @OneToMany(mappedBy = "chatroom")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Message> messages = new HashSet<>();
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

    public Chatroom createdDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getTopic() {
        return topic;
    }

    public Chatroom topic(String topic) {
        this.topic = topic;
        return this;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Profile getAdmin() {
        return admin;
    }

    public Chatroom admin(Profile profile) {
        this.admin = profile;
        return this;
    }

    public void setAdmin(Profile profile) {
        this.admin = profile;
    }

    public Set<Profile> getParticipants() {
        return participants;
    }

    public Chatroom participants(Set<Profile> profiles) {
        this.participants = profiles;
        return this;
    }

    public Chatroom addParticipant(Profile profile) {
        this.participants.add(profile);
        profile.getJoinedChatrooms().add(this);
        return this;
    }

    public Chatroom removeParticipant(Profile profile) {
        this.participants.remove(profile);
        profile.getJoinedChatrooms().remove(this);
        return this;
    }

    public void setParticipants(Set<Profile> profiles) {
        this.participants = profiles;
    }

    public Set<Message> getMessages() {
        return messages;
    }

    public Chatroom messages(Set<Message> messages) {
        this.messages = messages;
        return this;
    }

    public Chatroom addMessage(Message message) {
        this.messages.add(message);
        message.setChatroom(this);
        return this;
    }

    public Chatroom removeMessage(Message message) {
        this.messages.remove(message);
        message.setChatroom(null);
        return this;
    }

    public void setMessages(Set<Message> messages) {
        this.messages = messages;
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
        Chatroom chatroom = (Chatroom) o;
        if (chatroom.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), chatroom.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Chatroom{" +
            "id=" + getId() +
            ", createdDate='" + getCreatedDate() + "'" +
            ", topic='" + getTopic() + "'" +
            "}";
    }
}
