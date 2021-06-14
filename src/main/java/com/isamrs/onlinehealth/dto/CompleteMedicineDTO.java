package com.isamrs.onlinehealth.dto;

import com.isamrs.onlinehealth.model.Medicine;

public class CompleteMedicineDTO {

    private String identifier;
    private String name;
    private String manufacturer;
    private String description;
    private String type;
    private String dailyDose;
    private boolean isPrescription;
    private String remarks;
    private Boolean deleted;

    public CompleteMedicineDTO(){

    }

    public CompleteMedicineDTO( String identifier, String name, String manufacturer,
                               String description, String type, String dose, boolean perscription, String remark, boolean del) {

        this.identifier = identifier;
        this.name = name;
        this.manufacturer = manufacturer;
        this.description = description;
        this.type = type;
        this.dailyDose = dose;
        this.isPrescription = perscription;
        this.remarks = remark;
        this.deleted = del;
    }

    public CompleteMedicineDTO(Medicine m){
        this.identifier = m.getIdentifier();
        this.name = m.getName();
        this.manufacturer = m.getManufacturer();
        this.description = m.getDescription();
        this.type = m.getType();
        this.dailyDose = m.getDailyDose();
        this.isPrescription = m.isPrescription();
        this.remarks = m.getRemarks();
        this.deleted = m.getDeleted();
    }


    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public boolean isPrescription() {
        return isPrescription;
    }

    public void setPrescription(boolean prescription) {
        isPrescription = prescription;
    }

    public String getDailyDose() {
        return dailyDose;
    }

    public void setDailyDose(String dailyDose) {
        this.dailyDose = dailyDose;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
