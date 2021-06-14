package com.isamrs.onlinehealth.controller;

import com.isamrs.onlinehealth.dto.DermatologistDTO;
import com.isamrs.onlinehealth.dto.DermatologistSearchDTO;
import com.isamrs.onlinehealth.dto.ExaminationDTO;
import com.isamrs.onlinehealth.dto.NewLeaveRequestDTO;
import com.isamrs.onlinehealth.model.Dermatologist;
import com.isamrs.onlinehealth.model.USER_TYPE;
import com.isamrs.onlinehealth.service.DermatologistService;
import com.isamrs.onlinehealth.service.EmailService;
import com.isamrs.onlinehealth.service.RoleService;
import com.isamrs.onlinehealth.service.UserService;
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
import java.util.Random;
import java.util.stream.Collectors;



@RestController

public class DermatologistController {

    @Autowired
    private final DermatologistService dermatologistService;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private RoleService roleService;


    public DermatologistController(DermatologistService dermatologistService) {
        this.dermatologistService = dermatologistService;
    }

    @RequestMapping(value = "/api/dermatologists", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DermatologistDTO> updateDermatologist(@RequestBody DermatologistDTO dermatologistDTO){
        Dermatologist dermatologist = dermatologistService.findOneByUsername(dermatologistDTO.getUsername());

        if(dermatologist == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        dermatologist.setFirstName(dermatologistDTO.getFirstName());
        dermatologist.setLastName(dermatologistDTO.getLastName());
        dermatologist.setAddress(dermatologistDTO.getAddress());
        dermatologist.setCity(dermatologistDTO.getCity());
        dermatologist.setCountry(dermatologistDTO.getState());
        dermatologist.setPhoneNumber(dermatologistDTO.getPhoneNumber());


        dermatologist = dermatologistService.save(dermatologist);

        return new ResponseEntity<>(new DermatologistDTO(dermatologist), HttpStatus.OK);
    }

    @RequestMapping(value = "/api/dermatologists/{username}/examinations", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('DERMATOLOGIST')")
    public ResponseEntity<ArrayList<ExaminationDTO>> getAllExaminations(@PathVariable String username){
        Dermatologist dermatologist = dermatologistService.findOneByUsernameFetchExaminations(username);

        if(dermatologist == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(
                new ArrayList<>(dermatologist.getExaminations().stream()
                        .map(e -> new ExaminationDTO(e.getId(), e.getStart(), e.getEnd(), e.getReport(), e.getPrice(), e.getPatient() == null ? " " : e.getPatient().getFirstName() + " " + e.getPatient().getLastName(), e.getPharmacy().getId(), e.getPharmacy().getName()))
                        .collect(Collectors.toList())), HttpStatus.OK);

    }


    @RequestMapping(value = "/api/registrationDermatologist", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    public ResponseEntity<String> registerDermatologist(@RequestBody @Valid DermatologistDTO dermatologistDTO){
        Dermatologist dermatologist;

        if(userService.findByUsername(dermatologistDTO.getUsername()) != null){
            return ResponseEntity.ok().body("Username " + dermatologistDTO.getUsername() + " je već u upotrebi!");
        }

        if(userService.findByEmail(dermatologistDTO.getEmail()) != null){
            return ResponseEntity.ok().body("Email " + dermatologistDTO.getEmail() + " je već u upotrebi!");
        }
        dermatologist  = new Dermatologist();
        dermatologist.setUsername(dermatologistDTO.getUsername());
        dermatologist.setUser(USER_TYPE.DERMATOLOGIST);
        dermatologist.setEmail(dermatologistDTO.getEmail());
        dermatologist.setDeleted(false);
        dermatologist.setPasswordChanged(false);
        dermatologist.setAddress(dermatologistDTO.getAddress());
        dermatologist.setCity(dermatologistDTO.getCity());
        dermatologist.setCountry(dermatologistDTO.getState());
        dermatologist.setFirstName(dermatologistDTO.getFirstName());
        dermatologist.setLastName(dermatologistDTO.getLastName());
        dermatologist.setPhoneNumber(dermatologistDTO.getPhoneNumber());

        dermatologist.setPassword("dermatolog" +(new Random()).nextInt(50));
        dermatologist.setRoles(roleService.findByName("ROLE_DERMATOLOGIST"));
        dermatologist.setEnabled(true);

        try {
            emailService.sendEmailAsync("", dermatologist.getEmail(), "Registracija", "Dobar dan,\n" +
                    "Registrovani ste na portalu OnlineHealth, sajtu možete pristupiti vašom email-adresom ili korisničkim imenom: " + dermatologist.getUsername() +
                    ", i lozinkom: " + dermatologist.getPassword() + "\nSrdačan pozdrav,\nAdmin informacionog sistema OnlineHealth");
        }catch (Exception e){
            return ResponseEntity.ok().body("Mailovi nisu poslati dermatolozima.");
        }
        if(dermatologistService.register(dermatologist) == null){
            return ResponseEntity.ok().body("Došlo je do greške prilikom dodavanja.");
        }
        return ResponseEntity.ok().body("Dermatolog uspepšno dodat u sistem.");
    }

    @PreAuthorize("hasRole('PHARMACY_ADMIN')")
    @RequestMapping(value = "/api/dermatologists/getAllDermatologistsForPharmacy", method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ArrayList<DermatologistDTO>> getAllDermatologists(@RequestParam String username){
        ArrayList<DermatologistDTO> result =dermatologistService.getAllDermatologistsForPharmacy(username);
        return new ResponseEntity<>(result,HttpStatus.OK);

    }

    @PreAuthorize("hasRole('PHARMACY_ADMIN')")
    @RequestMapping(value = "/api/dermatologists/dermatologistSearch",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ArrayList<DermatologistDTO>> dermatologistSearch(@RequestParam String name, @RequestParam String surname){
        ArrayList<DermatologistDTO> result = dermatologistService.search(name, surname);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PHARMACY_ADMIN')")
    @RequestMapping(value = "/api/dermatologist/checkWorkTime", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> checkWorkTime(@RequestParam long dermId, @RequestParam String vremeOd, @RequestParam String vremeDo)
    {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime t1,t2;
        try {
            t1 = LocalTime.parse(vremeOd, dateTimeFormatter);
            t2 = LocalTime.parse(vremeDo, dateTimeFormatter);
        }catch (Exception e){
            return (new ResponseEntity<>(false , HttpStatus.OK));
        }

        Boolean available = dermatologistService.checkWorkTime(dermId,t1,t2);
        return (new ResponseEntity<>(available , HttpStatus.OK));

    }

    @PreAuthorize("hasRole('PHARMACY_ADMIN')")
    @RequestMapping(value = "/api/dermatologist/addToPharmacy", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DermatologistDTO> addDermatologistToPharmacy(@RequestParam String username, @RequestParam long dermId, @RequestParam String vremeOd, @RequestParam String vremeDo){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime t1,t2;
        try {
            t1 = LocalTime.parse(vremeOd, dateTimeFormatter);
            t2 = LocalTime.parse(vremeDo, dateTimeFormatter);
        }catch (Exception e){
            return (new ResponseEntity<DermatologistDTO>(new DermatologistDTO() , HttpStatus.OK));
        }
        return new ResponseEntity<DermatologistDTO>(dermatologistService.addToPharmacy(username,dermId,t1,t2),HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PHARMACY_ADMIN')")
    @RequestMapping(value = "/api/dermatologist/deleteFromPharmacy", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DermatologistDTO> addDermatologistToPharmacy(@RequestParam String username, @RequestParam long dermId){

        return new ResponseEntity<DermatologistDTO>(dermatologistService.deleteFromPharmacy(username,dermId),HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PHARMACY_ADMIN')")
    @RequestMapping(value = "/api/dermatologist/addApointment", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> addApointment(@RequestParam long dermId, @RequestParam String datum, @RequestParam LocalTime vremeOd, @RequestParam LocalTime vremeDo, @RequestParam double cena){

        LocalDate realDate = LocalDate.parse(datum,DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return new ResponseEntity<>(dermatologistService.addApointment(dermId,vremeOd,vremeDo,realDate,cena),HttpStatus.OK);
    }

    @RequestMapping(value = "/api/dermatologist/getAllForSearch", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ArrayList<DermatologistSearchDTO>> getAllDermatologistsForSearch()
    {
        ArrayList<DermatologistSearchDTO> result = dermatologistService.getAllDermatologistsForSearch();
        return new ResponseEntity<ArrayList<DermatologistSearchDTO>>(result,HttpStatus.OK);
    }

    @RequestMapping(value = "/api/dermatologist/listOfAll/search", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ArrayList<DermatologistSearchDTO>> searchAllDermatologistsForSearch(@RequestParam String name, @RequestParam String surname)
    {
        ArrayList<DermatologistSearchDTO> result = dermatologistService.searchByNameAndSurname(name,surname);
        return new ResponseEntity<ArrayList<DermatologistSearchDTO>>(result,HttpStatus.OK);
    }

    @RequestMapping(value = "/api/dermatologist/setRating", method = RequestMethod.GET)
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<Boolean> setRating(@RequestParam String patient,
                                             @RequestParam String dermatologist,
                                             @RequestParam Double rating) {
        return new ResponseEntity<Boolean>(dermatologistService.setRating(patient, dermatologist, rating), HttpStatus.OK);
    }

    @RequestMapping(value = "/api/dermatologist/getLeaveRequests", method = RequestMethod.GET)
    @PreAuthorize("hasRole('DERMATOLOGIST')")
    public ResponseEntity<?> getAllLeaveRequestsForDermatologist(@RequestParam String username) {
        return new ResponseEntity<>(dermatologistService.getAllLeaveRequests(username), HttpStatus.OK);
    }

    @RequestMapping(value = "/api/dermatologist/newLeaveRequest", method = RequestMethod.POST)
    @PreAuthorize("hasRole('DERMATOLOGIST')")
    public ResponseEntity<?> newLeaveRequestDermatologist(@RequestBody NewLeaveRequestDTO newLeaveRequestDTO) {
        if (dermatologistService.newLeaveRequest(newLeaveRequestDTO)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/api/dermatologist/cancelLeaveRequest", method = RequestMethod.GET)
    @PreAuthorize("hasRole('DERMATOLOGIST')")
    public ResponseEntity<?> cancelLeaveRequestDermatologist(@RequestParam Long id,
                                                             @RequestParam String username) {
        if (dermatologistService.cancelLeaveRequest(id, username)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


}
