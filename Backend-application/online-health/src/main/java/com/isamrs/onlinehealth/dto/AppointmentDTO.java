package com.isamrs.onlinehealth.dto;

import com.isamrs.onlinehealth.model.AppointmentType;

import javax.persistence.Column;
import java.time.LocalDateTime;

public class AppointmentDTO {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private AppointmentType type;
    private String patient;
    private String pharmacyWorker;
    private Double price;

    public AppointmentDTO() {
    }

    public AppointmentDTO(Long id, LocalDateTime start, LocalDateTime end, AppointmentType type, String patient, String pharmacyWorker) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.type = type;
        this.patient = patient;
        this.pharmacyWorker = pharmacyWorker;
    }
    public AppointmentDTO(Long id, LocalDateTime start, LocalDateTime end, AppointmentType type, String patient, String pharmacyWorker, Double price) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.type = type;
        this.patient = patient;
        this.pharmacyWorker = pharmacyWorker;
        this.price = price;
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

    public AppointmentType getType() {
        return type;
    }

    public void setType(AppointmentType type) {
        this.type = type;
    }

    public String getPatient() {
        return patient;
    }

    public void setPatient(String patient) {
        this.patient = patient;
    }

    public String getPharmacyWorker() {
        return pharmacyWorker;
    }

    public void setPharmacyWorker(String pharmacyWorker) {
        this.pharmacyWorker = pharmacyWorker;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
