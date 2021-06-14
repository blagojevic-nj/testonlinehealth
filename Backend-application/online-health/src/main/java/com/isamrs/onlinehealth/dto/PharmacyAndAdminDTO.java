package com.isamrs.onlinehealth.dto;

import java.util.List;

public class PharmacyAndAdminDTO{
    private String name;
    private String location;
    private String description;

    public String getConsultation_price() {
        return consultation_price;
    }

    public void setConsultation_price(String consultation_price) {
        this.consultation_price = consultation_price;
    }

    private String consultation_price;

    public PharmacyAndAdminDTO() {
    }

    public PharmacyAndAdminDTO(String name, String location, String description, String consultation_price, List<PharmacyAdminDTO> pharmacyAdmins) {
        this.name = name;
        this.location = location;
        this.description = description;
        this.consultation_price = consultation_price;
        this.pharmacyAdmins = pharmacyAdmins;
    }

    public List<PharmacyAdminDTO> getPharmacyAdmins() {
        return pharmacyAdmins;
    }

    public void setPharmacyAdmins(List<PharmacyAdminDTO> pharmacyAdmins) {
        this.pharmacyAdmins = pharmacyAdmins;
    }

    public List<PharmacyAdminDTO> pharmacyAdmins;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
