package com.isamrs.onlinehealth.dto;

public class EqrPharmacyPriceListDTO {
    String name;
    String identifier;
    String amount;
    String cost;

    public EqrPharmacyPriceListDTO() {
    }

    public EqrPharmacyPriceListDTO(String name, String identifier, String amount, String cost) {
        this.name = name;
        this.identifier = identifier;
        this.amount = amount;
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }
}
