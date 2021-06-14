package com.isamrs.onlinehealth.dto;

import java.time.LocalDateTime;

public class SupplyForOrderDTO {
    private String supplierUsername;
    private String totalCost;
    private String supplyId;
    private LocalDateTime dueDate;
    private PurchaseOrderDTO purchaseOrderDTO;


    public SupplyForOrderDTO() {
    }

    public SupplyForOrderDTO(String supplierUsername, String totalCost, String supplyId, LocalDateTime dueDate, PurchaseOrderDTO purchaseOrderDTO) {
        this.supplierUsername = supplierUsername;
        this.totalCost = totalCost;
        this.supplyId = supplyId;
        this.dueDate = dueDate;
        this.purchaseOrderDTO = purchaseOrderDTO;
    }

    public String getSupplierUsername() {
        return supplierUsername;
    }

    public void setSupplierUsername(String supplierUsername) {
        this.supplierUsername = supplierUsername;
    }

    public String getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(String totalCost) {
        this.totalCost = totalCost;
    }

    public String getSupplyId() {
        return supplyId;
    }

    public void setSupplyId(String supplyId) {
        this.supplyId = supplyId;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public PurchaseOrderDTO getPurchaseOrderDTO() {
        return purchaseOrderDTO;
    }

    public void setPurchaseOrderDTO(PurchaseOrderDTO purchaseOrderDTO) {
        this.purchaseOrderDTO = purchaseOrderDTO;
    }
}
