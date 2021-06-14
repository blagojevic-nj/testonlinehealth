package com.isamrs.onlinehealth.dto;

import com.isamrs.onlinehealth.model.Medicine;

public class MedicineDTO {

    private Long id;
    private String identifier;
    private String name;
    private String manufacturer;
    private String description;
    private String type;
    private boolean deleted;
    private boolean belongsToPharmacy;

    public MedicineDTO(){

    }

    public MedicineDTO(Long id, String identifier, String name, String manufacturer, String description, String type, boolean del) {
        this.id = id;
        this.identifier = identifier;
        this.name = name;
        this.manufacturer = manufacturer;
        this.description = description;
        this.type = type;
        this.deleted=del;
        this.belongsToPharmacy = false;

    }


    public MedicineDTO(Medicine m){
        this.id = m.getId();
        this.identifier = m.getIdentifier();
        this.name = m.getName();
        this.manufacturer = m.getManufacturer();
        this.description = m.getDescription();
        this.type = m.getType();
        this.deleted=false;
        this.belongsToPharmacy = false;

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
}
