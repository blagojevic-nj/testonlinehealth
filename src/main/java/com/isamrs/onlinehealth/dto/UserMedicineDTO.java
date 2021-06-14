package com.isamrs.onlinehealth.dto;

import com.isamrs.onlinehealth.model.PriceListItem;

public class UserMedicineDTO {
    private CompleteMedicineDTO medicine;
    private PriceListItem priceListItem;

    public UserMedicineDTO() {
    }

    public UserMedicineDTO(CompleteMedicineDTO medicine, PriceListItem priceListItem) {
        this.medicine = medicine;
        this.priceListItem = priceListItem;
    }

    public CompleteMedicineDTO getMedicine() {
        return medicine;
    }

    public void setMedicine(CompleteMedicineDTO medicine) {
        this.medicine = medicine;
    }

    public PriceListItem getPriceListItem() {
        return priceListItem;
    }

    public void setPriceListItem(PriceListItem priceListItem) {
        this.priceListItem = priceListItem;
    }
}
