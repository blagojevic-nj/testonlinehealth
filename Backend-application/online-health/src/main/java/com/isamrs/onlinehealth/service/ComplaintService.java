package com.isamrs.onlinehealth.service;

import com.isamrs.onlinehealth.model.Complaint;
import com.isamrs.onlinehealth.repository.ComplaintRepository;
import com.isamrs.onlinehealth.repository.SystemAdminRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ComplaintService {
    @Autowired
    ComplaintRepository complaintRepository;

    @Autowired
    SystemAdminRepository systemAdminRepository;

    public Complaint save(Complaint complaint){
        return complaintRepository.save(complaint);
    }

    public Complaint findById(Long id){
        return complaintRepository.getOne(id);
    }

    public List<Complaint> getAll(){
        return complaintRepository.findAll();
    }

    @Transactional
    public void setDeleted(Long id){
        Complaint c = complaintRepository.findOneById(id);
        Hibernate.initialize(c.getDeleted());
        Hibernate.unproxy(c);
        c.setDeleted(true);
        complaintRepository.save(c);
    }

    @Transactional
    public void setResponse(Long id, String response, LocalDateTime date, String username){
        Complaint c = complaintRepository.findOneById(id);
        Hibernate.initialize(c.getResponseText());
        Hibernate.unproxy(c);
        c.setResponseText(response);
        c.setDateResponded(date);
        c.setAdminUsername(username);
        complaintRepository.save(c);
    }
}
