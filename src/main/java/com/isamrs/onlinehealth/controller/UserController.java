package com.isamrs.onlinehealth.controller;

import com.isamrs.onlinehealth.dto.*;
import com.isamrs.onlinehealth.model.*;
import com.isamrs.onlinehealth.service.ComplaintService;
import com.isamrs.onlinehealth.service.PatientService;
import com.isamrs.onlinehealth.service.SystemAdminService;
import com.isamrs.onlinehealth.service.UserService;
import com.isamrs.onlinehealth.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;


@RestController
public class UserController {
    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    PatientService patientService;

    @Autowired
    UserService userService;

    @Autowired
    ComplaintService complaintService;

    @Autowired
    SystemAdminService systemAdminService;

    @RequestMapping(value = "/api/login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserTokenState> loginUser(@RequestBody UserLoginDTO userLoginDTO){
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    userLoginDTO.getUsername(), userLoginDTO.getPassword()));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(new UserTokenState());
        }
        // Ukoliko je autentifikacija uspesna, ubaci korisnika u trenutni security
        // kontekst
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = (User) authentication.getPrincipal();
        if (user.getUser() == USER_TYPE.PATIENT && ((Patient) user).getPenalties() == 3.0){
            return ResponseEntity.badRequest().body(new UserTokenState());
        }
        String jwt = tokenUtils.generateToken(user.getUsername());
        int expiresIn = tokenUtils.getExpiredIn();
        return ResponseEntity.ok(new UserTokenState(jwt, expiresIn));
    }

    @RequestMapping(value = "/api/getInfo" , method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('PATIENT', 'DERMATOLOGIST', 'PHARMACY_ADMIN', 'SYSTEM_ADMIN', 'PHARMACIST', 'SUPPLIER')")
    public ResponseEntity<UserDTO> getInfo(@RequestParam(name = "username", required = true) String username){
        User u = userService.findByUsername(username);
        if(u != null)
            return ResponseEntity.ok().body(new UserDTO(u.getUsername(), u.getEmail(), u.getPassword(), u.getFirstName(), u.getLastName(), u.getAddress(), u.getCountry(), u.getCity(), u.getUser().toString(), u.getPhoneNumber(), u.isPasswordChanged()));
        else return ResponseEntity.ok().body(new UserDTO());
    }

    @RequestMapping(value = "/api/passwordChange", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('DERMATOLOGIST', 'PHARMACY_ADMIN', 'SYSTEM_ADMIN', 'PHARMACIST', 'SUPPLIER', 'PATIENT')")
    public ResponseEntity<UserTokenState> changePassword(@RequestBody PasswordChangeDTO passwordChangeDTO){
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    passwordChangeDTO.getUsername(), passwordChangeDTO.getOldPassword()));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(new UserTokenState());
        }
        userService.changePassword(passwordChangeDTO.getUsername(), passwordChangeDTO.getNewPassword());
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    passwordChangeDTO.getUsername(), passwordChangeDTO.getNewPassword()));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(new UserTokenState());
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = (User) authentication.getPrincipal();
        String jwt = tokenUtils.generateToken(user.getUsername());
        int expiresIn = tokenUtils.getExpiredIn();
        return ResponseEntity.ok(new UserTokenState(jwt, expiresIn));
    }

    @RequestMapping(value = "/api/writeComplaint", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('PATIENT', 'SYSTEM_ADMIN')")
    public ResponseEntity<String> writeComplaint(@RequestBody ComplaintDTO complaintDTO){
        User u = userService.findByUsername(complaintDTO.getPatientItem());
        if(u.getUser() == USER_TYPE.PATIENT){
            Complaint complaint = new Complaint();
            complaint.setTo(complaintDTO.getTo());
            complaint.setComplaintText(complaintDTO.getComplaintText());
            complaint.setResponseText("");
            complaint.setDeleted(false);
            complaint.setDateComposed(LocalDateTime.now());
            patientService.addComplaint(complaint, u.getUsername());
            return ResponseEntity.ok().body("Žalba poslata!");
        }
        if(u.getUser() == USER_TYPE.SYSTEM_ADMIN){
            complaintService.setResponse(complaintDTO.getId(), complaintDTO.getResponseText(), LocalDateTime.now(), u.getUsername());
            systemAdminService.addResponseComplaint(complaintDTO.getId(), complaintDTO.getPatientItem());
            return ResponseEntity.ok().body("Odgovor na žalbu poslat!");
        }
        return ResponseEntity.badRequest().body("Slanje poruke nije uspešno!");
    }

    @RequestMapping(value = "/api/getComplaints", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('PATIENT', 'SYSTEM_ADMIN')")
    public ResponseEntity<AllComplaintsDTO> getComplaints(@RequestParam("username") String username){
        AllComplaintsDTO complaints = new AllComplaintsDTO();
        complaints.setComplaints(new ArrayList<>());
        User u = userService.findByUsername(username);
        Set<Complaint> allComplaints = new HashSet<>();
        if(u.getUser() == USER_TYPE.PATIENT){
            allComplaints = patientService.getComplaints(username);
            Set<Complaint> allComplaintsPatient = new HashSet<>();
            for (Complaint c: allComplaints) {
                if(!c.getDeleted())
                    allComplaintsPatient.add(c);
            }
            allComplaints = allComplaintsPatient;
        }
        if(u.getUser() == USER_TYPE.SYSTEM_ADMIN){
            Set<Complaint> allComplaintsSys = new LinkedHashSet<>(new ArrayList<>(complaintService.getAll()));
            for (Complaint c : allComplaintsSys){
                if(c.getAdminUsername()==null)
                    allComplaints.add(c);
                else{
                    if(c.getAdminUsername().equals(u.getUsername()) || c.getAdminUsername().equals(""))
                        allComplaints.add(c);
                }
            }
        }
        for (Complaint complaint: allComplaints) {
            ComplaintDTO complaintDTO = new ComplaintDTO();
            complaintDTO.setComplaintText(complaint.getComplaintText());
            complaintDTO.setDateComposed(complaint.getDateComposed().toString());
            if(complaint.getDateResponded()!=null)
                complaintDTO.setDateResponded(complaint.getDateResponded().toString());
            else
                complaintDTO.setDateResponded("");
            complaintDTO.setId(complaint.getId());
            complaintDTO.setResponseText(complaint.getResponseText());
            complaintDTO.setTo(complaint.getTo());
            complaintDTO.setPatientItem(complaint.getPatient().getUsername());
            complaints.getComplaints().add(complaintDTO);
        }
        return ResponseEntity.ok(complaints);
    }

    @RequestMapping(value = "/api/getComplaintsReplied", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    public ResponseEntity<AllComplaintsDTO> getComplaintsReplied(@RequestParam("username") String username){
        AllComplaintsDTO complaints = new AllComplaintsDTO();
        complaints.setComplaints(new ArrayList<>());
        Set<Complaint> allComplaints = systemAdminService.getComplaints(username);
        for (Complaint complaint: allComplaints) {
            ComplaintDTO complaintDTO = new ComplaintDTO();
            complaintDTO.setComplaintText(complaint.getComplaintText());
            complaintDTO.setDateComposed(complaint.getDateComposed().toString());
            if(complaint.getDateResponded()!=null)
                complaintDTO.setDateResponded(complaint.getDateResponded().toString());
            else
                complaintDTO.setDateResponded("");
            complaintDTO.setId(complaint.getId());
            complaintDTO.setResponseText(complaint.getResponseText());
            complaintDTO.setTo(complaint.getTo());
            complaintDTO.setPatientItem(complaint.getPatient().getUsername());
            complaints.getComplaints().add(complaintDTO);
        }
        return ResponseEntity.ok(complaints);
    }

    @RequestMapping(value = "/api/deleteComplaints", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<String> deleteComplaints(@RequestBody AllComplaintsDTO allComplaintsDTO){
        for (ComplaintDTO complaintDTO: allComplaintsDTO.getComplaints()) {
            complaintService.setDeleted(complaintDTO.getId());
        }
        return ResponseEntity.ok("Akcija uspešna!");
    }

    @RequestMapping(value = "/api/updateProfile" , method = RequestMethod.GET)
    @PreAuthorize("hasAnyRole('PATIENT', 'DERMATOLOGIST', 'PHARMACY_ADMIN', 'SYSTEM_ADMIN', 'PHARMACIST', 'SUPPLIER')")
    public ResponseEntity<Void> updateProfile(@RequestParam String username,
                                              @RequestParam String firstName,
                                              @RequestParam String lastName,
                                              @RequestParam String phoneNumber,
                                              @RequestParam String address,
                                              @RequestParam String city,
                                              @RequestParam String state){
        userService.updateProfile(username, firstName, lastName, phoneNumber, address,
                city, state);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
