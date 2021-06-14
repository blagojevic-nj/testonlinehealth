package com.isamrs.onlinehealth.dto;

import java.util.List;

public class EqrPharmacyDTO {
    String id;
    String name;
    String rating;
    List<EqrPharmacyPriceListDTO> prices;
    String totalCost;

    public EqrPharmacyDTO() {
    }

    public EqrPharmacyDTO(String id, String name, String rating, List<EqrPharmacyPriceListDTO> prices, String totalCost) {
        this.id = id;
        this.name = name;
        this.rating = rating;
        this.prices = prices;
        this.totalCost = totalCost;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public List<EqrPharmacyPriceListDTO> getPrices() {
        return prices;
    }

    public void setPrices(List<EqrPharmacyPriceListDTO> prices) {
        this.prices = prices;
    }

    public String getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(String totalCost) {
        this.totalCost = totalCost;
    }
}
