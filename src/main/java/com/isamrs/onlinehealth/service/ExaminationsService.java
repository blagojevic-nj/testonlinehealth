package com.isamrs.onlinehealth.service;

import com.isamrs.onlinehealth.dto.ExaminationDTO;
import com.isamrs.onlinehealth.dto.NewExamPatientDermatologistPharmacyDTO;
import com.isamrs.onlinehealth.dto.ReportDTO;
import com.isamrs.onlinehealth.model.*;
import com.isamrs.onlinehealth.repository.*;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class ExaminationsService {
    @Autowired
    private ExaminationsRepository examinationsRepository;
    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private ConsultationsRepository consultationsRepository;


    @Autowired
    private PharmacyRepository pharmacyRepository;

    @Autowired
    private DermatologistRepository dermatologistRepository;

    @Autowired
    private LoyaltyProgramService loyaltyProgramService;

    @Autowired
    private EmailService emailService;

    public ArrayList<ExaminationDTO> getFreeExaminations(Long id) {
        ArrayList<ExaminationDTO> retVal = new ArrayList<>();
        ArrayList<Examination> examinations = (ArrayList<Examination>) examinationsRepository.findByPharmacyId(id);
        for (Examination e : examinations) {

            if (!e.getDeleted() && e.getPatient() == null && LocalDateTime.now().isBefore(e.getStart())) {
                retVal.add(new ExaminationDTO(e.getId(), e.getStart(), e.getEnd(), e.getReport(), e.getPrice(),
                        null, e.getPharmacy().getId(), e.getPharmacy().getName(),
                        e.getDermatologist().getUsername()));
            }
        }
        return retVal;
    }


    @Transactional
    public Optional<Examination> getExaminationWithPatientDermatologist(Long id) {
        Optional<Examination> e = examinationsRepository.findById(id);
        if (e.isPresent()) {
            Hibernate.initialize(e.get().getDermatologist());
            Hibernate.initialize(e.get().getPatient());
            Hibernate.initialize(e.get().getPharmacy());
            Hibernate.unproxy(e);
        }
        return e;
    }

    @Transactional
    public boolean setShowedUp(Long id, Boolean shownUp) {
        Optional<Examination> e = examinationsRepository.findById(id);
        if((LocalDateTime.now().isBefore(e.get().getStart()) || LocalDateTime.now().isAfter(e.get().getEnd()))) return false;
        if(e.isPresent()) {
            e.ifPresent(examination -> examination.setOngoing(shownUp));
            if (!shownUp) {
                e.get().setOngoing(true);
                e.get().setFinished(true);
                e.get().setOngoing(false);
                e.get().setReport("OVAJ PACIJENT SE NIJE POJAVIO!");
            }
            Hibernate.initialize(e.get().getPatient());
            if (!shownUp) e.get().getPatient().setPenalties(e.get().getPatient().getPenalties() + 1);
            examinationsRepository.save(e.get());
        }
        return true;
    }

    public void setReport(ReportDTO reportDTO){
        examinationsRepository.findById(reportDTO.getIdExam())
                .ifPresent(e ->{
                    if(e.getOngoing()) {
                        e.setReport(reportDTO.getText());
                        examinationsRepository.save(e);
                    }
                });
    }

    @Transactional(readOnly=false)
    public void schedule(Long id, String username) {
        Patient patient = patientRepository.findOneByUsername(username);
        Optional<Examination> examination = examinationsRepository.findByIdAndLock(id);
        if (examination.isEmpty())
            return;
        if (patient != null && examination.get().getPatient() == null && LocalDateTime.now().isBefore(examination.get().getStart())) {
            examination.get().setPatient(patient);
            examinationsRepository.save(examination.get());
            try {
                emailService.sendEmailAsync("",
                        patient.getEmail(),
                        "Zakazivanje pregleda",
                        "Uspešno ste zakazali termin za " + examination.get().getStart().toString() + ".");
            } catch (InterruptedException e) {
                System.out.println("Neka greska!");
            }
        }
    }

    @Transactional
    public boolean cancel(Long id, String username) {
        Patient patient = patientRepository.findOneByUsername(username);
        if(examinationsRepository.findById(id).isEmpty())
            return false;

        if(patient != null) {
            Hibernate.initialize(patient.getExaminations());
            Hibernate.unproxy(patient);
            for(Examination e : patient.getExaminations()){
                if (e.getId().equals(id) && LocalDateTime.now().isBefore(e.getStart())
                        && LocalDateTime.now().until(e.getStart(), ChronoUnit.HOURS) >= 24) {
                    e.setPatient(null);
                    patient.getExaminations().remove(e);
                    patientRepository.save(patient);
                    break;
                }
            }
            return true;
        }
        return false;
    }

    public static boolean isOverlapping(LocalDateTime start1, LocalDateTime end1, LocalDateTime start2, LocalDateTime end2) {
        return start1.isBefore(end2) && start2.isBefore(end1);
    }

    public static boolean isOverlappingDate(LocalDateTime start1, LocalDateTime end1, LocalDateTime start2, LocalDateTime end2) {
        return start1.toLocalDate().isBefore(end2.toLocalDate()) && start2.toLocalDate().isBefore(end1.toLocalDate()) && !(end1.toLocalDate().equals(end2.toLocalDate())) && !(start1.toLocalDate().equals(start2.toLocalDate()));
    }


    @Transactional
    public ArrayList<Examination> getAllInPharmacyAndDermatologist(Long pharmacyId, String username) {
        return new ArrayList<>(examinationsRepository.findExaminationForScheduling(username, LocalDateTime.now(), pharmacyId));
    }

    @Transactional
    public boolean scheduleNew(NewExamPatientDermatologistPharmacyDTO e) {
        Dermatologist d = dermatologistRepository.findOneByUsername(e.getDermatologistUsername());
        Optional<Patient> p = patientRepository.findById(e.getPatientId());
        Pharmacy pharmacy = pharmacyRepository.findOneById(e.getPharmacyId());

        Examination exam = new Examination(null, e.getStart(), e.getEnd(), "", e.getPrice(), d, p.get(), false, pharmacy);

        if (!(e.getStart().toLocalDate().equals(e.getEnd().toLocalDate()))) return false;

        for (LeaveRequest leaveRequest : d.getLeaveRequests()) {
            if (isOverlappingDate(exam.getStart(), exam.getEnd(), leaveRequest.getStart(), leaveRequest.getEnd()))
                return false;
        }

        if (!(d.getWorkHoursPharmacies().get(pharmacy).getEnd().isAfter(exam.getEnd().toLocalTime()) && d.getWorkHoursPharmacies().get(pharmacy).getStart().isBefore(exam.getStart().toLocalTime())))
            return false;

        for (Consultation con : consultationsRepository.findConsultationsForPatient(p.get().getId()))
            if (isOverlapping(con.getStart(), con.getEnd(), exam.getStart(), exam.getEnd())) return false;

        for (Examination ex : examinationsRepository.findExaminationsForPatient(p.get().getId()))
            if (isOverlapping(ex.getStart(), ex.getEnd(), exam.getStart(), exam.getEnd())) return false;

        for (Examination ex : d.getExaminations())
            if (isOverlapping(ex.getStart(), ex.getEnd(), exam.getStart(), exam.getEnd())) return false;


        pharmacy.getExaminations().add(exam);
        p.get().getExaminations().add(exam);
        d.getExaminations().add(exam);

        examinationsRepository.save(exam);
        dermatologistRepository.save(d);
        patientRepository.save(p.get());
        pharmacyRepository.save(pharmacy);

        try {
            emailService.sendEmailAsync("",
                    p.get().getEmail(),
                    "Zakazivanje pregleda",
                    "Uspesno ste zakazali termin za " + exam.getStart().toString() + ".");
        } catch (InterruptedException exception) {
            System.out.println("Neka greska!");
        }

        return true;
    }

    @Transactional
    public boolean scheduleExisting(Long patientId, String dermatologistUsername, Long examinationId) {
        Optional<Examination> e = examinationsRepository.findByIdAndLock(examinationId);
        if (e.isPresent()) {
            Dermatologist d = dermatologistRepository.findOneByUsername(dermatologistUsername);
            Optional<Patient> p = patientRepository.findById(patientId);
            Pharmacy pharmacy = pharmacyRepository.findOneById(e.get().getPharmacy().getId());
            if (!(e.get().getStart().toLocalDate().equals(e.get().getEnd().toLocalDate()))) return false;

            for (LeaveRequest leaveRequest : d.getLeaveRequests()) {
                if (isOverlappingDate(e.get().getStart(), e.get().getEnd(), leaveRequest.getStart(), leaveRequest.getEnd()))
                    return false;
            }

            if (!(d.getWorkHoursPharmacies().get(pharmacy).getEnd().isAfter(e.get().getEnd().toLocalTime()) && d.getWorkHoursPharmacies().get(pharmacy).getStart().isBefore(e.get().getStart().toLocalTime())))
                return false;

            for (Consultation con : consultationsRepository.findConsultationsForPatient(p.get().getId()))
                if (isOverlapping(con.getStart(), con.getEnd(), e.get().getStart(), e.get().getEnd())) return false;

            for (Examination ex : examinationsRepository.findExaminationsForPatient(p.get().getId()))
                if (isOverlapping(ex.getStart(), ex.getEnd(), e.get().getStart(), e.get().getEnd())) return false;

            for (Examination ex : d.getExaminations())
                if(!ex.getId().equals(e.get().getId())) {
                    if (isOverlapping(ex.getStart(), ex.getEnd(), e.get().getStart(), e.get().getEnd())) return false;
                }

            e.get().setPatient(p.get());
            p.get().getExaminations().add(e.get());
            d.getExaminations().add(e.get());

            examinationsRepository.save(e.get());
            dermatologistRepository.save(d);
            patientRepository.save(p.get());

            try {
                emailService.sendEmailAsync("",
                        p.get().getEmail(),
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
    public ArrayList<ExaminationDTO> getFinishedExaminations(String username){
        ArrayList<ExaminationDTO> retVal = new ArrayList<>();

        Patient patient = patientRepository.findOneByUsername(username);
        if(patient != null) {
            Hibernate.initialize(patient.getExaminations());
            Hibernate.unproxy(patient);
            for(Examination e : patient.getExaminations()){
                if(e.getEnd().isBefore(LocalDateTime.now()) && !e.getDeleted()){
                    ExaminationDTO tmp = new ExaminationDTO();
                    tmp.setId(e.getId());
                    tmp.setPatient(patient.getUsername());
                    tmp.setDermatologist(e.getDermatologist().getUsername());
                    tmp.setStart(e.getStart());
                    tmp.setEnd(e.getEnd());
                    tmp.setPrice(e.getPrice());
                    tmp.setPharmacy(e.getPharmacy().getName());
                    tmp.setPharmacy_id(e.getPharmacy().getId());
                    tmp.setReport(e.getReport());
                    retVal.add(tmp);
                }
            }
        }
        return retVal;
    }

    public ArrayList<ExaminationDTO> getFilterExaminations(
            long pharmacyId, LocalDate filterDatum1, LocalDate filterDatum2, LocalTime filtervreme1, LocalTime filtervreme2, int filterCena) {
        ArrayList<ExaminationDTO> retVal = new ArrayList<>();
        ArrayList<Examination> examinations = (ArrayList<Examination>) examinationsRepository.findByPharmacyId(pharmacyId);

        boolean isFD1=false,isFD2=false,isFV1=false,isFV2=false,isFC=false;
        LocalDate testDate = LocalDate.of(1000,1,1);
        LocalTime testTime = LocalTime.of(0,0);
        isFD1 = !filterDatum1.isEqual(testDate);
        isFD2 = !filterDatum2.isEqual(testDate);

        isFV1 = !(filtervreme1.compareTo(testTime)==0);
        isFV2 = !(filtervreme2.compareTo(testTime)==0);

        isFC = filterCena!=-1;


        for (Examination e : examinations) {

            if (!e.getDeleted() && e.getPatient() == null && LocalDateTime.now().isBefore(e.getStart())) {
                if(isFD1){
                   if(e.getStart().isBefore(filterDatum1.atStartOfDay()))
                       continue;
                }
                if (isFD2){
                    if(e.getStart().isAfter(filterDatum2.atStartOfDay()))
                        continue;
                }
                if(isFV1){
                    if(e.getStart().toLocalTime().isBefore(filtervreme1))
                        continue;
                }
                if(isFV2){
                    if(e.getStart().toLocalTime().isAfter(filtervreme2))
                        continue;
                }
                if(isFC)
                {
                    if(e.getPrice()>filterCena)
                        continue;
                }


            retVal.add(new ExaminationDTO(e.getId(), e.getStart(), e.getEnd(), e.getReport(), e.getPrice(),
                    null, e.getPharmacy().getId(), e.getPharmacy().getName(),
                    e.getDermatologist().getUsername()));
            }
        }
        return retVal;
    }

    public boolean endExamination(long id) {
        Optional<Examination> e = examinationsRepository.findById(id);
        if(e.isPresent()) {
            e.ifPresent(examination -> examination.setFinished(true));
            examinationsRepository.save(e.get());

            LoyaltyProgram loyaltyProgram = null;

            try {
                loyaltyProgram = loyaltyProgramService.getLoyaltyPrograms().get(0);
            } catch (Exception exception) {
                exception.printStackTrace();
            }

            if (loyaltyProgram != null) {
                Optional<Patient> patient = patientRepository.findById(e.get().getPatient().getId());

                if (patient.isPresent() && null != loyaltyProgram.getExaminationPoints()) {
                    patient.get().addPoints(loyaltyProgram.getExaminationPoints());

                    patientRepository.save(patient.get());
                }
            }

            return e.get().getFinished();
        }
        return false;
    }
}
