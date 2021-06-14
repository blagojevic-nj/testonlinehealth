package com.isamrs.onlinehealth.controller;

import com.isamrs.onlinehealth.dto.ComplaintToDTO;
import com.isamrs.onlinehealth.dto.PatientDTO;
import com.isamrs.onlinehealth.dto.StatsInfoDTO;
import com.isamrs.onlinehealth.model.*;
import com.isamrs.onlinehealth.service.EmailService;
import com.isamrs.onlinehealth.service.PatientService;
import com.isamrs.onlinehealth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class PatientController {

    @Autowired
    private PatientService patientService;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @RequestMapping(value = "/api/patients/search", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> searchByFirstNameAndLastName(@RequestParam(value = "firstName", defaultValue = "") String firstName, @RequestParam(value = "lastName", defaultValue = "") String lastName) {
        return new ResponseEntity<>(
                patientService.searchByFirstNameLastName(firstName, lastName)
                        .stream()
                        .collect(Collectors.toMap(Patient::getId, patient -> patient.getFirstName() + " " + patient.getLastName()))
                , HttpStatus.OK);
    }


    @RequestMapping(value = "api/patients/examined", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('DERMATOLOGIST')")
    public ResponseEntity<?> searchExamined(@RequestParam(value = "username", defaultValue = "") String username,
                                            @RequestParam(value = "firstName", defaultValue = "") String firstName,
                                            @RequestParam(value = "lastName", defaultValue = "") String lastName) {
        return new ResponseEntity<>(
                new ArrayList<>(patientService.getExamined(username, firstName, lastName)), HttpStatus.OK
        );
    }

    @RequestMapping(value = "api/patients/consulted", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('PHARMACIST')")
    public ResponseEntity<?> searchConsulted(@RequestParam(value = "username", defaultValue = "") String username,
                                             @RequestParam(value = "firstName", defaultValue = "") String firstName,
                                             @RequestParam(value = "lastName", defaultValue = "") String lastName) {
        return new ResponseEntity<>(
                new ArrayList<>(patientService.getConsulted(username, firstName, lastName)), HttpStatus.OK
        );
    }


    @RequestMapping(value = "/api/registration", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('')")
    public ResponseEntity<String> registerNewPatient(@RequestParam("confirmPath") String path, @RequestBody @Valid PatientDTO patientDTO, HttpServletRequest request) {
        Patient registeredPatient = new Patient();

        String email = patientDTO.getEmail();

        if (userService.findByUsername(patientDTO.getUsername()) != null) {
            return ResponseEntity.ok().body("Username " + patientDTO.getUsername() + " je već u upotrebi!");
        }

        if(userService.findByEmail(email)!=null){
            return ResponseEntity.ok().body("Email " + patientDTO.getEmail() + " je već u upotrebi!");
        }

        if(!patientDTO.getPassword().equals(patientDTO.getMatchingPassword())){
            return ResponseEntity.ok().body("Password-i se ne poklapaju!");
        }
        String token = UUID.randomUUID().toString();
        try {
            String appUrl = path + "/api/confirmRegistration?token=" + token;
            String message = "Hvala vam što ste odabrali našu aplikaciju!\n Kliknite na link ispod da aktivirate vaš nalog.\n\n" + appUrl;
            String from = "";
            String to = patientDTO.getEmail();
            String subject = "Registracija";
            emailService.sendEmailAsync(from,to,subject,message);
        }catch (Exception re){
            return ResponseEntity.ok().body("Slanje email poruke nije uspelo.");
        }
        registeredPatient = patientService.registerPatient(patientDTO);
        patientService.createVerificationToken(registeredPatient, token);
        return ResponseEntity.ok().body("Poslat vam je email na vašu adresu!");
    }

    @RequestMapping(value = "/api/confirmRegistration", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('')")
    public ResponseEntity<String> confirmRegistration(@RequestParam("token") String token) throws Exception {
        VerificationToken verificationToken = patientService.getVerificationToken(token);
        if(verificationToken == null){
            return ResponseEntity.ok().body("Verifikacioni token nije validan.");
        }

        Patient patient = verificationToken.getPatient();
        Calendar calendar = Calendar.getInstance();
        if((verificationToken.getExpiry_date().getTime() - calendar.getTime().getTime()) <= 0){
            return ResponseEntity.ok().body("Aktivacioni link je istekao! Molimo vas da se registrujete ponovo.");
        }

        patient.setEnabled(true);
        patientService.save(patient);
        return ResponseEntity.ok().body("Registracija uspešna!");
    }

    @RequestMapping(value = "/api/getComplaintItems", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<ComplaintToDTO> getComplaintItems(@RequestParam("username") String username){
        Patient patient = patientService.findByUsername(username);
        ComplaintToDTO complaintToDTO = new ComplaintToDTO();
        Set<String> items = new HashSet<>();
        items.addAll(patientService.getConsultations(username));
        items.addAll(patientService.getExaminations(username));
        items.addAll(patientService.getReservations(username));
        complaintToDTO.setItems(new ArrayList<>(items));
        return ResponseEntity.ok().body(complaintToDTO);
    }

    @RequestMapping(value = "/api/getPenalties", method = RequestMethod.GET)
    @PreAuthorize("hasRole('PATIENT')")
    public  ResponseEntity<Double> getPenalties(@RequestParam("username") String username){
        return new ResponseEntity<Double>(patientService.getPenalties(username), HttpStatus.OK);
    }

    @RequestMapping(value = "/api/getLoyaltyStats", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('PATIENT')")
    public  ResponseEntity<StatsInfoDTO> getLoyaltyStats(@RequestParam("username") String username){
        return new ResponseEntity<StatsInfoDTO>(patientService.getStats(username), HttpStatus.OK);
    }

}
