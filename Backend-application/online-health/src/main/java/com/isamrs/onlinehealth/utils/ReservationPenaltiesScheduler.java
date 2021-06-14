package com.isamrs.onlinehealth.utils;

import com.isamrs.onlinehealth.model.ReservationList;
import com.isamrs.onlinehealth.model.ReservationStatus;
import com.isamrs.onlinehealth.repository.PatientRepository;
import com.isamrs.onlinehealth.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ReservationPenaltiesScheduler {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Scheduled(cron = "0 8 * * * *")
    public void run() throws InterruptedException {
        List<ReservationList> lista = reservationRepository.findAll();

        for(ReservationList rL: lista) {
            if (rL.getDeadline().isBefore(LocalDateTime.now()) && rL.getStatus() == ReservationStatus.RESERVED) {
                rL.getPatient().setPenalties(rL.getPatient().getPenalties() + 1);
                rL.setStatus(ReservationStatus.CANCELED);
                patientRepository.save(rL.getPatient());
                reservationRepository.save(rL);
            }
        }
    }
}
