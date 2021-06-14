package com.isamrs.onlinehealth.dto;

import com.isamrs.onlinehealth.model.ReservationStatus;

import java.time.LocalDateTime;
import java.util.Set;

public class ReservationListDTO {
    private Long id = 0l;
    private String patient;
    private Long pharmacy;
    private ReservationStatus status;
    private Set<ReservationItemDTO> items;
    private LocalDateTime deadline;
    private double totalPrice;

    public ReservationListDTO() {
    }

    public ReservationListDTO(String patient,
                              Long pharmacy,
                              Set<ReservationItemDTO> items,
                              LocalDateTime deadline,
                              double totalPrice) {
        this.patient = patient;
        this.pharmacy = pharmacy;
        this.items = items;
        this.deadline = deadline;
        this.totalPrice = totalPrice;
    }

    public ReservationListDTO(Long id,String patient,
                              Long pharmacy,
                              ReservationStatus status,
                              Set<ReservationItemDTO> items,
                              LocalDateTime deadline,
                              double totalPrice) {
        this.id = id;
        this.patient = patient;
        this.pharmacy = pharmacy;
        this.status = status;
        this.items = items;
        this.deadline = deadline;
        this.totalPrice = totalPrice;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPatient() {
        return patient;
    }

    public void setPatient(String patient) {
        this.patient = patient;
    }

    public Long getPharmacy() {
        return pharmacy;
    }

    public void setPharmacy(Long pharmacy) {
        this.pharmacy = pharmacy;
    }

    public Set<ReservationItemDTO> getItems() {
        return items;
    }

    public void setItems(Set<ReservationItemDTO> items) {
        this.items = items;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
