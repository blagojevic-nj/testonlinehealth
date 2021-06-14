package com.isamrs.onlinehealth.utils;

import com.isamrs.onlinehealth.model.Patient;
import com.isamrs.onlinehealth.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class PenaltiesScheduler {

    @Autowired
    private PatientRepository patientRepository;

    @Scheduled(cron = "0 0 1 * * *")
    public void run() throws InterruptedException {
        for(Patient p : patientRepository.findAll()){
            p.setPenalties(0.0);
            patientRepository.save(p);
        }
    }
}
