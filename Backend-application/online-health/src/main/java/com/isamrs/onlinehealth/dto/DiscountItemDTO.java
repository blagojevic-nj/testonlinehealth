package com.isamrs.onlinehealth.dto;

import com.isamrs.onlinehealth.model.DiscountItem;

public class DiscountItemDTO {

    private long idLeka;
    private double cena;


    public DiscountItemDTO(long idLeka, double cena) {
        this.idLeka = idLeka;
        this.cena = cena;
    }

    public DiscountItemDTO(DiscountItem di) {
        this.idLeka=di.getLek().getId();
        this.cena=di.getPrice();
    }

    public DiscountItemDTO() {
    }

    public long getIdLeka() {
        return idLeka;
    }

    public void setIdLeka(long idLeka) {
        this.idLeka = idLeka;
    }

    public double getCena() {
        return cena;
    }

    public void setCena(double cena) {
        this.cena = cena;
    }
}
