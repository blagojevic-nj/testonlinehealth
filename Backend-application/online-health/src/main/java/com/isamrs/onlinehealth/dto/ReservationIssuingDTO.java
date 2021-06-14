package com.isamrs.onlinehealth.dto;

import com.isamrs.onlinehealth.model.ReservationList;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

public class ReservationIssuingDTO {
    private Long id;
    private String pharmacy;
    private String patient;
    private Double price;
    private LocalDateTime deadline;
    private String details;


    public ReservationIssuingDTO(ReservationList reservationList) {

        this.id = reservationList.getId();
        this.pharmacy = reservationList.getPharmacy().getName();
        this.patient = reservationList.getPatient().getFirstName() + " " + reservationList.getPatient().getLastName();
        this.price = reservationList.getTotalPrice();
        this.deadline = reservationList.getDeadline();
        this.details = reservationList.getReservationItems().stream().map(reservationItem -> reservationItem.getMedicine().getName() + " " + reservationItem.getQuantity()).collect(Collectors.joining(" | "));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPharmacy() {
        return pharmacy;
    }

    public void setPharmacy(String pharmacy) {
        this.pharmacy = pharmacy;
    }

    public String getPatient() {
        return patient;
    }

    public void setPatient(String patient) {
        this.patient = patient;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
