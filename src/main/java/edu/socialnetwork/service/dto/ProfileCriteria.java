package edu.socialnetwork.service.dto;

import edu.socialnetwork.domain.enumeration.UnitSystem;
import io.github.jhipster.service.filter.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * Criteria class for the Profile entity. This class is used in ProfileResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /profiles?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ProfileCriteria implements Serializable {
    /**
     * Class for filtering UnitSystem
     */
    public static class UnitSystemFilter extends Filter<UnitSystem> {
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter birthDate;

    private DoubleFilter height;

    private DoubleFilter weight;

    private UnitSystemFilter unitSystem;

    private StringFilter aboutMe;

    private StringFilter displayName;

    private BooleanFilter showAge;

    private BooleanFilter banned;

    private StringFilter filterPreferences;

    private LongFilter locationId;

    private LongFilter userId;

    private LongFilter relationshipId;

    private LongFilter genderId;

    private LongFilter ethnicityId;

    private LongFilter sentInvitationId;

    private LongFilter receivedInvitationId;

    private LongFilter sentBlockId;

    private LongFilter receivedBlockId;

    private LongFilter sentMessageId;

    private LongFilter sentDirectMessageId;

    private LongFilter receivedDirectMessageId;

    private LongFilter adminChatroomId;

    private LongFilter joinedChatroomId;

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LocalDateFilter getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDateFilter birthDate) {
        this.birthDate = birthDate;
    }

    public DoubleFilter getHeight() {
        return height;
    }

    public void setHeight(DoubleFilter height) {
        this.height = height;
    }

    public DoubleFilter getWeight() {
        return weight;
    }

    public void setWeight(DoubleFilter weight) {
        this.weight = weight;
    }

    public UnitSystemFilter getUnitSystem() {
        return unitSystem;
    }

    public void setUnitSystem(UnitSystemFilter unitSystem) {
        this.unitSystem = unitSystem;
    }

    public StringFilter getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(StringFilter aboutMe) {
        this.aboutMe = aboutMe;
    }

    public StringFilter getDisplayName() {
        return displayName;
    }

    public void setDisplayName(StringFilter displayName) {
        this.displayName = displayName;
    }

    public BooleanFilter getShowAge() {
        return showAge;
    }

    public void setShowAge(BooleanFilter showAge) {
        this.showAge = showAge;
    }

    public BooleanFilter getBanned() {
        return banned;
    }

    public void setBanned(BooleanFilter banned) {
        this.banned = banned;
    }

    public StringFilter getFilterPreferences() {
        return filterPreferences;
    }

    public void setFilterPreferences(StringFilter filterPreferences) {
        this.filterPreferences = filterPreferences;
    }

    public LongFilter getLocationId() {
        return locationId;
    }

    public void setLocationId(LongFilter locationId) {
        this.locationId = locationId;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public LongFilter getRelationshipId() {
        return relationshipId;
    }

    public void setRelationshipId(LongFilter relationshipId) {
        this.relationshipId = relationshipId;
    }

    public LongFilter getGenderId() {
        return genderId;
    }

    public void setGenderId(LongFilter genderId) {
        this.genderId = genderId;
    }

    public LongFilter getEthnicityId() {
        return ethnicityId;
    }

    public void setEthnicityId(LongFilter ethnicityId) {
        this.ethnicityId = ethnicityId;
    }

    public LongFilter getSentInvitationId() {
        return sentInvitationId;
    }

    public void setSentInvitationId(LongFilter sentInvitationId) {
        this.sentInvitationId = sentInvitationId;
    }

    public LongFilter getReceivedInvitationId() {
        return receivedInvitationId;
    }

    public void setReceivedInvitationId(LongFilter receivedInvitationId) {
        this.receivedInvitationId = receivedInvitationId;
    }

    public LongFilter getSentBlockId() {
        return sentBlockId;
    }

    public void setSentBlockId(LongFilter sentBlockId) {
        this.sentBlockId = sentBlockId;
    }

    public LongFilter getReceivedBlockId() {
        return receivedBlockId;
    }

    public void setReceivedBlockId(LongFilter receivedBlockId) {
        this.receivedBlockId = receivedBlockId;
    }

    public LongFilter getSentMessageId() {
        return sentMessageId;
    }

    public void setSentMessageId(LongFilter sentMessageId) {
        this.sentMessageId = sentMessageId;
    }

    public LongFilter getSentDirectMessageId() {
        return sentDirectMessageId;
    }

    public void setSentDirectMessageId(LongFilter sentDirectMessageId) {
        this.sentDirectMessageId = sentDirectMessageId;
    }

    public LongFilter getReceivedDirectMessageId() {
        return receivedDirectMessageId;
    }

    public void setReceivedDirectMessageId(LongFilter receivedDirectMessageId) {
        this.receivedDirectMessageId = receivedDirectMessageId;
    }

    public LongFilter getAdminChatroomId() {
        return adminChatroomId;
    }

    public void setAdminChatroomId(LongFilter adminChatroomId) {
        this.adminChatroomId = adminChatroomId;
    }

    public LongFilter getJoinedChatroomId() {
        return joinedChatroomId;
    }

    public void setJoinedChatroomId(LongFilter joinedChatroomId) {
        this.joinedChatroomId = joinedChatroomId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ProfileCriteria that = (ProfileCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(birthDate, that.birthDate) &&
            Objects.equals(height, that.height) &&
            Objects.equals(weight, that.weight) &&
            Objects.equals(unitSystem, that.unitSystem) &&
            Objects.equals(aboutMe, that.aboutMe) &&
            Objects.equals(displayName, that.displayName) &&
            Objects.equals(showAge, that.showAge) &&
            Objects.equals(banned, that.banned) &&
            Objects.equals(filterPreferences, that.filterPreferences) &&
            Objects.equals(locationId, that.locationId) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(relationshipId, that.relationshipId) &&
            Objects.equals(genderId, that.genderId) &&
            Objects.equals(ethnicityId, that.ethnicityId) &&
            Objects.equals(sentInvitationId, that.sentInvitationId) &&
            Objects.equals(receivedInvitationId, that.receivedInvitationId) &&
            Objects.equals(sentBlockId, that.sentBlockId) &&
            Objects.equals(receivedBlockId, that.receivedBlockId) &&
            Objects.equals(sentMessageId, that.sentMessageId) &&
            Objects.equals(sentDirectMessageId, that.sentDirectMessageId) &&
            Objects.equals(receivedDirectMessageId, that.receivedDirectMessageId) &&
            Objects.equals(adminChatroomId, that.adminChatroomId) &&
            Objects.equals(joinedChatroomId, that.joinedChatroomId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        birthDate,
        height,
        weight,
        unitSystem,
        aboutMe,
        displayName,
        showAge,
        banned,
        filterPreferences,
        locationId,
        userId,
        relationshipId,
        genderId,
        ethnicityId,
        sentInvitationId,
        receivedInvitationId,
        sentBlockId,
        receivedBlockId,
        sentMessageId,
        sentDirectMessageId,
        receivedDirectMessageId,
        adminChatroomId,
        joinedChatroomId
        );
    }

    @Override
    public String toString() {
        return "ProfileCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (birthDate != null ? "birthDate=" + birthDate + ", " : "") +
                (height != null ? "height=" + height + ", " : "") +
                (weight != null ? "weight=" + weight + ", " : "") +
                (unitSystem != null ? "unitSystem=" + unitSystem + ", " : "") +
                (aboutMe != null ? "aboutMe=" + aboutMe + ", " : "") +
                (displayName != null ? "displayName=" + displayName + ", " : "") +
                (showAge != null ? "showAge=" + showAge + ", " : "") +
                (banned != null ? "banned=" + banned + ", " : "") +
                (filterPreferences != null ? "filterPreferences=" + filterPreferences + ", " : "") +
                (locationId != null ? "locationId=" + locationId + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
                (relationshipId != null ? "relationshipId=" + relationshipId + ", " : "") +
                (genderId != null ? "genderId=" + genderId + ", " : "") +
                (ethnicityId != null ? "ethnicityId=" + ethnicityId + ", " : "") +
                (sentInvitationId != null ? "sentInvitationId=" + sentInvitationId + ", " : "") +
                (receivedInvitationId != null ? "receivedInvitationId=" + receivedInvitationId + ", " : "") +
                (sentBlockId != null ? "sentBlockId=" + sentBlockId + ", " : "") +
                (receivedBlockId != null ? "receivedBlockId=" + receivedBlockId + ", " : "") +
                (sentMessageId != null ? "sentMessageId=" + sentMessageId + ", " : "") +
                (sentDirectMessageId != null ? "sentDirectMessageId=" + sentDirectMessageId + ", " : "") +
                (receivedDirectMessageId != null ? "receivedDirectMessageId=" + receivedDirectMessageId + ", " : "") +
                (adminChatroomId != null ? "adminChatroomId=" + adminChatroomId + ", " : "") +
                (joinedChatroomId != null ? "joinedChatroomId=" + joinedChatroomId + ", " : "") +
            "}";
    }

}
