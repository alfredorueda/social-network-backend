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
 * Criteria class for the Message entity. This class is used in MessageResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /messages?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class MessageCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private ZonedDateTimeFilter createdDate;

    private StringFilter message;

    private StringFilter url;

    private LongFilter locationId;

    private LongFilter senderId;

    private LongFilter chatroomId;

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

    public StringFilter getMessage() {
        return message;
    }

    public void setMessage(StringFilter message) {
        this.message = message;
    }

    public StringFilter getUrl() {
        return url;
    }

    public void setUrl(StringFilter url) {
        this.url = url;
    }

    public LongFilter getLocationId() {
        return locationId;
    }

    public void setLocationId(LongFilter locationId) {
        this.locationId = locationId;
    }

    public LongFilter getSenderId() {
        return senderId;
    }

    public void setSenderId(LongFilter senderId) {
        this.senderId = senderId;
    }

    public LongFilter getChatroomId() {
        return chatroomId;
    }

    public void setChatroomId(LongFilter chatroomId) {
        this.chatroomId = chatroomId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final MessageCriteria that = (MessageCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(message, that.message) &&
            Objects.equals(url, that.url) &&
            Objects.equals(locationId, that.locationId) &&
            Objects.equals(senderId, that.senderId) &&
            Objects.equals(chatroomId, that.chatroomId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        createdDate,
        message,
        url,
        locationId,
        senderId,
        chatroomId
        );
    }

    @Override
    public String toString() {
        return "MessageCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (createdDate != null ? "createdDate=" + createdDate + ", " : "") +
                (message != null ? "message=" + message + ", " : "") +
                (url != null ? "url=" + url + ", " : "") +
                (locationId != null ? "locationId=" + locationId + ", " : "") +
                (senderId != null ? "senderId=" + senderId + ", " : "") +
                (chatroomId != null ? "chatroomId=" + chatroomId + ", " : "") +
            "}";
    }

}
