package com.isamrs.onlinehealth.dto;

public class LoyaltyMedicinePointsDTO {
    String id;//Long

    String points; //Double

    public LoyaltyMedicinePointsDTO() {
    }

    public LoyaltyMedicinePointsDTO(String id, String points) {
        this.id = id;
        this.points = points;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }
}
