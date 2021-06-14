package com.isamrs.onlinehealth.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.isamrs.onlinehealth.dto.AppointedPatientDTO;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "patients")
@NamedNativeQuery(
        name = "find_examined_dto",
        query = "select p.user_id as patientId, p.first_name as firstName , p.last_name as lastName, max(e.start_time) as lastAppointed, e.appointment_id as idAppointment from patients as p inner join examinations e on p.user_id = e.patient and e.start_time < now() and lower(p.first_name) like '%' || lower(:firstName) || '%' and lower(p.last_name) like '%' || lower(:lastName) || '%' inner join dermatologists d on e.dermatologist = d.user_id where d.username = :username  group by p.user_id, e.appointment_id",
        resultSetMapping = "AppointedPatientDTO"
)

@NamedNativeQuery(
        name = "find_consulted_dto",
        query = "select p.user_id as patientId, p.first_name as firstName , p.last_name as lastName, max(e.start_time) as lastAppointed, e.appointment_id as idAppointment from patients as p inner join consultations e on p.user_id = e.patient and e.start_time < now() and lower(p.first_name) like '%' || lower(:firstName) || '%' and lower(p.last_name) like '%' || lower(:lastName) || '%' inner join pharmacists d on e.pharmacist = d.user_id where d.username = :username  group by p.user_id, e.appointment_id ",
        resultSetMapping = "AppointedPatientDTO"
)
@SqlResultSetMapping(
        name = "AppointedPatientDTO",
        classes = @ConstructorResult(
                targetClass = AppointedPatientDTO.class,
                columns = {
                        @ColumnResult(name = "patientId", type = Long.class),
                        @ColumnResult(name = "firstName", type = String.class),
                        @ColumnResult(name = "lastName", type = String.class),
                        @ColumnResult(name = "lastAppointed", type = LocalDateTime.class),
                        @ColumnResult(name = "idAppointment", type = Long.class)
                }
        )
)
public class Patient extends User implements Serializable {
    @JsonIgnore
    @OneToMany(mappedBy = "patient", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private final Set<Examination> examinations = new HashSet<>();
    @Column(
            name = "points",
            nullable = false
    )
    private Double points;
    @JsonIgnore
    @OneToMany(mappedBy = "patient", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private final Set<Consultation> consultations = new HashSet<>();
    @Column(
            name = "penalties",
            nullable = false
    )
    private Double penalties;  @JsonIgnore
    @OneToMany(mappedBy = "patient", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private final Set<EPrescription> ePrescriptions = new HashSet<>();
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "patients_allergies",
            joinColumns = @JoinColumn(name = "patient_user_id"),
            inverseJoinColumns = @JoinColumn(name = "allergies_medicine_id"))  @JsonIgnore
    private Set<Medicine> allergies;  @JsonIgnore
    @OneToMany(mappedBy = "patient", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Complaint> complaints = new HashSet<>();
    @Column(
            name = "deleted"
    )
    private Boolean deleted = false;

    @OneToMany(mappedBy = "patient", fetch = FetchType.EAGER, cascade = CascadeType.ALL)  @JsonIgnore
    private Set<ReservationList> reservations = new HashSet<>();

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }


    public Patient() {

    }

    public Patient(Long id, String username, String email, String password, String firstName, String lastName, String address, String city, String state, String phoneNumber, USER_TYPE user, boolean passwordChanged, Set<Medicine> allergies, Double points, Set<Complaint> complaints, Set<ReservationList> reservations, Double penalties) {
        super(id, username, email, password, firstName, lastName, address, city, state, phoneNumber, user, passwordChanged);
        this.allergies = allergies;
        this.points = points;
        this.complaints = complaints;
        this.penalties = penalties;
        this.reservations = reservations;
    }

    public Set<Complaint> getComplaints() {
        return complaints;
    }

    public void setComplaints(Set<Complaint> complaints) {
        this.complaints = complaints;
    }

    public Set<Medicine> getAllergies() {
        return allergies;
    }

    public void setAllergies(Set<Medicine> allergies) {
        this.allergies = allergies;
    }

    public Double getPoints() {
        return points;
    }

    public void setPoints(Double points) {
        this.points = points;
    }

    public Double getPenalties() {
        return penalties;
    }

    public void setPenalties(Double penals) {
        this.penalties = penals;
    }

    public Set<ReservationList> getReservations() {
        return reservations;
    }

    public void setReservations(Set<ReservationList> reservations) {
        this.reservations = reservations;
    }

    public Set<Examination> getExaminations() {
        return examinations;
    }

    public Set<Consultation> getConsultations() {
        return consultations;
    }

    public Set<EPrescription> getePrescriptions() {
        return ePrescriptions;
    }

    public void addPoints(double points) {
        this.points += points;
    }
}
