package com.isamrs.onlinehealth.model;


import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@Entity
@Table(name = "eprescription")
public class EPrescription implements Serializable {
    @Column(
            name = "eprescription_id",
            unique = true,
            nullable = false
    )
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<EPrescriptionItem> ePrescriptionItems = new HashSet<>();
    @ManyToOne
    @JoinColumn(name = "patient")
    private Patient patient;

//    @ManyToOne(fetch = FetchType.EAGER)
//    private Pharmacy pharmacy;

    @Column(
            name = "status",
            nullable = false
    )
    @Enumerated(EnumType.STRING)
    protected EPrescriptionStatus status;

    @Column(
            name = "issue_date",
            nullable = false
    )
    private LocalDateTime issueDate;
    @Column(
            name = "deleted"
    )
    private Boolean deleted = false;

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }


    public EPrescription() {
        
    }

    public EPrescription(Long id, Set<EPrescriptionItem> ePrescriptionItems, Patient patient, LocalDateTime issueDate) {
        this.id = id;
        this.ePrescriptionItems = ePrescriptionItems;
        this.patient = patient;
        this.issueDate = issueDate;
    }

    public EPrescription(Long id, Set<EPrescriptionItem> ePrescriptionItems, Patient patient, Pharmacy pharmacy, LocalDateTime issueDate, Boolean deleted) {
        this.id = id;
        this.ePrescriptionItems = ePrescriptionItems;
        this.patient = patient;
        //this.pharmacy = pharmacy;
        this.issueDate = issueDate;
        this.deleted = deleted;
    }

    public EPrescription(Long id, Set<EPrescriptionItem> ePrescriptionItems, Patient patient, EPrescriptionStatus status, LocalDateTime issueDate, Boolean deleted) {
        this.id = id;
        this.ePrescriptionItems = ePrescriptionItems;
        this.patient = patient;
        this.status = status;
        this.issueDate = issueDate;
        this.deleted = deleted;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<EPrescriptionItem> getePrescriptionItems() {
        return ePrescriptionItems;
    }

    public void setePrescriptionItems(Set<EPrescriptionItem> ePrescriptionItems) {
        this.ePrescriptionItems = ePrescriptionItems;
    }

    public LocalDateTime getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDateTime issueDate) {
        this.issueDate = issueDate;
    }

//    public Pharmacy getPharmacy() {
//        return pharmacy;
//    }
//
//    public void setPharmacy(Pharmacy pharmacy) {
//        this.pharmacy = pharmacy;
//    }


    public EPrescriptionStatus getStatus() {
        return status;
    }

    public void setStatus(EPrescriptionStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EPrescription ePrescription = (EPrescription) o;

        return Objects.equals(id, ePrescription.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
