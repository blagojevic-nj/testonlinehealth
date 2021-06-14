package com.isamrs.onlinehealth.service;

import com.isamrs.onlinehealth.dto.*;
import com.isamrs.onlinehealth.model.*;
import com.isamrs.onlinehealth.repository.PurchaseOrderRepository;
import com.isamrs.onlinehealth.repository.SupplierRepository;
import com.isamrs.onlinehealth.repository.SupplyRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SupplierService {
    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;
    @Autowired
    SupplyRepository supplyRepository;
    @Autowired
    PharmacyAdminService pharmacyAdminService;

    public Supplier findByEmail(String email){
        return supplierRepository.findOneByEmail(email);
    }

    public Supplier findByUsername(String username){
        return supplierRepository.findOneByUsername(username);
    }

    public Supplier register(Supplier supplier){
        supplier.setPassword(passwordEncoder.encode(supplier.getPassword()));
        return supplierRepository.save(supplier);
    }

    public void setQuantityMedicines(Map<Medicine, Integer> medQuant, Supplier s){
        s.setMedicineQuantity(medQuant);
        supplierRepository.save(s);
    }

    @Transactional
    public PurchaseOrder getPurchaseOrderFromSupply(Supply s){
        Hibernate.initialize(s.getPurchaseOrder());
        Hibernate.unproxy(s);
        return s.getPurchaseOrder();
    }

    @Transactional
    public void setPurchaseOrderForSupply(PurchaseOrder purchaseOrder, Supply s){
        Hibernate.initialize(s.getPurchaseOrder());
        Hibernate.unproxy(s);
        s.setPurchaseOrder(purchaseOrder);
        supplyRepository.save(s);
    }

    @Transactional
    public List<PurchaseOrderItemDTO> getMedicinesForOrder(PurchaseOrder purchaseOrder){
        Hibernate.initialize(purchaseOrder.getMedicineQuantity());
        Hibernate.unproxy(purchaseOrder);
        List<PurchaseOrderItemDTO> list = new ArrayList<>();
        for(Map.Entry<Medicine, Integer> entry : purchaseOrder.getMedicineQuantity().entrySet()){
            PurchaseOrderItemDTO purchaseOrderItemDTO = new PurchaseOrderItemDTO();
            purchaseOrderItemDTO.setIdentifier(entry.getKey().getIdentifier());
            purchaseOrderItemDTO.setKolicina(entry.getValue());
            purchaseOrderItemDTO.setMedicineId(entry.getKey().getId());
            purchaseOrderItemDTO.setMedicineName(entry.getKey().getName());
            list.add(purchaseOrderItemDTO);
        }
        return list;
    }

    @Transactional
    public List<PurchaseOrderDTO> getAllPurchaseOrders(String username){
        Supplier s = supplierRepository.findOneByUsername(username);
        List<PurchaseOrder> purchaseOrders = purchaseOrderRepository.findAll();
        Hibernate.initialize(s.getSupplies());
        Hibernate.unproxy(s);
        List<PurchaseOrderDTO> purchaseOrderDTOS = new ArrayList<>();
        for(PurchaseOrder purchaseOrder : purchaseOrders){
            PurchaseOrderDTO purchaseOrderDTO = new PurchaseOrderDTO(purchaseOrder, false);
            if(s.getSupplies() != null){
                for(Supply supply : s.getSupplies()){
                    if(getPurchaseOrderFromSupply(supply).getId().equals(purchaseOrder.getId())){
                        purchaseOrderDTO.setMyOrder(true);
                        if(supply.getDueDate().isBefore(LocalDateTime.now()))
                            purchaseOrderDTO.setMyOrder(false);
                        break;
                    }
                }
            }
            purchaseOrderDTO.setRok(purchaseOrder.getDueDate().toLocalDate());
            purchaseOrderDTO.setStatus(purchaseOrder.getStatus());
            purchaseOrderDTO.setMedicines(getMedicinesForOrder(purchaseOrder));
            purchaseOrderDTO.setId(purchaseOrder.getId());
            pharmacyAdminService.checkOrderDate(purchaseOrder);
            if(purchaseOrder.getStatus().equals(PurchaseOrderStatus.PENDING) && !purchaseOrder.getDeleted())
                purchaseOrderDTOS.add(purchaseOrderDTO);
        }
        return purchaseOrderDTOS;
    }

    @Transactional
    public List<NeededForOrderDTO> checkConstraintOrder(String username, PurchaseOrderDTO purchaseOrderDTO){
        Supplier s = supplierRepository.findOneByUsername(username);
        List<NeededForOrderDTO> needed = new ArrayList<>();
        for(PurchaseOrderItemDTO itemDTO : purchaseOrderDTO.getMedicines()){
            NeededForOrderDTO neededForOrderDTO = new NeededForOrderDTO();
            if(!s.getMedicineQuantity().isEmpty()){
                Boolean found = false;
                for (Map.Entry<Medicine, Integer> entry : s.getMedicineQuantity().entrySet()){
                    if(entry.getKey().getIdentifier().equals(itemDTO.getIdentifier())){
                        if(itemDTO.getKolicina() >= entry.getValue()){
                            neededForOrderDTO.setHaveAmount(entry.getValue().toString());
                            neededForOrderDTO.setNeededAmount(itemDTO.getKolicina() + "");
                            neededForOrderDTO.setIdentifier(itemDTO.getIdentifier());
                            neededForOrderDTO.setMedicineName(itemDTO.getMedicineName());
                        }else found=true;
                        break;
                    }
                }
                if(neededForOrderDTO.getHaveAmount().equals("") && !found){
                    neededForOrderDTO.setHaveAmount("0");
                    neededForOrderDTO.setNeededAmount(itemDTO.getKolicina()+"");
                    neededForOrderDTO.setMedicineName(itemDTO.getMedicineName());
                    neededForOrderDTO.setIdentifier(itemDTO.getIdentifier());
                }
                if(!neededForOrderDTO.getHaveAmount().equals(""))
                    needed.add(neededForOrderDTO);
            }else{
                neededForOrderDTO.setHaveAmount("0");
                neededForOrderDTO.setNeededAmount(itemDTO.getKolicina()+"");
                neededForOrderDTO.setMedicineName(itemDTO.getMedicineName());
                neededForOrderDTO.setIdentifier(itemDTO.getIdentifier());
                needed.add(neededForOrderDTO);
            }
        }
        return needed;
    }

    @Transactional
    public String addSupply(SupplyForOrderDTO supplyForOrderDTO){
        if(supplyForOrderDTO.getDueDate().toLocalDate().isBefore(supplyForOrderDTO.getPurchaseOrderDTO().getRok()))
            return "Rok porudzbine ne moze biti pre roka narudzbine!";
        Supplier s = supplierRepository.findOneByUsername(supplyForOrderDTO.getSupplierUsername());
        Supply supply = new Supply();
        Hibernate.initialize(s.getSupplies());
        Hibernate.unproxy(s);
        PurchaseOrder purchaseOrder = purchaseOrderRepository.getOne(supplyForOrderDTO.getPurchaseOrderDTO().getId());
        supply.setDeleted(false);
        supply.setDueDate(supplyForOrderDTO.getDueDate());
        supply.setTotalPrice(Double.parseDouble(supplyForOrderDTO.getTotalCost()));
        supply = supplyRepository.save(supply);
        setPurchaseOrderForSupply(purchaseOrder, supply);
        s.getSupplies().add(supply);
        supplierRepository.save(s);
        return "Porudzbina uspesno poslata!";
    }

    @Transactional
    public List<SupplyForOrderDTO> getAllSupplies(String username){
        Supplier supplier = supplierRepository.findOneByUsername(username);
        Hibernate.initialize(supplier.getSupplies());
        Hibernate.unproxy(supplier);
        List<SupplyForOrderDTO> list = new ArrayList<>();
        for(Supply supply: supplier.getSupplies()){
            SupplyForOrderDTO dto = new SupplyForOrderDTO();
            PurchaseOrderDTO purchaseOrderDTO = new PurchaseOrderDTO(getPurchaseOrderFromSupply(supply), false);
            dto.setPurchaseOrderDTO(purchaseOrderDTO);
            dto.setDueDate(supply.getDueDate());
            dto.setSupplierUsername(username);
            dto.setSupplyId(supply.getId().toString());
            dto.setTotalCost(supply.getTotalPrice().toString());
            list.add(dto);
        }
        return list;
    }

    @Transactional
    public void removeSupply(Long supplyId, String username){
        Supplier supplier = supplierRepository.findOneByUsername(username);
        Hibernate.initialize(supplier.getSupplies());
        Hibernate.unproxy(supplier);
        Supply supply = supplyRepository.getOne(supplyId);
        supplier.getSupplies().remove(supply);
        supplierRepository.save(supplier);
        supplyRepository.delete(supply);
    }
}
