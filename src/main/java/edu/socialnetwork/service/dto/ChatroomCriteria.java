package edu.socialnetwork.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the Chatroom entity. This class is used in ChatroomResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /chatrooms?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ChatroomCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private ZonedDateTimeFilter createdDate;

    private StringFilter topic;

    private LongFilter adminId;

    private LongFilter participantId;

    private LongFilter messageId;

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public ZonedDateTimeFilter getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(ZonedDateTimeFilter createdDate) {
        this.createdDate = createdDate;
    }

    public StringFilter getTopic() {
        return topic;
    }

    public void setTopic(StringFilter topic) {
        this.topic = topic;
    }

    public LongFilter getAdminId() {
        return adminId;
    }

    public void setAdminId(LongFilter adminId) {
        this.adminId = adminId;
    }

    public LongFilter getParticipantId() {
        return participantId;
    }

    public void setParticipantId(LongFilter participantId) {
        this.participantId = participantId;
    }

    public LongFilter getMessageId() {
        return messageId;
    }

    public void setMessageId(LongFilter messageId) {
        this.messageId = messageId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ChatroomCriteria that = (ChatroomCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(topic, that.topic) &&
            Objects.equals(adminId, that.adminId) &&
            Objects.equals(participantId, that.participantId) &&
            Objects.equals(messageId, that.messageId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        createdDate,
        topic,
        adminId,
        participantId,
        messageId
        );
    }

    @Override
    public String toString() {
        return "ChatroomCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (createdDate != null ? "createdDate=" + createdDate + ", " : "") +
                (topic != null ? "topic=" + topic + ", " : "") +
                (adminId != null ? "adminId=" + adminId + ", " : "") +
                (participantId != null ? "participantId=" + participantId + ", " : "") +
                (messageId != null ? "messageId=" + messageId + ", " : "") +
            "}";
    }

}
