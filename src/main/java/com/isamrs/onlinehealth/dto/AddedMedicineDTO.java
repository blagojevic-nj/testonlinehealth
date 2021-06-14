package com.isamrs.onlinehealth.dto;

import com.isamrs.onlinehealth.model.Medicine;

import java.util.List;
import java.util.Set;

public class AddedMedicineDTO {
    private String identifier;
    private String name;
    private String manufacturer;
    private String description;
    private String type;
    private String dailyDose;
    private boolean isPrescription;
    private String remarks;
    private Boolean deleted;
    private List<String> replacementMedicines;



    public AddedMedicineDTO(String identifier, String name, String manufacturer, String description, String type, String dailyDose, boolean isPrescription, String remarks, Boolean deleted, List<String> replacementMedicines) {
        this.identifier = identifier;
        this.name = name;
        this.manufacturer = manufacturer;
        this.description = description;
        this.type = type;
        this.dailyDose = dailyDose;
        this.isPrescription = isPrescription;
        this.remarks = remarks;
        this.deleted = deleted;
        this.replacementMedicines = replacementMedicines;
    }

    public AddedMedicineDTO() {
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

    public String getDailyDose() {
        return dailyDose;
    }

    public void setDailyDose(String dailyDose) {
        this.dailyDose = dailyDose;
    }

    public boolean isPrescription() {
        return isPrescription;
    }

    public void setPrescription(boolean prescription) {
        isPrescription = prescription;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public List<String> getReplacementMedicines() {
        return replacementMedicines;
    }

    public void setReplacementMedicines(List<String> replacementMedicines) {
        this.replacementMedicines = replacementMedicines;
    }
}
