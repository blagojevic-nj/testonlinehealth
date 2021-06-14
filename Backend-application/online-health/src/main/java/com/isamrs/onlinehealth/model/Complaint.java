package com.isamrs.onlinehealth.model;


import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "complaints")
public class Complaint implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(
            name = "complaint_id",
            unique = true,
            nullable = false
    )
    private Long id;
    @Column(
            name = "object_complaint",
            nullable = false
    )
    private String to;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false)
    private Patient patient;
    @Column(
            name = "complaint_text",
            nullable = false
    )
    private String complaintText;
    @Column(
            name = "response_text"
    )
    private String responseText;
    @Column(
            name = "deleted"
    )
    private Boolean deleted = false;

    public LocalDateTime getDateComposed() {
        return dateComposed;
    }

    public void setDateComposed(LocalDateTime dateComposed) {
        this.dateComposed = dateComposed;
    }

    @Column(
            name = "date_composed"
    )
    private LocalDateTime dateComposed;

    @Column(
            name = "date_responded"
    )
    private LocalDateTime dateResponded;

    @Column(
            name = "admin_username"
    )
    private String adminUsername;

    @Version
    @Column(
            nullable = false
    )
    private Long version;

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Complaint() {
    }

    public Complaint(Long id, String to, Patient patient, String complaintText, String responseText, LocalDateTime dateComposed, LocalDateTime dateResponded) {
        this.id = id;
        this.to = to;
        this.patient = patient;
        this.complaintText = complaintText;
        this.responseText = responseText;
        this.dateComposed = dateComposed;
        this.dateResponded = dateResponded;
        this.adminUsername = "";
    }

    public String getTo() {
        return to;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public String getComplaintText() {
        return complaintText;
    }

    public void setComplaintText(String complaintText) {
        this.complaintText = complaintText;
    }

    public String getResponseText() {
        return responseText;
    }

    public void setResponseText(String responseText) {
        this.responseText = responseText;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Complaint complaint = (Complaint) o;

        return Objects.equals(id, complaint.id);
    }


    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    public LocalDateTime getDateResponded() {
        return dateResponded;
    }

    public void setDateResponded(LocalDateTime dateResponded) {
        this.dateResponded = dateResponded;
    }

    public String getAdminUsername() {
        return adminUsername;
    }

    public void setAdminUsername(String adminUsername) {
        this.adminUsername = adminUsername;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
