package com.isamrs.onlinehealth.controller;

import com.isamrs.onlinehealth.dto.ReservationListDTO;
import com.isamrs.onlinehealth.service.PatientService;
import com.isamrs.onlinehealth.service.PharmacyService;
import com.isamrs.onlinehealth.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
public class ReservationController {
    @Autowired
    private ReservationService reservationService;
    @Autowired
    private PharmacyService pharmacyService;
    @Autowired
    private PatientService patientService;

    @RequestMapping(value = "/api/reservations/add",
            method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
//    @PreAuthorize("hasRole('PATIENT')")
    public Boolean addReservation(@RequestBody ReservationListDTO reservationListDTO) {
        return reservationService.addReservation(reservationListDTO, pharmacyService, patientService);
    }

    @RequestMapping(value = "/api/reservations/addAppointmentReservation",
            method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('DERMATOLOGIST', 'PHARMACIST')")
    public ResponseEntity<String> addReservationFromAppointment(@RequestBody ReservationListDTO reservationListDTO) {
        return new ResponseEntity<>(reservationService.addReservationFromAppointment(reservationListDTO, pharmacyService, patientService) ? "Uspesno napravljeno rezervacija" : "Neuspesno nenapravljena rezervacija", HttpStatus.OK);
    }

    @RequestMapping(value = "/api/reservations/cancel/{id}",
            method = RequestMethod.GET)
    @PreAuthorize("hasRole('PATIENT')")
    public Boolean cancelReservation(@PathVariable Long id) {
        return reservationService.cancelReservation(id);
    }

    @RequestMapping(value = "/api/reservations/getAll",
            method = RequestMethod.GET)
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<ArrayList<ReservationListDTO>> getAll(@RequestParam String username) {
        return new ResponseEntity<ArrayList<ReservationListDTO>>(reservationService.getAll(username), HttpStatus.OK);
    }

    @RequestMapping(
            value = "/api/reservations/search",
            method = RequestMethod.GET
    )
    @PreAuthorize("hasRole('PHARMACIST')")
    public ResponseEntity<?> search(@RequestParam String reservationId, @RequestParam String username) {
        return new ResponseEntity<>(reservationService.getForIssuing(reservationId, username), HttpStatus.OK);
    }

    @RequestMapping(
            value = "/api/reservations/issue",
            method = RequestMethod.GET
    )
    @PreAuthorize("hasRole('PHARMACIST')")
    public ResponseEntity<?> issue(@RequestParam String reservationId, @RequestParam String username) {
        return new ResponseEntity<>(reservationService.issue(reservationId, username), HttpStatus.OK);
    }

}
