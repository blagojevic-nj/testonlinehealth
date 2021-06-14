package com.isamrs.onlinehealth.model;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalTime;
import java.util.Objects;

@Entity
@Table(name = "work_hours")
public class WorkHours implements Serializable {
    @Column(
            name = "work_hours_id",
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
    private LocalTime start;
    @Column(
            name = "end_time",
            nullable = false
    )
    private LocalTime end;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(
            name = "deleted"
    )
    private Boolean deleted = false;

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public WorkHours() {
    }

    public WorkHours(Long id, LocalTime start, LocalTime end) {
        this.id = id;
        this.start = start;
        this.end = end;
    }

    public WorkHours(LocalTime start, LocalTime end, Boolean deleted) {
        this.start = start;
        this.end = end;
        this.deleted = deleted;
    }

    public LocalTime getStart() {
        return start;
    }

    public void setStart(LocalTime start) {
        this.start = start;
    }

    public LocalTime getEnd() {
        return end;
    }

    public void setEnd(LocalTime end) {
        this.end = end;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WorkHours that = (WorkHours) o;

        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
