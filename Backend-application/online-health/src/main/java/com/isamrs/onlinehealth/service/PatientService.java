package com.isamrs.onlinehealth.service;

import com.isamrs.onlinehealth.dto.AppointedPatientDTO;
import com.isamrs.onlinehealth.dto.PatientDTO;
import com.isamrs.onlinehealth.dto.StatsInfoDTO;
import com.isamrs.onlinehealth.model.Complaint;
import com.isamrs.onlinehealth.model.Patient;
import com.isamrs.onlinehealth.model.VerificationToken;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PatientService {


    Patient findByUsername(String username);

    List<Patient> searchByFirstNameLastName(String firstName, String lastName);


    Patient findByEmail(String email);

    List<AppointedPatientDTO> getExamined(String username, String firstName, String lastName);

    List<AppointedPatientDTO> getConsulted(String username, String firstName, String lastName);

    Patient save(Patient patient);


    void createVerificationToken(Patient patient, String token);

    VerificationToken getVerificationToken(String token);

    Patient registerPatient(PatientDTO patientDTO);

    Set<Complaint> getComplaints(String username);

    Set<String> getExaminations(String username);

    Set<String> getConsultations(String username);

    Set<String> getReservations(String username);

    void addComplaint(Complaint c, String username);

    Optional<Patient> findById(Long patientId);

    Double getPenalties(String username);

    StatsInfoDTO getStats(String username);
}
