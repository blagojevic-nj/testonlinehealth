package com.isamrs.onlinehealth.controller;

import com.isamrs.onlinehealth.dto.AddedMedicineDTO;
import com.isamrs.onlinehealth.dto.CompleteMedicineDTO;
import com.isamrs.onlinehealth.dto.MedicineDTO;
import com.isamrs.onlinehealth.dto.SearchItemMedicineDTO;
import com.isamrs.onlinehealth.model.Medicine;
import com.isamrs.onlinehealth.service.MedicineService;
import com.isamrs.onlinehealth.service.PharmacyAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class MedicineController {

    @Autowired
    private MedicineService medicineService;
    @Autowired
    PharmacyAdminService pharmacyAdminService;


    @RequestMapping(value = "/api/medicine/getAll",
            method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ArrayList<MedicineDTO>> getMedicines() {
        return new ResponseEntity<ArrayList<MedicineDTO>>(medicineService.getAllMedicines(), HttpStatus.OK);
    }

    @RequestMapping(value = "/api/medicine/getNonAllergic",
            method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('DERMATOLOGIST', 'PHARMACIST')")
    public ResponseEntity<ArrayList<MedicineDTO>> getMedicinesNonAllergic(@RequestParam(name = "patientId") Long patientId) {
        return new ResponseEntity<ArrayList<MedicineDTO>>(medicineService.getAllMedicinesNonAllergic(patientId), HttpStatus.OK);
    }


    @RequestMapping(value = "/api/medicine",
            method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ArrayList<MedicineDTO>> getMedicinesByName(@RequestParam String name) {
        return new ResponseEntity<ArrayList<MedicineDTO>>(medicineService.getMedicinesByName(name), HttpStatus.OK);
    }

    @RequestMapping(value = "/api/medicine/{id}",
            method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MedicineDTO> getMedicinesById(@PathVariable Long id) {
        return new ResponseEntity<MedicineDTO>(medicineService.getMedicineById(id), HttpStatus.OK);
    }


    @RequestMapping(value = "/api/getCompleteMedicine",
            method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CompleteMedicineDTO> getCompleteMedicine(@RequestParam String identifier)
    {
        return new ResponseEntity<CompleteMedicineDTO>(
                medicineService.getCompleteMedicineDTO(identifier),HttpStatus.OK
        );
    }

    @RequestMapping(value = "/api/createNewMedicine",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            headers="Accept=application/json")
    public ResponseEntity<Void> createNewMedicine(@RequestBody Medicine m)
    {

             if(medicineService.isCreateAvailable(m))
        {
            System.out.println("Proslo");
            medicineService.createNewMedicine(m);
            System.out.println("jupiiii");

        }
        else{
            return null;
        }

        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }

    @RequestMapping(value = "/api/addMedicine", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    public ResponseEntity<String> addMedicine(@RequestBody AddedMedicineDTO addedMedicineDTO){
        Medicine medicine = medicineService.findMedicine(addedMedicineDTO.getIdentifier());
        if(medicine!=null){
            return ResponseEntity.ok().body("Lek sa unetim identifikatorom već postoji!");
        }
        medicine = medicineService.addMedicine(addedMedicineDTO);
        if(medicine==null) {
            return ResponseEntity.ok().body("Lek nije dodat u sistem.");
        }
        return ResponseEntity.ok().body("Lek je uspešno dodat u sistem.");
    }

    @RequestMapping(value = "/api/addMedicine/getAll",
            method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    public ResponseEntity<ArrayList<MedicineDTO>> getAllMedicines() {
        return new ResponseEntity<ArrayList<MedicineDTO>>(medicineService.getAllMedicines(), HttpStatus.OK);
    }

    @RequestMapping(value = "/api/deleteMedicine",
            method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteMedicine(@RequestParam String id) {
        medicineService.deleteMedicine(id);
        return new ResponseEntity<Void>( HttpStatus.OK);
    }

    @RequestMapping(value = "/api/allergies/add",
            method = RequestMethod.GET)
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<Void> addAllergies(@RequestParam Long id, @RequestParam String username) {
        medicineService.addAllergies(id, username);
        return new ResponseEntity<Void>( HttpStatus.OK);
    }

    @RequestMapping(value = "/api/allergies/remove",
            method = RequestMethod.GET)
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<Void> removeAllergies(@RequestParam Long id, @RequestParam String username) {
        medicineService.removeAllergies(id, username);
        return new ResponseEntity<Void>( HttpStatus.OK);
    }

    @RequestMapping(value = "/api/allergies/getAll",
            method = RequestMethod.GET)
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<ArrayList<MedicineDTO>> getAllergies(@RequestParam String username) {
        return new ResponseEntity<ArrayList<MedicineDTO>>(medicineService.getAllergies(username), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PHARMACY_ADMIN')")
    @RequestMapping(value = "/api/medicine/getAllForPharmacy",
            method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ArrayList<MedicineDTO>> getAllForPharmacy(@RequestParam String username) {
        int pharmacyId = (int) pharmacyAdminService.getPharmacyId(username);
        return new ResponseEntity<ArrayList<MedicineDTO>>(medicineService.getAllForPharmacy(pharmacyId), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PHARMACY_ADMIN')")
    @RequestMapping(value = "/api/medicine/getAllBelongsToPharmacy",
            method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ArrayList<MedicineDTO>> getAllBelongsToPharmacy(@RequestParam String username) {
        return new ResponseEntity<ArrayList<MedicineDTO>>(medicineService.getAllBelongsToPharmacy(username), HttpStatus.OK);
    }



    @PreAuthorize("hasRole('PHARMACY_ADMIN')")
    @RequestMapping(value = "/api/medicine/addToPharmacy",
            method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ArrayList<MedicineDTO>> addToPharmacy(@RequestParam String username, @RequestParam long medicineId) {
        int pharmacyId = (int) pharmacyAdminService.getPharmacyId(username);
        return new ResponseEntity<ArrayList<MedicineDTO>>(medicineService.addToPharmacy(pharmacyId,medicineId), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PHARMACY_ADMIN')")
    @RequestMapping(value = "/api/medicine/deleteFromPharmacy",
            method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ArrayList<MedicineDTO>> deleteFromPharmacy(@RequestParam String username, @RequestParam long medicineId) {
        int pharmacyId = (int) pharmacyAdminService.getPharmacyId(username);
        return new ResponseEntity<ArrayList<MedicineDTO>>(medicineService.deleteFromPharmacy(pharmacyId, medicineId), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PHARMACY_ADMIN')")
    @RequestMapping(value = "/api/medicineSearch",
            method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ArrayList<MedicineDTO>> searchMedicineAdmin(@RequestParam String username, @RequestParam String name, @RequestParam String identifier) {
        int pharmacyId = (int) pharmacyAdminService.getPharmacyId(username);
        return new ResponseEntity<ArrayList<MedicineDTO>>(medicineService.searchMedicineAdmin(pharmacyId, name, identifier), HttpStatus.OK);
    }

    @RequestMapping(value = "/api/medicine/getAvailableOrReplacement",
            method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('DERMATOLOGIST', 'PHARMACIST')")
    public ResponseEntity<?> getAvailableOrReplacement(
            @RequestParam(name = "username") String username,
            @RequestParam(name = "patientId") Long patientId,
            @RequestParam(name = "medicineIdentifier") String medicineIdentifier,
            @RequestParam(name = "pharmacyId") Long pharmacyId,
            @RequestParam(name = "quantity") Long quantity) throws InterruptedException {
        return new ResponseEntity<>(medicineService.getAvailableOrReplacement(patientId, medicineIdentifier, pharmacyId, quantity, username), HttpStatus.OK);
    }

    @RequestMapping(value = "/api/medicine/setRating", method = RequestMethod.GET)
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<Boolean> setRating(@RequestParam String patient,
                                             @RequestParam Long id,
                                             @RequestParam Double rating){
        return new ResponseEntity<Boolean>(medicineService.setRating(patient, id, rating), HttpStatus.OK);
    }

    @RequestMapping(value = "/api/searchftlMedicine", method = RequestMethod.GET)
    public ResponseEntity<List<SearchItemMedicineDTO>> getSearchedMedicine(@RequestParam("name")String name, @RequestParam("type")String type, @RequestParam("ratingLow")String ratingLow, @RequestParam("ratingHigh")String ratingHigh){
        List<Medicine> medicines = medicineService.findAllMedicineWithCost();
        if(ratingHigh == null || ratingHigh.equals(""))
            ratingHigh = "5.0";
        if(ratingLow == null || ratingLow.equals(""))
            ratingLow = "0.0";
        List<SearchItemMedicineDTO> medicineDTOS = new ArrayList<>();
        for(Medicine m: medicines){
            if(m.getName().toLowerCase().contains(name.toLowerCase()) && m.getType().toLowerCase().contains(type.toLowerCase()) && m.getRating() >= Double.parseDouble(ratingLow) && m.getRating() <= Double.parseDouble(ratingHigh)){
                SearchItemMedicineDTO toAdd = new SearchItemMedicineDTO();
                toAdd.convert(m);
                toAdd.setPharmacies(medicineService.getCostsForMedicine(m));
                medicineDTOS.add(toAdd);
            }
        }
        return ResponseEntity.ok().body(medicineDTOS);
    }
}

