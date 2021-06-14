package com.isamrs.onlinehealth.model;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "consultations")
public class Consultation extends Appointment implements Serializable {
    @Column(
            name = "price",
            nullable = false
    )
    private Double price;

    @ManyToOne
    @JoinColumn(name = "patient")
    protected Patient patient;

    @ManyToOne
    @JoinColumn(name = "pharmacist")
    private Pharmacist pharmacist;

    @ManyToOne
    @JoinColumn(name = "pharmacy_id", nullable = false)
    private Pharmacy pharmacy;

    public Pharmacist getPharmacist() {
        return pharmacist;
    }

    public void setPharmacist(Pharmacist pharmacist) {
        this.pharmacist = pharmacist;
    }

    public Consultation() {
    }

    public Consultation(Long id, LocalDateTime start, LocalDateTime end, String report, Patient patient) {
        super(id, start, end, report);
        this.patient = patient;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Pharmacy getPharmacy() {
        return pharmacy;
    }

    public void setPharmacy(Pharmacy pharmacy) {
        this.pharmacy = pharmacy;
    }
}
