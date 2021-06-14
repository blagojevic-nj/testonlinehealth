package com.isamrs.onlinehealth.dto;

public class NeededForOrderDTO {
    String neededAmount;
    String haveAmount;
    String identifier;
    String medicineName;

    public NeededForOrderDTO() {
        neededAmount = "";
        haveAmount = "";
        identifier = "";
        medicineName = "";
    }

    public NeededForOrderDTO(String neededAmount, String haveAmount, String identifier, String medicineName) {
        this.neededAmount = neededAmount;
        this.haveAmount = haveAmount;
        this.identifier = identifier;
        this.medicineName = medicineName;
    }

    public String getNeededAmount() {
        return neededAmount;
    }

    public void setNeededAmount(String neededAmount) {
        this.neededAmount = neededAmount;
    }

    public String getHaveAmount() {
        return haveAmount;
    }

    public void setHaveAmount(String haveAmount) {
        this.haveAmount = haveAmount;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }
}
