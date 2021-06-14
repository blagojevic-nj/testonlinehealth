package com.isamrs.onlinehealth.controller;

import com.isamrs.onlinehealth.dto.*;
import com.isamrs.onlinehealth.model.Examination;
import com.isamrs.onlinehealth.service.AppointmentService;
import com.isamrs.onlinehealth.service.ExaminationsService;
import com.isamrs.onlinehealth.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class ExaminationsController {
    @Autowired
    private ExaminationsService examinationsService;
    @Autowired
    private AppointmentService appointmentService;
    @Autowired
    private PatientService patientService;


    @RequestMapping(value= "/api/examinations/getFreeExaminations", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<ArrayList<ExaminationDTO>> getExaminations(@RequestParam long id) {
        return new ResponseEntity<ArrayList<ExaminationDTO>>(examinationsService.getFreeExaminations(id), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PATIENT')")
    @RequestMapping(value= "/api/examinations/getFilterExaminations", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ArrayList<ExaminationDTO>> getFilterExaminations(@RequestParam long pharmacyId,
                                                                           @RequestParam String filterDatum1,
                                                                           @RequestParam String filterDatum2,
                                                                           @RequestParam String filterVreme1,
                                                                           @RequestParam String filterVreme2,
                                                                           @RequestParam int filterCena) {
        LocalDate d1,d2;
        try {
            d1 = LocalDate.parse(filterDatum1, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            d2 = LocalDate.parse(filterDatum2, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }catch (Exception e){
            return new ResponseEntity<ArrayList<ExaminationDTO>>(examinationsService.getFreeExaminations(pharmacyId), HttpStatus.OK);
        }
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime t1,t2;
        try {
            t1 = LocalTime.parse(filterVreme1, dateTimeFormatter);
            t2 = LocalTime.parse(filterVreme2, dateTimeFormatter);
        }catch (Exception e){
            return new ResponseEntity<ArrayList<ExaminationDTO>>(examinationsService.getFreeExaminations(pharmacyId), HttpStatus.OK);
        }

        return new ResponseEntity<ArrayList<ExaminationDTO>>(examinationsService.getFilterExaminations(
                pharmacyId,d1,d2,t1,t2,filterCena), HttpStatus.OK);
    }

    @RequestMapping(value= "/api/examinations/getScheduledAppointments", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<ArrayList<AppointmentDTO>> getScheduledAppointments(@RequestParam String username) {

        return new ResponseEntity<ArrayList<AppointmentDTO>>(appointmentService.getScheduledAppointments(username), HttpStatus.OK);
    }

    @RequestMapping(value = "/api/examinations/scheduling", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<Void> scheduleExamination(@RequestParam long id, @RequestParam String username) {
        examinationsService.schedule(id, username);
        if(patientService.findByUsername(username) == null)
            return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @RequestMapping(value = "/api/examinations/canceling", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<Void> cancelExamination(@RequestParam long id, @RequestParam String username) {
        boolean rez = examinationsService.cancel(id, username);
        if(rez)
            return new ResponseEntity<Void>(HttpStatus.OK);
        else
            return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
    }


    @RequestMapping(value = "/api/examinations", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('DERMATOLOGIST')")
    public ResponseEntity<ExaminationDTO> getOneExamination(@RequestParam(name = "id") long id, @RequestParam(name = "username") String username) {
        Optional<Examination> e = examinationsService.getExaminationWithPatientDermatologist(id);
        if(e.get().getDermatologist().getUsername().equals(username)){
            return new ResponseEntity<>(new ExaminationDTO(
e.get()), HttpStatus.OK);
        }
        else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "api/examinations/start", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('DERMATOLOGIST')")
    public ResponseEntity<?> startExamination(@RequestParam(name = "id") long id, @RequestParam(name = "showed", required = true) String showedUp) {
        boolean shownUp = showedUp.equals("yes");

        if(examinationsService.setShowedUp(id, shownUp))
            return new ResponseEntity<>(HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/api/examinations/end", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('DERMATOLOGIST')")
    public ResponseEntity<?> endExamination(@RequestParam(name = "id") long id) {

        if(examinationsService.endExamination(id))
            return new ResponseEntity<>(HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


    @RequestMapping(value = "api/examinations/report", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('DERMATOLOGIST')")
    public ResponseEntity<?> postReport(@RequestBody ReportDTO reportDTO) {
        examinationsService.setReport(reportDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @RequestMapping(value = "api/examinations/pharmacy", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('DERMATOLOGIST')")
    public ResponseEntity<?> getExaminationsInPharmacy(@RequestParam(name = "pharmacyId", required = true) Long pharmacyId, @RequestParam(name = "username", required = true) String username) {

        return new ResponseEntity<>(new ArrayList<>(examinationsService.getAllInPharmacyAndDermatologist(pharmacyId, username)
                .stream()
                .map(ExaminationDTO::new)
                .collect(Collectors.toList())), HttpStatus.OK);
    }

    @RequestMapping(value = "api/examinations/scheduleExisting", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('DERMATOLOGIST')")
    public ResponseEntity<?> scheduleExistingExam(@RequestParam(value = "patientId") Long patientId,
                                                  @RequestParam(value = "dermatologistUsername") String dermatologistUsername,
                                                  @RequestParam(value = "examinationId") Long examinationId) {

        if (examinationsService.scheduleExisting(patientId, dermatologistUsername, examinationId))
            return new ResponseEntity<>(HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "api/examinations/scheduleNew", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('DERMATOLOGIST')")
    public ResponseEntity<?> scheduleNewExam(@RequestBody @Valid NewExamPatientDermatologistPharmacyDTO e) {
        if (examinationsService.scheduleNew(e))
            return new ResponseEntity<>(HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value= "/api/examinations/getFinishedExaminations", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<ArrayList<ExaminationDTO>> getFinishedExaminations(@RequestParam String username) {
        return new ResponseEntity<ArrayList<ExaminationDTO>>(examinationsService.getFinishedExaminations(username), HttpStatus.OK);
    }
}
