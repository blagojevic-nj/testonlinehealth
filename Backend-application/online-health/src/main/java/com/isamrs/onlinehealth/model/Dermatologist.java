package com.isamrs.onlinehealth.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "dermatologists")
public class Dermatologist extends User implements Serializable {
        @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
        @JoinTable(
                name = "dermatologist_pharmacy_work_hours",
                joinColumns = {@JoinColumn(name = "dermatologist_id", referencedColumnName = "user_id")},
                inverseJoinColumns = {@JoinColumn(name = "work_hour_id", referencedColumnName = "work_hours_id")}
        )  @JsonIgnore
        @MapKeyJoinColumn(name = "pharmacy_id")
        private Map<Pharmacy, WorkHours> workHoursPharmacies;

        @OneToMany(mappedBy = "dermatologist", fetch = FetchType.EAGER, cascade = CascadeType.ALL)  @JsonIgnore
        private Set<Examination> examinations;

        @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)  @JsonIgnore
        private Set<RatingItem> ratingItems;

        @Column(name = "rating")
        private Double rating = 0.0;

        @JsonIgnore
        @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
        private Set<LeaveRequest> leaveRequests;
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


        public Dermatologist() {
                this.examinations = new HashSet<>();
                this.ratingItems = new HashSet<>();
                this.leaveRequests = new HashSet<>();
                this.workHoursPharmacies = new HashMap<>();
        }

        public Set<LeaveRequest> getLeaveRequests() {
                return leaveRequests;
        }

        public void setLeaveRequests(Set<LeaveRequest> leaveRequests) {
                this.leaveRequests = leaveRequests;
        }


        public Map<Pharmacy, WorkHours> getWorkHoursPharmacies() {
                return workHoursPharmacies;
        }

        public void setWorkHoursPharmacies(HashMap<Pharmacy, WorkHours> workHoursPharmacies) {
                this.workHoursPharmacies = workHoursPharmacies;
        }

        public Set<RatingItem> getRatingItems() {
                return ratingItems;
        }

        public void setRatingItems(Set<RatingItem> ratingItems) {
                this.ratingItems = ratingItems;
        }

        public Set<Examination> getExaminations() {
                return examinations;
        }

        public void setExaminations(Set<Examination> examinations) {
                this.examinations = examinations;
        }

        public Double getRating() {
                return rating;
        }

        public void setRating(Double rating) {
                this.rating = rating;
        }
}