package com.isamrs.onlinehealth.model;

import org.hibernate.annotations.Fetch;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Entity
@Table(name = "purchase_orders")
public class PurchaseOrder implements Serializable {
    @Column(
            name = "purchase_order_id",
            unique = true,
            nullable = false
    )
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ElementCollection
    @CollectionTable(
            name = "purchase_order_medicine_quantity",
            joinColumns = {@JoinColumn(name = "purchase_order_id", referencedColumnName = "purchase_order_id")}
    )
    @MapKeyColumn(name = "medicine_id")
    @Column(name = "quantity")
    @Min(value = 1)
    private Map<Medicine, Integer> medicineQuantity = new HashMap<>();
    @Column(
            name = "due_date",
            nullable = false
    )
    private LocalDateTime dueDate;
    @Column(
            name = "deleted"
    )
    private Boolean deleted = false;

    @Column(
            name = "status",
            nullable = false
    )
    private PurchaseOrderStatus status;

    public PurchaseOrderStatus getStatus() {
        return status;
    }

    public void setStatus(PurchaseOrderStatus status) {
        this.status = status;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public PurchaseOrder() {
        this.medicineQuantity = new HashMap<>();
    }

    public PurchaseOrder(Long id, HashMap<Medicine, Integer> medicineQuantity, LocalDateTime dueDate) {
        this.id = id;
        this.medicineQuantity = medicineQuantity;
        this.dueDate = dueDate;
    }

    public Map<Medicine, Integer> getMedicineQuantity() {
        return medicineQuantity;
    }

    public void setMedicineQuantity(Map<Medicine, Integer> medicineQuantity) {
        this.medicineQuantity = medicineQuantity;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PurchaseOrder that = (PurchaseOrder) o;

        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }


}
