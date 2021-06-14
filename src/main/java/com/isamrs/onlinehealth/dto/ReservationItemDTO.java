package com.isamrs.onlinehealth.dto;

public class ReservationItemDTO {
    private Long medicineId;
    private int quantity;
    private double price;

    public ReservationItemDTO() {
    }

    public ReservationItemDTO(Long medicineId, int quantity, double price) {
        this.medicineId = medicineId;
        this.quantity = quantity;
        this.price = price;
    }

    public Long getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(Long medicineId) {
        this.medicineId = medicineId;
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
