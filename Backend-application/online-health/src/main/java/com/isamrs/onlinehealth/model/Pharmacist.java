package com.isamrs.onlinehealth.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "pharmacists")
public class Pharmacist extends User implements Serializable {
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)  @JsonIgnore
    private Set<RatingItem> ratings = new HashSet<>();
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<LeaveRequest> leaveRequests = new HashSet<>();

    @OneToOne
    private WorkHours workHours;

    @OneToMany(mappedBy = "pharmacist", fetch = FetchType.EAGER, cascade = CascadeType.ALL)  @JsonIgnore
    private Set<Consultation> consultations;

    @Column(name="rating")
    private double rating = 0;

    public Pharmacist(Long id, String username, String email, String password, String firstName,
                      String lastName, String address, String city, String state, String phoneNumber,
                      USER_TYPE user, boolean passwordChanged, Set<RatingItem> ratings,
                      Set<LeaveRequest> leaveRequests) {
        super(id, username, email, password, firstName, lastName, address, city,
                state, phoneNumber, user, passwordChanged);
        this.ratings = ratings;
        this.leaveRequests = leaveRequests;
        this.consultations = new HashSet<>();
    }

    public Pharmacist(Long id, String username,
                      String email, String password, String firstName, String lastName,
                      String address, String city, String country, String phoneNumber,
                      USER_TYPE user, boolean passwordChanged, Set<RatingItem> ratings,
                      Set<LeaveRequest> leaveRequests, Set<Consultation> consultations,
                      double rating, Boolean deleted, WorkHours workHours) {
        super(id, username, email, password, firstName, lastName, address,
                city, country, phoneNumber, user, passwordChanged);
        this.ratings = ratings;
        this.leaveRequests = leaveRequests;
        this.consultations = consultations;
        this.rating = rating;
        this.deleted = deleted;
        this.workHours = workHours;
    }

    public Set<Consultation> getConsultations() {
        return consultations;
    }

    @Column(
            name = "deleted"
    )
    private Boolean deleted = false;

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public void setConsultations(Set<Consultation> consultations) {
        this.consultations = consultations;
    }

    public Pharmacist() {

    }

    public Set<LeaveRequest> getLeaveRequests() {
        return leaveRequests;
    }

    public void setLeaveRequests(Set<LeaveRequest> leaveRequests) {
        this.leaveRequests = leaveRequests;
    }

    public Set<RatingItem> getRatings() {
        return ratings;
    }

    public void setRatings(Set<RatingItem> ratings) {
        this.ratings = ratings;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public WorkHours getWorkHours() {
        return workHours;
    }

    public void setWorkHours(WorkHours workHours) {
        this.workHours = workHours;
    }
}
