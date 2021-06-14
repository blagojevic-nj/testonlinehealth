package com.isamrs.onlinehealth.model;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "examinations")
public class Examination extends Appointment implements Serializable {

    @Column(
            name = "price",
            nullable = false
    )
    private Double price;

    @ManyToOne
    @JoinColumn(name = "dermatologist")
    private Dermatologist dermatologist;
    @ManyToOne
    @JoinColumn(name = "patient")
    private Patient patient;
    @Column(
            name = "deleted"
    )
    private Boolean deleted = false;

    @ManyToOne
    @JoinColumn(name = "pharmacy_id", nullable = false)
    private Pharmacy pharmacy;

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }


    public Examination(Long id, LocalDateTime start, LocalDateTime end, String report, Double price, Dermatologist dermatologist, Patient patient, Boolean deleted, Pharmacy pharmacy) {
        super(id, start, end, report);
        this.price = price;
        this.dermatologist = dermatologist;
        this.patient = patient;
        this.deleted = deleted;
        this.pharmacy = pharmacy;
    }

    public Pharmacy getPharmacy() {
        return pharmacy;
    }

    public void setPharmacy(Pharmacy pharmacy) {
        this.pharmacy = pharmacy;
    }

    public Examination() {
    }

    public Dermatologist getDermatologist() {
        return dermatologist;
    }

    public void setDermatologist(Dermatologist dermatologist) {
        this.dermatologist = dermatologist;
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

}
