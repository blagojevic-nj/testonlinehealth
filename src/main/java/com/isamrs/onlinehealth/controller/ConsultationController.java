package com.isamrs.onlinehealth.controller;

import com.isamrs.onlinehealth.dto.ConsultationDTO;
import com.isamrs.onlinehealth.dto.ExaminationDTO;
import com.isamrs.onlinehealth.dto.NewExamPatientDermatologistPharmacyDTO;
import com.isamrs.onlinehealth.dto.ReportDTO;
import com.isamrs.onlinehealth.model.Consultation;
import com.isamrs.onlinehealth.service.ConsultationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class ConsultationController {
    @Autowired
    private ConsultationService consultationService;

//Transakcije
    @RequestMapping(value = "/api/consultation/scheduling", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<Void> scheduleExamination(@RequestParam String datum, @RequestParam String od,
                                                    @RequestParam String doo, @RequestParam String patient,
                                                    @RequestParam String pharmacist) {
        consultationService.schedule(LocalDate.parse(datum), LocalTime.parse(od), LocalTime.parse(doo), patient, pharmacist);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @RequestMapping(value = "/api/consultation/canceling", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<Void> cancelConsultation(@RequestParam long id, @RequestParam String username) {
        consultationService.cancel(id, username);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @RequestMapping(value = "/api/consultations/getFinishedConsultations", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<ArrayList<ConsultationDTO>> getFinishedExaminations(@RequestParam String username) {
        return new ResponseEntity<ArrayList<ConsultationDTO>>(consultationService.getFinishedConsultations(username), HttpStatus.OK);
    }


    @RequestMapping(value = "api/consultations/start", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('PHARMACIST')")
    public ResponseEntity<?> startExamination(@RequestParam(name = "id") long id, @RequestParam(name = "showed", required = true) String showedUp) {
        boolean shownUp = showedUp.equals("yes");

        if (consultationService.setShowedUp(id, shownUp))
            return new ResponseEntity<>(HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/api/consultations", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('PHARMACIST')")
    public ResponseEntity<ExaminationDTO> getOneExamination(@RequestParam(name = "id") long id, @RequestParam(name = "username") String username) {
        Optional<Consultation> e = consultationService.getConsultationWithPatientPharmacist(id);
        if (e.get().getPharmacist().getUsername().equals(username)) {
            return new ResponseEntity<>(new ExaminationDTO(
                    e.get()), HttpStatus.OK);
        } else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


    @RequestMapping(value = "api/consultations/report", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('PHARMACIST')")
    public ResponseEntity<?> postReport(@RequestBody ReportDTO reportDTO) {
        consultationService.setReport(reportDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "api/consultations/pharmacy", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('PHARMACIST')")
    public ResponseEntity<?> getConsultationsInPharmacy(@RequestParam(name = "pharmacyId", required = true) Long pharmacyId, @RequestParam(name = "username", required = true) String username) {

        return new ResponseEntity<>(new ArrayList<>(consultationService.getAllInPharmacyAndPharmacist(pharmacyId, username)
                .stream()
                .map(ExaminationDTO::new)
                .collect(Collectors.toList())), HttpStatus.OK);
    }

    @RequestMapping(value = "api/consultations/scheduleExisting", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('PHARMACIST')")
    public ResponseEntity<?> scheduleExistingExam(@RequestParam(value = "patientId") Long patientId,
                                                  @RequestParam(value = "pharmacistUsername") String pharmacistUsername,
                                                  @RequestParam(value = "consultationId") Long consultationId) {

        if (consultationService.scheduleExisting(patientId, pharmacistUsername, consultationId))
            return new ResponseEntity<>(HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "api/consultations/scheduleNew", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('DERMATOLOGIST')")
    public ResponseEntity<?> scheduleNewExam(@RequestBody @Valid NewExamPatientDermatologistPharmacyDTO e) {
        if (consultationService.scheduleNew(e))
            return new ResponseEntity<>(HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/api/consultations/end", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('PHARMACIST')")
    public ResponseEntity<?> endExamination(@RequestParam(name = "id") long id) {

        if (consultationService.endConsultation(id))
            return new ResponseEntity<>(HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
