package com.isamrs.onlinehealth.model;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "reservation_lists")
public class ReservationList implements Serializable {
    @Column(
            name = "reservation_list_id",
            unique = true,
            nullable = false
    )
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.EAGER)
    private Pharmacy pharmacy;

    @OneToMany(fetch = FetchType.EAGER)
    private Set<ReservationItem> reservationItems = new HashSet<ReservationItem>();

    @Column(name = "deadline")
    private LocalDateTime deadline;

    @Column(name = "total_price")
    private double totalPrice;

    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    private ReservationStatus status;

    public ReservationList(Long id, Pharmacy pharmacy, Patient patient, Set<ReservationItem> items, LocalDateTime deadline, Double totalPrice, ReservationStatus status) {
        this.id = id;
        this.pharmacy = pharmacy;
        this.patient = patient;
        this.reservationItems = items;
        this.deadline = deadline;
        this.totalPrice = totalPrice;
        this.status = status;
    }

    public ReservationList() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<ReservationItem> getReservationItems() {
        return reservationItems;
    }

    public void setReservationItems(Set<ReservationItem> reservationItems) {
        this.reservationItems = reservationItems;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
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

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Pharmacy getPharmacy() {
        return pharmacy;
    }

    public void setPharmacy(Pharmacy pharmacy) {
        this.pharmacy = pharmacy;
    }
}
