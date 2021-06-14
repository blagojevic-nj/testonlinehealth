package com.isamrs.onlinehealth.dto;

import com.isamrs.onlinehealth.model.Medicine;

public class PurchaseOrderItemDTO {

    private Long medicineId;
    private int kolicina;
    private String medicineName;
    private String identifier;


    public PurchaseOrderItemDTO(Long id, int kolicina) {
        this.medicineId = id;
        this.kolicina = kolicina;
        this.medicineName = "";
        this.identifier = "";
    }

    public PurchaseOrderItemDTO() {
    }

    public PurchaseOrderItemDTO(Medicine m, int kolicina) {
        this.medicineId = m.getId();
        this.kolicina = kolicina;
        this.medicineName = m.getName();
        this.identifier = m.getIdentifier();
    }

    public Long getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(Long medicineId) {
        this.medicineId = medicineId;
    }

    public int getKolicina() {
        return kolicina;
    }

    public void setKolicina(int kolicina) {
        this.kolicina = kolicina;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
}
