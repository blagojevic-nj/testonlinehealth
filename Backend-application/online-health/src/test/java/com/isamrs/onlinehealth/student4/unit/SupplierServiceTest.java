package com.isamrs.onlinehealth.student4.unit;

import com.isamrs.onlinehealth.dto.NeededForOrderDTO;
import com.isamrs.onlinehealth.dto.PurchaseOrderDTO;
import com.isamrs.onlinehealth.dto.PurchaseOrderItemDTO;
import com.isamrs.onlinehealth.dto.SupplyForOrderDTO;
import com.isamrs.onlinehealth.model.*;
import com.isamrs.onlinehealth.repository.PharmacyAdminRepository;
import com.isamrs.onlinehealth.repository.PurchaseOrderRepository;
import com.isamrs.onlinehealth.repository.SupplierRepository;
import com.isamrs.onlinehealth.repository.SupplyRepository;
import com.isamrs.onlinehealth.service.PharmacyAdminService;
import com.isamrs.onlinehealth.service.SupplierService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SupplierServiceTest {
    @InjectMocks
    private SupplierService supplierServiceMock;
    @Mock
    private SupplierRepository supplierRepositoryMock;
    @Mock
    private SupplyRepository supplyRepositoryMock;
    @Mock
    private PurchaseOrderRepository purchaseOrderRepositoryMock;
    @Mock
    private PharmacyAdminService pharmacyAdminService;

    private Supplier mockSupplier;
    private Supply mockSupply;
    private Supply mockSupplyInvalid;
    private PurchaseOrder mockPurchaseOrder;
    private PurchaseOrderDTO mockPurchaseOrderDTO;

    @Before
    public void setUp() {
        //medicine for all
        Map<Medicine, Integer> map = new HashMap<>();
        for (int i=0;i<3;i++){
            Medicine medicine = new Medicine();
            medicine.setRating(i*1.0);
            medicine.setReplacementMedicines(new HashSet<>());
            medicine.setType("Tip" + i);
            medicine.setRemarks("Napomena" + i);
            medicine.setName("Lek" + i);
            medicine.setManufacturer("Proizvodjac" + i);
            medicine.setIsPrescription(false);
            medicine.setPrescription(false);
            medicine.setIdentifier(1111111*(i+1) + "");
            medicine.setDescription("Opis" + i);
            medicine.setDailyDose(""  + i);
            medicine.setRatings(new HashSet<>());
            medicine.setId(i + 7L);
            map.put(medicine, i + 1);
        }
        //

        mockSupplier = new Supplier();
        mockSupplier.setUsername("dobavljac");
        mockSupplier.setEmail("dobavljac@gmail.com");
        mockSupplier.setPassword("password");
        mockSupplier.setFirstName("Borislav");
        mockSupplier.setLastName("Borilovic");
        mockSupplier.setEnabled(true);
        mockSupplier.setDeleted(false);
        mockSupplier.setSupplies(new HashSet<>());
        mockSupplier.setUser(USER_TYPE.SUPPLIER);
        mockSupplier.setAddress("Nova Ulica 1");
        mockSupplier.setCity("Novi Sad");
        mockSupplier.setCountry("Srbija");
        mockSupplier.setId(3L);
        mockSupplier.setPasswordChanged(true);
        mockSupplier.setPhoneNumber("0988817766");
        mockSupplier.setMedicineQuantity(map);

        mockPurchaseOrder = new PurchaseOrder();
        mockPurchaseOrder.setDeleted(false);
        mockPurchaseOrder.setDueDate(LocalDateTime.parse("2021-07-29T19:10:10.00"));
        mockPurchaseOrder.setId(4L);
        mockPurchaseOrder.setStatus(PurchaseOrderStatus.PENDING);
        mockPurchaseOrder.setMedicineQuantity(map);

        mockSupply = new Supply();
        mockSupply.setTotalPrice(1000.0);
        mockSupply.setDueDate(LocalDateTime.parse("2021-08-29T19:10:10.00"));
        mockSupply.setDeleted(false);
        mockSupply.setId(5L);
        mockSupply.setPurchaseOrder(mockPurchaseOrder);

        mockSupplyInvalid = new Supply();
        mockSupplyInvalid.setTotalPrice(1000.0);
        mockSupplyInvalid.setDueDate(LocalDateTime.parse("2021-06-29T19:10:10.00"));
        mockSupplyInvalid.setDeleted(false);
        mockSupplyInvalid.setId(6L);
        mockSupplyInvalid.setPurchaseOrder(mockPurchaseOrder);

        List<PurchaseOrderItemDTO> dtos = new ArrayList<>();
        for(Map.Entry<Medicine,Integer> m:mockPurchaseOrder.getMedicineQuantity().entrySet()){
            PurchaseOrderItemDTO dto = new PurchaseOrderItemDTO();
            dto.setMedicineName(m.getKey().getName());
            dto.setMedicineId(m.getKey().getId());
            dto.setKolicina(m.getValue());
            dto.setIdentifier(m.getKey().getIdentifier());
        }

        mockPurchaseOrderDTO = new PurchaseOrderDTO();
        mockPurchaseOrderDTO.setId(mockPurchaseOrder.getId());
        mockPurchaseOrderDTO.setStatus(mockPurchaseOrder.getStatus());
        mockPurchaseOrderDTO.setRok(LocalDate.parse("2021-07-29"));
        mockPurchaseOrderDTO.setMyOrder(false);
        mockPurchaseOrderDTO.setMedicines(dtos);
    }

    @Test()
    @Transactional
    @Rollback()
    public void checkInvalidAddSupply(){
        SupplyForOrderDTO dto = new SupplyForOrderDTO();
        dto.setTotalCost(mockSupplyInvalid.getTotalPrice().toString());
        dto.setSupplyId(mockSupplyInvalid.getId().toString());
        dto.setSupplierUsername(mockSupplier.getUsername());
        dto.setDueDate(mockSupplyInvalid.getDueDate());
        dto.setPurchaseOrderDTO(mockPurchaseOrderDTO);

        Mockito.when(supplierRepositoryMock.findOneByUsername("dobavljac")).thenReturn(mockSupplier);
        Mockito.when(purchaseOrderRepositoryMock.getOne(4L)).thenReturn(mockPurchaseOrder);
        Mockito.when(supplyRepositoryMock.save(mockSupply)).thenReturn(mockSupply);
        Mockito.when(supplierRepositoryMock.save(mockSupplier)).thenReturn(mockSupplier);

        String retVal = supplierServiceMock.addSupply(dto);

        assertEquals("Rok porudzbine ne moze biti pre roka narudzbine!",  retVal);

    }

    @Test()
    @Transactional
    @Rollback()
    public void checkValidConstraintOrder(){
        Mockito.when(supplierRepositoryMock.findOneByUsername("dobavljac")).thenReturn(mockSupplier);

        assertEquals(new ArrayList<>(), supplierServiceMock.checkConstraintOrder(mockSupplier.getUsername(), mockPurchaseOrderDTO));
    }

    @Test()
    @Transactional
    @Rollback()
    public void checkValidGetAllPurchaseOrders(){
        List<PurchaseOrder> purchaseOrders = new ArrayList<>();
        purchaseOrders.add(mockPurchaseOrder);
        mockSupplier.getSupplies().add(mockSupply);

        Mockito.when(supplierRepositoryMock.findOneByUsername("dobavljac")).thenReturn(mockSupplier);
        Mockito.when(purchaseOrderRepositoryMock.findAll()).thenReturn(purchaseOrders);
        Mockito.when(purchaseOrderRepositoryMock.save(mockPurchaseOrder)).thenReturn(mockPurchaseOrder);

        Mockito.doNothing().when(pharmacyAdminService).checkOrderDate(mockPurchaseOrder);
        List<PurchaseOrderDTO> retVal = supplierServiceMock.getAllPurchaseOrders(mockSupplier.getUsername());
        assertEquals(true, mockPurchaseOrder.getId().equals(retVal.get(0).getId()));
    }

}
