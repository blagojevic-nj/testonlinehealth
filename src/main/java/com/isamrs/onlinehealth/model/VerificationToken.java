package com.isamrs.onlinehealth.model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

@Entity
@Table(name = "verification_token")
public class VerificationToken {
    private static final int EXPIRATION = 60 * 24;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="token")
    private String token;

    @OneToOne(targetEntity = Patient.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name="expiry_date")
    private Date expiry_date;

    public VerificationToken() {
    }

    public VerificationToken(Long id, String token, Patient patient, Date createdDate, Date expiry_date){
        this.id = id;
        this.token = token;
        this.patient = patient;
        this.createdDate = createdDate;
        this.expiry_date = expiry_date;
    }

    public VerificationToken(String token, Patient patient){
        this.token = token;
        this.patient = patient;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Date getExpiry_date() {
        return expiry_date;
    }

    public void setExpiry_date(Date expiry_date) {
        this.expiry_date = expiry_date;
    }

    private Date calculateExpiryDate(int expiryTimeInMinutes){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Timestamp(calendar.getTime().getTime()));
        calendar.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Date(calendar.getTime().getTime());
    }
}
