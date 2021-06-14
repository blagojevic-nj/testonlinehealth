package com.isamrs.onlinehealth.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "reservation_list_items")
public class ReservationItem implements Serializable {
    @Column(name = "reservation_list_item_id",unique = true, nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    private Medicine medicine;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "price")
    private double price;

    public ReservationItem(Long id, Medicine medicine, int quantity, double price) {
        this.id = id;
        this.medicine = medicine;
        this.quantity = quantity;
        this.price = price;
    }

    public ReservationItem() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Medicine getMedicine() {
        return medicine;
    }

    public void setMedicine(Medicine medicine) {
        this.medicine = medicine;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
