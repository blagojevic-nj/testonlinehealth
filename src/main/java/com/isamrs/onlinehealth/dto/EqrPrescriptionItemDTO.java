package com.isamrs.onlinehealth.dto;

public class EqrPrescriptionItemDTO {
    String identifier;
    String name;
    String amount;

    public EqrPrescriptionItemDTO() {
    }

    public EqrPrescriptionItemDTO(String identifier, String name, String amount) {
        this.identifier = identifier;
        this.name = name;
        this.amount = amount;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
