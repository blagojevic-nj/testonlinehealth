package com.isamrs.onlinehealth.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "system_admins")
public class SystemAdmin extends User implements Serializable {
    @OneToMany(fetch = FetchType.LAZY)
    @Column(
            name = "complaint_id"
    )  @JsonIgnore
    private Set<Complaint> complaints = new HashSet<>();
    @OneToOne(fetch = FetchType.LAZY)  @JsonIgnore
    private LoyaltyProgram loyaltyProgram;
    @Column(
            name = "deleted"
    )
    private Boolean deleted = false;

    public SystemAdmin() {

    }

    public Boolean getDeleted() {
        return deleted;
    }

    public SystemAdmin(Long id, String username, String email, String password, String firstName, String lastName, String address, String city, String state, String phoneNumber, USER_TYPE user, boolean passwordChanged, Set<Complaint> complaints, LoyaltyProgram loyaltyProgram) {
        super(id, username, email, password, firstName, lastName, address, city, state, phoneNumber, user, passwordChanged);
        this.complaints = complaints;
        this.loyaltyProgram = loyaltyProgram;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Set<Complaint> getComplaints() {
        return complaints;
    }

    public void setComplaints(Set<Complaint> complaints) {
        this.complaints = complaints;
    }

    public LoyaltyProgram getLoyaltyProgram() {
        return loyaltyProgram;
    }

    public void setLoyaltyProgram(LoyaltyProgram loyaltyProgram) {
        this.loyaltyProgram = loyaltyProgram;
    }
}
