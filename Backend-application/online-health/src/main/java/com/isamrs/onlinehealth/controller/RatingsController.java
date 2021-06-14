package com.isamrs.onlinehealth.controller;

import com.isamrs.onlinehealth.dto.PatientsRatingDTO;
import com.isamrs.onlinehealth.service.RatingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class RatingsController {

    @Autowired
    private RatingsService ratingsService;

    @RequestMapping(value = "/api/ratings", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<ArrayList<PatientsRatingDTO>> getRatingsForPatient(@RequestParam String username){
        return new ResponseEntity<ArrayList<PatientsRatingDTO>>(ratingsService.getRatings(username), HttpStatus.OK);
    }

}
