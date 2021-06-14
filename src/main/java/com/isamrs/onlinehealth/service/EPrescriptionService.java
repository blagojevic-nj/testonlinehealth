package com.isamrs.onlinehealth.service;

import com.isamrs.onlinehealth.dto.EqrPharmacyDTO;
import com.isamrs.onlinehealth.dto.EqrPharmacyPriceListDTO;
import com.isamrs.onlinehealth.dto.EqrPrescriptionDTO;
import com.isamrs.onlinehealth.dto.EqrPrescriptionItemDTO;
import com.isamrs.onlinehealth.model.*;
import com.isamrs.onlinehealth.repository.*;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class EPrescriptionService {
    @Autowired
    private EPrescriptionRepository ePrescriptionRepository;
    @Autowired
    private EPrescriptionItemRepository ePrescriptionItemRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private MedicineRepository medicineRepository;
    @Autowired
    private PharmacyRepository pharmacyRepository;
    @Autowired
    PriceListRepository priceListRepository;
    @Autowired
    PriceListItemRepository priceListItemRepository;

    @Transactional
    public EPrescription saveDTO(EqrPrescriptionDTO dto){
        EPrescription ePrescription = new EPrescription();
        if(!dto.getId().equals(""))
            ePrescription.setId(Long.parseLong(dto.getId()));
        ePrescription.setPatient(patientRepository.findOneByUsername(dto.getUsername()));
//        if(!dto.getId().equals(""))
//            ePrescription.setPharmacy(pharmacyRepository.findOneById(Long.parseLong(dto.getPharmacyId())));
        ePrescription.setStatus(EPrescriptionStatus.NOV);
        ePrescription.setDeleted(false);
        ePrescription.setIssueDate(LocalDateTime.parse(dto.getDate()));
        ePrescription = ePrescriptionRepository.save(ePrescription);
        Hibernate.initialize(ePrescription.getePrescriptionItems());
        Hibernate.unproxy(ePrescription);
        ePrescription.setePrescriptionItems(new HashSet<>());
        Set<EPrescriptionItem> ePrescriptionItems = new HashSet<>();
        for(EqrPrescriptionItemDTO dtoItem: dto.getMedicines()){
            ePrescriptionItems.add(saveItemDTO(dtoItem));
        }
        ePrescription.setePrescriptionItems(ePrescriptionItems);
        return ePrescriptionRepository.save(ePrescription);
    }

    @Transactional
    public EPrescriptionItem saveItemDTO(EqrPrescriptionItemDTO dto){
        EPrescriptionItem ePrescriptionItem = new EPrescriptionItem();
        ePrescriptionItem.setDeleted(false);
        ePrescriptionItem.setQuantity(Integer.parseInt(dto.getAmount()));
        ePrescriptionItem = ePrescriptionItemRepository.save(ePrescriptionItem);
        Hibernate.initialize(ePrescriptionItem.getMedicine());
        Hibernate.unproxy(ePrescriptionItem);
        ePrescriptionItem.setMedicine(medicineRepository.findOneByIdentifier(dto.getIdentifier()));
        return ePrescriptionItemRepository.save(ePrescriptionItem);
    }

    @Transactional
    public Medicine getPrescriptionItemMedicine(EPrescriptionItem ePrescriptionItem){
        Hibernate.initialize(ePrescriptionItem.getMedicine());
        return ePrescriptionItem.getMedicine();
    }

    @Transactional
    public List<EqrPharmacyDTO> retPharmacySearchResults(String id){
        List<Pharmacy> pharmacies = pharmacyRepository.findAll();
        EPrescription ePrescription = ePrescriptionRepository.getOne(Long.parseLong(id));
        Hibernate.initialize(ePrescription.getePrescriptionItems());
        Hibernate.unproxy(ePrescription);
        Set<EPrescriptionItem> ePrescriptionItems = ePrescription.getePrescriptionItems();
        List<EqrPharmacyDTO> pharmacyDTOS = new ArrayList<>();
        if(!pharmacies.isEmpty()){
            for(Pharmacy pharmacy : pharmacies){
                PriceList priceList = pharmacy.getPricelist();
                Set<PriceListItem> priceListItems = priceList.getPriceListItems();
                EqrPharmacyDTO dtoPharmacy = new EqrPharmacyDTO();
                dtoPharmacy.setId(pharmacy.getId().toString());
                dtoPharmacy.setName(pharmacy.getName());
                dtoPharmacy.setRating(pharmacy.getRating() + "");
                Double totalCost = 0.0;
                int totalFound = 0;
                List<EqrPharmacyPriceListDTO> priceListDTOS = new ArrayList<>();
                for(EPrescriptionItem ePrescriptionItem : ePrescriptionItems){
                    EqrPharmacyPriceListDTO priceListDTO = new EqrPharmacyPriceListDTO();
                    for(PriceListItem priceListItem : priceListItems){
                        if(getPrescriptionItemMedicine(ePrescriptionItem).getIdentifier().equals(priceListItem.getLek().getIdentifier()) &&
                           priceListItem.getAvailableQuantity() >= ePrescriptionItem.getQuantity()){
                            totalFound++;
                            priceListDTO.setIdentifier(priceListItem.getLek().getIdentifier());
                            priceListDTO.setAmount(ePrescriptionItem.getQuantity() + "");
                            priceListDTO.setCost(priceListItem.getPrice().toString());
                            priceListDTO.setName(priceListItem.getLek().getName());
                            priceListDTOS.add(priceListDTO);
                            totalCost = totalCost + ePrescriptionItem.getQuantity()*priceListItem.getPrice();
                        }
                    }
                }
                dtoPharmacy.setPrices(priceListDTOS);
                dtoPharmacy.setTotalCost(totalCost.toString());
                if(totalFound == ePrescriptionItems.size())
                    pharmacyDTOS.add(dtoPharmacy);
            }
        }
        return pharmacyDTOS;
    }

    @Transactional(readOnly = false)
    public void subtractMedicine(EqrPharmacyDTO eqrPharmacyDTO){
        Pharmacy pharmacy = pharmacyRepository.findOneById(Long.parseLong(eqrPharmacyDTO.getId()));
        Hibernate.initialize(pharmacy.getPricelist().getPriceListItems());
        Hibernate.unproxy(pharmacy);
        PriceList pl = priceListRepository.findOneById(pharmacy.getPricelist().getId());
        for(EqrPharmacyPriceListDTO priceListDTO : eqrPharmacyDTO.getPrices()){
            for(PriceListItem priceListItem : pl.getPriceListItems()){
                if(priceListDTO.getIdentifier().equals(priceListItem.getLek().getIdentifier())) {
                    priceListItem.setAvailableQuantity(priceListItem.getAvailableQuantity() - Integer.parseInt(priceListDTO.getAmount()));
                    priceListItemRepository.save(priceListItem);
                    break;
                }
            }
            priceListRepository.save(pl);
        }
        priceListRepository.save(pharmacy.getPricelist());
        pharmacyRepository.save(pharmacy);
    }

    @Transactional
    public ArrayList<EqrPrescriptionDTO> getAll(String username){
        ArrayList<EqrPrescriptionDTO> retVal = new ArrayList<>();
        Patient patient = patientRepository.findOneByUsername(username);
        for(EPrescription ePrescription: ePrescriptionRepository.findAllByPatientId(patient.getId())){
            Hibernate.initialize(ePrescription.getePrescriptionItems());
            Hibernate.unproxy(ePrescription);
            EqrPrescriptionDTO dto = new EqrPrescriptionDTO();
            dto.setId(ePrescription.getId().toString());
            dto.setDate(ePrescription.getIssueDate().toString());
            dto.setFirstName(patient.getFirstName());
            dto.setLastName(patient.getLastName());
            dto.setUsername(patient.getUsername());
            dto.setStatus(ePrescription.getStatus().toString());
            ArrayList<EqrPrescriptionItemDTO> medicines = new ArrayList<>();
            for(EPrescriptionItem item: ePrescription.getePrescriptionItems())
            {
                EqrPrescriptionItemDTO itemDTO = new EqrPrescriptionItemDTO();
                itemDTO.setIdentifier(item.getMedicine().getIdentifier());
                itemDTO.setName(item.getMedicine().getName());
                itemDTO.setAmount(""+item.getQuantity());
                medicines.add(itemDTO);
            }

            dto.setMedicines(medicines);
            retVal.add(dto);
        }
        return retVal;
    }
    @Transactional
    public void changeStatus(Long id){
        EPrescription ePrescription = ePrescriptionRepository.getOne(id);
        Hibernate.initialize(ePrescription.getStatus());
        Hibernate.unproxy(ePrescription);
        ePrescription.setStatus(EPrescriptionStatus.OBRADJEN);
        ePrescriptionRepository.save(ePrescription);
    }
}
