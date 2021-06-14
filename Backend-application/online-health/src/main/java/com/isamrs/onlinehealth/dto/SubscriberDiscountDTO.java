package com.isamrs.onlinehealth.dto;

import java.util.List;

public class SubscriberDiscountDTO {
    private String pharmacyName;
    private Long pharmacyId;
    private List<DiscountDTO> discounts;
    private List<PromotionDTO> promotions;

    public SubscriberDiscountDTO() {
    }

    public SubscriberDiscountDTO(String pharmacyName, Long pharmacyId, List<DiscountDTO> discounts, List<PromotionDTO> promotions) {
        this.pharmacyName = pharmacyName;
        this.pharmacyId = pharmacyId;
        this.discounts = discounts;
        this.promotions = promotions;
    }

    public String getPharmacyName() {
        return pharmacyName;
    }

    public void setPharmacyName(String pharmacyName) {
        this.pharmacyName = pharmacyName;
    }

    public Long getPharmacyId() {
        return pharmacyId;
    }

    public void setPharmacyId(Long pharmacyId) {
        this.pharmacyId = pharmacyId;
    }

    public List<DiscountDTO> getDiscounts() {
        return discounts;
    }

    public void setDiscounts(List<DiscountDTO> discounts) {
        this.discounts = discounts;
    }

    public List<PromotionDTO> getPromotions() {
        return promotions;
    }

    public void setPromotions(List<PromotionDTO> promotions) {
        this.promotions = promotions;
    }
}
