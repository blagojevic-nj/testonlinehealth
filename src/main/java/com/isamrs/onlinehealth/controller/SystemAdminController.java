package com.isamrs.onlinehealth.controller;

import com.isamrs.onlinehealth.dto.*;
import com.isamrs.onlinehealth.model.*;
import com.isamrs.onlinehealth.service.EmailService;
import com.isamrs.onlinehealth.service.LoyaltyProgramService;
import com.isamrs.onlinehealth.service.SystemAdminService;
import com.isamrs.onlinehealth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
public class SystemAdminController {
    @Autowired
    SystemAdminService systemAdminService;
    @Autowired
    EmailService emailService;
    @Autowired
    UserService userService;
    @Autowired
    LoyaltyProgramService loyaltyProgramService;

    @RequestMapping(value = "/api/registrationAdmin", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    public ResponseEntity<String> registerSystemAdmin(@RequestBody @Valid SystemAdminDTO systemAdminDTO) {
        SystemAdmin systemAdmin;
        if(userService.findByUsername(systemAdminDTO.getUsername())!=null || userService.findByEmail(systemAdminDTO.getEmail())!=null)
            return ResponseEntity.ok().body("Administrator sa unetim kredencijalima već postoji u sistemu.");
        String password = "sistemskiAdmin" + (new Random()).nextInt();
        systemAdmin = new SystemAdmin(null, systemAdminDTO.getUsername(), systemAdminDTO.getEmail(), password, systemAdminDTO.getFirstName(), systemAdminDTO.getLastName(), systemAdminDTO.getAddress(), systemAdminDTO.getCity(), systemAdminDTO.getState(), systemAdminDTO.getPhoneNumber(), USER_TYPE.SYSTEM_ADMIN, false, new HashSet<Complaint>(), new LoyaltyProgram());

            try {
                emailService.sendEmailAsync("", systemAdmin.getEmail(), "Registracija", "Dobar dan,\n" +
                        "Registrovani ste na portalu OnlineHealth, sajtu možete pristupiti vašom email-adresom ili korisničkim imenom: " + systemAdmin.getUsername() +
                        ", i lozinkom: " + systemAdmin.getPassword() + "\nSrdačan pozdrav,\nAdmin informacionog sistema OnlineHealth");
            }catch (Exception e){
                return ResponseEntity.ok().body("Mail nije poslat administratoru.");
            }
        systemAdmin.setEnabled(true);
        try {
            emailService.sendEmailAsync("", systemAdmin.getEmail(), "Registracija", "Dobar dan,\n" +
                    "Registrovani ste na portalu OnlineHealth, sajtu možete pristupiti vašom email-adresom ili korisničkim imenom: " + systemAdmin.getUsername() +
                    ", i lozinkom: " + systemAdmin.getPassword() + "\nSrdačan pozdrav,\nAdmin informacionog sistema OnlineHealth");
        }catch (Exception e){
            return ResponseEntity.ok().body("Mail nije poslat administratoru.");
        }
        if(systemAdminService.save(systemAdmin)!=null) {
            return ResponseEntity.ok().body("Administrator uspešno dodat u sistem.");
        }
        return  ResponseEntity.ok().body("Došlo je do greške prilikom dodavanja administratora u sistem.");
    }

    @RequestMapping(value = "/api/getLoyaltyProgram", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    public ResponseEntity<LoyaltyProgramDTO> getLoyaltyProgram(){
        List<LoyaltyProgram> loyaltyPrograms = loyaltyProgramService.getLoyaltyPrograms();
        if(loyaltyPrograms.isEmpty())
            return ResponseEntity.ok(new LoyaltyProgramDTO());
        LoyaltyProgram loyaltyProgram = loyaltyPrograms.get(0);
        LoyaltyProgramDTO loyaltyProgramDTO = new LoyaltyProgramDTO();
        loyaltyProgramDTO.setId(loyaltyProgram.getId().toString());
        loyaltyProgramDTO.setConsultationPoints(loyaltyProgram.getConsultingPoints().toString());
        loyaltyProgramDTO.setExaminationPoints(loyaltyProgram.getExaminationPoints().toString());
        List<LoyaltyMedicinePointsDTO> tmp = new ArrayList<>();
        for(Map.Entry<Long, Double> medicinePoints : loyaltyProgramService.getMedicinePoints(loyaltyProgram.getId()).entrySet()){
            LoyaltyMedicinePointsDTO loyaltyMedicinePointsDTO = new LoyaltyMedicinePointsDTO(medicinePoints.getKey().toString(), medicinePoints.getValue().toString());
            tmp.add(loyaltyMedicinePointsDTO);
        }
        loyaltyProgramDTO.setMedicinePoints(tmp);
        List<LoyaltyCategoryDTO> tmp2 = new ArrayList<>();
        for(LoyaltyCategory loyaltyCategory : loyaltyProgramService.getLoyaltyCategories(loyaltyProgram.getId())){
            Double dr = loyaltyCategory.getDiscountRate();
            LoyaltyCategoryDTO loyaltyCategoryDTO = new LoyaltyCategoryDTO(loyaltyCategory.getId().toString(), loyaltyCategory.getName(), loyaltyCategory.getLowLimit().toString(), loyaltyCategory.getHighLimit().toString(), dr.toString());
            tmp2.add(loyaltyCategoryDTO);
        }
        loyaltyProgramDTO.setLoyaltyCategories(tmp2);
        return ResponseEntity.ok(loyaltyProgramDTO);
    }

    @RequestMapping(value = "/api/addLoyaltyProgram", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    public ResponseEntity<String> addLoyaltyProgram(@RequestBody LoyaltyProgramDTO loyaltyProgramDTO){
        LoyaltyProgram loyaltyProgram = new LoyaltyProgram();
        if(loyaltyProgramDTO.getId() != null)
            if(!loyaltyProgramDTO.getId().equals(""))
            loyaltyProgram.setId(Long.parseLong(loyaltyProgramDTO.getId()));
        else
            loyaltyProgram = loyaltyProgramService.saveProgram(loyaltyProgram);
        loyaltyProgram.setConsultingPoints(Double.parseDouble(loyaltyProgramDTO.getConsultationPoints()));
        loyaltyProgram.setExaminationPoints(Double.parseDouble(loyaltyProgramDTO.getExaminationPoints()));
        Map<Long, Double> map = new HashMap<>();
        for(LoyaltyMedicinePointsDTO l : loyaltyProgramDTO.getMedicinePoints()){
            map.put(Long.parseLong(l.getId()), Double.parseDouble(l.getPoints()));
        }
        loyaltyProgram =loyaltyProgramService.saveProgram(loyaltyProgram);
        loyaltyProgram = loyaltyProgramService.setMedicinePoints(loyaltyProgram.getId(),map);
        if(loyaltyProgramDTO.getLoyaltyCategories() != null)
        for(LoyaltyCategoryDTO l : loyaltyProgramDTO.getLoyaltyCategories()){
            LoyaltyCategory loyaltyCategory = new LoyaltyCategory();
            if(l.getId() != null)
                if(!l.getId().equals(""))
                loyaltyCategory.setId(Long.parseLong(l.getId()));
            loyaltyCategory.setName(l.getName());
            loyaltyCategory.setLowLimit(Double.parseDouble(l.getLowLimit()));
            loyaltyCategory.setHighLimit(Double.parseDouble(l.getHighLimit()));
            loyaltyCategory.setDiscountRate(Double.parseDouble(l.getDiscountRate()));
            loyaltyProgramService.saveCategory(loyaltyCategory);
        }
        loyaltyProgram = loyaltyProgramService.setCategories(loyaltyProgram.getId());
        loyaltyProgram =loyaltyProgramService.saveProgram(loyaltyProgram);
        return ResponseEntity.ok("");
    }

    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    @RequestMapping(value = "/api/pharmacy/getAllLeaveRequestsSystemAdmin", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<LeaveRequestDTO>> getAllLeaveRequests()
    {
        return new ResponseEntity<List<LeaveRequestDTO>>(systemAdminService.getLeaveRequests(), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    @RequestMapping(value = "/api/pharmacy/declineLeaveRequestSystemAdmin",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Boolean> declineLeaveRequest(@RequestParam long id, @RequestBody String poruka)
    {
        return new ResponseEntity<Boolean>(systemAdminService.declineLeaveRequest(id, poruka), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    @RequestMapping(value = "/api/pharmacy/acceptLeaveRequestSystemAdmin", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public  ResponseEntity<Boolean> acceptLeaveRequest(@RequestParam long id)
    {
        return new ResponseEntity<Boolean>(systemAdminService.acceptLeaveRequest(id), HttpStatus.OK);
    }
}
