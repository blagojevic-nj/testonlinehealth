package com.isamrs.onlinehealth.dto;

import java.time.LocalDateTime;

public class ConsultationDTO {
    Long id;
    LocalDateTime start;
    LocalDateTime end;
    String report;
    String patient;
    String pharmacist;
    double price;
    String pharmacy_id;
    String pharmacy;


    public ConsultationDTO() {
    }

    public ConsultationDTO(Long id, LocalDateTime start, LocalDateTime end, String report, String patient, String pharmacist) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.report = report;
        this.patient = patient;
        this.pharmacist = pharmacist;
    }

    public ConsultationDTO(Long id, LocalDateTime start, LocalDateTime end, String report, String patient, String pharmacist, double price) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.report = report;
        this.patient = patient;
        this.pharmacist = pharmacist;
        this.price = price;
    }

    public ConsultationDTO(Long id, LocalDateTime start, LocalDateTime end, String report, String patient, String pharmacist, double price, String pharmacy_id, String pharmacy) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.report = report;
        this.patient = patient;
        this.pharmacist = pharmacist;
        this.price = price;
        this.pharmacy_id = pharmacy_id;
        this.pharmacy = pharmacy;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public String getPatient() {
        return patient;
    }

    public void setPatient(String patient) {
        this.patient = patient;
    }

    public String getPharmacist() {
        return pharmacist;
    }

    public void setPharmacist(String pharmacist) {
        this.pharmacist = pharmacist;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getPharmacy_id() {
        return pharmacy_id;
    }

    public void setPharmacy_id(String pharmacy_id) {
        this.pharmacy_id = pharmacy_id;
    }

    public String getPharmacy() {
        return pharmacy;
    }

    public void setPharmacy(String pharmacy) {
        this.pharmacy = pharmacy;
    }
}
