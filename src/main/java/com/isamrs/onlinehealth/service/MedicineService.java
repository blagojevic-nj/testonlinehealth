package com.isamrs.onlinehealth.service;

import com.isamrs.onlinehealth.dto.*;
import com.isamrs.onlinehealth.model.*;
import com.isamrs.onlinehealth.repository.*;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class MedicineService {

    @Autowired
    private MedicineRepository medicineRepository;
    @Autowired
    private RatingItemRepository ratingItemRepository;

    @Autowired
    private PatientService patientService;

    @Autowired
    private PharmacyService pharmacyService;

    @Autowired
    private PharmacyAdminService pharmacyAdminService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private PriceListItemRepository priceListItemRepository;

    @Autowired
    private PriceListRepository priceListRepository;

    @Autowired
    private MedicineInquiryRepository medicineInquiryRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    PharmacyRepository pharmacyRepository;


    public ArrayList<MedicineDTO> getAllMedicines() {
        ArrayList<MedicineDTO> retVal = new ArrayList<>();
        for (Medicine m : medicineRepository.findAll())
            retVal.add(new MedicineDTO(m.getId(), m.getIdentifier(), m.getName(), m.getManufacturer(), m.getDescription(), m.getType(), false));
        return retVal;
    }

    public ArrayList<MedicineDTO> getMedicinesByName(String name) {
        ArrayList<MedicineDTO> retVal = new ArrayList<>();
        for(Medicine m :medicineRepository.findByNameContainingIgnoreCase(name))
            retVal.add(new MedicineDTO(m.getId(), m.getIdentifier(), m.getName(), m.getManufacturer(), m.getDescription(), m.getType(),false));
        return retVal;
    }

    public ArrayList<MedicineDTO> getMedicinesByIdentifier(String identifier) {
        ArrayList<MedicineDTO> retVal = new ArrayList<>();
        for(Medicine m :medicineRepository.findByIdentifierContaining(identifier))
            retVal.add(new MedicineDTO(m.getId(), m.getIdentifier(), m.getName(), m.getManufacturer(), m.getDescription(), m.getType(),false));
        return retVal;
    }


    public CompleteMedicineDTO getCompleteMedicineDTO(String identifier)
    {
        Medicine m = medicineRepository.findByIdentifierEquals(identifier);
        return  new CompleteMedicineDTO(
                m.getIdentifier(),
                m.getName(),
                m.getManufacturer(),
                m.getDescription(),
                m.getType(),
                m.getDailyDose(),
                m.isPrescription(),
                m.getRemarks(),
                m.getDeleted()
                );
    }

    public boolean isCreateAvailable(Medicine m) {
        return !medicineRepository.existsByIdentifierEquals(m.getIdentifier());
    }

    public void createNewMedicine(Medicine m) {


        while(true)
        {
            long generatedLong = new Random().nextLong();
            if(!medicineRepository.existsById(generatedLong))
            {
                m.setId(generatedLong);
                break;
            }
        }
        medicineRepository.save(m);
    }

    public boolean deleteMedicine(String identifier) {
        Medicine m = medicineRepository.findByIdentifierEquals(identifier);
        m.setDeleted(true);
        medicineRepository.save(m);

        return true;
    }

    public MedicineDTO getMedicineById(Long id) {
        Optional<Medicine> m = medicineRepository.findById(id);

        if (m.isEmpty()) return null;

        return new MedicineDTO(m.get().getId(), m.get().getIdentifier(), m.get().getName(), m.get().getManufacturer(), m.get().getDescription(), m.get().getType(), false);
    }
    public Medicine addMedicine(AddedMedicineDTO addedMedicineDTO){
        Set<Medicine> replacementMedicine = new HashSet<>();
        for (String replacementMedicineID : addedMedicineDTO.getReplacementMedicines()) {
            if(findMedicine(replacementMedicineID)!=null){
                replacementMedicine.add(findMedicine(replacementMedicineID));
            }
            else
                return null;
        }
        Medicine medicine = new Medicine(null, addedMedicineDTO.getIdentifier(), addedMedicineDTO.getName(), addedMedicineDTO.getManufacturer(), addedMedicineDTO.getDescription(),
                addedMedicineDTO.getType(), replacementMedicine, addedMedicineDTO.getDailyDose(), addedMedicineDTO.isPrescription(), addedMedicineDTO.getRemarks());
        medicine.setRating(0.0);
        return medicineRepository.save(medicine);
    }

    public Medicine findMedicine(String identifier) {
        return medicineRepository.findOneByIdentifier(identifier);
    }

    public void addAllergies(Long id, String username){
        Patient patient = patientService.findByUsername(username);
        Optional<Medicine> medicine = medicineRepository.findById(id);
        if (patient != null && medicine.isPresent()){
            patient.getAllergies().add(medicine.get());
            patientService.save(patient);
        }
    }

    public void removeAllergies(Long id, String username){
        Patient patient = patientService.findByUsername(username);
        Optional<Medicine> medicine = medicineRepository.findById(id);
        if (patient != null && medicine.isPresent()){
            patient.getAllergies().remove(medicine.get());
            patientService.save(patient);
        }
    }

    public ArrayList<MedicineDTO> getAllergies(String username){
        Patient patient = patientService.findByUsername(username);
        ArrayList<MedicineDTO> retVal = new ArrayList<>();
        for (Medicine m : patient.getAllergies()){
            retVal.add(new MedicineDTO(m.getId(), m.getIdentifier(), m.getName(),
                    m.getManufacturer(),
                    m.getDescription(), m.getType(), false));

        }

        return retVal;
    }

    public ArrayList<MedicineDTO> getAllForPharmacy(long id) {
        ArrayList<MedicineDTO> result = new ArrayList<MedicineDTO>();
        Pharmacy apoteka = pharmacyService.findById(id);

        List<Medicine> allMedicinesList = medicineRepository.findAll();
        Set<Medicine> myMedicines = new HashSet<>();
        for(PriceListItem pli : apoteka.getPricelist().getPriceListItems())
        {
            //&& pli.getEnd().isAfter(LocalDateTime.now())
            if(!pli.getDeleted() )
                myMedicines.add(pli.getLek());

        }
        for (Medicine m : allMedicinesList)
        {
            MedicineDTO medicineToAdd = new MedicineDTO(m);
            if(myMedicines.contains(m))
            {
                medicineToAdd.setBelongsToPharmacy(true);
            }
            result.add(medicineToAdd);

        }

        return result;


    }


    public ArrayList<MedicineDTO> searchMedicineAdmin(long id, String name, String identifier) {
        boolean idEmpty=false;
        ArrayList<MedicineDTO> meds = getAllForPharmacy(id);
        if(name.toLowerCase().trim().equals("")&& identifier.toLowerCase().trim().equals("") )
            return meds;
        if(identifier.toLowerCase().trim().equals(""))
            idEmpty=true;

        ArrayList<MedicineDTO> ret = new ArrayList<>();
        for (MedicineDTO m : meds)
        {
            if (m.getName().toLowerCase().contains(name.toLowerCase().trim()))
                ret.add(m);
        }
        if (idEmpty)
            return ret;
        else{
            ArrayList<MedicineDTO> filtered = new ArrayList<>();
            for (MedicineDTO m : ret)
            {
                if (m.getIdentifier().toLowerCase().equals(identifier.toLowerCase().trim()))
                    filtered.add(m);
            }
            return filtered;

        }
    }

    public ArrayList<MedicineDTO> addToPharmacy(long pharmacyId, long medicineId) {
        ArrayList<MedicineDTO> result = new ArrayList<MedicineDTO>();
        Pharmacy apoteka = pharmacyService.findById(pharmacyId);
        Optional<Medicine> opt = medicineRepository.findById(medicineId);
        Medicine m=null;
        if(opt.isPresent())
            m=opt.get();
        else
            return getAllForPharmacy(pharmacyId);
        PriceListItem pli = new PriceListItem();
        pli.setAvailableQuantity(0);
        pli.setDeleted(false);
        pli.setLek(m);
        pli.setStart(LocalDateTime.now());
        pli.setEnd(LocalDateTime.of(2100,1,1,1,1));
        pli.setPrice(0.0);

        apoteka.getPricelist().getPriceListItems().add(pli);

        priceListItemRepository.save(pli);
        priceListRepository.save(apoteka.getPricelist());
        pharmacyService.save(apoteka);
        return getAllForPharmacy(pharmacyId);
    }

    private boolean medicineReserved(Pharmacy p, Medicine m)
    {
        ArrayList<ReservationListDTO> list = reservationService.getForPharmacy(p.getId());
        for (ReservationListDTO l : list)
        {
            for(ReservationItemDTO item :l.getItems())
            {
                if(item.getMedicineId()==m.getId())
                return true;
            }
        }
        return false;
    }

    public ArrayList<MedicineDTO> deleteFromPharmacy(long pharmacyId, long medicineId) {
        Pharmacy apoteka = pharmacyService.findById(pharmacyId);
        Medicine m = medicineRepository.getOne(medicineId);
        if(medicineReserved(apoteka,m))
            return getAllForPharmacy(pharmacyId);
        PriceListItem toRemove=null;
        for (PriceListItem pli : apoteka.getPricelist().getPriceListItems())
        {
            if (pli.getLek().getId()==medicineId)
            {
                toRemove=pli;
                break;
            }
        }
        if (toRemove == null)
            return getAllForPharmacy(pharmacyId);
        apoteka.getPricelist().getPriceListItems().remove(toRemove);
        priceListRepository.save(apoteka.getPricelist());
        pharmacyService.save(apoteka);
        return getAllForPharmacy(pharmacyId);

    }



    public ArrayList<MedicineDTO> getAllMedicinesNonAllergic(Long patientId) {
        Optional<Patient> p = patientService.findById(patientId);
        if (!p.isPresent()) return null;
        Hibernate.initialize(p.get().getAllergies());
        Hibernate.unproxy(p);
        ArrayList<MedicineDTO> retVal = new ArrayList<>();
        for (Medicine m : medicineRepository.findAll())
            if (!p.get().getAllergies().contains(m))
                retVal.add(new MedicineDTO(m.getId(), m.getIdentifier(), m.getName(), m.getManufacturer(), m.getDescription(), m.getType(), false));
        return retVal;
    }

    @Transactional
    public String getAvailableOrReplacement(Long patientId, String medicineIdentifier, Long pharmacyId, Long quantity, String username) throws InterruptedException {
        Optional<Patient> patient = patientService.findById(patientId);
        Medicine medicine = medicineRepository.findOneByIdentifier(medicineIdentifier);
        Pharmacy pharmacy = pharmacyService.findById(pharmacyId);
        Hibernate.initialize(patient.get().getAllergies());
        Hibernate.initialize(pharmacy.getPricelist());
        Hibernate.unproxy(pharmacy);
        Hibernate.unproxy(patient);
        for (PriceListItem priceListItem : pharmacy.getPricelist().getPriceListItems()) {
            if (priceListItem.getLek().equals(medicine))
                if (priceListItem.getAvailableQuantity() > quantity)
                    return medicineIdentifier;
        }

       /* emailService.sendEmailAsync(
                "",
                pharmacyAdminService.getPharmacyAdminByPharmacyId(pharmacyId).getEmail(),
                "Nema leka " + medicineIdentifier,
                "Obavestovamo vas da je zatrazen lek " + medicineIdentifier + " u kolicini " + quantity + ", medjutim tog lika nije bilo u toj kolicinu u apoteci"
        );
*/

        MedicineInquiry medicineInquiry = new MedicineInquiry(medicine, LocalDateTime.now(), quantity, userService.findByUsername(username));

        medicineInquiryRepository.save(medicineInquiry);

        pharmacyAdminService.getPharmacyAdminByPharmacyId(pharmacyId).getMedicineInquiries().add(medicineInquiry);


        for (Medicine replacement : medicine.getReplacementMedicines()) {
            for (PriceListItem priceListItem : pharmacy.getPricelist().getPriceListItems()) {
                if (priceListItem.getLek().equals(replacement) && !patient.get().getAllergies().contains(replacement))
                    if (priceListItem.getAvailableQuantity() > quantity)
                        return replacement.getIdentifier();
            }
        }
        return "";
    }

    @Transactional(readOnly = false)
    public Boolean setRating(String username, Long id, Double rating) {
        Patient patient = patientService.findByUsername(username);
        Medicine med = medicineRepository.findOneByIdForRating(id);

        if (patient == null || med == null)
            return false;

        Medicine medicine = med;
        boolean added = false;
        for (RatingItem ri : medicine.getRatings()) {
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
            medicine.getRatings().add(ri);
        }

        double sum = 0.0;
        double couner = 0;
        for (RatingItem ri : medicine.getRatings()) {
            if (!ri.getDeleted()) {
                sum += ri.getRating();
                couner++;
            }
        }

        medicine.setRating(sum / couner);
        medicineRepository.save(medicine);
        return true;
    }
      public ArrayList<MedicineDTO> getAllBelongsToPharmacy(String username) {

        Pharmacy apoteka = pharmacyAdminService.getPharmacyByAdminUsername(username);
        ArrayList<MedicineDTO> result = new ArrayList<MedicineDTO>();

        List<Medicine> allMedicinesList = medicineRepository.findAll();
        Set<Medicine> myMedicines = new HashSet<>();
        for(PriceListItem pli : apoteka.getPricelist().getPriceListItems())
        {
            if(!pli.getDeleted() )
                myMedicines.add(pli.getLek());

        }
        for (Medicine m : allMedicinesList)
        {
            MedicineDTO medicineToAdd = new MedicineDTO(m);
            if(myMedicines.contains(m))
            {
                medicineToAdd.setBelongsToPharmacy(true);
                result.add(medicineToAdd);
            }
        }

        return result;


    }

    public List<Medicine> findAllMedicineWithCost(){
        List<Pharmacy> pharmacies = pharmacyRepository.findAll();
        List<Medicine> list = new ArrayList<>();
        for(Medicine m : medicineRepository.findAll()) {
            for (Pharmacy p : pharmacies) {
                for (PriceListItem pp : p.getPricelist().getPriceListItems()) {
                    if (pp.getLek().getIdentifier().equals(m.getIdentifier())) {
                        list.add(m);
                        break;
                    }
                }
            }
        }
        return list;
    }

    public List<Medicine> findAllMedicine(){
        return medicineRepository.findAll();
    }

    public List<SearchItemMedicinePharmacyDTO> getCostsForMedicine(Medicine m){
        List<Pharmacy> pharmacies = pharmacyRepository.findAll();
        List<SearchItemMedicinePharmacyDTO> list = new ArrayList<>();
        for(Pharmacy p : pharmacies){
            for(PriceListItem pp  : p.getPricelist().getPriceListItems()){
                if(pp.getLek().getIdentifier().equals(m.getIdentifier())){
                    list.add(new SearchItemMedicinePharmacyDTO(p.getName(), pp.getPrice().toString()));
                    break;
                }
            }
        }
        return list;
    }
}
