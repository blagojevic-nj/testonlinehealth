package com.isamrs.onlinehealth.dto;

public class PatientsRatingDTO {
    private RatingType ratingType;
    private Long entityId;
    private Double rating;
    private String username;

    public PatientsRatingDTO(RatingType ratingType, Long entityId, Double rating, String username) {
        this.ratingType = ratingType;
        this.entityId = entityId;
        this.rating = rating;
        this.username = username;
    }

    public PatientsRatingDTO() {
    }

    public RatingType getRatingType() {
        return ratingType;
    }

    public void setRatingType(RatingType ratingType) {
        this.ratingType = ratingType;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
