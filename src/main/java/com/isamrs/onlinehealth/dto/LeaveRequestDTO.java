package com.isamrs.onlinehealth.dto;

import com.isamrs.onlinehealth.model.LEAVE_REQUEST_STATUS;
import com.isamrs.onlinehealth.model.LeaveRequest;
import com.isamrs.onlinehealth.model.Pharmacist;

import javax.persistence.Column;
import java.time.LocalDateTime;

public class LeaveRequestDTO {

    private Long id;
    private String pharmacistUsername;
    private LocalDateTime start;
    private LocalDateTime end;
    private LEAVE_REQUEST_STATUS status;


    public LeaveRequestDTO() {
    }

    public LeaveRequestDTO(Long id, LocalDateTime start, LocalDateTime end, LEAVE_REQUEST_STATUS status, String pharmacistUsername) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.status = status;
        this.pharmacistUsername=pharmacistUsername;
    }

    public LeaveRequestDTO(LeaveRequest l, Pharmacist p) {
        this.id = l.getId();
        this.pharmacistUsername = p.getUsername();
        this.start = l.getStart();
        this.end = l.getEnd();
        this.status = l.getStatus();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public LEAVE_REQUEST_STATUS getStatus() {
        return status;
    }

    public void setStatus(LEAVE_REQUEST_STATUS status) {
        this.status = status;
    }

    public String getPharmacistUsername() {
        return pharmacistUsername;
    }

    public void setPharmacistUsername(String pharmacistUsername) {
        this.pharmacistUsername = pharmacistUsername;
    }
}
