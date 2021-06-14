package com.isamrs.onlinehealth.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "loyalty_programs")
public class LoyaltyProgram implements Serializable {
    @Column(
            name = "loyalty_program_id",
            unique = true,
            nullable = false
    )
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection
    @CollectionTable(
            name = "medicine_points",
            joinColumns = {@JoinColumn(name = "loyalty_id", referencedColumnName = "loyalty_program_id")}
    )
    @MapKeyColumn(name = "medicine_id")
    @Column(name = "points")
    private Map<Medicine, Double> medicinePoints = new HashMap<>();
    @Column(
            name = "consulting_points"
    )
    private Double consultingPoints;
    @Column(
            name = "examination_points"
    )
    private Double examinationPoints;
    @OneToMany
    private Set<LoyaltyCategory> loyaltyCategories;

    public LoyaltyProgram() {
    }

    public Long getId() {
        return id;
    }

    @Column(
            name = "deleted"
    )
    private Boolean deleted = false;

    @Version
    @Column(
            name = "version"
    )
    private Long version;

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }


    public LoyaltyProgram(Long id, Map<Medicine, Double> medicinePoints, Double consultingPoints, Double examinationPoints, Set<LoyaltyCategory> loyaltyCategories) {
        this.id = id;
        this.medicinePoints = medicinePoints;
        this.consultingPoints = consultingPoints;
        this.examinationPoints = examinationPoints;
        this.loyaltyCategories = loyaltyCategories;
    }

    public Map<Medicine, Double> getMedicinePoints() {
        return medicinePoints;
    }

    public void setMedicinePoints(Map<Medicine, Double> medicinePoints) {
        this.medicinePoints = medicinePoints;
    }

    public Double getConsultingPoints() {
        return consultingPoints;
    }

    public void setConsultingPoints(Double consultingPoints) {
        this.consultingPoints = consultingPoints;
    }

    public Double getExaminationPoints() {
        return examinationPoints;
    }

    public void setExaminationPoints(Double examinationPoints) {
        this.examinationPoints = examinationPoints;
    }

    public Set<LoyaltyCategory> getLoyaltyCategories() {
        return loyaltyCategories;
    }

    public void setLoyaltyCategories(Set<LoyaltyCategory> loyaltyCategories) {
        this.loyaltyCategories = loyaltyCategories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LoyaltyProgram that = (LoyaltyProgram) o;

        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
