package edu.socialnetwork.service.dto;

import edu.socialnetwork.domain.Profile;

public class ProfileDistanceDTO {
    private Profile profile;
    private Double distance;

    public ProfileDistanceDTO(Profile profile, Double distance) {
        this.profile = profile;
        this.distance = distance;
    }

    public Profile getProfile() {
        return profile;
    }

    public Double getDistance() {
        return distance;
    }
}
