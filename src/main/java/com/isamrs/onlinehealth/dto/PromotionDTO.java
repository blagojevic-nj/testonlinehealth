package com.isamrs.onlinehealth.dto;

import com.isamrs.onlinehealth.model.Promotion;

import java.time.LocalDate;

public class PromotionDTO {
    private int snizenje;
    private LocalDate start;
    private LocalDate end;


    public PromotionDTO(int snizenje, LocalDate start, LocalDate end) {
        this.snizenje = snizenje;
        this.start = start;
        this.end = end;
    }

    public PromotionDTO(Promotion p) {
        this.end = p.getEnd().toLocalDate();
        this.start = p.getStart().toLocalDate();
        this.snizenje = (int)(p.getDiscountRate()*100);
    }

    public PromotionDTO() {
    }

    public int getSnizenje() {
        return snizenje;
    }

    public void setSnizenje(int snizenje) {
        this.snizenje = snizenje;
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

