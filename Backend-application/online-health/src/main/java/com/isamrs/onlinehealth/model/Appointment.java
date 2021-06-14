package com.isamrs.onlinehealth.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@MappedSuperclass
public abstract class Appointment {
    @Column(
            name = "appointment_id",
            unique = true,
            nullable = false
    )
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    @Column(
            name = "start_time",
            nullable = false
    )
    protected LocalDateTime start;
    @Column(
            name = "end_time",
            nullable = false
    )
    protected LocalDateTime end;
    @Column(
            name = "report"
    )
    protected String report;

    @Column(
            name = "deleted"
    )
    private Boolean deleted = false;


    @Version
    @Column(nullable = false)
    private Long version;

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    protected Boolean ongoing = false;

    public Boolean getOngoing() {
        return ongoing && LocalDateTime.now().isBefore(this.end) && LocalDateTime.now().isAfter(start);
    }

    public void setOngoing(Boolean ongoing) {
        this.ongoing = ongoing;
    }

    protected Boolean finished = false;

    public Boolean getFinished() {
        return finished || LocalDateTime.now().isAfter(this.end);
    }

    public void setFinished(Boolean finished) {
        this.finished = finished && getOngoing();
        if(this.finished) this.ongoing=false;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Appointment() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Appointment(Long id, LocalDateTime start, LocalDateTime end, String report) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.report = report;
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

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Appointment appointment = (Appointment) o;

        return Objects.equals(id, appointment.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
