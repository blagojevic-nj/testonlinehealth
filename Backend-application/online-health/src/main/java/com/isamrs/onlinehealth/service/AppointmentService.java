package com.isamrs.onlinehealth.service;

import com.isamrs.onlinehealth.dto.AppointmentDTO;
import com.isamrs.onlinehealth.model.*;
import com.isamrs.onlinehealth.repository.PatientRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class AppointmentService {

    @Autowired
    private PatientRepository patientRepository;

    @Transactional
    public ArrayList<AppointmentDTO> getScheduledAppointments(String username){
        ArrayList<AppointmentDTO> retVal = new ArrayList<>();
        Patient patient = patientRepository.findOneByUsername(username);
        if(patient != null) {
            Hibernate.initialize(patient.getExaminations());
            Hibernate.initialize(patient.getConsultations());
            Hibernate.unproxy(patient);
            for(Examination e : patient.getExaminations()){
                if(e.getStart().isAfter(LocalDateTime.now())){
                    retVal.add(new AppointmentDTO(e.getId(), e.getStart(), e.getEnd(), AppointmentType.EXAMINATION,
                            username, e.getDermatologist().getUsername(), e.getPrice()));
                }
            }

            for(Consultation c : patient.getConsultations()){
                if(c.getStart().isAfter(LocalDateTime.now())) {
                    retVal.add(new AppointmentDTO(c.getId(), c.getStart(), c.getEnd(), AppointmentType.CONSULTATION,
                            username, c.getPharmacist().getUsername(), c.getPrice()));
                }
            }
        }
        return retVal;
    }
}
