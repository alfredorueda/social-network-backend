package edu.socialnetwork.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DirectMessage.
 */
@Entity
@Table(name = "direct_message")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DirectMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "created_date", nullable = false)
    private ZonedDateTime createdDate;

    @Column(name = "message")
    private String message;

    @Column(name = "url")
    private String url;

    @Lob
    @Column(name = "picture")
    private byte[] picture;

    @Column(name = "picture_content_type")
    private String pictureContentType;

    @ManyToOne
    @JsonIgnoreProperties("sentDirectMessages")
    private Profile sender;

    @ManyToOne
    @JsonIgnoreProperties("receivedDirectMessages")
    private Profile recipient;

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

    public DirectMessage createdDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getMessage() {
        return message;
    }

    public DirectMessage message(String message) {
        this.message = message;
        return this;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUrl() {
        return url;
    }

    public DirectMessage url(String url) {
        this.url = url;
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public byte[] getPicture() {
        return picture;
    }

    public DirectMessage picture(byte[] picture) {
        this.picture = picture;
        return this;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public String getPictureContentType() {
        return pictureContentType;
    }

    public DirectMessage pictureContentType(String pictureContentType) {
        this.pictureContentType = pictureContentType;
        return this;
    }

    public void setPictureContentType(String pictureContentType) {
        this.pictureContentType = pictureContentType;
    }

    public Profile getSender() {
        return sender;
    }

    public DirectMessage sender(Profile profile) {
        this.sender = profile;
        return this;
    }

    public void setSender(Profile profile) {
        this.sender = profile;
    }

    public Profile getRecipient() {
        return recipient;
    }

    public DirectMessage recipient(Profile profile) {
        this.recipient = profile;
        return this;
    }

    public void setRecipient(Profile profile) {
        this.recipient = profile;
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
        DirectMessage directMessage = (DirectMessage) o;
        if (directMessage.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), directMessage.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DirectMessage{" +
            "id=" + getId() +
            ", createdDate='" + getCreatedDate() + "'" +
            ", message='" + getMessage() + "'" +
            ", url='" + getUrl() + "'" +
            ", picture='" + getPicture() + "'" +
            ", pictureContentType='" + getPictureContentType() + "'" +
            "}";
    }
}
