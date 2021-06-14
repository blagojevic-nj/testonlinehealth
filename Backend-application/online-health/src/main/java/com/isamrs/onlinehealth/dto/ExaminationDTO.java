package com.isamrs.onlinehealth.dto;



import com.isamrs.onlinehealth.model.Consultation;
import com.isamrs.onlinehealth.model.Examination;

import java.time.LocalDateTime;

public class ExaminationDTO {
    Long id;
    LocalDateTime start;
    LocalDateTime end;
    String report;
    Double price;
    String patient;

    Long patientId;

    public ExaminationDTO(Long id, LocalDateTime start, LocalDateTime end, String report, Double price, String patient, Long pharmacy_id, String pharmacy, String dermatologist, Boolean ongoing, Long patientId, Boolean finished) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.report = report;
        this.price = price;
        this.patient = patient;
        this.pharmacy_id = pharmacy_id;
        this.pharmacy = pharmacy;
        this.dermatologist = dermatologist;
        this.ongoing = ongoing;
        this.patientId = patientId;
        this.finished = finished;
    }

    Long pharmacy_id;
    String pharmacy;
    String dermatologist;
    Boolean ongoing;
    Boolean finished;

    public ExaminationDTO(Examination e) {
        this.id = e.getId();
        this.start = e.getStart();
        this.end = e.getEnd();
        this.report = e.getReport();
        this.price = e.getPrice();
        this.patient = e.getPatient() != null ? e.getPatient().getFirstName() + " " + e.getPatient().getLastName() : " ";
        this.pharmacy_id = e.getPharmacy().getId();
        this.pharmacy = e.getPharmacy().getName();
        this.dermatologist = e.getDermatologist() != null ? e.getDermatologist().getUsername() : " ";
        this.ongoing = e.getOngoing();
        this.patientId = e.getPatient() != null ? e.getPatient().getId() : 0;
        this.finished = e.getFinished();
    }

    public ExaminationDTO(Consultation e) {
        this.id = e.getId();
        this.start = e.getStart();
        this.end = e.getEnd();
        this.report = e.getReport();
        this.price = e.getPrice();
        this.patient = e.getPatient() != null ? e.getPatient().getFirstName() + " " + e.getPatient().getLastName() : " ";
        this.pharmacy_id = e.getPharmacy().getId();
        this.pharmacy = e.getPharmacy().getName();
        this.dermatologist = e.getPharmacist() != null ? e.getPharmacist().getUsername() : " ";
        this.ongoing = e.getOngoing();
        this.patientId = e.getPatient() != null ? e.getPatient().getId() : 0;
        this.finished = e.getFinished();
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public ExaminationDTO(Long id, LocalDateTime start, LocalDateTime end, String report, Double price, String patient, Long pharmacy_id, String pharmacy, String dermatologist, Boolean ongoing) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.report = report;
        this.price = price;
        this.patient = patient;
        this.pharmacy_id = pharmacy_id;
        this.pharmacy = pharmacy;
        this.dermatologist = dermatologist;
        this.ongoing = ongoing;
    }

    public ExaminationDTO(Long id, LocalDateTime start, LocalDateTime end, String report, Double price, String patient, Long patientId, Long pharmacy_id, String pharmacy, String dermatologist, Boolean ongoing, Boolean finished) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.report = report;
        this.price = price;
        this.patient = patient;
        this.patientId = patientId;
        this.pharmacy_id = pharmacy_id;
        this.pharmacy = pharmacy;
        this.dermatologist = dermatologist;
        this.ongoing = ongoing;
        this.finished = finished;
    }

    public Boolean getOngoing() {
        return ongoing;
    }

    public void setOngoing(Boolean ongoing) {
        this.ongoing = ongoing;
    }

    public ExaminationDTO() {
    }

    public ExaminationDTO(Long id, LocalDateTime start, LocalDateTime end, String report, Double price, String patient, Long pharmacy_id, String pharmacy) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.report = report;
        this.price = price;
        this.patient = patient==null ? "Prazan termin" : patient;
        this.pharmacy_id = pharmacy_id;
        this.pharmacy = pharmacy;
    }

    public ExaminationDTO(Long id, LocalDateTime start, LocalDateTime end, String report, Double price,
                          String patient, Long pharmacy_id, String pharmacy, String dermatologist) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.report = report;
        this.price = price;
        this.patient = patient==null ? "Prazan termin" : patient;
        this.pharmacy_id = pharmacy_id;
        this.pharmacy = pharmacy;
        this.dermatologist = dermatologist;
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

    public Boolean getFinished() {
        return finished;
    }

    public void setFinished(Boolean finished) {
        this.finished = finished;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getPatient() {
        return patient;
    }

    public void setPatient(String patient) {
        this.patient = patient;
    }

    public Long getPharmacy_id() {
        return pharmacy_id;
    }

    public void setPharmacy_id(Long pharmacy_id) {
        this.pharmacy_id = pharmacy_id;
    }

    public String getPharmacy() {
        return pharmacy;
    }

    public void setPharmacy(String pharmacy) {
        this.pharmacy = pharmacy;
    }

    public String getDermatologist() {
        return dermatologist;
    }

    public void setDermatologist(String dermatologist) {
        this.dermatologist = dermatologist;
    }
}
