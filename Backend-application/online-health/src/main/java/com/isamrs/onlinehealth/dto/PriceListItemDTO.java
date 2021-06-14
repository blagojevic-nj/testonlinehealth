package com.isamrs.onlinehealth.dto;

import com.isamrs.onlinehealth.model.PriceListItem;

import java.time.LocalDate;

public class PriceListItemDTO {
    private long idLeka;
    private String identifier;
    private String name;
    private Double cena;
    private LocalDate start;
    private LocalDate end;


    public PriceListItemDTO() {
    }

    public PriceListItemDTO(PriceListItem pli) {
        this.idLeka = pli.getLek().getId();
        this.identifier=pli.getLek().getIdentifier();
        this.name = pli.getLek().getName();
        this.cena = pli.getPrice();
        this.start = pli.getStart().toLocalDate();
        this.end = pli.getEnd().toLocalDate();
    }

    public PriceListItemDTO(long idLeka, String identifier, String name, Double cena, LocalDate start, LocalDate end) {
        this.idLeka = idLeka;
        this.identifier = identifier;
        this.name = name;
        this.cena = cena;
        this.start = start;
        this.end = end;
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

    public long getIdLeka() {
        return idLeka;
    }

    public void setIdLeka(long idLeka) {
        this.idLeka = idLeka;
    }

    public Double getCena() {
        return cena;
    }

    public void setCena(Double cena) {
        this.cena = cena;
    }

    public LocalDate getStart() {
        return start;
    }

    public void setStart(LocalDate start) {
        this.start = start;
    }

    public LocalDate getEnd() {
        return end;
    }

    public void setEnd(LocalDate end) {
        this.end = end;
    }
}
