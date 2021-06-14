package com.isamrs.onlinehealth.controller;

import com.isamrs.onlinehealth.dto.EqrPharmacyDTO;
import com.isamrs.onlinehealth.dto.EqrPharmacyPriceListDTO;
import com.isamrs.onlinehealth.dto.EqrPrescriptionDTO;
import com.isamrs.onlinehealth.dto.ExaminationDTO;
import com.isamrs.onlinehealth.model.Patient;
import com.isamrs.onlinehealth.service.EPrescriptionService;
import com.isamrs.onlinehealth.service.EmailService;
import com.isamrs.onlinehealth.service.PatientService;
import com.isamrs.onlinehealth.service.PharmacyService;
import com.isamrs.onlinehealth.utils.QRCodeGen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class EPrescriptionController {
    @Autowired
    EPrescriptionService ePrescriptionService;
    @Autowired
    PatientService patientService;
    @Autowired
    EmailService emailService;
    @Autowired
    PharmacyService pharmacyService;

    @RequestMapping(value = "/api/qrCodeUpload", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<EqrPrescriptionDTO> getCompleteEPrescription(@RequestParam("qrPath") String qrPath, @RequestParam("username") String username){
        String fullPath = "src/main/java/com/isamrs/onlinehealth/public/" + qrPath;
        String response = QRCodeGen.read(fullPath);
        EqrPrescriptionDTO eqrPrescriptionDTO = new EqrPrescriptionDTO();
        if(response.equals("error"))
            return ResponseEntity.badRequest().body(eqrPrescriptionDTO);
        eqrPrescriptionDTO = eqrPrescriptionDTO.deserialize(response);
        if(!eqrPrescriptionDTO.getUsername().equals(username))
            return ResponseEntity.badRequest().body(new EqrPrescriptionDTO());
        return ResponseEntity.ok().body(eqrPrescriptionDTO.deserialize(response));
    }

    @RequestMapping(value = "/api/qrSearchResults", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<List<EqrPharmacyDTO>> getSearchResults(@RequestBody EqrPrescriptionDTO qrID){
        return ResponseEntity.ok().body(ePrescriptionService.retPharmacySearchResults(qrID.getId()));
    }

    @RequestMapping(value = "/api/acceptPrescription", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<?> acceptPrescription(@RequestBody EqrPharmacyDTO pharmacyDTO, @RequestParam("username")String username, @RequestParam("id")Long id){
        ePrescriptionService.changeStatus(id);
        ePrescriptionService.subtractMedicine(pharmacyDTO);
        String from = "";
        Patient patient = patientService.findByUsername(username);
        String to =patient.getEmail();
        String subject = "E-Recept, Potvrda porudžbine";
        StringBuilder message = new StringBuilder("Poštovani,\nHvala vam na porudžbini u Online Health, u prilogu se nalazi vaš račun," +
                "\nSve najbolje!\nOnline Health\n\n");
        message.append("======================================================\n");
        message.append("Račun u apoteci ").append(pharmacyDTO.getName()).append(":\n").append("======================================================\n");
        for(EqrPharmacyPriceListDTO item : pharmacyDTO.getPrices()){
            message.append(item.getName()).append("     ").append(item.getAmount()).append(" x ").append(item.getCost()).append("RSD\n");
        }
        message.append("======================================================\n");
        message.append("Ukupna cena: ").append(pharmacyDTO.getTotalCost()).append("RSD\n");
        message.append("======================================================\n");
        try {
            emailService.sendEmailAsync(from,to,subject,message.toString());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("");
        }
        return ResponseEntity.ok().body("");
    }

    @RequestMapping(value = "/api/ePrescriptions/getAll", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<ArrayList<EqrPrescriptionDTO>> getAllForPatient(@RequestParam("username")String username){
        return new ResponseEntity<ArrayList<EqrPrescriptionDTO>>(ePrescriptionService.getAll(username), HttpStatus.OK);
    }
}
