package com.isamrs.onlinehealth.dto;

import com.isamrs.onlinehealth.model.Location;
import com.isamrs.onlinehealth.model.Pharmacy;
import com.isamrs.onlinehealth.model.RatingItem;

import java.util.Iterator;
import java.util.Set;

public class PharmacyDTO {
    private Long id;
    private String name;
    private Location location;
    private String description;
    private double rating;
    private double consultation_price;

    public PharmacyDTO(){

    }

    public PharmacyDTO(Long id, String name, Location location, String description, Set<RatingItem> ratings, double rating){
        this.id = id;
        this.name = name;
        this.location = location;
        this.description = description;
        this.rating = rating;
    }
    public PharmacyDTO(Pharmacy p){
        this.id = p.getId();
        this.name = p.getName();
        this.location = p.getLocation();
        this.description = p.getDescription();
        this.rating = p.getRating();
    }

    private void calculateRating(Set<RatingItem> ratings){
        double sum = 0;
//        for(RatingItem ri:ratings){
//            sum += ri.getRating();
//        }
//        this.rating = sum/ratings.size();
        this.rating = 3.5;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public double getConsultation_price() {
        return consultation_price;
    }

    public void setConsultation_price(double consultation_price) {
        this.consultation_price = consultation_price;
    }
}
