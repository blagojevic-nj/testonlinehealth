package com.isamrs.onlinehealth.model;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "leave_requests")
public class LeaveRequest implements Serializable {
    @Column(
            name = "leave_request_id",
            unique = true,
            nullable = false
    )
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(
            name = "start_time",
            nullable = false
    )
    private LocalDateTime start;
    @Column(
            name = "end_time",
            nullable = false
    )
    private LocalDateTime end;
    @Column(
            name = "deleted"
    )
    private Boolean deleted = false;

    @Column
            (
                    name = "status"
            )
    private LEAVE_REQUEST_STATUS status;

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean accepted) {
        this.deleted = deleted;
    };

    public LeaveRequest(Long id, LocalDateTime start, LocalDateTime end, LEAVE_REQUEST_STATUS status) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.status = status;
    }

    public LeaveRequest() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LeaveRequest that = (LeaveRequest) o;

        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
