package com.isamrs.onlinehealth.controller;

import com.isamrs.onlinehealth.dto.ConsultationDTO;
import com.isamrs.onlinehealth.dto.NewLeaveRequestDTO;
import com.isamrs.onlinehealth.dto.PharmacistDTO;
import com.isamrs.onlinehealth.model.Pharmacist;
import com.isamrs.onlinehealth.service.PharmacistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.stream.Collectors;

@RestController
public class PharmacistController {
    @Autowired
    PharmacistService pharmacistService;

    public PharmacistController(PharmacistService pharmacistService) {
        this.pharmacistService = pharmacistService;
    }

    public PharmacistController() {
    }

    @RequestMapping(value = "/api/pharmacists/{username}/consultations", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('PHARMACIST')")
    public ResponseEntity<ArrayList<ConsultationDTO>> getAllConsultations(@PathVariable String username) {
        Pharmacist pharmacist = pharmacistService.findOneByUsernameFetchConsultations(username);

        if (pharmacist == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(
                new ArrayList<>(pharmacist.getConsultations().stream()
                        .map(e -> new ConsultationDTO(e.getId(), e.getStart(), e.getEnd(), e.getReport(), e.getPatient().getFirstName() + " " + e.getPatient().getLastName(), pharmacist.getFirstName() + " " + pharmacist.getLastName(), e.getPrice(), e.getPharmacy().getId().toString(), e.getPharmacy().getName()))
                        .collect(Collectors.toList())), HttpStatus.OK);

    }
    @PreAuthorize("hasRole('PHARMACY_ADMIN')")
    @RequestMapping(value = "/api/pharmacists/getAllPharmacistsForPharmacy", method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ArrayList<PharmacistDTO>> getAllPharmacists(@RequestParam String username){
        ArrayList<PharmacistDTO> result =pharmacistService.getAllPharmacistsForPharmacy(username);
        return new ResponseEntity<>(result, HttpStatus.OK);

    }

    @PreAuthorize("hasRole('PHARMACY_ADMIN')")
    @RequestMapping(value = "/api/pharmacists/search", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ArrayList<PharmacistDTO>> searchAllPharmacistsForPharmacy(@RequestParam String username, @RequestParam String name, @RequestParam String surname)
    {
        ArrayList<PharmacistDTO> result = pharmacistService.searchByNameAndSurname(username, name, surname);
        return new ResponseEntity<ArrayList<PharmacistDTO>>(result,HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PHARMACY_ADMIN')")
    @RequestMapping(value = "/api/pharmacists/deleteFromPharmacy", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ArrayList<PharmacistDTO>> deletePharmacistFromPharmacy(@RequestParam String username, @RequestParam long pharmacistId){

        return new ResponseEntity<ArrayList<PharmacistDTO>>(pharmacistService.deleteFromPharmacy(username,pharmacistId),HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PHARMACY_ADMIN')")
    @RequestMapping(value = "/api/pharmacists/addToPharmacy", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ArrayList<PharmacistDTO>> addPharmacistToPharmacy(@RequestParam String username, @RequestParam long pharmacistId){

        return new ResponseEntity<ArrayList<PharmacistDTO>>(pharmacistService.addToPharmacy(username,pharmacistId),HttpStatus.OK);
    }


    @RequestMapping(value = "/api/pharmacists/getAllForSearch", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ArrayList<PharmacistDTO>> getAllPharmacistsForSearch()
    {
        ArrayList<PharmacistDTO> result = pharmacistService.getAllPharmacistsForSearch();
        return new ResponseEntity<ArrayList<PharmacistDTO>>(result,HttpStatus.OK);
    }

    @RequestMapping(value = "/api/pharmacists/listOfAll/search", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ArrayList<PharmacistDTO>> searchAllPharmacistsForSearch(@RequestParam String name, @RequestParam String surname)
    {
        ArrayList<PharmacistDTO> result = pharmacistService.searchByNameAndSurname(name,surname);
        return new ResponseEntity<ArrayList<PharmacistDTO>>(result,HttpStatus.OK);
    }

    @RequestMapping(value = "/api/pharmacists/setRating", method = RequestMethod.GET)
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<Boolean> setRating(@RequestParam String patient,
                                             @RequestParam String pharmacist,
                                             @RequestParam Double rating) {
        return new ResponseEntity<Boolean>(pharmacistService.setRating(patient, pharmacist, rating), HttpStatus.OK);
    }

    @RequestMapping(value = "/api/pharmacist/getLeaveRequests", method = RequestMethod.GET)
    @PreAuthorize("hasRole('PHARMACIST')")
    public ResponseEntity<?> getAllLeaveRequestsForPharmacist(@RequestParam String username) {
        return new ResponseEntity<>(pharmacistService.getAllLeaveRequests(username), HttpStatus.OK);
    }

    @RequestMapping(value = "/api/pharmacist/newLeaveRequest", method = RequestMethod.POST)
    @PreAuthorize("hasRole('PHARMACIST')")
    public ResponseEntity<?> newLeaveRequestPharmacist(@RequestBody NewLeaveRequestDTO newLeaveRequestDTO) {
        if (pharmacistService.newLeaveRequest(newLeaveRequestDTO)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/api/pharmacist/cancelLeaveRequest", method = RequestMethod.GET)
    @PreAuthorize("hasRole('PHARMACIST')")
    public ResponseEntity<?> cancelLeaveRequestPharmacist(@RequestParam Long id,
                                                          @RequestParam String username) {
        if (pharmacistService.cancelLeaveRequest(id, username)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


}
