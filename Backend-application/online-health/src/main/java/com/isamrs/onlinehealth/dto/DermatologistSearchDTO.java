package com.isamrs.onlinehealth.dto;

import com.isamrs.onlinehealth.model.Dermatologist;
import com.isamrs.onlinehealth.model.Pharmacy;
import com.isamrs.onlinehealth.model.RatingItem;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class DermatologistSearchDTO {

    private long id;
    private String username;
    private String firstName;
    private String lastName;
    private double rating;
    private List<String> pharmacies;

    public DermatologistSearchDTO(Dermatologist dermatologist) {
        this.id=dermatologist.getId();
        this.username=dermatologist.getUsername();
        this.firstName=dermatologist.getFirstName();
        this.lastName=dermatologist.getLastName();
        this.initRating(dermatologist);
        this.initPharmacies(dermatologist);

    }

    private void initRating(Dermatologist d)
    {
        DecimalFormat df2 = new DecimalFormat("#.##");
        double ocena =0, counter =0;
        for (RatingItem r : d.getRatingItems())
        {
            ocena+=r.getRating();
            counter++;
        }
        this.rating = Double.parseDouble(df2.format(ocena/counter));
        if(Double.isNaN(this.rating))
            this.rating=0;
    }

    private void initPharmacies(Dermatologist d){
        this.pharmacies = new ArrayList<String>();
        for (Pharmacy p : d.getWorkHoursPharmacies().keySet())
        {
            this.pharmacies.add(p.getName());
        }
    }


    public DermatologistSearchDTO() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public List<String> getPharmacies() {
        return pharmacies;
    }

    public void setPharmacies(List<String> pharmacies) {
        this.pharmacies = pharmacies;
    }
}
