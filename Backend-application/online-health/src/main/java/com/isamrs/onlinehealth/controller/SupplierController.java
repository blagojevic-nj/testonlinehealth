package com.isamrs.onlinehealth.controller;

import com.isamrs.onlinehealth.dto.*;
import com.isamrs.onlinehealth.model.Dermatologist;
import com.isamrs.onlinehealth.model.Supplier;
import com.isamrs.onlinehealth.model.USER_TYPE;
import com.isamrs.onlinehealth.service.EmailService;
import com.isamrs.onlinehealth.service.SupplierService;
import com.isamrs.onlinehealth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@RestController
public class SupplierController {
    @Autowired
    private SupplierService supplierService;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @RequestMapping(value = "/api/registrationSupplier", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    public ResponseEntity<String> registerSupplier(@RequestBody @Valid SupplierDTO supplierDTO){
        Supplier supplier;

        if(userService.findByUsername(supplierDTO.getUsername()) != null){
            return ResponseEntity.ok().body("Korisničko ime " + supplierDTO.getUsername() + " je već u upotrebi!");
        }


        if(userService.findByEmail(supplierDTO.getEmail()) != null){
            return ResponseEntity.ok().body("Email " + supplierDTO.getEmail() + " je već u upotrebi!");
        }

        supplier = new Supplier();
        supplier.setUsername(supplierDTO.getUsername());
        supplier.setUser(USER_TYPE.SUPPLIER);
        supplier.setEmail(supplierDTO.getEmail());
        supplier.setDeleted(false);
        supplier.setPasswordChanged(false);
        supplier.setAddress(supplierDTO.getAddress());
        supplier.setCity(supplierDTO.getCity());
        supplier.setCountry(supplierDTO.getState());
        supplier.setFirstName(supplierDTO.getFirstName());
        supplier.setLastName(supplierDTO.getLastName());
        supplier.setPhoneNumber(supplierDTO.getPhoneNumber());
        supplier.setPassword("dobavljac" + (new Random()).nextInt(50));
        supplier.setEnabled(true);

        try {
            emailService.sendEmailAsync("", supplier.getEmail(), "Registracija", "Dobar dan,\n" +
                    "Registrovani ste na portalu OnlineHealth, sajtu možete pristupiti vašom email-adresom ili korisničkim imenom: " + supplier.getUsername() +
                    ", i lozinkom: " + supplier.getPassword() + "\nSrdačan pozdrav,\nAdmin informacionog sistema OnlineHealth");
        }catch (Exception e){
            return ResponseEntity.ok().body("Mailovi nisu poslati dobavljačima.");
        }

        if(supplierService.register(supplier) == null){
            return ResponseEntity.ok().body("Došlo je do greške prilikom dodavanja.");
        }
        return ResponseEntity.ok().body("Dobavljač uspešno dodat u sistem.");
    }

    @RequestMapping(value = "/api/getAllOrdersForSupplier", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('SUPPLIER')")
    public ResponseEntity<List<PurchaseOrderDTO>> getAllOrders(@RequestParam("username") String username){
        return ResponseEntity.ok().body(supplierService.getAllPurchaseOrders(username));
    }

    @RequestMapping(value = "/api/clickedOnOrder", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('SUPPLIER')")
    public ResponseEntity<List<NeededForOrderDTO>> clickOnOrder(@RequestParam("username") String username, @RequestBody PurchaseOrderDTO purchaseOrderDTO){
        return ResponseEntity.ok().body(supplierService.checkConstraintOrder(username, purchaseOrderDTO));
    }

    @RequestMapping(value = "/api/addSupplySupplier", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('SUPPLIER')")
    public ResponseEntity<?> addSupply(@RequestBody SupplyForOrderDTO supplyForOrderDTO){
        return ResponseEntity.ok().body(supplierService.addSupply(supplyForOrderDTO));
    }

    @RequestMapping(value = "/api/getAllSuppliesForSupplier", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('SUPPLIER')")
    public ResponseEntity<List<SupplyForOrderDTO>> getAllSupplies(@RequestParam("username") String username){
        return ResponseEntity.ok().body(supplierService.getAllSupplies(username));
    }

    @RequestMapping(value = "/api/removeSupplyForSupplier", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('SUPPLIER')")
    public ResponseEntity<List<SupplyForOrderDTO>> removeSupply(@RequestParam("username") String username, @RequestParam("id") String id){
        supplierService.removeSupply(Long.parseLong(id), username);
        return ResponseEntity.ok().body(supplierService.getAllSupplies(username));
    }
}
