package com.isamrs.onlinehealth.dto;

import java.util.List;

public class AllComplaintsDTO {
    List<ComplaintDTO> complaints;

    public AllComplaintsDTO() {
    }

    public AllComplaintsDTO(List<ComplaintDTO> complaints) {
        this.complaints = complaints;
    }

    public List<ComplaintDTO> getComplaints() {
        return complaints;
    }

    public void setComplaints(List<ComplaintDTO> complaints) {
        this.complaints = complaints;
    }
}
