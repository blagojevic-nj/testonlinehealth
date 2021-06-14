package com.isamrs.onlinehealth.dto;

import java.util.List;

public class LoyaltyProgramDTO {
    String id; //Long
    List<LoyaltyMedicinePointsDTO> medicinePoints;
    String examinationPoints; //Double
    String consultationPoints; //Double
    List<LoyaltyCategoryDTO> loyaltyCategories;

    public LoyaltyProgramDTO() {
    }


    public LoyaltyProgramDTO(String id, List<LoyaltyMedicinePointsDTO> medicinePoints, String examinationPoints, String consultationPoints, List<LoyaltyCategoryDTO> loyaltyCategories) {
        this.id = id;
        this.medicinePoints = medicinePoints;
        this.examinationPoints = examinationPoints;
        this.consultationPoints = consultationPoints;
        this.loyaltyCategories = loyaltyCategories;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<LoyaltyMedicinePointsDTO> getMedicinePoints() {
        return medicinePoints;
    }

    public void setMedicinePoints(List<LoyaltyMedicinePointsDTO> medicinePoints) {
        this.medicinePoints = medicinePoints;
    }

    public String getExaminationPoints() {
        return examinationPoints;
    }

    public void setExaminationPoints(String examinationPoints) {
        this.examinationPoints = examinationPoints;
    }

    public String getConsultationPoints() {
        return consultationPoints;
    }

    public void setConsultationPoints(String consultationPoints) {
        this.consultationPoints = consultationPoints;
    }

    public List<LoyaltyCategoryDTO> getLoyaltyCategories() {
        return loyaltyCategories;
    }

    public void setLoyaltyCategories(List<LoyaltyCategoryDTO> loyaltyCategories) {
        this.loyaltyCategories = loyaltyCategories;
    }
}
