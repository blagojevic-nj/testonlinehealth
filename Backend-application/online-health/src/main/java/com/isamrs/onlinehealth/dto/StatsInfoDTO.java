package com.isamrs.onlinehealth.dto;

public class StatsInfoDTO {

    private Double points;
    private String categoryName;
    private Double discount;

    public StatsInfoDTO(Double points, String categoryName, Double discount) {
        this.points = points;
        this.categoryName = categoryName;
        this.discount = discount;
    }

    public StatsInfoDTO() {}

    public Double getPoints() {
        return points;
    }

    public void setPoints(Double points) {
        this.points = points;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }
}
