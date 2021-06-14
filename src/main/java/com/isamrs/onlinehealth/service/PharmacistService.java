package com.isamrs.onlinehealth.service;

import com.isamrs.onlinehealth.dto.NewLeaveRequestDTO;
import com.isamrs.onlinehealth.dto.PharmacistDTO;
import com.isamrs.onlinehealth.model.*;
import com.isamrs.onlinehealth.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class PharmacistService {
    @Autowired
    private PharmacistRepository pharmacistRepository;
    @Autowired
    private PharmacyRepository apotekaRepository;

    @Autowired
    private RatingItemRepository ratingItemRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private PharmacyAdminService pharmacyAdminService;

    @Autowired
    private LeaveRequestRepository leaveRequestRepository;


    public Pharmacist findOneByUsernameFetchConsultations(String username) {

        return pharmacistRepository.findByUsernameAndFetchConsultationsEagerly(pharmacistRepository.findOneByUsername(username).getId());
    }

    public ArrayList<PharmacistDTO> getAllPharmacistsForPharmacy(String username) {

        Pharmacy apoteka = pharmacyAdminService.getPharmacyByAdminUsername(username);
        ArrayList<PharmacistDTO> result = new ArrayList<PharmacistDTO>();
        Set<Pharmacist> pharmacists = apoteka.getPharmacists();
        Set<Pharmacist> allPharmacists = new HashSet<>(pharmacistRepository.findAll()) ;
        for (Pharmacist p : allPharmacists )
        {
            if(zaposlenNaDrugomMestu(p,apoteka.getId()))
                continue;
            PharmacistDTO dto = new PharmacistDTO(p);
            if(pharmacists.contains(p))
                dto.setZaposlen("true");
            result.add(dto);
        }
        return result;
    }

    private boolean zaposlenNaDrugomMestu(Pharmacist p,long id) {
        List<Pharmacy> apoteke = apotekaRepository.findAll();
        for(Pharmacy apoteka : apoteke)
        {
            for (Pharmacist pharmacist : apoteka.getPharmacists())
            {
                //ako je negde zaposlen
                if(p.getId()==pharmacist.getId())
                {
                    //ali ne u mojoj apoteci
                    if(apoteka.getId()!=id)
                        return true;
                }
            }
        }
        return false;
    }

    private boolean isOccupied(Pharmacist p)    {
        Set<Consultation> consultations = p.getConsultations();
        for (Appointment c : consultations)
        {
            if(c.getEnd().isAfter(LocalDateTime.now()))
                return true;
        }
        return false;

    }

    public ArrayList<PharmacistDTO> searchByNameAndSurname(String username, String name, String surname) {

        ArrayList<PharmacistDTO> result;
        ArrayList<PharmacistDTO> retVal = new ArrayList<PharmacistDTO>();

        if (!name.trim().equals("") || !surname.trim().equals("")) {

            if (name.trim().equals(""))
                name = "abdd!@$%^&*kl|%$##$%q";

            if (surname.trim().equals(""))
                surname = "abdd!@$%^&*kl|%$##$%q";
        }
        result = this.getAllPharmacistsForPharmacy(username);

        if(result.isEmpty()) return result;

        for(PharmacistDTO dto : result)
            if(dto.getFirstName().toLowerCase().contains(name.trim().toLowerCase())
                    || dto.getLastName().toLowerCase().contains(surname.trim().toLowerCase()))
                retVal.add(dto);

        return retVal;
    }

    public ArrayList<PharmacistDTO> deleteFromPharmacy(String username, long pharmacistId) {
        Optional<Pharmacist> p = pharmacistRepository.findById(pharmacistId);
        Pharmacy apoteka = pharmacyAdminService.getPharmacyByAdminUsername(username);
        if(!p.isPresent()) return getAllPharmacistsForPharmacy(username);
        if(isOccupied(p.get())) return getAllPharmacistsForPharmacy(username);
        //nije zauzet moze se brisati
        apoteka.getPharmacists().remove(p.get());
        apotekaRepository.save(apoteka);
        return getAllPharmacistsForPharmacy(username);
    }

    public ArrayList<PharmacistDTO> addToPharmacy(String username, long pharmacistId) {
        Optional<Pharmacist> p = pharmacistRepository.findById(pharmacistId);
        Pharmacy apoteka = pharmacyAdminService.getPharmacyByAdminUsername(username);
        if(!p.isPresent()) return getAllPharmacistsForPharmacy(username);
        if(zaposlenNaDrugomMestu(p.get(),apoteka.getId()))
            return getAllPharmacistsForPharmacy(username);
        //znaci slobodan
        apoteka.getPharmacists().add(p.get());
        apotekaRepository.save(apoteka);
        return getAllPharmacistsForPharmacy(username);


    }

    public ArrayList<PharmacistDTO> getAllPharmacistsForSearch() {
        List<Pharmacist> result = pharmacistRepository.findAll();
        ArrayList<PharmacistDTO> retVal = new ArrayList<PharmacistDTO>();
        if(result.isEmpty()) return retVal;

        for(Pharmacist d : result)
        {
            PharmacistDTO dto = new PharmacistDTO(d);
            Pharmacy worksHere = findPharmacy(d);
            if (worksHere != null) {
                dto.setZaposlen("true");
                dto.setPharmacy(worksHere.getName());
            }
            else {
                dto.setZaposlen("false");
                dto.setPharmacy("");
            }
            retVal.add(dto);
        }
        return retVal;
    }

    public ArrayList<PharmacistDTO> searchByNameAndSurname(String name, String surname) {

            List<Pharmacist> result;
            ArrayList<PharmacistDTO> retVal = new ArrayList<PharmacistDTO>();

            if (name.trim().equals("") && surname.trim().equals(""))
                result = pharmacistRepository.findAll();
            else {
                if (name.trim().equals(""))
                    name = "abdd!@$%^&*";

                if (surname.trim().equals(""))
                    surname = "abdd!@$%^&*";

                result = pharmacistRepository.findByFirst_nameContainingAndLast_nameContaining(name,surname);
            }

            if(result.isEmpty()) return retVal;

            for(Pharmacist d : result)
            {
                PharmacistDTO dto = new PharmacistDTO(d);
                Pharmacy worksHere = findPharmacy(d);
                if (worksHere != null) {
                    dto.setZaposlen("true");
                    dto.setPharmacy(worksHere.getName());
                }
                else {
                    dto.setZaposlen("false");
                    dto.setPharmacy("Nezaposlen");
                }
                retVal.add(dto);
            }
            return retVal;
        }

    public Pharmacy findPharmacy(Pharmacist p){
        List<Pharmacy> sveApoteke = apotekaRepository.findAll();

        for(Pharmacy apoteka : sveApoteke)
        {
            if(apoteka.getPharmacists().contains(p))
                return apoteka;
        }
        return null;

    }
    @Transactional(readOnly = false)
    public Boolean setRating(String username, String derma, Double rating){
        Patient patient = patientRepository.findOneByUsername(username);
        Pharmacist pharmacist = pharmacistRepository.findOneByUsernameForRating(derma);

        if(patient == null || pharmacist == null)
            return false;

        boolean added = false;
        for(RatingItem ri: pharmacist.getRatings()){
            if(!ri.getDeleted() && ri.getPatient().getUsername().equals(username)){
                ri.setRating(rating);
                ratingItemRepository.save(ri);
                added = true;
                break;
            }
        }

        if(!added){
            RatingItem ri = new RatingItem();
            ri.setRating(rating);
            ri.setPatient(patient);
            ri.setDeleted(false);
            ratingItemRepository.save(ri);
            pharmacist.getRatings().add(ri);
        }

        double sum = 0.0;
        double couner = 0;
        for(RatingItem ri: pharmacist.getRatings()){
            if (!ri.getDeleted()) {
                sum += ri.getRating();
                couner++;
            }
        }

        pharmacist.setRating(sum / couner);
        pharmacistRepository.save(pharmacist);
        return true;
    }

    public Pharmacist findOneByUsername(String username) {

        return pharmacistRepository.findOneByUsername(username);
    }

    public static boolean isOverlapping(LocalDateTime start1, LocalDateTime end1, LocalDateTime start2, LocalDateTime end2) {
        return start1.toLocalDate().isBefore(end2.toLocalDate()) && start2.toLocalDate().isBefore(end1.toLocalDate()) && !(end1.toLocalDate().equals(end2.toLocalDate())) && !(start1.toLocalDate().equals(start2.toLocalDate()));
    }

    public Collection<LeaveRequest> getAllLeaveRequests(String username) {
        return pharmacistRepository.findOneByUsername(username).getLeaveRequests();
    }

    public boolean newLeaveRequest(NewLeaveRequestDTO newLeaveRequestDTO) {
        if (newLeaveRequestDTO.getStart().isAfter(newLeaveRequestDTO.getEnd())) return false;
        if (newLeaveRequestDTO.getStart().toLocalDate().equals(newLeaveRequestDTO.getEnd().toLocalDate())) return false;
        Pharmacist pharmacist = pharmacistRepository.findOneByUsername(newLeaveRequestDTO.getUsername());
        for (LeaveRequest leaveRequest : pharmacist.getLeaveRequests()) {
            if (isOverlapping(newLeaveRequestDTO.getStart(), newLeaveRequestDTO.getEnd(), leaveRequest.getStart(), leaveRequest.getEnd()))
                return false;
        }
        LeaveRequest leaveRequest = new LeaveRequest();
        leaveRequest.setEnd(newLeaveRequestDTO.getEnd());
        leaveRequest.setStart(newLeaveRequestDTO.getStart());
        leaveRequest.setStatus(LEAVE_REQUEST_STATUS.PENDING);
        pharmacist.getLeaveRequests().add(leaveRequestRepository.save(leaveRequest));
        pharmacistRepository.save(pharmacist);
        return true;
    }

    public boolean cancelLeaveRequest(Long id, String username) {
        Pharmacist pharmacist = pharmacistRepository.findOneByUsername(username);
        Optional<LeaveRequest> leaveRequest = leaveRequestRepository.findById(id);
        if (pharmacist.getLeaveRequests().contains(leaveRequest.get()) && !(leaveRequest.get().getStatus().equals(LEAVE_REQUEST_STATUS.REJECTED))) {
            pharmacist.getLeaveRequests().remove(leaveRequest.get());
            pharmacistRepository.save(pharmacist);
            return true;
        }
        return false;
    }
}
