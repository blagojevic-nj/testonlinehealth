package com.isamrs.onlinehealth.service;

import com.isamrs.onlinehealth.dto.DermatologistDTO;
import com.isamrs.onlinehealth.dto.DermatologistSearchDTO;
import com.isamrs.onlinehealth.dto.NewLeaveRequestDTO;
import com.isamrs.onlinehealth.model.*;
import com.isamrs.onlinehealth.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DermatologistService {

    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private RatingItemRepository ratingItemRepository;
    @Autowired
    private DermatologistRepository dermatologistRepository;
    @Autowired
    private PharmacyRepository pharmacyRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private PharmacyAdminService pharmacyAdminService;
    @Autowired
    private LeaveRequestRepository leaveRequestRepository;
    @Autowired
    private ExaminationsRepository examinationsRepository;


    private String first_name;
    private String last_name;

    public Dermatologist findOneByUsername(String username) {
        return dermatologistRepository.findOneByUsername(username);
    }

    public Dermatologist findOneByEmail(String email) {
        return dermatologistRepository.findOneByEmail(email);
    }

    public Dermatologist findOneByUsernameFetchExaminations(String username){

        return dermatologistRepository.findByUsernameAndFetchExaminationsEagerly(dermatologistRepository.findOneByUsername(username).getId());
    }

    public Dermatologist save(Dermatologist dermatologist) {
         return dermatologistRepository.save(dermatologist);
    }


    public Dermatologist register(Dermatologist dermatologist){
        dermatologist.setPassword(passwordEncoder.encode(dermatologist.getPassword()));
        return dermatologistRepository.save(dermatologist);
    }

    public ArrayList<DermatologistDTO> getAllDermatologistsForPharmacy(String username) {

        Pharmacy apoteka = pharmacyAdminService.getPharmacyByAdminUsername(username);
        ArrayList<DermatologistDTO>dermatologists = new ArrayList<DermatologistDTO>();
        for (Dermatologist d : dermatologistRepository.findAll() )
        {

            DermatologistDTO dto = new DermatologistDTO(d);
            for(Pharmacy p : d.getWorkHoursPharmacies().keySet())
            {
                    if(p.getId()==apoteka.getId()){
                        //znaci zaposlen je u toj apoteci
                        dto.setZaposlen(true);
                        break;
                    }
            }
            dermatologists.add(dto);
        }

        return dermatologists;
    }

    public ArrayList<DermatologistDTO> search(String first_name, String last_name) {
        List<Dermatologist> dermatologists;
        if(first_name.trim().equals("")&&last_name.trim().equals(""))
            dermatologists = dermatologistRepository.findAll();
        else{
            if(first_name.trim().equals(""))
                first_name="abdd!@$%^&*";
            if(last_name.trim().equals(""))
                last_name="abdd!@$%^&*";
            dermatologists= dermatologistRepository.findByFirst_nameContainingAndLast_nameContaining(first_name,last_name);
        }


        ArrayList<DermatologistDTO> result = new ArrayList<>();
        for(Dermatologist d : dermatologists){
            result.add(new DermatologistDTO(d));
        }
        return result;
    }


    public Boolean checkWorkTime(long dermId, LocalTime newStart, LocalTime newEnd) {
        Optional<Dermatologist> dOptional = dermatologistRepository.findById(dermId);
        if(dOptional.isPresent()){
            Dermatologist d = dOptional.get();
            Collection<WorkHours> sati = d.getWorkHoursPharmacies().values();
            for(WorkHours wh : sati)
            {
                if((newStart.isAfter(wh.getStart()) && newStart.isBefore(wh.getEnd())) || (newStart.isBefore(wh.getStart()) && newEnd.isAfter(wh.getStart())))
                {
                    return false;
                }
            }
            return true;
        }else {
            return false;
        }
    }

    public DermatologistDTO addToPharmacy(String username, long dermId, LocalTime vremeOd, LocalTime vremeDo) {
        Optional<Dermatologist> dOptional = dermatologistRepository.findById(dermId);
        Pharmacy p = pharmacyAdminService.getPharmacyByAdminUsername(username);
        if(dOptional.isPresent() && p != null) {
            Dermatologist d = dOptional.get();
            WorkHours wh = new WorkHours();
            wh.setStart(vremeOd);
            wh.setEnd(vremeDo);
            d.getWorkHoursPharmacies().put(p,wh);
            dermatologistRepository.save(d);
            DermatologistDTO dto = new DermatologistDTO(d);
            dto.setZaposlen(true);
            return dto;
        }
        return new DermatologistDTO();


    }

    public DermatologistDTO deleteFromPharmacy(String username, long dermId) {
        Optional<Dermatologist> dOptional = dermatologistRepository.findById(dermId);
        Pharmacy p = pharmacyAdminService.getPharmacyByAdminUsername(username);
        if(dOptional.isPresent() && p!=null) {
            Dermatologist d = dOptional.get();

            d.getWorkHoursPharmacies().remove(p);
            dermatologistRepository.save(d);
            DermatologistDTO dto = new DermatologistDTO(d);
            dto.setZaposlen(true);
            return dto;
        }
        return new DermatologistDTO();

    }

    public Boolean addApointment(long dermId, LocalTime t1, LocalTime t2, LocalDate datum, double cena) {
        if(datum.isBefore(LocalDate.now()))
            return false;
        Optional<Dermatologist> dOptional = dermatologistRepository.findById(dermId);
        Optional<Pharmacy> pOptional = pharmacyRepository.findById(1L);
        LocalDateTime start = datum.atTime(t1);
        LocalDateTime end = datum.atTime(t2);
        if(dOptional.isPresent() && pOptional.isPresent() ) {
            Dermatologist d = dOptional.get();
            Pharmacy p = pOptional.get();
            Appointment a = new Examination();
            a.setStart(start);
            a.setEnd(end);
            a.setDeleted(false);
            if(!checkIfAppointmentAllowed(d,a,p))
                return false;
            Examination newExamination = (Examination)a;
            newExamination.setPrice(cena);
            newExamination.setPharmacy(p);
            newExamination.setDermatologist(d);
            d.getExaminations().add(newExamination);
            examinationsRepository.save(newExamination);
            dermatologistRepository.save(d);
            return true;

        }
        return false;
    }

    private boolean checkIfAppointmentAllowed(Dermatologist d, Appointment a,Pharmacy p)
    {
        /**Proeri prvo radno vreme*/
        WorkHours radnoVreme = d.getWorkHoursPharmacies().get(p);
        if(radnoVreme==null)
            return false;
       if(!intervalDateBetween(a.getStart().toLocalTime(),a.getEnd().toLocalTime(),radnoVreme.getStart(),radnoVreme.getEnd()))
           return false;
       /**Proveri onda da li je na odsustu*/
       List<LeaveRequest> odsustva = d.getLeaveRequests().stream().sorted(Comparator.comparing(LeaveRequest::getStart).reversed()).collect(Collectors.toList());
       //sad imam od najkasnijeg pa ka starijima
        for (LeaveRequest l : odsustva)
        {
            //ako se zavrsilo pre danasnjeg dana ne moras vie da gledas bezbedno je...
            if(l.getEnd().isBefore(LocalDateTime.now()))
                break;
            //ako se igde preklapaju intervali ne moze...
            if(intervalDateBetween(a.getStart(),a.getEnd(),l.getStart(),l.getEnd()))
                return false;
        }

        /**Nije na odsustvu i u okviru radnog vremena je... samoproveri jos ostale zakazane*/
        Set<Examination> examinations = d.getExaminations();
        for(Examination e: examinations)
        {
            //u intervalu postojeceg pregleda...
            if(intervalDateBetween(a.getStart(),a.getEnd(),e.getStart(),e.getEnd()))
                return false;
        }
        return true;
    }

    private boolean dateBetween(LocalTime checkDate, LocalTime start, LocalTime end) {
        return (checkDate.isAfter(start)||checkDate.equals(start))&& (checkDate.isBefore(end)||checkDate.equals(end));
    }
    private boolean dateBetween(LocalDateTime checkDate, LocalDateTime start, LocalDateTime end) {
        return (checkDate.isAfter(start)||checkDate.equals(start))&& (checkDate.isBefore(end)||checkDate.equals(end));
    }
    private boolean intervalDateBetween(LocalDateTime checkDateStart,LocalDateTime checkDateEnd, LocalDateTime start, LocalDateTime end) {
        return dateBetween(checkDateStart,start,end)&&dateBetween(checkDateEnd,start,end);
    }
    private boolean intervalDateBetween(LocalTime checkDateStart,LocalTime checkDateEnd, LocalTime start, LocalTime end) {
        return dateBetween(checkDateStart,start,end)&&dateBetween(checkDateEnd,start,end);
    }

    public ArrayList<DermatologistSearchDTO> getAllDermatologistsForSearch() {
        List<Dermatologist> result = dermatologistRepository.findAll();
        ArrayList<DermatologistSearchDTO> retVal = new ArrayList<DermatologistSearchDTO>();
        if(result.isEmpty()) return retVal;

        for(Dermatologist d : result)
        {
            DermatologistSearchDTO dto = new DermatologistSearchDTO(d);
            retVal.add(dto);
        }
        return retVal;
    }

    public ArrayList<DermatologistSearchDTO> searchByNameAndSurname(String name, String surname) {
        List<Dermatologist> result;
        ArrayList<DermatologistSearchDTO> retVal = new ArrayList<DermatologistSearchDTO>();

        if (name.trim().equals("") && surname.trim().equals(""))
            result = dermatologistRepository.findAll();
        else {
            if (name.trim().equals(""))
                name = "abdd!@$%^&*";

            if (surname.trim().equals(""))
                surname = "abdd!@$%^&*";

            result = dermatologistRepository.findByFirst_nameContainingAndLast_nameContaining(name,surname);
        }

        if(result.isEmpty()) return retVal;

        for(Dermatologist d : result)
        {
            DermatologistSearchDTO dto = new DermatologistSearchDTO(d);
            retVal.add(dto);
        }
        return retVal;
    }

    @Transactional(readOnly = false)
    public Boolean setRating(String username, String derma, Double rating){
        Patient patient = patientRepository.findOneByUsername(username);
        Dermatologist dermatologist = dermatologistRepository.findOneByUsernameForRating(derma);

        if(patient == null || dermatologist == null)
            return false;

        boolean added = false;
        for(RatingItem ri: dermatologist.getRatingItems()){
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
            dermatologist.getRatingItems().add(ri);
        }

        double sum = 0.0;
        double couner = 0;
        for(RatingItem ri: dermatologist.getRatingItems()){
            if (!ri.getDeleted()) {
                sum += ri.getRating();
                couner++;
            }
        }

        dermatologist.setRating(sum / couner);
        dermatologistRepository.save(dermatologist);
        return true;
    }

    public static boolean isOverlapping(LocalDateTime start1, LocalDateTime end1, LocalDateTime start2, LocalDateTime end2) {
        return start1.toLocalDate().isBefore(end2.toLocalDate()) && start2.toLocalDate().isBefore(end1.toLocalDate()) && !(end1.toLocalDate().equals(end2.toLocalDate())) && !(start1.toLocalDate().equals(start2.toLocalDate()));
    }

    public Collection<LeaveRequest> getAllLeaveRequests(String username) {
        return dermatologistRepository.findOneByUsername(username).getLeaveRequests();
    }

    public boolean newLeaveRequest(NewLeaveRequestDTO newLeaveRequestDTO) {
        if (newLeaveRequestDTO.getStart().isAfter(newLeaveRequestDTO.getEnd())) return false;
        //if (newLeaveRequestDTO.getStart().toLocalDate().equals(newLeaveRequestDTO.getEnd().toLocalDate())) return false;
        Dermatologist dermatologist = dermatologistRepository.findOneByUsername(newLeaveRequestDTO.getUsername());
        for (LeaveRequest leaveRequest : dermatologist.getLeaveRequests()) {
            if (isOverlapping(newLeaveRequestDTO.getStart(), newLeaveRequestDTO.getEnd(), leaveRequest.getStart(), leaveRequest.getEnd()))
                return false;
        }
        LeaveRequest leaveRequest = new LeaveRequest();
        leaveRequest.setEnd(newLeaveRequestDTO.getEnd());
        leaveRequest.setStart(newLeaveRequestDTO.getStart());
        leaveRequest.setStatus(LEAVE_REQUEST_STATUS.PENDING);
        dermatologist.getLeaveRequests().add(leaveRequestRepository.save(leaveRequest));
        dermatologistRepository.save(dermatologist);
        return true;
    }

    public boolean cancelLeaveRequest(Long id, String username) {
        Dermatologist dermatologist = dermatologistRepository.findOneByUsername(username);
        Optional<LeaveRequest> leaveRequest = leaveRequestRepository.findById(id);
        if (dermatologist.getLeaveRequests().contains(leaveRequest.get()) && !(leaveRequest.get().getStatus().equals(LEAVE_REQUEST_STATUS.REJECTED))) {
            dermatologist.getLeaveRequests().remove(leaveRequest.get());
            dermatologistRepository.save(dermatologist);
            return true;
        }
        return false;
    }
}
