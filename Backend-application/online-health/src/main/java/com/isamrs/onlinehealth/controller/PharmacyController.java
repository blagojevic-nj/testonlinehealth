package com.isamrs.onlinehealth.controller;

import com.isamrs.onlinehealth.dto.*;
import com.isamrs.onlinehealth.model.*;
import com.isamrs.onlinehealth.service.MedicineService;
import com.isamrs.onlinehealth.service.PatientService;
import com.isamrs.onlinehealth.service.PharmacyAdminService;
import com.isamrs.onlinehealth.service.PharmacyService;
import com.isamrs.onlinehealth.utils.LocationFinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class PharmacyController {

    @Autowired
    private PharmacyService pharmacyService;
    @Autowired
    MedicineService medicineService;
    @Autowired
    PatientService patientService;
    @Autowired
    PharmacyAdminService pharmacyAdminService;

    @RequestMapping(value = "/api/pharmacy/getAll",
            method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ArrayList<PharmacyDTO>> getPharmacies(){
        return new ResponseEntity<ArrayList<PharmacyDTO>>(pharmacyService.getAllPharmacy(), HttpStatus.OK);
    }

    @RequestMapping(value = "/api/pharmacy",
            method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ArrayList<PharmacyDTO>> getPharmacyResult(@RequestParam String name,
                                                                    @RequestParam String address,
                                                                    @RequestParam String ratingLow,
                                                                    @RequestParam String ratingHigh,
                                                                    @RequestParam String sort) {

        return new ResponseEntity<ArrayList<PharmacyDTO>>(pharmacyService.getPharmacyResult(name, address, ratingLow, ratingHigh, sort), HttpStatus.OK);
    }

    @RequestMapping(value = "/api/pharmacyById",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PharmacyDTO> getPharmacyById(@RequestParam long id){

        Pharmacy p = pharmacyService.getPharmacyById(id).get();
        PharmacyDTO ret = new PharmacyDTO(p.getId(), p.getName(), p.getLocation(), p.getDescription(), p.getRatings(), p.getRating());
        return new ResponseEntity<PharmacyDTO>(ret,HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PHARMACY_ADMIN')")
    @RequestMapping(value = "/api/pharmacyByAdmin",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PharmacyDTO> getPharmacyByAdmin(@RequestParam String username){

        Pharmacy p = pharmacyAdminService.getPharmacyByAdminUsername(username);
        PharmacyDTO ret = new PharmacyDTO(p.getId(), p.getName(), p.getLocation(), p.getDescription(), p.getRatings(), p.getRating());
        return new ResponseEntity<PharmacyDTO>(ret,HttpStatus.OK);
    }


    @RequestMapping(value= "/api/pharmacy/getMedicines", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ArrayList<UserMedicineDTO>> getMedicinesByPharmacy(@RequestParam long id){

        ArrayList<UserMedicineDTO>result = new ArrayList<UserMedicineDTO>();
        Pharmacy p = pharmacyService.getPharmacyById(id).get();
        PriceList pricelist = p.getPricelist();
        if(pricelist.getPriceListItems().isEmpty() || pricelist.getPriceListItems() == null)
        {
            return new ResponseEntity<ArrayList<UserMedicineDTO>>(result,HttpStatus.OK);

        }
        for(PriceListItem a :pricelist.getPriceListItems())
        {
            if(!a.getDeleted()){
                Medicine lek = a.getLek();
                result.add(new UserMedicineDTO(new CompleteMedicineDTO(lek),a));
            }
        }
        return new ResponseEntity<ArrayList<UserMedicineDTO>>(result,HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PHARMACY_ADMIN')")
    @RequestMapping(value= "/api/pharmacy/getTopEndMedicines", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ArrayList<UserMedicineDTO>> getTopEndMedicines(@RequestParam String username){

        ArrayList<UserMedicineDTO>result = new ArrayList<UserMedicineDTO>();
        Pharmacy p = pharmacyAdminService.getPharmacyByAdminUsername(username);
        PriceList pricelist = p.getPricelist();
        if(pricelist.getPriceListItems().isEmpty() || pricelist.getPriceListItems() == null)
        {
            return new ResponseEntity<ArrayList<UserMedicineDTO>>(result,HttpStatus.OK);

        }
        List<PriceListItem> items = pricelist.getPriceListItems().stream()
                .sorted((i1,i2) -> Integer.compare(i1.getAvailableQuantity(),i2.getAvailableQuantity()))
                .collect(Collectors.toList());
        int count = 0;
        for(PriceListItem a :items)
        {
            if(!a.getDeleted()){
                count++;
                Medicine lek = a.getLek();
                result.add(new UserMedicineDTO(new CompleteMedicineDTO(lek),a));
            }
            if(count==5)
                break;
        }
        return new ResponseEntity<ArrayList<UserMedicineDTO>>(result,HttpStatus.OK);
    }


    @RequestMapping(value = "/api/pharmacy/get",
            method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<ArrayList<PharmacyDTO>> getPharmaciesWithFreeAppointments(@RequestParam String datum, @RequestParam String od, @RequestParam String doo){
        return new ResponseEntity<ArrayList<PharmacyDTO>>(pharmacyService.getAPharmaciesWithFreeAppointments(LocalDate.parse(datum), LocalTime.parse(od), LocalTime.parse(doo)), HttpStatus.OK);
    }
    //---------------------- [SUBSCRIBER SEGMENT] -------------------------
    /**/
    /**/
    /**/
    @RequestMapping(value = "/api/pharmacy/isSubscribed", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<Boolean> isSubscribed(@RequestParam("idApoteke") Long idApoteke, @RequestParam("username") String username)
    {
        Pharmacy p = pharmacyService.getPharmacyById(idApoteke).get();
        Patient patient = patientService.findByUsername(username);
        boolean isSubscribed = p.getSubscribers().contains(patient);
        return new ResponseEntity<>(isSubscribed,HttpStatus.OK);
    }

    @RequestMapping(value = "/api/pharmacy/subscribe", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<Boolean> subscribe(@RequestParam("idApoteke") Long idApoteke, @RequestParam("username") String username)
    {
        Pharmacy p = pharmacyService.getPharmacyById(idApoteke).get();
        Patient patient = patientService.findByUsername(username);
        boolean success = pharmacyService.subscribe(p,patient);
        return new ResponseEntity<>(success,HttpStatus.OK);
    }

    @RequestMapping(value = "/api/pharmacy/getAllPromotionsPharmacy", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<SubscriberDiscountDTO> getAllPromotionsPharmacy(@RequestParam("pharmacyId") Long pharmacyId)
    {
        SubscriberDiscountDTO discountDTO = new SubscriberDiscountDTO();
        Pharmacy p = pharmacyService.getPharmacyById(pharmacyId).get();
        List<PromotionDTO> promotions = pharmacyService.getAllPromotions(p);
        List<DiscountDTO> discounts = pharmacyService.getAllDiscounts(p);
        discountDTO.setDiscounts(discounts);
        discountDTO.setPromotions(promotions);
        discountDTO.setPharmacyId(p.getId());
        discountDTO.setPharmacyName(p.getName());
        return ResponseEntity.ok().body(discountDTO);

    }

    @RequestMapping(value = "/api/pharmacy/getAllPromotionsSubscriber", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<List<SubscriberDiscountDTO>> getAllPromotionsSubscriber(@RequestParam("username") String username) {
        List<Pharmacy> list = pharmacyService.getAllSubscribedPharmacies(username);
        List<SubscriberDiscountDTO> list1 = new ArrayList<>();
        for(Pharmacy p: list){
            SubscriberDiscountDTO discountDTO = new SubscriberDiscountDTO();
            List<PromotionDTO> promotions = pharmacyService.getAllPromotions(p);
            List<DiscountDTO> discounts = pharmacyService.getAllDiscounts(p);
            discountDTO.setDiscounts(discounts);
            discountDTO.setPromotions(promotions);
            discountDTO.setPharmacyId(p.getId());
            discountDTO.setPharmacyName(p.getName());
            list1.add(discountDTO);
        }
        return ResponseEntity.ok().body(list1);
    }

    @RequestMapping(value = "/api/unsubscribe", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<?> unsub(@RequestParam("username") String username, @RequestParam("pharmacyId") Long pharmacyId){
        pharmacyService.unsubscribe(username, pharmacyId);
        return ResponseEntity.ok().body("");
    }
    /**/
    /**/
    /**/
    //---------------------- [SUBSCRIBER SEGMENT] -------------------------

    @RequestMapping(value = "/api/pharmacy/getFreePharmacists",
            method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<ArrayList<PharmacistDTO>> getFreePharmacists(@RequestParam String datum,
                                                                     @RequestParam String od,
                                                                     @RequestParam String doo,
                                                                     @RequestParam Long apoteka){
        return new ResponseEntity<ArrayList<PharmacistDTO>>(pharmacyService.getFreePharmacists(LocalDate.parse(datum), LocalTime.parse(od), LocalTime.parse(doo), apoteka), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PHARMACY_ADMIN')")
    @RequestMapping(value = "api/pharmacyByAdminUsername", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PharmacyDTO> pharmacyByAdminUsername(@RequestParam String username){
        Pharmacy p = pharmacyAdminService.getPharmacyByAdminUsername(username);
        return new ResponseEntity<PharmacyDTO>(new PharmacyDTO(p),HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PHARMACY_ADMIN')")
    @RequestMapping(value = "api/pharmacy/getProfileInfoUpdate", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PharmacyDTO> getProfileInfoUpdate(@RequestParam String username){
        Pharmacy p = pharmacyAdminService.getPharmacyByAdminUsername(username);
        PharmacyDTO dto = new PharmacyDTO(p);
        dto.setRating(p.getConsultation_price());
        return new ResponseEntity<PharmacyDTO>(dto,HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PHARMACY_ADMIN')")
    @RequestMapping(value = "/api/pharmacy/updatePharmacyInfo", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PharmacyDTO> updatePharmacyInfo(@RequestParam String username,
                                                          @RequestParam String name,
                                                          @RequestParam double price,
                                                          @RequestParam String location,
                                                          @RequestParam String description){
        Pharmacy p = pharmacyAdminService.getPharmacyByAdminUsername(username);
        PharmacyDTO dto = pharmacyService.update(p,name, location,description,price);
        return new ResponseEntity<PharmacyDTO>(new PharmacyDTO(p),HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PHARMACY_ADMIN')")
    @RequestMapping(value = "/api/pharmacy/testAddress", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Location> prikaziAdresu(@RequestParam String address){

        Location loc = LocationFinder.locate(address);
        if (loc == null){
            loc = new Location();
            loc.setAddress("");
            loc.setLatitude(-1.0);
            loc.setLongitude(-1.0);
        }
        return new ResponseEntity<Location>(loc,HttpStatus.OK);
    }

    @RequestMapping(value = "/api/pharmacy/setRating", method = RequestMethod.GET)
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<Double> setRating(@RequestParam String patient,
                                             @RequestParam Long pharmacy,
                                             @RequestParam Double rating){
        return new ResponseEntity<Double>(pharmacyService.setRating(patient, pharmacy, rating), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PHARMACY_ADMIN')")
    @RequestMapping(value = "/api/pharmacy/newPriceList", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Boolean> newPriceList(@RequestParam String username, @RequestBody List<PriceListItemDTO> priceList)
    {
        Pharmacy p = pharmacyAdminService.getPharmacyByAdminUsername(username);
        boolean b = pharmacyService.newPriceList(p, priceList);

        return new ResponseEntity<Boolean>(true,HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PHARMACY_ADMIN')")
    @RequestMapping(value = "/api/pharmacy/updatePriceList", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Boolean> updatePriceList(@RequestParam String username, @RequestBody List<PriceListItemDTO> priceList)
    {
        Pharmacy p = pharmacyAdminService.getPharmacyByAdminUsername(username);
        boolean succ = pharmacyService.updatePriceList(p,priceList);

        return new ResponseEntity<Boolean>(succ,HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PHARMACY_ADMIN')")
    @RequestMapping(value = "/api/pharmacy/getMedicinesForUpdatePriceList",
    method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PriceListItemDTO>> getMedicinesForUpdatePriceList(@RequestParam String username)
    {
        Pharmacy p = pharmacyAdminService.getPharmacyByAdminUsername(username);
        List<PriceListItemDTO> ret = pharmacyService.getMedicinesForPriceListUpdate(p);
        return new ResponseEntity<List<PriceListItemDTO>>(ret,HttpStatus.OK);

    }

    @PreAuthorize("hasRole('PHARMACY_ADMIN')")
    @RequestMapping(value = "/api/pharmacy/newDiscount",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Boolean> newDiscount(@RequestParam String username, @RequestBody DiscountDTO discountDTO)
    {
        Pharmacy p = pharmacyAdminService.getPharmacyByAdminUsername(username);
        boolean result = pharmacyService.newDiscount(p,discountDTO);
        if(!result)
            return new ResponseEntity<Boolean>(false, HttpStatus.OK);
        pharmacyService.notifyNewDiscount(p,discountDTO);
        return new ResponseEntity<Boolean>(result, HttpStatus.OK);

    }

    @PreAuthorize("hasRole('PHARMACY_ADMIN')")
    @RequestMapping(value = "/api/pharmacy/newPromotion",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Boolean> newPromotion(@RequestParam String username, @RequestBody PromotionDTO promotionDTO)
    {
        Pharmacy p = pharmacyAdminService.getPharmacyByAdminUsername(username);
        boolean result = pharmacyService.newPromotion(p,promotionDTO);
        if(!result)
            return new ResponseEntity<Boolean>(false, HttpStatus.OK);
        pharmacyService.notifyNewPromotion(p,promotionDTO);
        return new ResponseEntity<Boolean>(result, HttpStatus.OK);

    }

    @PreAuthorize("hasRole('PHARMACY_ADMIN')")
    @RequestMapping(value = "/api/pharmacy/getAllPromotions", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PromotionDTO>> getAllPromotions(@RequestParam String username)
    {
        Pharmacy p = pharmacyAdminService.getPharmacyByAdminUsername(username);
        List<PromotionDTO> promotions = pharmacyService.getAllPromotions(p);
        return new ResponseEntity<List<PromotionDTO>>(promotions, HttpStatus.OK);

    }

    @PreAuthorize("hasRole('PHARMACY_ADMIN')")
    @RequestMapping(value = "/api/pharmacy/getAllDiscounts", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DiscountDTO>> getAllDiscounts(@RequestParam String username) {
        Pharmacy p = pharmacyAdminService.getPharmacyByAdminUsername(username);
        List<DiscountDTO> discounts = pharmacyService.getAllDiscounts(p);
        return new ResponseEntity<List<DiscountDTO>>(discounts, HttpStatus.OK);

    }

    @RequestMapping(value = "/api/pharmacy/priceConsultation", method = RequestMethod.GET)
    @PreAuthorize("hasRole('PHARMACIST')")
    public ResponseEntity<?> getPriceConsultation(@RequestParam(name = "id") Long id) {
        return new ResponseEntity<>(pharmacyService.getPharmacyById(id).get().getConsultation_price(), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PHARMACY_ADMIN')")
    @RequestMapping(value = "/api/pharmacy/getAllLeaveRequests", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<LeaveRequestDTO>> getAllLeaveRequests(@RequestParam String username)
    {
        return new ResponseEntity<List<LeaveRequestDTO>>(pharmacyService.getLeaveRequests(username), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PHARMACY_ADMIN')")
    @RequestMapping(value = "/api/pharmacy/declineLeaveRequest",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Boolean> declineLeaveRequest(@RequestParam String username,@RequestParam long id, @RequestBody String poruka)
    {
        return new ResponseEntity<Boolean>(pharmacyService.declineLeaveRequest(username,id,poruka), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PHARMACY_ADMIN')")
    @RequestMapping(value = "/api/pharmacy/acceptLeaveRequest", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public  ResponseEntity<Boolean> acceptLeaveRequest(@RequestParam String username, @RequestParam long id)
    {
        return new ResponseEntity<Boolean>(pharmacyService.acceptLeaveRequest(username,id), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PHARMACY_ADMIN')")
    @RequestMapping(value = "/api/pharmacy/appointmentsChart", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public  ResponseEntity<List<ChartDTO>> appointmentsChart(@RequestParam String username)
    {
        ChartDTO mesecni   = pharmacyService.getAppointmentsMonthlyChart(username);
        ChartDTO kvartalni = pharmacyService.getAppointmentsQuartalChart(mesecni);
        ChartDTO godisnji  = pharmacyService.getAppointments5YearChart(username);
        List<ChartDTO>ret = new ArrayList<>();
        ret.add(mesecni);
        ret.add(kvartalni);
        ret.add(godisnji);
        return new ResponseEntity<List<ChartDTO>>(ret, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PHARMACY_ADMIN')")
    @RequestMapping(value = "/api/pharmacy/medicineChart", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public  ResponseEntity<List<ChartDTO>> medicineChart(@RequestParam String username)
    {
        ChartDTO mesecni   = pharmacyService.getMedicineMonthlyChart(username);
        ChartDTO kvartalni = pharmacyService.getMedicineQuartalChart(mesecni);
        ChartDTO godisnji  = pharmacyService.getMedicine5YearChart(username);
        List<ChartDTO>ret = new ArrayList<>();
        ret.add(mesecni);
        ret.add(kvartalni);
        ret.add(godisnji);
        return new ResponseEntity<List<ChartDTO>>(ret, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PHARMACY_ADMIN')")
    @RequestMapping(value = "/api/pharmacy/prihodiChart", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public  ResponseEntity<ChartDTO> prihodiChart(@RequestParam String username,
                                                  @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate datumOd,
                                                  @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate datumDo)
    {
        ChartDTO izvestaj   = pharmacyService.getPrihodiChart(username,datumOd,datumDo);

        return new ResponseEntity<ChartDTO>(izvestaj, HttpStatus.OK);
    }







}
