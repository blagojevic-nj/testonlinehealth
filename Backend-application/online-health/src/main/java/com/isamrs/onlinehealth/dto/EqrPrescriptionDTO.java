package com.isamrs.onlinehealth.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class EqrPrescriptionDTO {
    String id;
    String username;
    String firstName;
    String lastName;
    String date;
    String pharmacyId;
    String status;
    List<EqrPrescriptionItemDTO> medicines;

    public EqrPrescriptionDTO() {
        this.id = "";
        this.username = "";
        this.firstName = "";
        this.lastName = "";
        this.date = "";
        this.pharmacyId = "";
        this.medicines = new ArrayList<>();
    }

    public EqrPrescriptionDTO(String id, String username, String firstName, String lastName, String date, String pharmacyId, List<EqrPrescriptionItemDTO> medicines) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.date = date;
        this.pharmacyId = pharmacyId;
        this.medicines = medicines;
    }

    public EqrPrescriptionDTO(String id, String username, String firstName, String lastName, String date, String pharmacyId, String status, List<EqrPrescriptionItemDTO> medicines) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.date = date;
        this.pharmacyId = pharmacyId;
        this.status = status;
        this.medicines = medicines;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPharmacyId() {
        return pharmacyId;
    }

    public void setPharmacyId(String pharmacyId) {
        this.pharmacyId = pharmacyId;
    }

    public List<EqrPrescriptionItemDTO> getMedicines() {
        return medicines;
    }

    public void setMedicines(List<EqrPrescriptionItemDTO> medicines) {
        this.medicines = medicines;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @JsonIgnore
    public String serialize(){
        return new Gson().toJson(this);
    }

    @JsonIgnore
    public EqrPrescriptionDTO deserialize(String jsonString){
        return new Gson().fromJson(jsonString, EqrPrescriptionDTO.class);
    }
}
