package com.isamrs.onlinehealth.service;

import com.isamrs.onlinehealth.dto.LeaveRequestDTO;
import com.isamrs.onlinehealth.model.*;
import com.isamrs.onlinehealth.repository.ComplaintRepository;
import com.isamrs.onlinehealth.repository.LeaveRequestRepository;
import com.isamrs.onlinehealth.repository.PharmacistRepository;
import com.isamrs.onlinehealth.repository.SystemAdminRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SystemAdminService {
    @Autowired
    SystemAdminRepository systemAdminRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    ComplaintRepository complaintRepository;
    @Autowired
    PharmacistRepository pharmacistRepository;
    @Autowired
    LeaveRequestRepository leaveRequestRepository;
    @Autowired
    EmailService emailService;

    public SystemAdmin find(String username, String email){
        SystemAdmin systemAdmin = null;
        systemAdmin = systemAdminRepository.findOneByUsername(username);
        if(systemAdmin==null)
            systemAdmin = systemAdminRepository.findOneByEmail(email);
        return systemAdmin;
    }

    public SystemAdmin save(SystemAdmin systemAdmin){
        systemAdmin.setPassword(passwordEncoder.encode(systemAdmin.getPassword()));
        return systemAdminRepository.save(systemAdmin);
    }

    @Transactional
    public Set<Complaint> getComplaints(String username){
        SystemAdmin systemAdmin = systemAdminRepository.findOneByUsername(username);
        Hibernate.initialize(systemAdmin.getComplaints());
        Hibernate.unproxy(systemAdmin);
        return systemAdmin.getComplaints();
    }

    @Transactional
    public void addResponseComplaint(Long id, String username){
        SystemAdmin systemAdmin = systemAdminRepository.findOneByUsername(username);
        Complaint complaint = complaintRepository.findOneById(id);
        Hibernate.initialize(systemAdmin.getComplaints());
        Hibernate.unproxy(systemAdmin);
        if(systemAdmin.getComplaints() == null)
            systemAdmin.setComplaints(new HashSet<>());
        systemAdmin.getComplaints().add(complaint);
        systemAdminRepository.save(systemAdmin);
    }

    //------------GODISNJI ODMORI
    @Transactional
    public boolean acceptLeaveRequest(long id)
    {
        Pair<Pharmacist,LeaveRequest> pair = findParticularLeaveRequest(id);
        Pharmacist requestingPharmacist = pair.getFirst();
        LeaveRequest request = pair.getSecond();
        if (requestingPharmacist.getId()==null)
            return false;
        if(request.getStatus()!=LEAVE_REQUEST_STATUS.PENDING)
            return false;

        request.setStatus(LEAVE_REQUEST_STATUS.ACCEPTED);

        leaveRequestRepository.save(request);

        sendAcceptedLeaveRequest(requestingPharmacist);

        return true;

    }

    @Transactional
    public boolean declineLeaveRequest(long id, String poruka)
    {

        Pair<Pharmacist,LeaveRequest> pair = findParticularLeaveRequest(id);
        Pharmacist requestingPharmacist = pair.getFirst();
        LeaveRequest request = pair.getSecond();
        if (requestingPharmacist.getId()==null)
            return false;
        if(request.getStatus()!=LEAVE_REQUEST_STATUS.PENDING)
            return false;

        request.setStatus(LEAVE_REQUEST_STATUS.REJECTED);

        leaveRequestRepository.save(request);

        sendRejectedLeaveRequest(requestingPharmacist, poruka);

        return true;

    }

    private void sendRejectedLeaveRequest(Pharmacist requestingPharmacist, String poruka) {
        String mail = requestingPharmacist.getEmail();
        String body = "Odbijen je vaš zahtev za godišnji odmor,\n" +
                poruka + "\n" +
                "Srdačan pozdrav,\nAdmin informacionog sistema OnlineHealth";
        try {
            emailService.sendEmailAsync("tim10isamrs2021@gmail.com",
                    mail,
                    "Zahtev za godišnji odmor",
                    body);
        } catch (InterruptedException e) {
            System.out.println("Greska prilikom slanja maila!");
        }
    }

    @Transactional
    public ArrayList<LeaveRequestDTO> getLeaveRequests()
    {
        ArrayList<LeaveRequestDTO> ret = new ArrayList<>();
        List<Pharmacist> pharmacists = pharmacistRepository.findAll();
        for(Pharmacist p : pharmacists) {
            Hibernate.initialize(p.getLeaveRequests().size());
            Hibernate.unproxy(p);
            for(LeaveRequest l : p.getLeaveRequests()) {
                if(l.getStatus() == LEAVE_REQUEST_STATUS.PENDING)
                ret.add(new LeaveRequestDTO(l, p));
            }
        }
        return ret;
    }

    @Transactional
    public Pair<Pharmacist,LeaveRequest> findParticularLeaveRequest(Long requestId)
    {
        List<Pharmacist> pharmacists = pharmacistRepository.findAll();
        Pharmacist requestingPharmacist = new Pharmacist();
        LeaveRequest request = new LeaveRequest();
        for(Pharmacist p : pharmacists)
        {
            for(LeaveRequest l : p.getLeaveRequests())
            {
                if(l.getId().equals(requestId))
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

    private void sendAcceptedLeaveRequest(Pharmacist requestingPharmacist) {
        String mail = requestingPharmacist.getEmail();
        String body = "Prihvaćen je vaš zahtev za godišnji odmor,\nSrdačan pozdrav,\nAdmin informacionog sistema OnlineHealth";
        try {
            emailService.sendEmailAsync("tim10isamrs2021@gmail.com",
                    mail,
                    "Zahtev za godišnji odmor",
                    body);
        } catch (InterruptedException e) {
            System.out.println("Greska prilikom slanja maila!");
        }
    }
}
