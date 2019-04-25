package edu.socialnetwork.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import edu.socialnetwork.domain.enumeration.UnitSystem;

/**
 * A Profile.
 */
@Entity
@Table(name = "profile")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Profile implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Lob
    @Column(name = "picture")
    private byte[] picture;

    @Column(name = "picture_content_type")
    private String pictureContentType;

    @Column(name = "height")
    private Double height;

    @Column(name = "weight")
    private Double weight;

    @Enumerated(EnumType.STRING)
    @Column(name = "unit_system")
    private UnitSystem unitSystem;

    @Column(name = "about_me")
    private String aboutMe;

    @NotNull
    @Column(name = "display_name", nullable = false)
    private String displayName;

    @Column(name = "show_age")
    private Boolean showAge;

    @Column(name = "banned")
    private Boolean banned;

    @Column(name = "filter_preferences")
    private String filterPreferences;

    @OneToOne
    @JoinColumn(unique = true)
    private Location location;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @ManyToOne
    @JsonIgnoreProperties("users")
    private Relationship relationship;

    @ManyToOne
    @JsonIgnoreProperties("users")
    private Gender gender;

    @ManyToOne
    @JsonIgnoreProperties("users")
    private Ethnicity ethnicity;

    @OneToMany(mappedBy = "sent")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Invitation> sentInvitations = new HashSet<>();
    @OneToMany(mappedBy = "received")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Invitation> receivedInvitations = new HashSet<>();
    @OneToMany(mappedBy = "sent")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Block> sentBlocks = new HashSet<>();
    @OneToMany(mappedBy = "received")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Block> receivedBlocks = new HashSet<>();
    @OneToMany(mappedBy = "sender")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Message> sentMessages = new HashSet<>();
    @OneToMany(mappedBy = "admin")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Chatroom> adminChatrooms = new HashSet<>();
    @ManyToMany(mappedBy = "participants")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<Chatroom> joinedChatrooms = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public Profile birthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public byte[] getPicture() {
        return picture;
    }

    public Profile picture(byte[] picture) {
        this.picture = picture;
        return this;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public String getPictureContentType() {
        return pictureContentType;
    }

    public Profile pictureContentType(String pictureContentType) {
        this.pictureContentType = pictureContentType;
        return this;
    }

    public void setPictureContentType(String pictureContentType) {
        this.pictureContentType = pictureContentType;
    }

    public Double getHeight() {
        return height;
    }

    public Profile height(Double height) {
        this.height = height;
        return this;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Double getWeight() {
        return weight;
    }

    public Profile weight(Double weight) {
        this.weight = weight;
        return this;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public UnitSystem getUnitSystem() {
        return unitSystem;
    }

    public Profile unitSystem(UnitSystem unitSystem) {
        this.unitSystem = unitSystem;
        return this;
    }

    public void setUnitSystem(UnitSystem unitSystem) {
        this.unitSystem = unitSystem;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public Profile aboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
        return this;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Profile displayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Boolean isShowAge() {
        return showAge;
    }

    public Profile showAge(Boolean showAge) {
        this.showAge = showAge;
        return this;
    }

    public void setShowAge(Boolean showAge) {
        this.showAge = showAge;
    }

    public Boolean isBanned() {
        return banned;
    }

    public Profile banned(Boolean banned) {
        this.banned = banned;
        return this;
    }

    public void setBanned(Boolean banned) {
        this.banned = banned;
    }

    public String getFilterPreferences() {
        return filterPreferences;
    }

    public Profile filterPreferences(String filterPreferences) {
        this.filterPreferences = filterPreferences;
        return this;
    }

    public void setFilterPreferences(String filterPreferences) {
        this.filterPreferences = filterPreferences;
    }

    public Location getLocation() {
        return location;
    }

    public Profile location(Location location) {
        this.location = location;
        return this;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public User getUser() {
        return user;
    }

    public Profile user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Relationship getRelationship() {
        return relationship;
    }

    public Profile relationship(Relationship relationship) {
        this.relationship = relationship;
        return this;
    }

    public void setRelationship(Relationship relationship) {
        this.relationship = relationship;
    }

    public Gender getGender() {
        return gender;
    }

    public Profile gender(Gender gender) {
        this.gender = gender;
        return this;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Ethnicity getEthnicity() {
        return ethnicity;
    }

    public Profile ethnicity(Ethnicity ethnicity) {
        this.ethnicity = ethnicity;
        return this;
    }

    public void setEthnicity(Ethnicity ethnicity) {
        this.ethnicity = ethnicity;
    }

    public Set<Invitation> getSentInvitations() {
        return sentInvitations;
    }

    public Profile sentInvitations(Set<Invitation> invitations) {
        this.sentInvitations = invitations;
        return this;
    }

    public Profile addSentInvitation(Invitation invitation) {
        this.sentInvitations.add(invitation);
        invitation.setSent(this);
        return this;
    }

    public Profile removeSentInvitation(Invitation invitation) {
        this.sentInvitations.remove(invitation);
        invitation.setSent(null);
        return this;
    }

    public void setSentInvitations(Set<Invitation> invitations) {
        this.sentInvitations = invitations;
    }

    public Set<Invitation> getReceivedInvitations() {
        return receivedInvitations;
    }

    public Profile receivedInvitations(Set<Invitation> invitations) {
        this.receivedInvitations = invitations;
        return this;
    }

    public Profile addReceivedInvitation(Invitation invitation) {
        this.receivedInvitations.add(invitation);
        invitation.setReceived(this);
        return this;
    }

    public Profile removeReceivedInvitation(Invitation invitation) {
        this.receivedInvitations.remove(invitation);
        invitation.setReceived(null);
        return this;
    }

    public void setReceivedInvitations(Set<Invitation> invitations) {
        this.receivedInvitations = invitations;
    }

    public Set<Block> getSentBlocks() {
        return sentBlocks;
    }

    public Profile sentBlocks(Set<Block> blocks) {
        this.sentBlocks = blocks;
        return this;
    }

    public Profile addSentBlock(Block block) {
        this.sentBlocks.add(block);
        block.setSent(this);
        return this;
    }

    public Profile removeSentBlock(Block block) {
        this.sentBlocks.remove(block);
        block.setSent(null);
        return this;
    }

    public void setSentBlocks(Set<Block> blocks) {
        this.sentBlocks = blocks;
    }

    public Set<Block> getReceivedBlocks() {
        return receivedBlocks;
    }

    public Profile receivedBlocks(Set<Block> blocks) {
        this.receivedBlocks = blocks;
        return this;
    }

    public Profile addReceivedBlock(Block block) {
        this.receivedBlocks.add(block);
        block.setReceived(this);
        return this;
    }

    public Profile removeReceivedBlock(Block block) {
        this.receivedBlocks.remove(block);
        block.setReceived(null);
        return this;
    }

    public void setReceivedBlocks(Set<Block> blocks) {
        this.receivedBlocks = blocks;
    }

    public Set<Message> getSentMessages() {
        return sentMessages;
    }

    public Profile sentMessages(Set<Message> messages) {
        this.sentMessages = messages;
        return this;
    }

    public Profile addSentMessage(Message message) {
        this.sentMessages.add(message);
        message.setSender(this);
        return this;
    }

    public Profile removeSentMessage(Message message) {
        this.sentMessages.remove(message);
        message.setSender(null);
        return this;
    }

    public void setSentMessages(Set<Message> messages) {
        this.sentMessages = messages;
    }

    public Set<Chatroom> getAdminChatrooms() {
        return adminChatrooms;
    }

    public Profile adminChatrooms(Set<Chatroom> chatrooms) {
        this.adminChatrooms = chatrooms;
        return this;
    }

    public Profile addAdminChatroom(Chatroom chatroom) {
        this.adminChatrooms.add(chatroom);
        chatroom.setAdmin(this);
        return this;
    }

    public Profile removeAdminChatroom(Chatroom chatroom) {
        this.adminChatrooms.remove(chatroom);
        chatroom.setAdmin(null);
        return this;
    }

    public void setAdminChatrooms(Set<Chatroom> chatrooms) {
        this.adminChatrooms = chatrooms;
    }

    public Set<Chatroom> getJoinedChatrooms() {
        return joinedChatrooms;
    }

    public Profile joinedChatrooms(Set<Chatroom> chatrooms) {
        this.joinedChatrooms = chatrooms;
        return this;
    }

    public Profile addJoinedChatroom(Chatroom chatroom) {
        this.joinedChatrooms.add(chatroom);
        chatroom.getParticipants().add(this);
        return this;
    }

    public Profile removeJoinedChatroom(Chatroom chatroom) {
        this.joinedChatrooms.remove(chatroom);
        chatroom.getParticipants().remove(this);
        return this;
    }

    public void setJoinedChatrooms(Set<Chatroom> chatrooms) {
        this.joinedChatrooms = chatrooms;
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
        Profile profile = (Profile) o;
        if (profile.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), profile.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Profile{" +
            "id=" + getId() +
            ", birthDate='" + getBirthDate() + "'" +
            ", picture='" + getPicture() + "'" +
            ", pictureContentType='" + getPictureContentType() + "'" +
            ", height=" + getHeight() +
            ", weight=" + getWeight() +
            ", unitSystem='" + getUnitSystem() + "'" +
            ", aboutMe='" + getAboutMe() + "'" +
            ", displayName='" + getDisplayName() + "'" +
            ", showAge='" + isShowAge() + "'" +
            ", banned='" + isBanned() + "'" +
            ", filterPreferences='" + getFilterPreferences() + "'" +
            "}";
    }

}
