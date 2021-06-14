package com.isamrs.onlinehealth.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "suppliers")
public class Supplier extends User implements Serializable {
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "supplier_medicine_quantity",
            joinColumns = {@JoinColumn(name = "supplier_id", referencedColumnName = "user_id")}
    )
    @MapKeyColumn(name = "medicine_id")
    @Column(name = "available_quantity")
    @JsonIgnore
    private Map<Medicine, Integer> medicineQuantity = new HashMap<>();
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)  @JsonIgnore
    private Set<Supply> supplies = new HashSet<>();
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


    public Supplier(Long id, String username, String email, String password, String firstName, String lastName, String address, String city, String state, String phoneNumber, USER_TYPE user, boolean passwordChanged, @Min(value = 1) Map<Medicine, Integer> medicineQuantity, Set<Supply> supplies) {
        super(id, username, email, password, firstName, lastName, address, city, state, phoneNumber, user, passwordChanged);
        this.medicineQuantity = medicineQuantity;
        this.supplies = supplies;
    }

    public Supplier() {

    }

    public Map<Medicine, Integer> getMedicineQuantity() {
        return medicineQuantity;
    }

    public void setMedicineQuantity(Map<Medicine, Integer> medicineQuantity) {
        this.medicineQuantity = medicineQuantity;
    }

    public Set<Supply> getSupplies() {
        return supplies;
    }

    public void setSupplies(Set<Supply> supplies) {
        this.supplies = supplies;
    }
}
