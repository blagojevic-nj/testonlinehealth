package com.isamrs.onlinehealth.dto;

import java.time.LocalDateTime;

public class NewExamPatientDermatologistPharmacyDTO {
    private LocalDateTime start;
    private LocalDateTime end;
    private Double price;
    private String dermatologistUsername;
    private Long pharmacyId;
    private Long patientId;

    public NewExamPatientDermatologistPharmacyDTO() {
    }

    public NewExamPatientDermatologistPharmacyDTO(LocalDateTime start, LocalDateTime end, Double price, String dermatologistUsername, Long pharmacyId, Long patientId) {
        this.start = start;
        this.end = end;
        this.price = price;
        this.dermatologistUsername = dermatologistUsername;
        this.pharmacyId = pharmacyId;
        this.patientId = patientId;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDermatologistUsername() {
        return dermatologistUsername;
    }

    public void setDermatologistUsername(String dermatologistUsername) {
        this.dermatologistUsername = dermatologistUsername;
    }

    public Long getPharmacyId() {
        return pharmacyId;
    }

    public void setPharmacyId(Long pharmacyId) {
        this.pharmacyId = pharmacyId;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }
}
