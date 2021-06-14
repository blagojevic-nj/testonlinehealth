package com.isamrs.onlinehealth.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.isamrs.onlinehealth.model.Medicine;

import java.util.List;

public class SearchItemMedicineDTO {
    private Long id;
    private String identifier;
    private String name;
    private String manufacturer;
    private String description;
    private String type;
    private boolean deleted;
    private boolean belongsToPharmacy;
    private String rating;
    private List<SearchItemMedicinePharmacyDTO> pharmacies;

    public SearchItemMedicineDTO(){

    }

    public SearchItemMedicineDTO(Long id, String identifier, String name, String manufacturer, String description, String type, boolean del, String rating, List<SearchItemMedicinePharmacyDTO> pharmacies) {
        this.id = id;
        this.identifier = identifier;
        this.name = name;
        this.manufacturer = manufacturer;
        this.description = description;
        this.type = type;
        this.deleted=del;
        this.belongsToPharmacy = false;
        this.rating = rating;
        this.pharmacies = pharmacies;
    }

    @JsonIgnore
    public void convert(Medicine m){
        this.id = m.getId();
        this.identifier = m.getIdentifier();
        this.name = m.getName();
        this.manufacturer = m.getManufacturer();
        this.description = m.getDescription();
        this.type = m.getType();
        this.deleted=m.getDeleted();
        this.belongsToPharmacy = false;
        this.rating = m.getRating() + "";
    }

    public boolean isBelongsToPharmacy() {
        return belongsToPharmacy;
    }

    public void setBelongsToPharmacy(boolean belongsToPharmacy) {
        this.belongsToPharmacy = belongsToPharmacy;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public List<SearchItemMedicinePharmacyDTO> getPharmacies() {
        return pharmacies;
    }

    public void setPharmacies(List<SearchItemMedicinePharmacyDTO> pharmacies) {
        this.pharmacies = pharmacies;
    }
}
