package com.isamrs.onlinehealth.dto;

import java.time.LocalDateTime;

public class NewLeaveRequestDTO {
    private String username;
    private LocalDateTime start;
    private LocalDateTime end;

    public NewLeaveRequestDTO() {
    }

    public NewLeaveRequestDTO(String username, LocalDateTime start, LocalDateTime end) {
        this.username = username;
        this.start = start;
        this.end = end;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
}
