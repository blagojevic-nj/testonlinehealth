package com.isamrs.onlinehealth.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "pharmacy_admins")
public class PharmacyAdmin extends User implements Serializable {
    @OneToOne
    @JsonIgnore
    private Pharmacy pharmacy;
    @OneToMany(fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<PurchaseOrder> purchaseOrders = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<MedicineInquiry> medicineInquiries = new HashSet<>();


    @Column(
            name = "deleted"
    )
    private Boolean deleted = false;

    public PharmacyAdmin(Long id, String username, String email, String password, String firstName, String lastName, String address, String city, String country, String phoneNumber, USER_TYPE user, boolean passwordChanged) {
        super(id, username, email, password, firstName, lastName, address, city, country, phoneNumber, user, passwordChanged);
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }


    public PharmacyAdmin() {
    }

    public Set<PurchaseOrder> getPurchaseOrders() {
        return purchaseOrders;
    }

    public void setPurchaseOrders(Set<PurchaseOrder> purchaseOrders) {
        this.purchaseOrders = purchaseOrders;
    }

    public Pharmacy getPharmacy() {
        return pharmacy;
    }

    public void setPharmacy(Pharmacy pharmacy) {
        this.pharmacy = pharmacy;
    }

    public Set<MedicineInquiry> getMedicineInquiries() {
        return medicineInquiries;
    }

    public void setMedicineInquiries(Set<MedicineInquiry> medicineInquiries) {
        this.medicineInquiries = medicineInquiries;
    }
}
