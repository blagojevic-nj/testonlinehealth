package com.isamrs.onlinehealth.service;

import com.isamrs.onlinehealth.dto.ConsultationDTO;
import com.isamrs.onlinehealth.dto.NewExamPatientDermatologistPharmacyDTO;
import com.isamrs.onlinehealth.dto.ReportDTO;
import com.isamrs.onlinehealth.dto.StatsInfoDTO;
import com.isamrs.onlinehealth.model.*;
import com.isamrs.onlinehealth.repository.*;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;

@Service
public class ConsultationService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PharmacyRepository pharmacyRepository;

    @Autowired
    private PharmacistRepository pharmacistRepository;

    @Autowired
    private ExaminationsRepository examinationsRepository;

    @Autowired
    private ConsultationsRepository consultationsRepository;

    @Autowired
    private LoyaltyProgramService loyaltyProgramService;

    @Autowired
    private EmailService emailService;
    @Autowired
    private PatientService patientService;

    public void schedule(LocalDate datum, LocalTime od, LocalTime doo, String pacijent, String farmaceut) {
        Patient patient = patientRepository.findOneByUsername(pacijent);
        Pharmacist pharmacist = pharmacistRepository.findOneByUsername(farmaceut);
        Pharmacy pharmacy = null;
        if(patient != null && pharmacist != null){
            Consultation consultation = new Consultation();
            consultation.setPatient(patient);
            consultation.setPharmacist(pharmacist);
            consultation.setDeleted(false);
            consultation.setStart(datum.atTime(od));
            consultation.setEnd(datum.atTime(doo));

            for(Pharmacy ph : pharmacyRepository.findAll()){
                for(Pharmacist phar: ph.getPharmacists()){
                    if (phar.getUsername().equals(farmaceut)) {
                        pharmacy = ph;
                        break;

                    }
                }
                if (pharmacy != null){
                    break;
                }
            }
            StatsInfoDTO si = patientService.getStats(patient.getUsername());
            consultation.setPrice(pharmacy.getConsultation_price() * (100-si.getDiscount())/100);
            if(pharmacy.getSubscribers().contains(patient)){
                for(Promotion p : pharmacy.getPromotions()){
                    if(p.getStart().isBefore(LocalDateTime.now()) && p.getEnd().isAfter(LocalDateTime.now())
                            && !p.getDeleted())
                    {
                        consultation.setPrice(consultation.getPrice() * (1 - p.getDiscountRate()));
                    }
                }
            }

            consultation.setPharmacy(pharmacy);
            /**Provera*/
            boolean success = checkAndSchedule(consultation);
            if(!success)
            {
                System.out.println("Datum zauzet");
                return;
            }

            try {
                emailService.sendEmailAsync("",
                        patient.getEmail(),
                        "Zakazivanje savetovanja",
                        "Uspešno ste zakazali termin za " + consultation.getStart().toString() + ".");
            } catch (InterruptedException e) {
                System.out.println("Neka greska!");
            }
        }
    }
    @Transactional(readOnly = false)
    boolean checkAndSchedule(Consultation consultation) {
        long pharmacyId = consultation.getPharmacy().getId();
        long pharmacistId = consultation.getPharmacist().getId();
        Set<Consultation> consultations = consultationsRepository
                .getAllByPharmacyAndPharmacist(pharmacyId,pharmacistId,consultation.getStart(),consultation.getEnd());
        if(consultations.isEmpty())
            consultationsRepository.save(consultation);
        else return false;

        return true;
    }


    @Transactional
    public void cancel(Long id, String username) {
        Patient patient = patientRepository.findOneByUsername(username);
        if(patient != null) {
            Hibernate.initialize(patient.getConsultations());
            Hibernate.unproxy(patient);
            for(Consultation e : patient.getConsultations()){
                if (e.getId().equals(id) && LocalDateTime.now().isBefore(e.getStart())
                        && LocalDateTime.now().until(e.getStart(), ChronoUnit.HOURS) >= 24) {
                    patient.getConsultations().remove(e);
                    patientRepository.save(patient);
                    e.getPharmacist().getConsultations().remove(e);
                    consultationsRepository.delete(e);
                    break;
                }
            }
        }
    }

    @Transactional
    public ArrayList<ConsultationDTO> getFinishedConsultations(String username){
        ArrayList<ConsultationDTO> retVal = new ArrayList<>();

        Patient patient = patientRepository.findOneByUsername(username);
        if(patient != null) {
            Hibernate.initialize(patient.getExaminations());
            Hibernate.unproxy(patient);
            for(Consultation e : patient.getConsultations()){
                if(e.getEnd().isBefore(LocalDateTime.now()) && !e.getDeleted()){
                    ConsultationDTO tmp = new ConsultationDTO();
                    tmp.setId(e.getId());
                    tmp.setPatient(patient.getUsername());
                    tmp.setPharmacist(e.getPharmacist().getUsername());
                    tmp.setStart(e.getStart());
                    tmp.setEnd(e.getEnd());
                    tmp.setPrice(e.getPrice());
                    tmp.setPharmacy(e.getPharmacy().getName());
                    tmp.setPharmacy_id(e.getPharmacy().getId().toString());
                    tmp.setReport(e.getReport());
                    retVal.add(tmp);
                }
            }
        }
        return retVal;
    }

    public static boolean isOverlapping(LocalDateTime start1, LocalDateTime end1, LocalDateTime start2, LocalDateTime end2) {
        return start1.isBefore(end2) && start2.isBefore(end1);
    }


    public static boolean isOverlappingDate(LocalDateTime start1, LocalDateTime end1, LocalDateTime start2, LocalDateTime end2) {
        return start1.toLocalDate().isBefore(end2.toLocalDate()) && start2.toLocalDate().isBefore(end1.toLocalDate()) && !(end1.toLocalDate().equals(end2.toLocalDate())) && !(start1.toLocalDate().equals(start2.toLocalDate()));
    }


    @Transactional
    public boolean setShowedUp(Long id, Boolean shownUp) {
        Optional<Consultation> e = consultationsRepository.findById(id);
        if (e.isPresent()) {
            e.ifPresent(consultation -> consultation.setOngoing(shownUp));
            if (!shownUp) {
                e.get().setOngoing(true);
                e.get().setFinished(true);
                e.get().setOngoing(false);
                e.get().setReport("OVAJ PACIJENT SE NIJE POJAVIO!");
            }
            Hibernate.initialize(e.get().getPatient());
            if (!shownUp) e.get().getPatient().setPenalties(e.get().getPatient().getPenalties() + 1);
            consultationsRepository.save(e.get());
        }
        return true;
    }

    @Transactional
    public Optional<Consultation> getConsultationWithPatientPharmacist(Long id) {
        Optional<Consultation> e = consultationsRepository.findById(id);
        if (e.isPresent()) {
            Hibernate.initialize(e.get().getPharmacist());
            Hibernate.initialize(e.get().getPatient());
            Hibernate.initialize(e.get().getPharmacy());
            Hibernate.unproxy(e);
        }
        return e;
    }


    public void setReport(ReportDTO reportDTO) {
        consultationsRepository.findById(reportDTO.getIdExam())
                .ifPresent(e -> {
                    if (e.getOngoing()) {
                        e.setReport(reportDTO.getText());
                        consultationsRepository.save(e);
                    }
                });
    }


    @Transactional
    public ArrayList<Consultation> getAllInPharmacyAndPharmacist(Long pharmacyId, String username) {
        return new ArrayList<>(consultationsRepository.findConsultationsForScheduling(username, LocalDateTime.now(), pharmacyId));
    }

    @Transactional
    public boolean scheduleExisting(Long patientId, String pharmacistUsername, Long consultationId) {
        Optional<Consultation> e = consultationsRepository.findByIdAndLock(consultationId);
        if (e.isPresent()) {
            Pharmacist pharmacist = pharmacistRepository.findOneByUsername(pharmacistUsername);
            Optional<Patient> patient = patientRepository.findById(patientId);
            Hibernate.initialize(pharmacist.getConsultations());
            Hibernate.initialize(patient.get().getConsultations());
            for (Consultation consultation : patient.get().getConsultations())
                if (isOverlapping(e.get().getStart(), e.get().getEnd(), consultation.getStart(), consultation.getEnd()))
                    return false;

            for (Examination ex : examinationsRepository.findExaminationsForPatient(patient.get().getId()))
                if (isOverlapping(ex.getStart(), ex.getEnd(), e.get().getStart(), e.get().getEnd())) return false;
            for (Consultation con : consultationsRepository.findConsultationsForPatient(patient.get().getId()))
                if (isOverlapping(con.getStart(), con.getEnd(), e.get().getStart(), e.get().getEnd()))
                    return false;
            for (Consultation con : pharmacist.getConsultations()){
                if (!con.getId().equals(e.get().getId())) {
                    if (isOverlapping(con.getStart(), con.getEnd(), e.get().getStart(), e.get().getEnd()))
                        return false;
                }
            }
            e.get().setPatient(patient.get());
            patient.get().getConsultations().add(e.get());
            pharmacist.getConsultations().add(e.get());
            consultationsRepository.save(e.get());
            pharmacistRepository.save(pharmacist);
            patientRepository.save(patient.get());

            try {
                emailService.sendEmailAsync("",
                        patient.get().getEmail(),
                        "Zakazivanje pregleda",
                        "Uspesno ste zakazali termin za " + e.get().getStart().toString() + ".");
            } catch (InterruptedException exception) {
                System.out.println("Neka greska!");
            }
            return true;
        }
        return false;

    }

    @Transactional
    public boolean scheduleNew(NewExamPatientDermatologistPharmacyDTO e) {
        Pharmacist pharmacist = pharmacistRepository.findOneByUsername(e.getDermatologistUsername());
        Optional<Patient> p = patientRepository.findById(e.getPatientId());
        Pharmacy pharmacy = pharmacyRepository.findOneById(e.getPharmacyId());

        Consultation consultation = new Consultation(null, e.getStart(), e.getEnd(), "", p.get());

        consultation.setPharmacist(pharmacist);
        consultation.setPrice(pharmacy.getConsultation_price());
        consultation.setPharmacy(pharmacy);

        for (LeaveRequest leaveRequest : pharmacist.getLeaveRequests()) {
            if (isOverlappingDate(consultation.getStart(), consultation.getEnd(), leaveRequest.getStart(), leaveRequest.getEnd()))
                return false;
        }


        for (Examination ex : examinationsRepository.findExaminationsForPatient(p.get().getId()))
            if (isOverlapping(ex.getStart(), ex.getEnd(), consultation.getStart(), consultation.getEnd())) return false;
        for (Consultation con : consultationsRepository.findConsultationsForPatient(p.get().getId()))
            if (isOverlapping(con.getStart(), con.getEnd(), consultation.getStart(), consultation.getEnd()))
                return false;
        for (Consultation con : pharmacist.getConsultations())
            if (isOverlapping(con.getStart(), con.getEnd(), consultation.getStart(), consultation.getEnd()))
                return false;
        pharmacy.getConsultations().add(consultation);
        pharmacist.getConsultations().add(consultation);
        p.get().getConsultations().add(consultation);



        consultationsRepository.save(consultation);
        pharmacistRepository.save(pharmacist);
        patientRepository.save(p.get());
        pharmacyRepository.save(pharmacy);


        try {
            emailService.sendEmailAsync("",
                    p.get().getEmail(),
                    "Zakazivanje pregleda",
                    "Uspesno ste zakazali termin za " + consultation.getStart().toString() + ".");
        } catch (InterruptedException exception) {
            System.out.println("Neka greska!");
        }

        return true;
    }

    public boolean endConsultation(long id) {
        Optional<Consultation> consultation = consultationsRepository.findById(id);
        if (consultation.isPresent()) {
            consultation.ifPresent(con -> con.setFinished(true));
            consultationsRepository.save(consultation.get());

            LoyaltyProgram loyaltyProgram = null;

            try {
                loyaltyProgram = loyaltyProgramService.getLoyaltyPrograms().get(0);
            } catch (Exception exception) {
                exception.printStackTrace();
            }

            if (loyaltyProgram != null) {
                Optional<Patient> patient = patientRepository.findById(consultation.get().getPatient().getId());

                if (patient.isPresent() && null != loyaltyProgram.getConsultingPoints()) {
                    patient.get().addPoints(loyaltyProgram.getConsultingPoints());

                    patientRepository.save(patient.get());
                }
            }

            return consultation.get().getFinished();
        }
        return false;
    }
}
