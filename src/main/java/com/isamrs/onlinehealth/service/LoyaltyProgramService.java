package com.isamrs.onlinehealth.service;

import com.isamrs.onlinehealth.model.LoyaltyCategory;
import com.isamrs.onlinehealth.model.LoyaltyProgram;
import com.isamrs.onlinehealth.model.Medicine;
import com.isamrs.onlinehealth.repository.LoyaltyCategoryRepository;
import com.isamrs.onlinehealth.repository.LoyaltyProgramRepository;
import com.isamrs.onlinehealth.repository.MedicineRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class LoyaltyProgramService {
    @Autowired
    LoyaltyProgramRepository loyaltyProgramRepository;
    @Autowired
    LoyaltyCategoryRepository loyaltyCategoryRepository;
    @Autowired
    MedicineRepository medicineRepository;

    public LoyaltyProgram saveProgram(LoyaltyProgram loyaltyProgram){
        return loyaltyProgramRepository.save(loyaltyProgram);
    }

    public LoyaltyCategory saveCategory(LoyaltyCategory loyaltyCategory){
        return loyaltyCategoryRepository.save(loyaltyCategory);
    }

    public List<LoyaltyProgram> getLoyaltyPrograms(){
        return loyaltyProgramRepository.findAll();
    }

    @Transactional
    public Map<Long, Double> getMedicinePoints(Long id){
        LoyaltyProgram loyaltyProgram = loyaltyProgramRepository.getOne(id);
        Map<Long, Double> tmp = new HashMap<>();
        Hibernate.initialize(loyaltyProgram.getMedicinePoints());
        Hibernate.unproxy(loyaltyProgram);
        Map<Medicine, Double> map = loyaltyProgram.getMedicinePoints();
        for (Map.Entry<Medicine, Double> entry : map.entrySet()){
            tmp.put(entry.getKey().getId(), entry.getValue());
        }
        return tmp;
    }

    @Transactional
    public List<LoyaltyCategory> getLoyaltyCategories(Long id){
        LoyaltyProgram loyaltyProgram = loyaltyProgramRepository.getOne(id);
        Hibernate.initialize(loyaltyProgram.getLoyaltyCategories());
        Hibernate.unproxy(loyaltyProgram);
        return new ArrayList<>(loyaltyProgram.getLoyaltyCategories());
    }

    @Transactional
    public LoyaltyProgram setMedicinePoints(Long id, Map<Long, Double> points){
        LoyaltyProgram loyaltyProgram = loyaltyProgramRepository.getOne(id);
        for(Map.Entry<Long, Double> medicinePoints : points.entrySet()){
            Medicine m = medicineRepository.getOne(medicinePoints.getKey());
            loyaltyProgram.getMedicinePoints().put(m, medicinePoints.getValue());
        }
        return loyaltyProgramRepository.save(loyaltyProgram);
    }

    @Transactional
    public LoyaltyProgram setCategories(Long id){
        LoyaltyProgram loyaltyProgram = loyaltyProgramRepository.getOne(id);
        Hibernate.initialize(loyaltyProgram.getLoyaltyCategories());
        Hibernate.unproxy(loyaltyProgram);
        for(LoyaltyCategory lc : loyaltyCategoryRepository.findAll()){
            loyaltyProgram.getLoyaltyCategories().add(lc);
        }
        return loyaltyProgramRepository.save(loyaltyProgram);
    }
}
