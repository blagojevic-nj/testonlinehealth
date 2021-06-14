package com.isamrs.onlinehealth.dto;

import java.time.LocalDateTime;

public class AppointedPatientDTO {

    private long patientId;

    public long getPatientId() {
        return patientId;
    }

    public void setPatientId(long patientId) {
        this.patientId = patientId;
    }

    public AppointedPatientDTO(long patientId, String firstName, String lastName, LocalDateTime lastAppointed) {
        this.patientId = patientId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.lastAppointed = lastAppointed;
    }

    private String firstName;
    private String lastName;
    private LocalDateTime lastAppointed;
    private Long idAppointment;

    public AppointedPatientDTO(long patientId, String firstName, String lastName, LocalDateTime lastAppointed, Long idAppointment) {
        this.patientId = patientId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.lastAppointed = lastAppointed;
        this.idAppointment = idAppointment;
    }

    public Long getIdAppointment() {
        return idAppointment;
    }

    public void setIdAppointment(Long idAppointment) {
        this.idAppointment = idAppointment;
    }

    public AppointedPatientDTO() {
    }

    public AppointedPatientDTO(String firstName, String lastName, LocalDateTime lastAppointed) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.lastAppointed = lastAppointed;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDateTime getLastAppointed() {
        return lastAppointed;
    }

    public void setLastAppointed(LocalDateTime lastAppointed) {
        this.lastAppointed = lastAppointed;
    }
}
