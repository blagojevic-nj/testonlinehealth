package com.isamrs.onlinehealth.service;


import com.isamrs.onlinehealth.dto.ReservationIssuingDTO;
import com.isamrs.onlinehealth.dto.ReservationItemDTO;
import com.isamrs.onlinehealth.dto.ReservationListDTO;
import com.isamrs.onlinehealth.dto.StatsInfoDTO;
import com.isamrs.onlinehealth.model.*;
import com.isamrs.onlinehealth.repository.*;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.util.*;
import java.util.concurrent.ScheduledFuture;
import java.util.stream.Collectors;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private ReservationItemRepository reservationItemRepository;
    @Autowired
    private MedicineRepository medicineRepository;
    @Autowired
    private PriceListRepository priceListRepository;
    @Autowired
    private PriceListItemRepository priceListItemRepository;

    @Autowired
    private PharmacyRepository pharmacyRepository;

    @Autowired
    private PharmacistService pharmacistService;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private MailSender mailSender;

    @Autowired
    private PatientService patientService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private LoyaltyProgramService loyaltyProgramService;

    @Transactional(readOnly = false)
    public Boolean addReservation(ReservationListDTO reservationListDTO, PharmacyService pharmacyService,
                                  PatientService patientService) {
        Patient patient = patientService.findByUsername(reservationListDTO.getPatient());

        if (patient == null) {
            System.out.println("Pacijent je null");
            return false;
        }
        Optional<Pharmacy> pharmacy = pharmacyService.getPharmacyById(reservationListDTO.getPharmacy());
        if (pharmacy.isEmpty()) {
            System.out.println("Apoteka je null!");
            return false;

        }

        StatsInfoDTO si = patientService.getStats(patient.getUsername());
        ReservationList reservationList = new ReservationList();
        reservationList.setPatient(patient);
        reservationList.setPharmacy(pharmacy.get());
        PriceList pl = priceListRepository.findOneById(pharmacy.get().getPricelist().getId());
        reservationList.setDeadline(reservationListDTO.getDeadline());
        double totalPrice = 0;

        double promotionDiscount = 0;
        boolean subs = false;
        if(pharmacy.get().getSubscribers().contains(patient)){
            subs = true;
            for(Promotion p: pharmacy.get().getPromotions()){
                if(!p.getDeleted() && p.getStart().isBefore(LocalDateTime.now()) && p.getEnd().isAfter(LocalDateTime.now())){
                    promotionDiscount = p.getDiscountRate();
                }
            }
        }

        for(ReservationItemDTO item : reservationListDTO.getItems()){
            ReservationItem item1 = new ReservationItem();
            Optional<Medicine> medicine = medicineRepository.findById(item.getMedicineId());
            if(medicine.isEmpty()) {
                System.out.println("Lek je null!");
                return false;
            }

            item1.setMedicine(medicine.get());
            if(!reduceQuantity(pl, item)){
                System.out.println("Reduce vraca null!");
                return false;
            }
            item1.setQuantity(item.getQuantity());
            item1.setPrice(item.getPrice() * (100 - si.getDiscount())/100);

            if(subs){
                List<Discount> akcije = pharmacy.get().getDiscounts().stream().filter(d -> !d.getDeleted() && (d.getStart().isBefore(LocalDate.now()) || d.getStart().isEqual(LocalDate.now()) )
                        && (d.getEnd().isAfter(LocalDate.now()) || d.getEnd().isEqual(LocalDate.now()))).collect(Collectors.toList());
                for(Discount d : akcije){
                    List<DiscountItem> dis = d.getDiscountItems().stream().filter(i -> i.getLek().getId().equals(medicine.get().getId())).collect(Collectors.toList());
                    if(dis.size() != 0){
                        item1.setPrice(dis.stream().findFirst().get().getPrice());
                        item1.setPrice(item1.getPrice() * (100 - si.getDiscount())/100);
                        item1.setPrice(item1.getPrice() * (1 - promotionDiscount));
                        item1.setPrice(item1.getPrice() * item1.getQuantity());
                    }
                }
            }
            reservationList.getReservationItems().add(item1);
            reservationItemRepository.save(item1);

        }

        for(PriceListItem plitem : pl.getPriceListItems()) {
            priceListItemRepository.save(plitem);
        }

        for(ReservationItem ri : reservationList.getReservationItems()){
            totalPrice += ri.getPrice();
        }
        priceListRepository.save(pl);
        reservationList.setStatus(ReservationStatus.RESERVED);
        reservationList.setTotalPrice(totalPrice);
        reservationList = reservationRepository.save(reservationList);
        sendMail(reservationList.getId(), patient.getEmail());
        patient.getReservations().add(reservationList);
        patientService.save(patient);

        return true;
    }

    private void sendMail(Long id, String to){
        try {
            emailService.sendEmailAsync("",
                    to,
                    "Rezervacija leka",
                    "Uspesno ste rezervisali lekove. Broj vase rezervacije: " + id + ".");
        } catch (InterruptedException e) {
            System.out.println("Neka greska!");
        }
    }

    private boolean reduceQuantity(PriceList pl, ReservationItemDTO reservationItemDTO){
        for(PriceListItem plItem : pl.getPriceListItems()){
            if(plItem.getLek().getId().equals(reservationItemDTO.getMedicineId())){
                if(plItem.getAvailableQuantity() >= reservationItemDTO.getQuantity()){
                    plItem.setAvailableQuantity(plItem.getAvailableQuantity()-reservationItemDTO.getQuantity());
                    return true;
                }
                System.out.println("Kolicina leka " + reservationItemDTO.getMedicineId() + " u apoteci je:" + plItem.getAvailableQuantity() + " a meni je potrebno " + reservationItemDTO.getQuantity());
                return false;
            }
        }
        return false;
    }

    @Transactional(readOnly = false)
    public Boolean cancelReservation(Long id){
        Optional<ReservationList> reservationList = reservationRepository.findById(id);
        if(reservationList.isEmpty()) return false;
        reservationList.get().setStatus(ReservationStatus.CANCELED);
        PriceList pl = priceListRepository.findOneById(reservationList.get().getPharmacy().getPricelist().getId());
        for(ReservationItem ri : reservationList.get().getReservationItems()){
            addQuantity(pl, ri);
        }
        reservationRepository.save(reservationList.get());
        priceListRepository.save(pl);
        return true;
    }

    private void addQuantity(PriceList pl, ReservationItem ri){
        for(PriceListItem plItem : pl.getPriceListItems()) {
            if (plItem.getLek().getId().equals(ri.getMedicine().getId())) {
                plItem.setAvailableQuantity(plItem.getAvailableQuantity() + ri.getQuantity());
                priceListItemRepository.save(plItem);
            }
        }
    }

    public ArrayList<ReservationListDTO> getAll(String username){
        ArrayList<ReservationListDTO> retVal = new ArrayList<ReservationListDTO>();
        for(ReservationList rl : reservationRepository.getAllForUser(username)){
            ReservationListDTO rlDTO = new ReservationListDTO();
            rlDTO.setId(rl.getId());
            rlDTO.setPatient(rl.getPatient().getUsername());
            rlDTO.setPharmacy(rl.getPharmacy().getId());
            rlDTO.setStatus(rl.getStatus());
            rlDTO.setDeadline(rl.getDeadline());
            rlDTO.setTotalPrice(rl.getTotalPrice());
            rlDTO.setItems(new HashSet<>());
            for(ReservationItem ri : rl.getReservationItems()){
                rlDTO.getItems().add(new ReservationItemDTO(ri.getMedicine().getId(), ri.getQuantity(), ri.getPrice()));
            }

            retVal.add(rlDTO);
        }
        return retVal;
    }

    public ArrayList<ReservationListDTO> getForPharmacy(long id){
        ArrayList<ReservationListDTO> retVal = new ArrayList<ReservationListDTO>();
        for(ReservationList rl : reservationRepository.getAllForPharmacy(id)) {
            ReservationListDTO rlDTO = new ReservationListDTO();
            rlDTO.setId(rl.getId());
            rlDTO.setPatient(rl.getPatient().getUsername());
            rlDTO.setPharmacy(rl.getPharmacy().getId());
            rlDTO.setStatus(rl.getStatus());
            rlDTO.setDeadline(rl.getDeadline());
            rlDTO.setTotalPrice(rl.getTotalPrice());
            rlDTO.setItems(new HashSet<>());
            for (ReservationItem ri : rl.getReservationItems()) {
                rlDTO.getItems().add(new ReservationItemDTO(ri.getMedicine().getId(), ri.getQuantity(), ri.getPrice()));
            }

            retVal.add(rlDTO);
        }
        return retVal;


    }

    @Transactional(readOnly = false)
    public Boolean addReservationFromAppointment(ReservationListDTO reservationListDTO, PharmacyService pharmacyService, PatientService patientService) {

        if (reservationListDTO.getItems().size() == 0) return false;

        reservationListDTO.setDeadline(LocalDateTime.now().plusDays(7));

        Pharmacy pharmacy = pharmacyRepository.findOneById(reservationListDTO.getPharmacy());
        Optional<Patient> patient = patientRepository.findById(Long.valueOf(reservationListDTO.getPatient()));

        reservationListDTO.setPatient(patient.get().getUsername());


        int totalPrice = 0;

        Hibernate.initialize(pharmacy.getPricelist());
        Hibernate.unproxy(pharmacy);
        PriceList pl = priceListRepository.findOneById(pharmacy.getPricelist().getId());
        for (ReservationItemDTO reservationItemDTO : reservationListDTO.getItems()) {
            reservationItemDTO.setPrice(
                    pl.getPriceListItems()
                            .stream()
                            .filter(p -> p.getLek().getId().equals(reservationItemDTO.getMedicineId())).findFirst()
                            .get().getPrice() * reservationItemDTO.getQuantity()
            );
            totalPrice += reservationItemDTO.getPrice();
        }
        reservationListDTO.setTotalPrice(totalPrice);
        priceListRepository.save(pl);

        return this.addReservation(reservationListDTO, pharmacyService, patientService);
    }

    public ArrayList<ReservationIssuingDTO> getForIssuing(String id, String username) {

        Pharmacy pharmacy = pharmacistService.findPharmacy(pharmacistService.findOneByUsername(username));

        return (ArrayList<ReservationIssuingDTO>) reservationRepository.findForIssuing(pharmacy.getId(), LocalDateTime.now().plusDays(1), "%" + id + "%")
                .stream()
                .map(ReservationIssuingDTO::new)
                .collect(Collectors.toList());

    }

    @Transactional
    public String issue(String reservationId, String username) {
        Pharmacy pharmacy = pharmacistService.findPharmacy(pharmacistService.findOneByUsername(username));

        ReservationList reservationList = reservationRepository.findOneForIssueById(Long.valueOf(reservationId));

        if (!reservationList.getPharmacy().equals(pharmacy)) {
            return "pogresna apoteka :(";
        }

        if (!reservationList.getDeadline().isAfter(LocalDateTime.now().minusDays(1))) {
            return "prosao rok :(";
        }

        if (!reservationList.getStatus().equals(ReservationStatus.RESERVED)) {
            return "rezervacija vez izdata ili otkazana :(";
        }

        reservationList.setStatus(ReservationStatus.ISSUED);

        Patient patient = patientService.findByUsername(reservationList.getPatient().getUsername());

        LoyaltyProgram loyaltyProgram = null;

        try {
            loyaltyProgram = loyaltyProgramService.getLoyaltyPrograms().get(0);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        try {
            emailService.sendEmailAsync("",
                    patient.getEmail(),
                    "Pregled",
                    "Uspesno vam je izdata rezervacija");
        } catch (InterruptedException exception) {
            System.out.println("Neka greska!");
        }

        if (loyaltyProgram != null) {

            double points = 0;

            for (ReservationItem reservationItem : reservationList.getReservationItems()) {
                points += loyaltyProgram.getMedicinePoints().getOrDefault(reservationItem.getMedicine(), (double) 0);
            }

            patient.addPoints(points);


            patientService.save(patient);
        }

        reservationRepository.save(reservationList);


        return "Uspesno izdat lek";

    }
}
