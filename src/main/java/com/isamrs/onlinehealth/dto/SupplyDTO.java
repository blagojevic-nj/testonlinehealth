package com.isamrs.onlinehealth.dto;

public class SupplyDTO {
    private String supplierUsername;
    private Double TotalCost;
    private Long SupplyId;


    public SupplyDTO() {
    }

    public SupplyDTO(String supplierUsername, Double totalCost, Long supplyId) {
        this.supplierUsername = supplierUsername;
        TotalCost = totalCost;
        SupplyId = supplyId;
    }

    public String getSupplierUsername() {
        return supplierUsername;
    }

    public void setSupplierUsername(String supplierUsername) {
        this.supplierUsername = supplierUsername;
    }

    public Double getTotalCost() {
        return TotalCost;
    }

    public void setTotalCost(Double totalCost) {
        TotalCost = totalCost;
    }

    public Long getSupplyId() {
        return SupplyId;
    }

    public void setSupplyId(Long supplyId) {
        SupplyId = supplyId;
    }
}
