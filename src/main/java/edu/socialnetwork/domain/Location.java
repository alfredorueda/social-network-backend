package edu.socialnetwork.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Location.
 */
@Entity
@Table(name = "location")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Location implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @NotNull
    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @Column(name = "url_google_maps")
    private String urlGoogleMaps;

    @Column(name = "url_open_street_map")
    private String urlOpenStreetMap;

    @Column(name = "address")
    private String address;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "city")
    private String city;

    @Column(name = "state_provice")
    private String stateProvice;

    @Column(name = "county")
    private String county;

    @Column(name = "country")
    private String country;

    @OneToOne(mappedBy = "location")
    @JsonIgnore
    private Profile user;

    @OneToOne(mappedBy = "location")
    @JsonIgnore
    private Message message;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Location latitude(Double latitude) {
        this.latitude = latitude;
        return this;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Location longitude(Double longitude) {
        this.longitude = longitude;
        return this;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getUrlGoogleMaps() {
        return urlGoogleMaps;
    }

    public Location urlGoogleMaps(String urlGoogleMaps) {
        this.urlGoogleMaps = urlGoogleMaps;
        return this;
    }

    public void setUrlGoogleMaps(String urlGoogleMaps) {
        this.urlGoogleMaps = urlGoogleMaps;
    }

    public String getUrlOpenStreetMap() {
        return urlOpenStreetMap;
    }

    public Location urlOpenStreetMap(String urlOpenStreetMap) {
        this.urlOpenStreetMap = urlOpenStreetMap;
        return this;
    }

    public void setUrlOpenStreetMap(String urlOpenStreetMap) {
        this.urlOpenStreetMap = urlOpenStreetMap;
    }

    public String getAddress() {
        return address;
    }

    public Location address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public Location postalCode(String postalCode) {
        this.postalCode = postalCode;
        return this;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public Location city(String city) {
        this.city = city;
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStateProvice() {
        return stateProvice;
    }

    public Location stateProvice(String stateProvice) {
        this.stateProvice = stateProvice;
        return this;
    }

    public void setStateProvice(String stateProvice) {
        this.stateProvice = stateProvice;
    }

    public String getCounty() {
        return county;
    }

    public Location county(String county) {
        this.county = county;
        return this;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getCountry() {
        return country;
    }

    public Location country(String country) {
        this.country = country;
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Profile getUser() {
        return user;
    }

    public Location user(Profile profile) {
        this.user = profile;
        return this;
    }

    public void setUser(Profile profile) {
        this.user = profile;
    }

    public Message getMessage() {
        return message;
    }

    public Location message(Message message) {
        this.message = message;
        return this;
    }

    public void setMessage(Message message) {
        this.message = message;
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
        Location location = (Location) o;
        if (location.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), location.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Location{" +
            "id=" + getId() +
            ", latitude=" + getLatitude() +
            ", longitude=" + getLongitude() +
            ", urlGoogleMaps='" + getUrlGoogleMaps() + "'" +
            ", urlOpenStreetMap='" + getUrlOpenStreetMap() + "'" +
            ", address='" + getAddress() + "'" +
            ", postalCode='" + getPostalCode() + "'" +
            ", city='" + getCity() + "'" +
            ", stateProvice='" + getStateProvice() + "'" +
            ", county='" + getCounty() + "'" +
            ", country='" + getCountry() + "'" +
            "}";
    }
}
