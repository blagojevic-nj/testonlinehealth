package com.isamrs.onlinehealth.service;

import com.isamrs.onlinehealth.dto.*;
import com.isamrs.onlinehealth.model.*;
import com.isamrs.onlinehealth.repository.*;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PharmacyAdminService {

    @Autowired
    PharmacyAdminRepository pharmacyAdminRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    PharmacyService pharmacyService;
    @Autowired
    MedicineRepository medicineRepository;
    @Autowired
    PurchaseOrderRepository purchaseOrderRepository;
    @Autowired
    SupplyRepository supplyRepository;
    @Autowired
    SupplierRepository supplierRepository;
    @Autowired
    PriceListItemRepository priceListItemRepository;
    @Autowired
    PriceListRepository priceListRepository;
    @Autowired
    EmailService emailService;

    public PharmacyAdmin exists(String username, String email){
        PharmacyAdmin pharmacyAdmin = pharmacyAdminRepository.findOneByUsername(username);
        if(pharmacyAdmin==null)
            pharmacyAdmin = pharmacyAdminRepository.findOneByEmail(email);
        return pharmacyAdmin;
    }

    public PharmacyAdmin save(PharmacyAdmin pharmacyAdmin){
        pharmacyAdmin.setPassword(passwordEncoder.encode(pharmacyAdmin.getPassword()));
        return pharmacyAdminRepository.save(pharmacyAdmin);
    }


    public PharmacyAdminDTO getAdminByUsername(String username) {
       PharmacyAdmin pharmacyAdmin = pharmacyAdminRepository.findOneByUsername(username);
       PharmacyAdminDTO result = new PharmacyAdminDTO();

       if(pharmacyAdmin==null)
       {
           result.setUsername("-1");
           return result;
       }
        result.setUsername(pharmacyAdmin.getUsername());
        result.setAddress(pharmacyAdmin.getAddress());
        result.setCity(pharmacyAdmin.getCity());
        result.setState(pharmacyAdmin.getCountry());
        result.setFirstName(pharmacyAdmin.getFirstName());
        result.setLastName(pharmacyAdmin.getLastName());
        result.setPhoneNumber(pharmacyAdmin.getPhoneNumber());
        return result;

    }

    public Pharmacy getPharmacyByAdminUsername(String username)
    {
        PharmacyAdmin pharmacyAdmin = pharmacyAdminRepository.findOneByUsername(username);
        Pharmacy p = pharmacyAdmin.getPharmacy();
        return p;
    }
    public String update(PharmacyAdminDTO adminDTO) {
        PharmacyAdmin pharmacyAdmin = pharmacyAdminRepository.findOneByUsername(adminDTO.getUsername());
        if(pharmacyAdmin==null)
            return "notExists";
        pharmacyAdmin.setFirstName(adminDTO.getFirstName());
        pharmacyAdmin.setLastName(adminDTO.getLastName());
        pharmacyAdmin.setAddress(adminDTO.getAddress());
        pharmacyAdmin.setCity(adminDTO.getCity());
        pharmacyAdmin.setCountry(adminDTO.getState());
        pharmacyAdmin.setPhoneNumber(adminDTO.getPhoneNumber());

        pharmacyAdminRepository.save(pharmacyAdmin);
        return "sucess";
    }

    public long getPharmacyId(String adminUsername)
    {
        long ret = -1;
        PharmacyAdmin admin = pharmacyAdminRepository.findOneByUsername(adminUsername);
        if(admin==null)
            return ret;
        try {
            ret = admin.getPharmacy().getId();
        } catch (Exception e) {
            return -1;
        }
        return ret;

    }

    public PharmacyAdmin getPharmacyAdminByPharmacyId(Long pharmacyId) {
        return pharmacyAdminRepository.findPharmacyAdminByPharmacyId(pharmacyId);
    }

    private boolean orderDateChechFailed(LocalDate rok){
        if(rok.isBefore(LocalDate.now()))
            return true;
        return false;
    }

    public boolean makeNewPurchaseOrder(String username, PurchaseOrderDTO purchaseOrderDTO) {
        PharmacyAdmin admin = pharmacyAdminRepository.findOneByUsername(username);
        if(admin==null)
            return false;
        if(orderDateChechFailed(purchaseOrderDTO.getRok())){
            return false;
        }

        //napravi order, postavi datum
        PurchaseOrder order = new PurchaseOrder();
        order.setStatus(PurchaseOrderStatus.PENDING);
        order.setDueDate(purchaseOrderDTO.getRok().atStartOfDay());

        //napravi listu id, da bi imao jedan upit nad bazom
        List<Long>ids = purchaseOrderDTO.getMedicines().stream().map(PurchaseOrderItemDTO::getMedicineId).collect(Collectors.toList());
        List<Medicine> medicines = medicineRepository.findByListOfIds(ids);

        //mapiraj dobijene razultate na njihove id da bi mogao da postavis odgovarauce cene
        Map<Long, Medicine> mapaIdLek= medicines.stream().collect(Collectors.toMap(x->x.getId(),x->x));

        //svakoj stavki ordera pronadji odgovarajucu cenu
        Map<Medicine, Integer> stavke = purchaseOrderDTO.getMedicines().
                stream().collect(Collectors.toMap(x->mapaIdLek.get(x.getMedicineId()),
                x->x.getKolicina()));

        order.setMedicineQuantity(stavke);
        purchaseOrderRepository.save(order);

        admin.getPurchaseOrders().add(order);

        pharmacyAdminRepository.save(admin);

        return true;


    }

    @Transactional
    public List<PurchaseOrderDTO> getAllOrders(String username) {
        List<PurchaseOrderDTO> allOrders = new ArrayList<PurchaseOrderDTO>();
        //nadji apoteku
        Long pharmacyId = pharmacyAdminRepository.findOneByUsername(username).getPharmacy().getId();
        //sve njene admine...
        List<PharmacyAdmin> admins = pharmacyAdminRepository.findAllByPharmacyId(pharmacyId);


        //za svakog admina uzmi ordere i stavi u listu svih
        for(PharmacyAdmin a : admins)
        {
            Set<PurchaseOrder> orders = a.getPurchaseOrders();
            boolean isMyOrders = a.getUsername().equals(username);
            for(PurchaseOrder o : orders)
            {
                if(o.getDeleted())
                    continue;
/*****************************************************************/
                setACTIVETEST(o);
/*****************************************************************/
                Hibernate.initialize(o.getMedicineQuantity());
                Hibernate.unproxy(o);
                allOrders.add(new PurchaseOrderDTO(o,isMyOrders));
            }


        }

        return allOrders;
    }

    private PurchaseOrder checkIfMyOrder(String username, Long orderId)
    {
        PharmacyAdmin admin = pharmacyAdminRepository.findOneByUsername(username);
        Optional<PurchaseOrder> orderOptional = purchaseOrderRepository.findById(orderId);
        if(!orderOptional.isPresent())
            return null;
        PurchaseOrder order = orderOptional.get();
        if(admin.getPurchaseOrders().contains(order))
            return order;


        return null;
    }

    @Transactional
    public List<SupplyDTO> getSupplies(String username, Long orderId) {

        PurchaseOrder order = checkIfMyOrder(username,orderId);
        if(order==null)
            return null;
        checkOrderDate(order);
        List<Supply> supplies = supplyRepository.getAllSuppliesByOrderId(orderId);
        Set<Supplier> suppliers = supplierRepository.findSuppliersByOrderId(orderId);
        List<SupplyDTO>retVal = new ArrayList<>();

        for(Supplier supplier : suppliers)
        {
            for(Supply supply : supplier.getSupplies())
            {
                Hibernate.initialize(supply.getPurchaseOrder());
                Hibernate.unproxy(supply);
                if(orderId==supply.getPurchaseOrder().getId())
                {
                    SupplyDTO dto = new SupplyDTO();
                    dto.setSupplyId(supply.getId());
                    dto.setSupplierUsername(supplier.getUsername());
                    dto.setTotalCost(supply.getTotalPrice());
                    retVal.add(dto);
                }
            }
        }

        //sortiranje da bude po najnizoj ceni
        Comparator<SupplyDTO> poCeni = (s1, s2) -> {
            return s1.getTotalCost().compareTo(s2.getTotalCost());
        };

        return retVal.stream().sorted(poCeni).collect(Collectors.toList());

    }

    public void checkOrderDate(PurchaseOrder order) {
        if(order.getStatus()==PurchaseOrderStatus.PENDING)
            if(order.getDueDate().isBefore(LocalDate.now().atStartOfDay()))
                setOrderStatusACTIVE(order);
    }

    private void setOrderStatusACTIVE(PurchaseOrder order) {
        order.setStatus(PurchaseOrderStatus.ACTIVE);
        purchaseOrderRepository.save(order);
    }

    public List<SupplyDTO> getTESTSupplies(String username, Long orderId) {

        PurchaseOrder order = checkIfMyOrder(username,orderId);
        if(order==null)
            return null;
        List<Supply> supplies = supplyRepository.getAllSuppliesByOrderId(orderId);
        List<SupplyDTO>retVal = new ArrayList<>();

        for (int i = 0; i< 10; i++)
        {
            SupplyDTO dto = new SupplyDTO();
            dto.setSupplyId((long) i);
            dto.setSupplierUsername("NONE"+i);
            dto.setTotalCost(i*1100.00);
            retVal.add(dto);
        }
        return retVal;

    }

    private void setACTIVETEST (PurchaseOrder order)
    {
        if(order.getStatus()!=PurchaseOrderStatus.PENDING)
            return;
        if(order.getId()==1){
            setOrderStatusACTIVE(order);
        }
    }

    @Transactional(readOnly=false)
    public boolean acceptPonuda(String username, Long ponudaId, Long orderId) {
        PurchaseOrder order = checkIfMyOrder(username,orderId);
        if(order==null)
            return false;
        if(order.getStatus()!=PurchaseOrderStatus.ACTIVE)
            return false;
        order.setStatus(PurchaseOrderStatus.CLOSED);
        purchaseOrderRepository.save(order);
        sendEmailNotificationToSuppliers(ponudaId,orderId);
        Hibernate.initialize(order.getMedicineQuantity());
        Hibernate.unproxy(order);
        Map<Medicine, Integer> stavkeNarudzbe = order.getMedicineQuantity();
        Pharmacy pharmacy = getPharmacyByAdminUsername(username);
        PriceList pl = priceListRepository.findOneById(pharmacy.getPricelist().getId());
        Set<PriceListItem> pricelist = pl.getPriceListItems();
        for (PriceListItem pli : pricelist)
        {
            if(stavkeNarudzbe.containsKey(pli.getLek()))
            {
                pli.setAvailableQuantity(pli.getAvailableQuantity()+stavkeNarudzbe.get(pli.getLek()));
            }
        }
        priceListItemRepository.saveAll(pricelist);
        priceListRepository.save(pl);

        return true;
    }

    private void sendEmailNotificationToSuppliers(Long ponudaId, Long orderId) {
        Set<Supplier> suppliers = supplierRepository.findSuppliersByOrderId(orderId);
        boolean winnerFound=false;
        for(Supplier supplier : suppliers)
        {
            boolean winner = false;
            if(!winnerFound)
            {
                for(Supply supply : supplier.getSupplies())
                {
                    if(supply.getId()==ponudaId)
                    {
                        winnerFound=true;
                        winner=true;
                        break;
                    }

                }
            }

            if(winner)
                sendWinnerEmail(supplier, ponudaId);
            else
                sendLoserEmail(supplier,ponudaId);
        }
    }

    private void sendLoserEmail(Supplier supplier, Long ponudaId) {

        String mail = supplier.getEmail();

        String body = "Nažalost, Vaša ponuda sa id:"+ponudaId+" nije prihvaćena.";
        try {
            emailService.sendEmailAsync("tim10isamrs2021@gmail.com",
                    mail,
                    "Odbijena ponuda",
                    body);
        } catch (InterruptedException e) {
            System.out.println("Greska prilikom slanja maila Odbijena ponuda!");
        }
    }

    private void sendWinnerEmail(Supplier supplier, Long ponudaId) {
        String mail = supplier.getEmail();

        String body = "Vaša ponuda sa id:"+ponudaId+" je prihvaćena. Isporuku navedenih lekova očekujemo najkasnije u roku navedenom u okviru ponude.";

        try {
            emailService.sendEmailAsync("tim10isamrs2021@gmail.com",
                    mail,
                    "Prihvaćena ponuda",
                    body);
        } catch (InterruptedException e) {
            System.out.println("Greska prilikom slanja maila Prihvaćena ponuda!");
        }

    }

    public boolean deleteOrder(String username, Long orderId) {
        PurchaseOrder order = checkIfMyOrder(username,orderId);
        if(order==null)
            return false;
        if(cannotDeleteOrder(order))
            return false;
        order.setDeleted(true);
        purchaseOrderRepository.save(order);
        return true;


    }

    private boolean cannotDeleteOrder(PurchaseOrder order) {
        switch (order.getStatus())
        {
            case PENDING:
                List<Supply> supplies = supplyRepository.getAllSuppliesByOrderId(order.getId());
                if (supplies!=null)
                    return !supplies.isEmpty();
                else return false;
            case ACTIVE:
                return true;
            case CLOSED:
                return false;
            default:
                return true;
        }
    }

    public List<FailedQueryDTO> getAllFailedQueries(String username) {
        List<FailedQueryDTO> ret = new ArrayList<>();
        PharmacyAdmin admin = pharmacyAdminRepository.findOneByUsername(username);
        if(admin == null)
            return ret;


            Comparator<MedicineInquiry> poDatumu = (q1, q2) -> {
                return q1.getTime().compareTo(q2.getTime());

            };

        Set<MedicineInquiry> inquiries =
                admin.getMedicineInquiries().stream().sorted(poDatumu).collect(Collectors.toSet());
        for(MedicineInquiry q : inquiries)
            ret.add(new FailedQueryDTO(q));

        return ret;
    }
}
