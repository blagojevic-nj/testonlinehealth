package com.isamrs.onlinehealth.service;

import com.isamrs.onlinehealth.dto.AppointedPatientDTO;
import com.isamrs.onlinehealth.dto.PatientDTO;
import com.isamrs.onlinehealth.dto.StatsInfoDTO;
import com.isamrs.onlinehealth.model.*;
import com.isamrs.onlinehealth.repository.ComplaintRepository;
import com.isamrs.onlinehealth.repository.LoyaltyProgramRepository;
import com.isamrs.onlinehealth.repository.PatientRepository;
import com.isamrs.onlinehealth.repository.TokenRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class PatientServiceImpl implements PatientService {
    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ComplaintRepository complaintRepository;
    @Autowired
    private LoyaltyProgramRepository loyaltyProgramRepository;

    @Override
    public Patient findByUsername(String username) {
        return patientRepository.findOneByUsername(username);
    }

    @Override
    public List<Patient> searchByFirstNameLastName(String firstName, String lastName) {
        return patientRepository.findByFirstNameIgnoreCaseContainsAndLastNameIgnoreCaseContains(firstName, lastName);
    }

    @Override
    public Patient findByEmail(String email) {
        return patientRepository.findOneByEmail(email);
    }

    @Override
    public List<AppointedPatientDTO> getExamined(String username, String firstName, String lastName) {
        return patientRepository.findAllExaminedPatients(username, firstName, lastName);
    }

    @Override
    public List<AppointedPatientDTO> getConsulted(String username, String firstName, String lastName) {
        return patientRepository.findAllConsultedPatients(username, firstName, lastName);
    }

    @Override
    public Patient save(Patient patient) {
        return patientRepository.save(patient);
    }


    @Override
    public void createVerificationToken(Patient patient, String token) {
        VerificationToken newPatientToken = new VerificationToken(token, patient);
        Calendar currentDate = Calendar.getInstance();
        newPatientToken.setCreatedDate(currentDate.getTime());
        currentDate.add(Calendar.HOUR_OF_DAY, 24);
        newPatientToken.setExpiry_date(currentDate.getTime());
        tokenRepository.save(newPatientToken);
    }

    @Override
    public VerificationToken getVerificationToken(String token) {
        return tokenRepository.findOneByToken(token);
    }

    @Override
    public Patient registerPatient(PatientDTO patientDTO) {
        USER_TYPE userType = USER_TYPE.PATIENT;
        boolean passwordChanged = true;
        Double points = 0.0;
        Patient patient = new Patient(null, patientDTO.getUsername(), patientDTO.getEmail(), patientDTO.getPassword(),
                patientDTO.getFirstName(), patientDTO.getLastName(), patientDTO.getAddress(), patientDTO.getCity(), patientDTO.getState(),
                patientDTO.getPhoneNumber(), userType, passwordChanged, new HashSet<Medicine>(), points, new HashSet<Complaint>(), new HashSet<ReservationList>(), 0.0);
        List<Role> roles = roleService.findByName("ROLE_PATIENT");
        patient.setPassword(passwordEncoder.encode(patient.getPassword()));
        patient.setRoles(roles);
        return patientRepository.save(patient);
    }

    @Override
    @Transactional
    public Set<Complaint> getComplaints(String username) {
        Patient patient = patientRepository.findOneByUsername(username);
        Hibernate.initialize(patient.getComplaints());
        Hibernate.unproxy(patient);
        Set<Complaint> complaints = new HashSet<>();
        for (Complaint c: patient.getComplaints())
            if(!c.getDeleted())
                complaints.add(c);
        return complaints;
    }

    @Override
    @Transactional
    public Set<String> getExaminations(String username) {
        Patient patient = patientRepository.findOneByUsername(username);
        Hibernate.initialize(patient.getExaminations());
        Hibernate.unproxy(patient);
        Set<String> items = new HashSet<>();
        for (Examination e: patient.getExaminations()) {
            items.add(e.getDermatologist().getUsername());
            items.add(e.getPharmacy().getName());
        }
        return items;
    }

    @Override
    @Transactional
    public Set<String> getConsultations(String username) {
        Patient patient = patientRepository.findOneByUsername(username);
        Hibernate.initialize(patient.getConsultations());
        Hibernate.unproxy(patient);
        Set<String> items = new HashSet<>();
        for (Consultation c: patient.getConsultations()) {
            items.add(c.getPharmacist().getUsername());
        }
        return items;
    }

    @Override
    @Transactional
    public Set<String> getReservations(String username) {
        Patient patient = patientRepository.findOneByUsername(username);
        Hibernate.initialize(patient.getReservations());
        Hibernate.unproxy(patient);
        Set<String> items = new HashSet<>();
        for (ReservationList r: patient.getReservations()) {
            items.add(r.getPharmacy().getName());
        }
        return items;
    }

    @Override
    @Transactional
    public void addComplaint(Complaint c, String username) {
        Patient p = patientRepository.findOneByUsername(username);
        Hibernate.initialize(p.getComplaints());
        Hibernate.unproxy(p);
        if (p.getComplaints() == null)
            p.setComplaints(new HashSet<>());
        c.setPatient(p);
        p.getComplaints().add(complaintRepository.save(c));
        patientRepository.save(p);
    }

    @Override
    public Optional<Patient> findById(Long patiendId) {
        return patientRepository.findById(patiendId);
    }

    @Override
    public Double getPenalties(String username){
        Patient p = patientRepository.findOneByUsername(username);

        if (p != null){
            return p.getPenalties();
        }
        return -1.0;
    }

    @Override
    @Transactional
    public StatsInfoDTO getStats(String username){
        StatsInfoDTO si = new StatsInfoDTO();
        LoyaltyProgram lp = loyaltyProgramRepository.findAll().get(0);
        Hibernate.initialize(lp.getLoyaltyCategories());
        Hibernate.unproxy(lp);

        Patient p = patientRepository.findOneByUsername(username);
        if (p != null){
            si.setPoints(p.getPoints());
            if(lp != null){
                for(LoyaltyCategory lc : lp.getLoyaltyCategories()){
                    if(p.getPoints() >= lc.getLowLimit() && p.getPoints() < lc.getHighLimit()){
                        si.setCategoryName(lc.getName());
                        si.setDiscount(lc.getDiscountRate());
                        break;
                    }
                }
            }
        }
        return si;
    }
}
