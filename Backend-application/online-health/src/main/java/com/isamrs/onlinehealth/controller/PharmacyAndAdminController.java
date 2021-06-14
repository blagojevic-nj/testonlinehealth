package com.isamrs.onlinehealth.controller;

import com.isamrs.onlinehealth.dto.*;
import com.isamrs.onlinehealth.model.Location;
import com.isamrs.onlinehealth.model.Pharmacy;
import com.isamrs.onlinehealth.model.PharmacyAdmin;
import com.isamrs.onlinehealth.model.USER_TYPE;
import com.isamrs.onlinehealth.service.EmailService;
import com.isamrs.onlinehealth.service.PharmacyAdminService;
import com.isamrs.onlinehealth.service.PharmacyService;
import com.isamrs.onlinehealth.service.UserService;
import com.isamrs.onlinehealth.utils.LocationFinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@RestController
public class PharmacyAndAdminController {
    @Autowired
    PharmacyAdminService pharmacyAdminService;

    @Autowired
    UserService userService;

    @Autowired
    PharmacyService pharmacyService;

    @Autowired
    EmailService emailService;

    @RequestMapping(value = "/api/pharmacyAndAdminRegistration", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    public ResponseEntity<String> addPharmacyAndAdmins(@RequestBody @Valid PharmacyAndAdminDTO pharmacyAndAdminDTO){
        for (PharmacyAdminDTO pad: pharmacyAndAdminDTO.getPharmacyAdmins()) {
            if(userService.findByUsername(pad.getUsername())!=null || userService.findByEmail(pad.getEmail())!=null)
                return ResponseEntity.ok().body("Uneti administratori već postoje u sistemu.");
        }



        Pharmacy pharmacy = new Pharmacy();
        pharmacy.setName(pharmacyAndAdminDTO.getName());
        pharmacy.setDescription(pharmacyAndAdminDTO.getDescription());
        Location locationFound = LocationFinder.locate(pharmacyAndAdminDTO.getLocation());
        if(locationFound==null)
            return ResponseEntity.ok().body("Unešena adresa nije validna.");
        locationFound = pharmacyService.saveLocation(locationFound);
        if(locationFound==null)
            return ResponseEntity.ok().body("Došlo je do greške prilikom dodavanja apoteke u sistem.");
        pharmacy.setLocation(locationFound);
        pharmacy.setRating(0);
        pharmacy.setConsultation_price(Double.parseDouble(pharmacyAndAdminDTO.getConsultation_price()));
        pharmacy.setDeleted(false);
        pharmacyService.save(pharmacy);

        for (PharmacyAdminDTO pad: pharmacyAndAdminDTO.getPharmacyAdmins()) {
            USER_TYPE userType = USER_TYPE.PHARMACY_ADMIN;
            String password = "adminApoteke" + (new Random()).nextInt(50);
            PharmacyAdmin pharmacyAdmin = new PharmacyAdmin(null, pad.getUsername(), pad.getEmail(), password,
                    pad.getFirstName(), pad.getLastName(), pad.getAddress(), pad.getCity(), pad.getState(),
                    pad.getPhoneNumber(), userType, false);
            pharmacyAdmin.setPharmacy(pharmacy);
            pharmacyAdmin.setPassword(password);

            pharmacyAdmin.setEnabled(true);
            try {
                emailService.sendEmailAsync("", pharmacyAdmin.getEmail(), "Registracija", "Dobar dan,\n" +
                        "Registrovani ste na portalu OnlineHealth, sajtu možete pristupiti vašom email-adresom ili korisničkim imenom: " + pharmacyAdmin.getUsername() +
                        ", i lozinkom: " + pharmacyAdmin.getPassword() + "\nSrdačan pozdrav,\nAdmin informacionog sistema OnlineHealth");
            }catch (Exception e){
                return ResponseEntity.ok().body("Mailovi nisu poslati administratorima.");
            }
            if(pharmacyAdminService.save(pharmacyAdmin)==null){
                return ResponseEntity.ok().body("Administratori nisu uspešno dodati u sitem!");
            }
        }
        return ResponseEntity.ok().body("Apoteka uspešno dodata u sistem!");
    }

    @PreAuthorize("hasRole('PHARMACY_ADMIN')")
    @RequestMapping(value = "/api/pharmacyAndAdmin/getUser", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PharmacyAdminDTO> getAdminByUsername(@RequestParam String username){
        if(username.trim().equals(""))
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        PharmacyAdminDTO result = pharmacyAdminService.getAdminByUsername(username);
        if(result.getUsername().equals("-1"))
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<PharmacyAdminDTO>(result,HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PHARMACY_ADMIN')")
    @RequestMapping(value = "/api/pharmacyAndAdmin/updateUser", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateAdminProfile(@RequestBody @Valid PharmacyAdminDTO pharmacyAdminDTO){
        String result = pharmacyAdminService.update(pharmacyAdminDTO);
        if(result.equals("notExists"))
            return new ResponseEntity<>("User not Found", HttpStatus.NOT_FOUND);
        return ResponseEntity.ok().body("Successfully changed");
    }

    @PreAuthorize("hasRole('PHARMACY_ADMIN')")
    @RequestMapping(value = "/api/pharmacyAndAdmin/newOrder",
    method = RequestMethod.POST,
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> newPurchaseOrder(@RequestParam String username, @RequestBody PurchaseOrderDTO purchaseOrderDTO)
    {
        boolean ret = pharmacyAdminService.makeNewPurchaseOrder(username, purchaseOrderDTO);
        return new ResponseEntity<Boolean>(ret, HttpStatus.OK);
    }


    @PreAuthorize("hasRole('PHARMACY_ADMIN')")
    @RequestMapping(value = "/api/pharmacyAndAdmin/getAllOrders",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PurchaseOrderDTO>> getAllOrders(@RequestParam String username)
    {
        List<PurchaseOrderDTO> ret = pharmacyAdminService.getAllOrders(username);
        return new ResponseEntity<List<PurchaseOrderDTO>>(ret, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PHARMACY_ADMIN')")
    @RequestMapping(value = "/api/pharmacyAndAdmin/getSupplies", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SupplyDTO>> getSupplies(@RequestParam String username, @RequestParam Long orderId)
    {
        List<SupplyDTO> retVal = pharmacyAdminService.getSupplies(username,orderId);
        if(retVal==null)
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        return new ResponseEntity<List<SupplyDTO>>(retVal, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PHARMACY_ADMIN')")
    @RequestMapping(value = "/api/pharmacyAndAdmin/acceptSupply", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> prihvatiPonudu(@RequestParam String username, @RequestParam Long ponudaId, @RequestParam Long orderId)
    {
        boolean retVal = pharmacyAdminService.acceptPonuda(username,ponudaId,orderId);
        return new ResponseEntity<Boolean>(retVal, HttpStatus.OK);

    }

    @PreAuthorize("hasRole('PHARMACY_ADMIN')")
    @RequestMapping(value = "/api/pharmacyAndAdmin/deleteOrder", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PurchaseOrderDTO>> prihvatiPonudu(@RequestParam String username, @RequestParam Long orderId)
    {
        List<PurchaseOrderDTO>retVal = null;
        boolean success = pharmacyAdminService.deleteOrder(username,orderId);
        if(success){
            retVal = pharmacyAdminService.getAllOrders(username);
            return new ResponseEntity<List<PurchaseOrderDTO>>(retVal, HttpStatus.OK);
        }
        return new ResponseEntity<List<PurchaseOrderDTO>>(retVal, HttpStatus.BAD_REQUEST);

    }

    @PreAuthorize("hasRole('PHARMACY_ADMIN')")
    @RequestMapping(value = "/api/pharmacyAndAdmin/getAllFailedQueries",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<FailedQueryDTO>> getAllFailedQueries(@RequestParam String username)
    {
        List<FailedQueryDTO> ret = pharmacyAdminService.getAllFailedQueries(username);
        return new ResponseEntity<List<FailedQueryDTO>>(ret, HttpStatus.OK);
    }







}
