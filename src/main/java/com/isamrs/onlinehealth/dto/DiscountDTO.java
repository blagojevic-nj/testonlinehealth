package com.isamrs.onlinehealth.dto;

import com.isamrs.onlinehealth.model.Discount;
import com.isamrs.onlinehealth.model.DiscountItem;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class DiscountDTO {

        private List<DiscountItemDTO> artikli;
        private LocalDate start;
        private LocalDate end;

    public DiscountDTO(List<DiscountItemDTO> artikli, LocalDate start, LocalDate end) {
        this.artikli = artikli;
        this.start = start;
        this.end = end;
    }

    public DiscountDTO(Discount d) {
        this.end=d.getEnd();
        this.start=d.getStart();
        this.artikli=makeArtikli(d.getDiscountItems());

    }



    public DiscountDTO() {
    }

    public List<DiscountItemDTO> getArtikli() {
        return artikli;
    }

    public void setArtikli(List<DiscountItemDTO> artikli) {
        this.artikli = artikli;
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


    private List<DiscountItemDTO> makeArtikli(Set<DiscountItem> discountItems) {
        List<DiscountItemDTO> res = new ArrayList<>();
        for (DiscountItem di : discountItems)
            res.add(new DiscountItemDTO(di));
        return res;


    }



}
