package com.isamrs.onlinehealth.dto;

public class ReportDTO {
    String text;
    Long idExam;

    public ReportDTO(String text, Long idExam) {
        this.text = text;
        this.idExam = idExam;
    }

    public ReportDTO() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getIdExam() {
        return idExam;
    }

    public void setIdExam(Long idExam) {
        this.idExam = idExam;
    }
}
