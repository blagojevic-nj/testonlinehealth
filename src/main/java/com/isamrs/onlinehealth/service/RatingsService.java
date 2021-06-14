package com.isamrs.onlinehealth.service;

import com.isamrs.onlinehealth.dto.PatientsRatingDTO;
import com.isamrs.onlinehealth.dto.RatingType;
import com.isamrs.onlinehealth.model.*;
import com.isamrs.onlinehealth.repository.PatientRepository;
import com.isamrs.onlinehealth.repository.PharmacyRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.Access;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
public class RatingsService {

    @Autowired
    private PatientRepository patientRepository;

    @Transactional
    public ArrayList<PatientsRatingDTO> getRatings(String username){
        ArrayList<PatientsRatingDTO> retVal = new ArrayList<>();
        ArrayList<String> dermatologists = new ArrayList<>();
        ArrayList<Long> pharmacies = new ArrayList<>();
        ArrayList<Long> medicines = new ArrayList<>();

        ArrayList<Pharmacy> temp = new ArrayList<>();

        // prvo cemo da uzmemo ocene za dermatologa
        Patient patient = patientRepository.findOneByUsername(username);

        Hibernate.initialize(patient.getConsultations());
        Hibernate.initialize(patient.getExaminations());
        Hibernate.unproxy(patient);
        if (patient != null){
            for (Examination e : patient.getExaminations()){
                if(!e.getDeleted() && e.getEnd().isBefore(LocalDateTime.now())){
                    Dermatologist dermatologist = e.getDermatologist();
                    if(!dermatologists.contains(dermatologist.getUsername())){
                        dermatologists.add(dermatologist.getUsername());
                        boolean added = false;
                        for(RatingItem ri :dermatologist.getRatingItems()){
                            if(!ri.getDeleted() && ri.getPatient().getUsername().equals(username)){
                                PatientsRatingDTO newItem = new PatientsRatingDTO();
                                newItem.setRatingType(RatingType.DERMATOLOGIST);
                                newItem.setEntityId(dermatologist.getId());
                                newItem.setUsername(dermatologist.getUsername());
                                newItem.setRating(ri.getRating());
                                retVal.add(newItem);
                                added = true;
                                break;
                            }
                        }
                        if (!added){
                            PatientsRatingDTO newItem = new PatientsRatingDTO();
                            newItem.setRatingType(RatingType.DERMATOLOGIST);
                            newItem.setEntityId(dermatologist.getId());
                            newItem.setUsername(dermatologist.getUsername());
                            newItem.setRating(0.0);
                            retVal.add(newItem);
                        }
                    }

                    if(!temp.contains(e.getPharmacy())){
                        temp.add(e.getPharmacy());
                    }
                }
            }
            // zatim dobavljamo ocene za farmaceuta
            for(Consultation c: patient.getConsultations()){
                if(!c.getDeleted() && c.getEnd().isBefore(LocalDateTime.now())){
                    Pharmacist pharmacist = c.getPharmacist();
                    if(!dermatologists.contains(pharmacist.getUsername())){
                        dermatologists.add(pharmacist.getUsername());
                        boolean added = false;
                        for(RatingItem ri :pharmacist.getRatings()){
                            if(!ri.getDeleted() && ri.getPatient().getUsername().equals(username)){
                                PatientsRatingDTO newItem = new PatientsRatingDTO();
                                newItem.setRatingType(RatingType.PHARMACIST);
                                newItem.setEntityId(pharmacist.getId());
                                newItem.setUsername(pharmacist.getUsername());
                                newItem.setRating(ri.getRating());
                                retVal.add(newItem);
                                added = true;
                                break;
                            }
                        }
                        if (!added){
                            PatientsRatingDTO newItem = new PatientsRatingDTO();
                            newItem.setRatingType(RatingType.PHARMACIST);
                            newItem.setEntityId(pharmacist.getId());
                            newItem.setUsername(pharmacist.getUsername());
                            newItem.setRating(0.0);
                            retVal.add(newItem);
                        }
                    }
                    if(!temp.contains(c.getPharmacy())){
                        temp.add(c.getPharmacy());
                    }
                }
            }

            // sada za apoteke
            for (ReservationList rl: patient.getReservations()){
                if(rl.getStatus() == ReservationStatus.ISSUED){
                    Pharmacy pharmacy = rl.getPharmacy();
                    if(!pharmacies.contains(pharmacy.getId())){
                        pharmacies.add(pharmacy.getId());
                        boolean added = false;
                        for(RatingItem ri: pharmacy.getRatings()){
                            if(!ri.getDeleted() && ri.getPatient().getUsername().equals(username)){
                                PatientsRatingDTO newItem = new PatientsRatingDTO();
                                newItem.setRatingType(RatingType.PHARMACY);
                                newItem.setEntityId(pharmacy.getId());
                                newItem.setRating(ri.getRating());
                                retVal.add(newItem);
                                added = true;
                                break;
                            }
                        }

                        if(!added){
                            PatientsRatingDTO newItem = new PatientsRatingDTO();
                            newItem.setRatingType(RatingType.PHARMACY);
                            newItem.setEntityId(pharmacy.getId());
                            newItem.setRating(0.0);
                            retVal.add(newItem);
                        }
                    }
                }
            }

            for (Pharmacy pharmacy: temp){
                if(!pharmacies.contains(pharmacy.getId())){
                    pharmacies.add(pharmacy.getId());
                    boolean added = false;
                    for(RatingItem ri: pharmacy.getRatings()){
                        if(!ri.getDeleted() && ri.getPatient().getUsername().equals(username)){
                            PatientsRatingDTO newItem = new PatientsRatingDTO();
                            newItem.setRatingType(RatingType.PHARMACY);
                            newItem.setEntityId(pharmacy.getId());
                            newItem.setRating(ri.getRating());
                            retVal.add(newItem);
                            added = true;
                            break;
                        }
                    }

                    if(!added){
                        PatientsRatingDTO newItem = new PatientsRatingDTO();
                        newItem.setRatingType(RatingType.PHARMACY);
                        newItem.setEntityId(pharmacy.getId());
                        newItem.setRating(0.0);
                        retVal.add(newItem);
                    }
                }
            }

            // i na kraju za lekove
            for (ReservationList rl: patient.getReservations()){
                if(rl.getStatus() == ReservationStatus.ISSUED){
                    for(ReservationItem ri:rl.getReservationItems()){
                        Medicine m = ri.getMedicine();
                        if(!medicines.contains(m.getId())){
                            medicines.add(m.getId());
                            boolean added = false;
                            for(RatingItem rItem : m.getRatings()){
                                if(!rItem.getDeleted() && rItem.getPatient().getUsername().equals(username)){
                                    PatientsRatingDTO newItem = new PatientsRatingDTO();
                                    newItem.setRatingType(RatingType.MEDICINE);
                                    newItem.setEntityId(m.getId());
                                    newItem.setRating(rItem.getRating());
                                    retVal.add(newItem);
                                    added = true;
                                    break;
                                }
                            }
                            if(!added){
                                PatientsRatingDTO newItem = new PatientsRatingDTO();
                                newItem.setRatingType(RatingType.MEDICINE);
                                newItem.setEntityId(m.getId());
                                newItem.setRating(0.0);
                                retVal.add(newItem);
                            }
                        }
                    }
                }
            }
        }
        return retVal;
    }
}
