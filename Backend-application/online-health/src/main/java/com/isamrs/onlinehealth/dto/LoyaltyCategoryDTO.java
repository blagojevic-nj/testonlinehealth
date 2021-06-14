package com.isamrs.onlinehealth.dto;

public class LoyaltyCategoryDTO {
    String id; //Long
    String name;
    String lowLimit; //Double
    String highLimit; //Double
    String discountRate; //Double

    public LoyaltyCategoryDTO() {
    }


    public LoyaltyCategoryDTO(String id, String name, String lowLimit, String highLimit, String discountRate) {
        this.id = id;
        this.name = name;
        this.lowLimit = lowLimit;
        this.highLimit = highLimit;
        this.discountRate = discountRate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLowLimit() {
        return lowLimit;
    }

    public void setLowLimit(String lowLimit) {
        this.lowLimit = lowLimit;
    }

    public String getHighLimit() {
        return highLimit;
    }

    public void setHighLimit(String highLimit) {
        this.highLimit = highLimit;
    }

    public String getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(String discountRate) {
        this.discountRate = discountRate;
    }
}
