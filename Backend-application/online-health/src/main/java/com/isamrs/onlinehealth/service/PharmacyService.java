package com.isamrs.onlinehealth.service;

import com.isamrs.onlinehealth.dto.DermatologistDTO;
import com.isamrs.onlinehealth.dto.PharmacistDTO;
import com.isamrs.onlinehealth.dto.PharmacyDTO;
import com.isamrs.onlinehealth.dto.*;
import com.isamrs.onlinehealth.model.*;
import com.isamrs.onlinehealth.repository.*;
import com.isamrs.onlinehealth.utils.LocationFinder;
import org.apache.tomcat.jni.Local;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class PharmacyService {
    @Autowired
    private PharmacyRepository pharmacyRepository;

    @Autowired
    private PharmacistRepository pharmacistRepository;

    @Autowired
    private LocationRepositrory locationRepositrory;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private RatingItemRepository ratingItemRepository;

    @Autowired
    private PriceListRepository priceListRepository;

    @Autowired
    private PriceListItemRepository priceListItemRepository;

    @Autowired
    private DiscountItemRepository discountItemRepository;

    @Autowired
    private DiscountRepository discountRepository;

    @Autowired
    private PromotionRepository promotionRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PharmacyAdminService pharmacyAdminService;

    @Autowired
    private LeaveRequestRepository leaveRequestRepository;

    @Autowired
    private ExaminationsRepository examinationsRepository;

    @Autowired
    private ConsultationsRepository consultationsRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    public ArrayList<PharmacyDTO> getAllPharmacy(){
        ArrayList<PharmacyDTO> retVal = new ArrayList<>();
        for(Pharmacy p : pharmacyRepository.findAll())
            retVal.add(new PharmacyDTO(p.getId(), p.getName(), p.getLocation(), p.getDescription(), p.getRatings(), (double) Math.round(p.getRating() * 100) / 100));
        return retVal;
    }

    public Optional<Pharmacy> getPharmacyById(Long id){
        return pharmacyRepository.findById(id);
    }

    public PharmacyDTO getPharmacy(Long id)
    {

        Pharmacy p = pharmacyRepository.getOne(id);
        return new PharmacyDTO(p.getId(), p.getName(), p.getLocation(), p.getDescription(), p.getRatings(), p.getRating());
    }

    public ArrayList<PharmacyDTO> getPharmacyResult(String name, String address, String ratingLow, String ratingHigh, String sort){
        ArrayList<PharmacyDTO> retVal = new ArrayList<>();
        List<Pharmacy> result;

        switch (sort) {
            case "0":
                result = pharmacyRepository.findByNameContainingIgnoreCaseAndAddressContainingIgnoreCase(name, address, Double.parseDouble(ratingLow), Double.parseDouble(ratingHigh));
                break;
            case "1":
                result = pharmacyRepository.findByNameContainingIgnoreCaseAndAddressContainingOrderByNameAsc(name, address, Double.parseDouble(ratingLow), Double.parseDouble(ratingHigh));
                break;
            case "2":
                result = pharmacyRepository.findByNameContainingIgnoreCaseAndAddressContainingOrderByNameDesc(name, address, Double.parseDouble(ratingLow), Double.parseDouble(ratingHigh));
                break;
            case "3":
                result = pharmacyRepository.findByNameContainingIgnoreCaseAndAddressContainingOrderByAddressAsc(name, address, Double.parseDouble(ratingLow), Double.parseDouble(ratingHigh));
                break;
            case "4":
                result = pharmacyRepository.findByNameContainingIgnoreCaseAndAddressContainingOrderByAddressDesc(name, address, Double.parseDouble(ratingLow), Double.parseDouble(ratingHigh));
                break;
            case "5":
                result = pharmacyRepository.findByNameContainingIgnoreCaseAndAddressContainingOrderByRatingAsc(name, address, Double.parseDouble(ratingLow), Double.parseDouble(ratingHigh));
                break;
            case "6":
                result = pharmacyRepository.findByNameContainingIgnoreCaseAndAddressContainingOrderByRatingDesc(name, address, Double.parseDouble(ratingLow), Double.parseDouble(ratingHigh));
                break;
            default:
                result = pharmacyRepository.findAll();
        }
        for(Pharmacy p: result)
            retVal.add(new PharmacyDTO(p.getId(), p.getName(), p.getLocation(), p.getDescription(), p.getRatings(), p.getRating()));
        return retVal;
    }

    public Pharmacy save(Pharmacy p) { return pharmacyRepository.save(p);}

    public Location saveLocation(Location location){return locationRepositrory.save(location);}

    public Pharmacy findById(Long id){
        return pharmacyRepository.findOneById(id);
    }

    public ArrayList<PharmacyDTO> getAPharmaciesWithFreeAppointments(LocalDate datum, LocalTime od, LocalTime doo){
        ArrayList<PharmacyDTO> retVal = new ArrayList<>();
        for(Pharmacy pharmacy : pharmacyRepository.findAll())
        {

            for(Pharmacist p: pharmacy.getPharmacists()){
                if(od.isBefore(p.getWorkHours().getEnd()) && od.isAfter(p.getWorkHours().getStart())
                        && doo.isBefore(p.getWorkHours().getEnd())
                        && doo.isAfter(p.getWorkHours().getStart()) && doo.isAfter(od)
                        && !p.getDeleted())
                {
                    boolean free = true;
                    for(Consultation c: p.getConsultations()){
                        if(c.getStart().toLocalDate().equals(datum)){
                            if(c.getStart().toLocalTime().isAfter(od) && c.getStart().toLocalTime().isBefore(doo)){
                                free = false;
                            }
                            if(c.getEnd().toLocalTime().isAfter(od) && c.getEnd().toLocalTime().isBefore(doo)){
                                free = false;
                            }
                        }
                    }

                    LocalDateTime date1 = LocalDateTime.of(datum, od);
                    LocalDateTime date2 = LocalDateTime.of(datum, doo);
                    for(LeaveRequest lr : p.getLeaveRequests())
                    {
                        if(lr.getStatus() == LEAVE_REQUEST_STATUS.ACCEPTED && lr.getStart().isBefore(date1) && lr.getEnd().isAfter(date1)
                            || lr.getStart().isBefore(date2) && lr.getEnd().isAfter(date2))
                        {
                            free = false;
                        }
                    }

                    if (free){
                        PharmacyDTO dto = new PharmacyDTO();
                        dto.setId(pharmacy.getId());
                        dto.setName(pharmacy.getName());
                        dto.setDescription(pharmacy.getDescription());
                        dto.setLocation(pharmacy.getLocation());
                        dto.setRating(pharmacy.getRating());
                        dto.setConsultation_price(pharmacy.getConsultation_price());

                        retVal.add(dto);
                        break;
                    }
                }
            }

        }
        return retVal;
    }

    public ArrayList<PharmacistDTO> getFreePharmacists(LocalDate datum, LocalTime od, LocalTime doo, Long id) {
        ArrayList<PharmacistDTO> retVal = new ArrayList<>();
        Pharmacy pharmacy = pharmacyRepository.findOneById(id);
        if (pharmacy != null) {
            for (Pharmacist p : pharmacy.getPharmacists()) {
                if (od.isBefore(p.getWorkHours().getEnd()) && od.isAfter(p.getWorkHours().getStart())
                        && doo.isBefore(p.getWorkHours().getEnd())
                        && doo.isAfter(p.getWorkHours().getStart()) && doo.isAfter(od)
                        && !p.getDeleted()) {
                    boolean free = true;
                    for (Consultation c : p.getConsultations()) {
                        if (c.getStart().toLocalDate().equals(datum)) {
                            if (c.getStart().toLocalTime().isAfter(od) && c.getStart().toLocalTime().isBefore(doo)) {
                                free = false;
                            }
                            if (c.getEnd().toLocalTime().isAfter(od) && c.getEnd().toLocalTime().isBefore(doo)) {
                                free = false;
                            }
                        }
                    }

                    LocalDateTime date1 = LocalDateTime.of(datum, od);
                    LocalDateTime date2 = LocalDateTime.of(datum, doo);
                    for(LeaveRequest lr : p.getLeaveRequests())
                    {
                        if(lr.getStatus() == LEAVE_REQUEST_STATUS.ACCEPTED && lr.getStart().isBefore(date1) && lr.getEnd().isAfter(date1)
                                || lr.getStart().isBefore(date2) && lr.getEnd().isAfter(date2))
                        {
                            free = false;
                        }
                    }

                    if (free) {
                        PharmacistDTO dto = new PharmacistDTO();
                        dto.setId(p.getId());
                        dto.setUsername(p.getUsername());
                        dto.setFirstName(p.getFirstName());
                        dto.setLastName(p.getLastName());
                        dto.setRating(p.getRating());

                        retVal.add(dto);
                    }
                }
            }
        }
        return retVal;
    }
    public boolean subscribe(Pharmacy p, Patient patient) {
        p.getSubscribers().add(patient);
        pharmacyRepository.save(p);
        return true;
    }

    public PharmacyDTO update(Pharmacy p, String name, String location, String description,double price) {
        if(!name.trim().equals(""))
            p.setName(name);
        Location locationFound = LocationFinder.locate(location);
        if(locationFound==null)
            return new PharmacyDTO();
        locationFound = saveLocation(locationFound);
        if(locationFound==null)
            return new PharmacyDTO();
        if(price<0)
            return  new PharmacyDTO();
        p.setConsultation_price(price);
        p.setLocation(locationFound);
        p.setDescription(description);
        save(p);
        return new PharmacyDTO(p);
    }

    @Transactional(readOnly = false)
    public Double setRating(String username, Long pharmacyId, Double rating) {
        Patient patient = patientRepository.findOneByUsername(username);
        Pharmacy pharmacy = pharmacyRepository.findOneByIdForRating(pharmacyId);

        if (patient == null || pharmacy == null) {
            return 0.0;
        }
        Hibernate.initialize(pharmacy.getRatings());
        Hibernate.unproxy(pharmacy);
        boolean added = false;
        for (RatingItem ri : pharmacy.getRatings()) {
            if (!ri.getDeleted() && ri.getPatient().getUsername().equals(username)) {
                ri.setRating(rating);
                ratingItemRepository.save(ri);
                added = true;
                break;
            }
        }

        if (!added) {
            RatingItem ri = new RatingItem();
            ri.setRating(rating);
            ri.setPatient(patient);
            ri.setDeleted(false);
            ratingItemRepository.save(ri);
            pharmacy.getRatings().add(ri);
        }

        double sum = 0.0;
        double couner = 0;
        for (RatingItem ri : pharmacy.getRatings()) {
            if (!ri.getDeleted()) {
                sum += ri.getRating();
                couner++;
            }
        }

        pharmacy.setRating(sum / couner);
        pharmacyRepository.save(pharmacy);

        return pharmacy.getRating();
    }
      public boolean newPriceList(Pharmacy p, List<PriceListItemDTO> priceList) {
        if(priceListCheckFailed(priceList))
            return false;
        Map<Long, PriceListItem> mapaStarihLekova = p.getPricelist().getPriceListItems()
                .stream().collect(Collectors.toMap(x -> x.getLek().getId(),x->x));


        PriceList newPriceList = new PriceList();
        List<PriceListItem> newItems = new ArrayList<PriceListItem>();

        for(PriceListItemDTO dto : priceList){
            PriceListItem pli = new PriceListItem();
            PriceListItem oldPli = mapaStarihLekova.get(dto.getIdLeka());
            pli.setLek(oldPli.getLek());
            pli.setAvailableQuantity(oldPli.getAvailableQuantity());
            pli.setPrice(dto.getCena());
            pli.setStart(dto.getStart().atStartOfDay());
            pli.setEnd(dto.getEnd().atStartOfDay());
            newItems.add(pli);
        }
        priceListItemRepository.saveAll(newItems);
        newPriceList.setPriceListItems(new HashSet<PriceListItem>(newItems));
        priceListRepository.save(newPriceList);
        p.setPricelist(newPriceList);
        pharmacyRepository.save(p);
        return true;
    }

    public boolean updatePriceList(Pharmacy p, List<PriceListItemDTO> priceList) {
        if(priceListCheckFailed(priceList))
            return false;
        Map<Long, PriceListItem> mapaStarihLekova = p.getPricelist().getPriceListItems()
                .stream().collect(Collectors.toMap(x -> x.getLek().getId(),x->x));

        for(PriceListItemDTO dto : priceList){
            PriceListItem oldPli = mapaStarihLekova.get(dto.getIdLeka());
            oldPli.setPrice(dto.getCena());
            oldPli.setStart(dto.getStart().atStartOfDay());
            oldPli.setEnd(dto.getEnd().atStartOfDay());
        }
        priceListItemRepository.saveAll(p.getPricelist().getPriceListItems());
        priceListRepository.save(p.getPricelist());
        pharmacyRepository.save(p);
        return true;
    }

    private boolean priceListCheckFailed(List<PriceListItemDTO> priceList) {
        for (PriceListItemDTO p : priceList)
        {
            if(p.getStart().isAfter(p.getEnd()))
                return true;
        }
        return false;
    }

    public List<PriceListItemDTO> getMedicinesForPriceListUpdate(Pharmacy p) {
        List<PriceListItemDTO> medicines = new ArrayList<>();
        for (PriceListItem pli : p.getPricelist().getPriceListItems()){
            medicines.add(new PriceListItemDTO(pli));
        }
        return medicines;
    }

    public boolean newDiscount(Pharmacy p, DiscountDTO discountDTO) {

        Map<Long, PriceListItem> mapaStarihLekova = p.getPricelist().getPriceListItems()
                .stream().collect(Collectors.toMap(x -> x.getLek().getId(),x->x));

        Discount discount = new Discount();
        if(!checkDiscount(discountDTO))
            return false;

        List<DiscountItem> items = new ArrayList<>();
        for (DiscountItemDTO diDTO : discountDTO.getArtikli())
        {
            if(Double.isNaN(diDTO.getCena()) || !mapaStarihLekova.keySet().contains(diDTO.getIdLeka()))
                return false;
            DiscountItem newDI = new DiscountItem();
            newDI.setLek(mapaStarihLekova.get(diDTO.getIdLeka()).getLek());
            newDI.setPrice(diDTO.getCena());
            items.add(newDI);
        }
        discount.setStart(discountDTO.getStart());
        discount.setEnd(discountDTO.getEnd());
        discountItemRepository.saveAll(items);
        Set<DiscountItem>discountItems = new HashSet<>(items);
        discount.setDiscountItems(discountItems);
        discountRepository.save(discount);
        p.getDiscounts().add(discount);
        pharmacyRepository.save(p);
        return true;
    }

    private boolean checkDiscount(DiscountDTO dto){
        if(dto.getStart().isBefore(LocalDate.now()))
            return false;
        if(dto.getStart().isAfter(dto.getEnd()))
            return false;
        return true;
    }

    private boolean checkPromotion(PromotionDTO dto){
        if(dto.getStart().isBefore(LocalDate.now()))
            return false;
        if(dto.getStart().isAfter(dto.getEnd()))
            return false;
        return true;
    }

    public boolean newPromotion(Pharmacy p, PromotionDTO promotionDTO) {
            if(!checkPromotion(promotionDTO))
                return false;
            Promotion promotion = new Promotion();
            promotion.setStart(promotionDTO.getStart().atStartOfDay());
            promotion.setEnd(promotionDTO.getEnd().atStartOfDay());
            promotion.setDiscountRate((double) (promotionDTO.getSnizenje())/100);
            p.getPromotions().add(promotion);
            promotionRepository.save(promotion);
            pharmacyRepository.save(p);
            return true;
    }

    public List<PromotionDTO> getAllPromotions(Pharmacy p) {

        List<PromotionDTO> res = new ArrayList<>();
        for(Promotion promotion : p.getPromotions())
            res.add(new PromotionDTO(promotion));
        return res;
    }

    public List<DiscountDTO> getAllDiscounts(Pharmacy p) {
        List<DiscountDTO> res = new ArrayList<>();
        for (Discount d : p.getDiscounts())
            res.add(new DiscountDTO(d));
        return res;
    }

    public void notifyNewDiscount(Pharmacy pharmacy, DiscountDTO discountDTO) {

        for(Patient p : pharmacy.getSubscribers())
        {
            String mail = p.getEmail();
            String body = "Nova Akcija u apoteci " + pharmacy.getName() + "!!!\n"+
                    "Popust možete iskoristiti u periodu:\n" +
                    "\n" +discountDTO.getStart() + " Do "+ discountDTO.getEnd()+"\n\n"+
                    " Popust se odnosi na određene artikle.Popusti se međusobno ne sabiraju.\n";
            try {
                emailService.sendEmailAsync("tim10isamrs2021@gmail.com",
                        mail,
                        "NOVA AKCIJA!",
                        body);
            } catch (InterruptedException e) {
                System.out.println("Greska prilikom slanja maila Nova akcija!");
            }
        }
    }

    public void notifyNewPromotion(Pharmacy pharmacy, PromotionDTO promotionDTO) {

        for(Patient p : pharmacy.getSubscribers())
        {
            String mail = p.getEmail();
            String body = "Nova Promocija u apoteci " + pharmacy.getName() + "!!!\n"+
                    "Popust  NA SVE ARTIKLE u iznosu od "+ promotionDTO.getSnizenje()+"% možete iskoristiti u periodu:\n" +
                    "\n" +promotionDTO.getStart() + " Do "+ promotionDTO.getEnd()+"\n\n"+
                    " Popust se odnosi na sve artikle. Popusti se međusobno ne sabiraju.\n";


            try {
                emailService.sendEmailAsync("tim10isamrs2021@gmail.com",
                        mail,
                        "NOVA PROMOCIJA!",
                        body);
            } catch (InterruptedException e) {
                System.out.println("Greska prilikom slanja maila Nova promocija!");
            }



        }
    }

    public List<Pharmacy> getAllSubscribedPharmacies(String username){
        List<Pharmacy> pharmacies = new ArrayList<>();
        Patient patient = patientRepository.findOneByUsername(username);
        for(Pharmacy p : pharmacyRepository.findAll()){
            if(p.getSubscribers().contains(patient)){
                pharmacies.add(p);
            }
        }
        return pharmacies;
    }

    public void unsubscribe(String username, Long pharmacyId){
        Patient patient = patientRepository.findOneByUsername(username);
        Pharmacy pharmacy = pharmacyRepository.findOneById(pharmacyId);
        pharmacy.getSubscribers().remove(patient);
        pharmacyRepository.save(pharmacy);
    }


    /**********************************************************Godisnji odmori**********************************************************/


    public List<LeaveRequestDTO> getLeaveRequests(String username)
    {
            return getAllLeaveRequestsForPharmacy(username).stream()
                    .filter(x -> x.getStatus()==LEAVE_REQUEST_STATUS.PENDING).collect(Collectors.toList());

    }

    @Transactional
    public boolean acceptLeaveRequest(String username,long id)
    {
        Pharmacy apoteka = pharmacyAdminService.getPharmacyByAdminUsername(username);
        Hibernate.initialize(apoteka.getPharmacists());
        Hibernate.unproxy(apoteka);
        Pair<Pharmacist,LeaveRequest> pair = findParticularLeaveRequest(apoteka,id);
        Pharmacist requestingPharmacist = pair.getFirst();
        LeaveRequest request = pair.getSecond();
        LeaveRequest lrToChange = leaveRequestRepository.findOneByIdAndLock(request.getId());
        if (requestingPharmacist.getId()==null)
            return false;
        if(lrToChange.getStatus()!=LEAVE_REQUEST_STATUS.PENDING)
            return false;

        lrToChange.setStatus(LEAVE_REQUEST_STATUS.ACCEPTED);

        leaveRequestRepository.save(lrToChange);

        sendAcceptedLeaveRequest(request,requestingPharmacist,apoteka);

        return true;

    }

    @Transactional
    public boolean declineLeaveRequest(String username,long id,String poruka)
    {
        Pharmacy apoteka = pharmacyAdminService.getPharmacyByAdminUsername(username);
        Hibernate.initialize(apoteka.getPharmacists());
        Hibernate.unproxy(apoteka);
        Pair<Pharmacist,LeaveRequest> pair = findParticularLeaveRequest(apoteka,id);
        Pharmacist requestingPharmacist = pair.getFirst();
        LeaveRequest request = pair.getSecond();
        LeaveRequest lrToChange = leaveRequestRepository.findOneByIdAndLock(request.getId());
        if (requestingPharmacist.getId()==null)
            return false;
        if(lrToChange.getStatus()!=LEAVE_REQUEST_STATUS.PENDING)
            return false;

        lrToChange.setStatus(LEAVE_REQUEST_STATUS.REJECTED);

        leaveRequestRepository.save(lrToChange);

        sendRejectedLeaveRequest(lrToChange,requestingPharmacist,apoteka, poruka);

        return true;

    }

    private void sendRejectedLeaveRequest(LeaveRequest request, Pharmacist requestingPharmacist, Pharmacy apoteka, String poruka) {
        String obrazlozenje = poruka=="" ? "Napomena: \n\t"+poruka : "";
        String mail = requestingPharmacist.getEmail();
        String body = "Odbijen zahtev za odsustvo u periodu od: "
                + request.getStart()+ " do: " + request.getEnd() + "\n"+obrazlozenje+
                "\n\n\n"+apoteka.getName()+".";
        try {
            emailService.sendEmailAsync("tim10isamrs2021@gmail.com",
                    mail,
                    "Odbijen zahtev za odsustvo!",
                    body);
        } catch (InterruptedException e) {
            System.out.println("Greska prilikom slanja maila odbijen zahtev za odsustvo!");
        }
    }

    @Transactional
    private ArrayList<LeaveRequestDTO> getAllLeaveRequestsForPharmacy(String username)
    {
        ArrayList<LeaveRequestDTO> ret = new ArrayList<>();
        Pharmacy apoteka = pharmacyAdminService.getPharmacyByAdminUsername(username);
        Hibernate.initialize(apoteka.getPharmacists());
        Hibernate.unproxy(apoteka);
        Set<Pharmacist> pharmacists = apoteka.getPharmacists();
        for(Pharmacist p : pharmacists) {
            Hibernate.initialize(p.getLeaveRequests().size());
            Hibernate.unproxy(p);
            for(LeaveRequest l : p.getLeaveRequests())
                ret.add(new LeaveRequestDTO(l,p));
        }

        return ret;
    }

    @Transactional
    private Pair<Pharmacist,LeaveRequest> findParticularLeaveRequest(Pharmacy apoteka, Long requestId)
    {
        Set<Pharmacist> pharmacists = apoteka.getPharmacists();
        Pharmacist requestingPharmacist = new Pharmacist();
        LeaveRequest request = new LeaveRequest();
        for(Pharmacist p : pharmacists)
        {
            Hibernate.initialize(p.getLeaveRequests());
            Hibernate.unproxy(p);
            for(LeaveRequest l : p.getLeaveRequests())
            {
                if(l.getId()==requestId)
                {
                    requestingPharmacist=p;
                    request=l;
                    break;
                }

            }
            if (requestingPharmacist.getId()!=null)
                break;
        }

        return Pair.of(requestingPharmacist,request);
    }

    private void sendAcceptedLeaveRequest(LeaveRequest request, Pharmacist requestingPharmacist, Pharmacy apoteka) {
        String mail = requestingPharmacist.getEmail();
        String body = "Prihvaćen zahtev za odsustvo u periodu od: "
                + request.getStart()+ " do: " + request.getEnd() + "\n"+
                "\n\n\n"+apoteka.getName()+" Vam želi prijatan odmor.";
        try {
            emailService.sendEmailAsync("tim10isamrs2021@gmail.com",
                    mail,
                    "Prihvaćen zahtev za odsustvo!",
                    body);
        } catch (InterruptedException e) {
            System.out.println("Greska prilikom slanja maila prihvacen zahtev za odsustvo!");
        }
    }

/**********************************************************IZVESTAJI**********************************************************/


    public ChartDTO getAppointmentsMonthlyChart(String username)
    {
        Pharmacy p = pharmacyAdminService.getPharmacyByAdminUsername(username);
        String[] labelsMesecni = {"Januar", "Februar", "Mart","April", "Maj", "Jun","Jul", "Avgust","Septembar", "Oktobar", "Novembar", "Decembar"};
        int tekucaGodina = LocalDateTime.now().getYear();
        LocalDateTime start = LocalDateTime.of(tekucaGodina,1,1,0,0,0);
        LocalDateTime end = LocalDateTime.of(tekucaGodina+1,1,1,0,0,0);

        ChartDTO mesecniIzvestaj   = new ChartDTO(labelsMesecni,1);
        DataSetDTO mesecniDataset = mesecniIzvestaj.getdatasets()[0];
        mesecniDataset.setLabel("Mesečni Izveštaj");
        mesecniDataset.setData(new ArrayList<Integer>(Collections.nCopies(12, 0)));       //Liste odgovarajuce velicine ispunjene 0.


        //dobavi sve iz tekce godine
        Set<Examination> examinations   = examinationsRepository.getBetweenDatesForPharmacy(p.getId(),start, end);
        Set<Consultation> consultations = consultationsRepository.getBetweenDatesForPharmacy(p.getId(),start, end);
        
        Set<Appointment> allAppointments = new HashSet<>();
        allAppointments.addAll(examinations);
        allAppointments.addAll(consultations);

        //za svaki mesec u tekucoj godini rasporedi gde Sta ide
        
       Map<Integer, Long> rezMesecni = allAppointments.stream().collect(Collectors.groupingBy(x -> x.getStart().getMonthValue(), Collectors.counting()));

       for(int i = 1; i< 13; i++)
       {
           if(rezMesecni.containsKey(i))
               mesecniDataset.getData().set(i-1, rezMesecni.get(i).intValue());
       }

       return mesecniIzvestaj;
    }


    public ChartDTO getAppointmentsQuartalChart(ChartDTO mesecni)
    {
        String[] labelsKvartalni = {"Q1", "Q2", "Q3","Q4"};
        ChartDTO kvartalniIzvestaj = new ChartDTO(labelsKvartalni,1);
        DataSetDTO kvartalniDataset = kvartalniIzvestaj.getdatasets()[0];
        kvartalniDataset.setLabel("Kvartalni Izveštaj");
        kvartalniDataset.setData(new ArrayList<Integer>(Collections.nCopies(4, 0)));
        //napravi kvartale
        int count = 0;
        for(int i = 0; i<4; i++ ) {

            int kvartalTotal = 0;
            for (int j = 0; j < 3; j++) {
                //za svaki kvartal uzimamo po tri meseca; count ide 0-11
                kvartalTotal += mesecni.getdatasets()[0].getData().get(count);
                count++;
            }
            kvartalniDataset.getData().set(i,kvartalTotal);
        }

        return kvartalniIzvestaj;
    }

    public ChartDTO getAppointments5YearChart(String username)
    {
        Pharmacy p = pharmacyAdminService.getPharmacyByAdminUsername(username);
        int tekucaGodina = LocalDateTime.now().getYear();
        String[] labels = new String[5];
        //popuni od nazad da bude tekuca-5 ... -> tekuca
        for(int i = 0; i<5; i++)
        {
            labels[4-i]= Integer.toString(tekucaGodina-i);
        }
        ChartDTO godisnji = new ChartDTO(labels,1);
        DataSetDTO godisnjiDataset = godisnji.getdatasets()[0];
        godisnjiDataset.setData(new ArrayList<Integer>(Collections.nCopies(5, 0)));
        godisnjiDataset.setLabel("Godišjni Izveštaj");
        int godina = tekucaGodina-4;
        for(int i = 0; i<5; i++)
        {
            int rez = consultationsRepository.countBetweenDatesForPharmacy(p.getId(),LocalDateTime.of(godina+i,1,1,0,0,0),
                    LocalDateTime.of(godina+i+1,1,1,0,0,0));
            rez += examinationsRepository.countBetweenDatesForPharmacy(p.getId(),LocalDateTime.of(godina+i,1,1,0,0,0),
                    LocalDateTime.of(godina+i+1,1,1,0,0,0));
            godisnjiDataset.getData().set(i,rez);
        }

        return godisnji;
    }


    public ChartDTO getMedicineMonthlyChart(String username)
    {
        Pharmacy p = pharmacyAdminService.getPharmacyByAdminUsername(username);
        String[] labelsMesecni = {"Januar", "Februar", "Mart","April", "Maj", "Jun","Jul", "Avgust","Septembar", "Oktobar", "Novembar", "Decembar"};
        int tekucaGodina = LocalDateTime.now().getYear();
        LocalDateTime start = LocalDateTime.of(tekucaGodina,1,1,0,0,0);
        LocalDateTime end = LocalDateTime.of(tekucaGodina+1,1,1,0,0,0);

        ChartDTO mesecniIzvestaj   = new ChartDTO(labelsMesecni,1);
        DataSetDTO mesecniDataset = mesecniIzvestaj.getdatasets()[0];
        mesecniDataset.setLabel("Mesečni Izveštaj");
        mesecniDataset.setData(new ArrayList<Integer>(Collections.nCopies(12, 0)));       //Liste odgovarajuce velicine ispunjene 0.

        //nadji sve od ove godine
        List<ReservationList> reservations =  reservationRepository.getAllForPharmacyBetweenDates(p.getId(),start,end);

        //odvoji po mesecima liste
        Map<Integer, List<ReservationList>> rezMesecni = reservations.stream().collect(Collectors.groupingBy(x -> x.getDeadline().getMonthValue()));


        for(int i = 1; i< 13; i++)
        {
            //za svaki mesec
            if(rezMesecni.containsKey(i))
            {
                List<ReservationList> sveRezervacijeMeseca = rezMesecni.get(i);
                //sve stavke svih narudzbi iz ovog meseca
                int brojLekovaZamesec = sveRezervacijeMeseca.stream().map(ReservationList::getReservationItems)
                        .flatMap(Collection::stream).mapToInt(x -> x.getQuantity()).sum();
                mesecniDataset.getData().set(i,brojLekovaZamesec);
            }
        }

        return mesecniIzvestaj;

    }

    public ChartDTO getMedicineQuartalChart(ChartDTO mesecni)
    {
        String[] labelsKvartalni = {"Q1", "Q2", "Q3","Q4"};
        ChartDTO kvartalniIzvestaj = new ChartDTO(labelsKvartalni,1);
        DataSetDTO kvartalniDataset = kvartalniIzvestaj.getdatasets()[0];
        kvartalniDataset.setLabel("Kvartalni Izveštaj");
        kvartalniDataset.setData(new ArrayList<Integer>(Collections.nCopies(4, 0)));
        //napravi kvartale
        int count = 0;
        for(int i = 0; i<4; i++ ) {

            int kvartalTotal = 0;
            for (int j = 0; j < 3; j++) {
                //za svaki kvartal uzimamo po tri meseca; count ide 0-11
                kvartalTotal += mesecni.getdatasets()[0].getData().get(count);
                count++;
            }
            kvartalniDataset.getData().set(i,kvartalTotal);
        }

        return kvartalniIzvestaj;
    }

    public ChartDTO getMedicine5YearChart(String username)
    {
        Pharmacy p = pharmacyAdminService.getPharmacyByAdminUsername(username);
        int tekucaGodina = LocalDateTime.now().getYear();
        String[] labels = new String[5];
        //popuni od nazad da bude tekuca-5 ... -> tekuca
        for(int i = 0; i<5; i++)
        {
            labels[4-i]= Integer.toString(tekucaGodina-i);
        }
        ChartDTO godisnji = new ChartDTO(labels,1);
        DataSetDTO godisnjiDataset = godisnji.getdatasets()[0];
        godisnjiDataset.setData(new ArrayList<Integer>(Collections.nCopies(5, 0)));
        godisnjiDataset.setLabel("Godišjni Izveštaj");
        int godina = tekucaGodina-4;
        for(int i = 0; i<5; i++)
        {
            int rez = reservationRepository.countAllForPharmacyBetweenDates(p.getId(),LocalDateTime.of(godina+i,1,1,0,0,0),
                    LocalDateTime.of(godina+i+1,1,1,0,0,0));
            godisnjiDataset.getData().set(i,rez);
        }

        return godisnji;
    }

    public ChartDTO getPrihodiChart(String username, LocalDate datumOd, LocalDate datumDo) {
        Pharmacy p = pharmacyAdminService.getPharmacyByAdminUsername(username);
        LocalDateTime start = datumOd.atStartOfDay();
        LocalDateTime end = datumDo.atStartOfDay();

        //nadji u bazi sve iz intervala
        List<ReservationList> reservations =  reservationRepository.getAllForPharmacyBetweenDates(p.getId(),start,end);
        Set<Examination> examinations =  examinationsRepository.getBetweenDatesForPharmacy(p.getId(),start,end);
        Set<Consultation> consultations =  consultationsRepository.getBetweenDatesForPharmacy(p.getId(),start,end);

        //odvoji po mesecima liste
        Map<LocalDate, List<ReservationList>> rezMesecni = reservations.stream().collect(Collectors.groupingBy(x -> LocalDate.of(x.getDeadline().getYear(),x.getDeadline().getMonthValue(),1) ));
        Map<LocalDate, List<Examination>> rezExaminations = examinations.stream().collect(Collectors.groupingBy(x -> LocalDate.of(x.getStart().getYear(),x.getStart().getMonthValue(),1) ));
        Map<LocalDate, List<Consultation>> rezConsultations = consultations.stream().collect(Collectors.groupingBy(x -> LocalDate.of(x.getStart().getYear(),x.getStart().getMonthValue(),1) ));

        //spoji sve datume da mozes da iteriras, oni idu na x osu...
        Set<LocalDate> keySet = new HashSet<>();
        keySet.addAll(rezMesecni.keySet());
        keySet.addAll(rezExaminations.keySet());
        keySet.addAll(rezConsultations.keySet());
        keySet = keySet.stream().sorted().collect(Collectors.toSet());

        //napravi labela za x-osu i DTO...
        String[] labels = new String[keySet.size()];
        ChartDTO mesecniIzvestaj   = new ChartDTO(labels,1);
        DataSetDTO mesecniDataset = mesecniIzvestaj.getdatasets()[0];
        mesecniDataset.setLabel("Izveštaj Prihoda");
        mesecniDataset.setData(new ArrayList<Integer>(Collections.nCopies(labels.length, 0)));       //Liste odgovarajuce velicine ispunjene 0.

        int count = 0;
        for(LocalDate l : keySet)
        {
            //izracunaj uk cenu lekova
            Double sumMeds  = rezMesecni.containsKey(l)       ? rezMesecni.get(l).stream().mapToDouble(ReservationList::getTotalPrice).sum():0.0;
            Double sumExam  = rezExaminations.containsKey(l)  ? rezExaminations.get(l).stream().mapToDouble(Examination::getPrice).sum():0.0;
            Double sumCons  = rezConsultations.containsKey(l) ? rezConsultations.get(l).stream().mapToDouble(Consultation::getPrice).sum():0.0;

            Double sumTotal = sumMeds + sumCons + sumExam;
            //postavi labelu mesec/godina
            labels[count] = l.getMonthValue()+"/"+l.getYear();
            mesecniDataset.getData().set(count,sumTotal.intValue());
            count++;
        }


        return mesecniIzvestaj;

    }

}

