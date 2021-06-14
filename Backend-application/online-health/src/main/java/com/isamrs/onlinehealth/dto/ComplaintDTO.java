package com.isamrs.onlinehealth.dto;

public class ComplaintDTO {
    private String to;
    private String dateComposed;
    private String complaintText;
    private String responseText;
    private String patientItem;
    private String dateResponded;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private Long id;

    public ComplaintDTO() {
    }

    public ComplaintDTO(String to, String dateComposed, String complaintText, String responseText, String patientItem, Long id, String dateResponded) {
        this.to = to;
        this.dateComposed = dateComposed;
        this.complaintText = complaintText;
        this.responseText = responseText;
        this.patientItem = patientItem;
        this.id = id;
        this.dateResponded = dateResponded;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getDateComposed() {
        return dateComposed;
    }

    public void setDateComposed(String dateComposed) {
        this.dateComposed = dateComposed;
    }

    public String getComplaintText() {
        return complaintText;
    }

    public void setComplaintText(String complaintText) {
        this.complaintText = complaintText;
    }

    public String getResponseText() {
        return responseText;
    }

    public void setResponseText(String responseText) {
        this.responseText = responseText;
    }

    public String getPatientItem() {
        return patientItem;
    }

    public void setPatientItem(String patientItem) {
        this.patientItem = patientItem;
    }

    public String getDateResponded() {
        return dateResponded;
    }

    public void setDateResponded(String dateResponded) {
        this.dateResponded = dateResponded;
    }
}
