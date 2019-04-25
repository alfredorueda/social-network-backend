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

/**
 * Criteria class for the Location entity. This class is used in LocationResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /locations?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class LocationCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private DoubleFilter latitude;

    private DoubleFilter longitude;

    private StringFilter urlGoogleMaps;

    private StringFilter urlOpenStreetMap;

    private StringFilter address;

    private StringFilter postalCode;

    private StringFilter city;

    private StringFilter stateProvice;

    private StringFilter county;

    private StringFilter country;

    private LongFilter userId;

    private LongFilter messageId;

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public DoubleFilter getLatitude() {
        return latitude;
    }

    public void setLatitude(DoubleFilter latitude) {
        this.latitude = latitude;
    }

    public DoubleFilter getLongitude() {
        return longitude;
    }

    public void setLongitude(DoubleFilter longitude) {
        this.longitude = longitude;
    }

    public StringFilter getUrlGoogleMaps() {
        return urlGoogleMaps;
    }

    public void setUrlGoogleMaps(StringFilter urlGoogleMaps) {
        this.urlGoogleMaps = urlGoogleMaps;
    }

    public StringFilter getUrlOpenStreetMap() {
        return urlOpenStreetMap;
    }

    public void setUrlOpenStreetMap(StringFilter urlOpenStreetMap) {
        this.urlOpenStreetMap = urlOpenStreetMap;
    }

    public StringFilter getAddress() {
        return address;
    }

    public void setAddress(StringFilter address) {
        this.address = address;
    }

    public StringFilter getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(StringFilter postalCode) {
        this.postalCode = postalCode;
    }

    public StringFilter getCity() {
        return city;
    }

    public void setCity(StringFilter city) {
        this.city = city;
    }

    public StringFilter getStateProvice() {
        return stateProvice;
    }

    public void setStateProvice(StringFilter stateProvice) {
        this.stateProvice = stateProvice;
    }

    public StringFilter getCounty() {
        return county;
    }

    public void setCounty(StringFilter county) {
        this.county = county;
    }

    public StringFilter getCountry() {
        return country;
    }

    public void setCountry(StringFilter country) {
        this.country = country;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
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
        final LocationCriteria that = (LocationCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(latitude, that.latitude) &&
            Objects.equals(longitude, that.longitude) &&
            Objects.equals(urlGoogleMaps, that.urlGoogleMaps) &&
            Objects.equals(urlOpenStreetMap, that.urlOpenStreetMap) &&
            Objects.equals(address, that.address) &&
            Objects.equals(postalCode, that.postalCode) &&
            Objects.equals(city, that.city) &&
            Objects.equals(stateProvice, that.stateProvice) &&
            Objects.equals(county, that.county) &&
            Objects.equals(country, that.country) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(messageId, that.messageId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        latitude,
        longitude,
        urlGoogleMaps,
        urlOpenStreetMap,
        address,
        postalCode,
        city,
        stateProvice,
        county,
        country,
        userId,
        messageId
        );
    }

    @Override
    public String toString() {
        return "LocationCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (latitude != null ? "latitude=" + latitude + ", " : "") +
                (longitude != null ? "longitude=" + longitude + ", " : "") +
                (urlGoogleMaps != null ? "urlGoogleMaps=" + urlGoogleMaps + ", " : "") +
                (urlOpenStreetMap != null ? "urlOpenStreetMap=" + urlOpenStreetMap + ", " : "") +
                (address != null ? "address=" + address + ", " : "") +
                (postalCode != null ? "postalCode=" + postalCode + ", " : "") +
                (city != null ? "city=" + city + ", " : "") +
                (stateProvice != null ? "stateProvice=" + stateProvice + ", " : "") +
                (county != null ? "county=" + county + ", " : "") +
                (country != null ? "country=" + country + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
                (messageId != null ? "messageId=" + messageId + ", " : "") +
            "}";
    }

}
