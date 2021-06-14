package com.isamrs.onlinehealth.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class MedicineInquiry {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Medicine medicine;
    private LocalDateTime time;
    private Long quantity;
    @ManyToOne
    private User user;


    public MedicineInquiry() {
    }

    public MedicineInquiry(Medicine medicine, LocalDateTime time, Long quantity, User user) {
        this.medicine = medicine;
        this.time = time;
        this.quantity = quantity;
        this.user = user;
    }

    public Medicine getMedicine() {
        return medicine;
    }

    public void setMedicine(Medicine medicine) {
        this.medicine = medicine;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
