package com.isamrs.onlinehealth.dto;

import com.isamrs.onlinehealth.model.MedicineInquiry;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class FailedQueryDTO {
    private String medicineName;
    private String medicineIdentifier;
    private Long medicineId;
    private int kolicina;
    private LocalDate datum;
    private String username;

    public FailedQueryDTO(MedicineInquiry m) {
        this.datum = m.getTime().toLocalDate();
        this.kolicina=m.getQuantity().intValue();
        this.username=m.getUser().getUsername();
        this.medicineName=m.getMedicine().getName();
        this.medicineIdentifier=m.getMedicine().getIdentifier();
        this.medicineId=m.getMedicine().getId();
    }

    public FailedQueryDTO() {
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public String getMedicineIdentifier() {
        return medicineIdentifier;
    }

    public void setMedicineIdentifier(String medicineIdentifier) {
        this.medicineIdentifier = medicineIdentifier;
    }

    public Long getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(Long medicineId) {
        this.medicineId = medicineId;
    }

    public int getKolicina() {
        return kolicina;
    }

    public void setKolicina(int kolicina) {
        this.kolicina = kolicina;
    }

    public LocalDate getDatum() {
        return datum;
    }

    public void setDatum(LocalDate datum) {
        this.datum = datum;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
