package com.isamrs.onlinehealth.student1.unit;

import com.isamrs.onlinehealth.dto.ReservationItemDTO;
import com.isamrs.onlinehealth.dto.ReservationListDTO;
import com.isamrs.onlinehealth.dto.StatsInfoDTO;
import com.isamrs.onlinehealth.model.*;
import com.isamrs.onlinehealth.repository.*;
import com.isamrs.onlinehealth.service.EmailService;
import com.isamrs.onlinehealth.service.PatientService;
import com.isamrs.onlinehealth.service.PharmacyService;
import com.isamrs.onlinehealth.service.ReservationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ReservationServiceTest {

    @InjectMocks
    public ReservationService reservationServiceMock;
    @Mock
    public ReservationRepository reservationRepositoryMock;
    @Mock
    public PatientService patientServiceMock;
    @Mock
    public PharmacyService pharmacyServiceMock;
    @Mock
    public PriceListRepository priceListRepositoryMock;
    @Mock
    public MedicineRepository medicineRepositoryMock;
    @Mock
    public ReservationItemRepository reservationItemRepositoryMock;
    @Mock
    public PriceListItemRepository priceListItemRepositoryMock;

    @Mock
    private EmailService emailServiceMock;

    private Patient mockPatient;
    private Pharmacy mockPharmacy;
    private ReservationList mockReservationList;
    private PriceList mockPriceList;
    private Medicine mockMedicine;
    private StatsInfoDTO dtoMock;


    @Before
    public void setUp() {
        mockPatient = new Patient();
        mockPatient.setId(5L);
        mockPatient.setPassword("password");
        mockPatient.setUsername("pacijent");
        mockPatient.setEmail("pacijent" + "@gmail.com");
        mockPatient.setFirstName("Miroslav");
        mockPatient.setLastName("Milojevic");
        mockPatient.setPasswordChanged(true);
        mockPatient.setAddress("Ulica");
        mockPatient.setCity("Novi Sad");
        mockPatient.setCountry("Srbija");
        mockPatient.setPhoneNumber("061" + "9876412");
        mockPatient.setUser(USER_TYPE.PATIENT);
        mockPatient.setDeleted(false);
        mockPatient.setEnabled(true);
        mockPatient.setPenalties(1.0);
        mockPatient.setPoints(30.0);

        dtoMock = new StatsInfoDTO();
        dtoMock.setDiscount(0.0);
        dtoMock.setPoints(0.0);
        dtoMock.setCategoryName("");



        mockMedicine = new Medicine();
        mockMedicine.setId(1L);
        mockMedicine.setDailyDose("4x3");
        mockMedicine.setDeleted(false);
        mockMedicine.setIdentifier("LK141141");
        mockMedicine.setDescription("noga");
        mockMedicine.setManufacturer("galenika");
        mockMedicine.setIsPrescription(false);
        mockMedicine.setName("Rapidol");
        mockMedicine.setRating(4.2);
        mockMedicine.setRemarks("opasno");
        mockMedicine.setType("NSAID");

        mockPriceList = new PriceList();
        mockPriceList.setDeleted(false);
        mockPriceList.setId(1L);
        mockPriceList.setPriceListItems(new HashSet<>());
        mockPriceList.getPriceListItems().add(new PriceListItem(1L, LocalDateTime.of(2021, 3,10,12, 20), LocalDateTime.of(2022, 3,10,12, 20), mockMedicine, 1000.0, 10));

        mockPharmacy = new Pharmacy();
        mockPharmacy.setId(2L);
        mockPharmacy.setName("Benu");
        mockPharmacy.setDescription("Neka apoteka");
        mockPharmacy.setDeleted(false);
        mockPharmacy.setRating(3.7);
        mockPharmacy.setConsultation_price(2000);
        mockPharmacy.setRatings(new LinkedHashSet<>(new ArrayList<>()));
        mockPharmacy.setDermatologists(new LinkedHashSet<>());
        mockPharmacy.setPricelist(mockPriceList);

        mockReservationList = new ReservationList();
        mockReservationList.setPatient(mockPatient);
        mockReservationList.setStatus(ReservationStatus.RESERVED);
        mockReservationList.setDeadline(LocalDateTime.now());
        mockReservationList.setPharmacy(mockPharmacy);
        mockReservationList.setTotalPrice(1000);
        mockReservationList.setReservationItems(new HashSet<ReservationItem>());
        mockReservationList.getReservationItems().add(new ReservationItem(null, mockMedicine, 1, 1000));

    }

    @Test()
    @Transactional
    @Rollback(value = true)
    public void addReservation(){
        Mockito.when(patientServiceMock.findByUsername("pacijent")).thenReturn(mockPatient);
        Mockito.when(pharmacyServiceMock.getPharmacyById(2L)).thenReturn(java.util.Optional.ofNullable(mockPharmacy));
        Mockito.when(medicineRepositoryMock.findById(1L)).thenReturn(java.util.Optional.ofNullable(mockMedicine));
        Mockito.when(priceListRepositoryMock.findOneById(1L)).thenReturn(mockPriceList);
        Mockito.when(patientServiceMock.getStats("pacijent")).thenReturn(dtoMock);
        Mockito.when(reservationRepositoryMock.save(Mockito.any(ReservationList.class))).thenAnswer(i -> i.getArguments()[0]);
        try {
            Mockito.doNothing().when(emailServiceMock).sendEmailAsync("",
                    mockPatient.getEmail(),
                    "Rezervacija leka",
                    "Uspesno ste rezervisali lekove. Broj vase rezervacije: " + 1L + ".");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ReservationListDTO dto = new ReservationListDTO();
        dto.setStatus(ReservationStatus.RESERVED);
        dto.setPatient("pacijent");
        dto.setPharmacy(2L);
        dto.setDeadline(mockReservationList.getDeadline());
        dto.setTotalPrice(1000);
        dto.setPharmacy(2L);
        dto.setItems(new HashSet<>());
        dto.getItems().add(new ReservationItemDTO(1L, 1, 1000));

        boolean rez = reservationServiceMock.addReservation(dto, pharmacyServiceMock, patientServiceMock);
        assertEquals(true, rez);
    }

    @Test()
    @Transactional
    @Rollback(value = true)
    public void addReservationInvalid(){

        Mockito.when(patientServiceMock.findByUsername("pacijent")).thenReturn(mockPatient);
        Mockito.when(pharmacyServiceMock.getPharmacyById(2L)).thenReturn(java.util.Optional.ofNullable(mockPharmacy));
        Mockito.when(medicineRepositoryMock.findById(1L)).thenReturn(java.util.Optional.ofNullable(mockMedicine));
        Mockito.when(priceListRepositoryMock.findOneById(1L)).thenReturn(mockPriceList);
        Mockito.when(patientServiceMock.getStats("pacijent")).thenReturn(dtoMock);
        Mockito.when(reservationRepositoryMock.save(Mockito.any(ReservationList.class))).thenAnswer(i -> i.getArguments()[0]);
        try {
            Mockito.doNothing().when(emailServiceMock).sendEmailAsync("",
                    mockPatient.getEmail(),
                    "Rezervacija leka",
                    "Uspesno ste rezervisali lekove. Broj vase rezervacije: " + 1L + ".");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ReservationListDTO dto = new ReservationListDTO();
        dto.setStatus(ReservationStatus.RESERVED);
        dto.setPatient("pacijent");
        dto.setPharmacy(2L);
        dto.setDeadline(mockReservationList.getDeadline());
        dto.setTotalPrice(1000);
        dto.setPharmacy(2L);
        dto.setItems(new HashSet<>());
        dto.getItems().add(new ReservationItemDTO(1L, 111, 1000));

        boolean rez = reservationServiceMock.addReservation(dto, pharmacyServiceMock, patientServiceMock);
        assertFalse(rez);
    }

    @Test()
    @Transactional
    @Rollback(value = true)
    public void cancelReservation(){

        Mockito.when(patientServiceMock.findByUsername("pacijent")).thenReturn(mockPatient);
        Mockito.when(pharmacyServiceMock.getPharmacyById(2L)).thenReturn(java.util.Optional.ofNullable(mockPharmacy));
        Mockito.when(medicineRepositoryMock.findById(1L)).thenReturn(java.util.Optional.ofNullable(mockMedicine));
        Mockito.when(priceListRepositoryMock.findOneById(1L)).thenReturn(mockPriceList);
        Mockito.when(patientServiceMock.getStats("pacijent")).thenReturn(dtoMock);
        Mockito.when(reservationRepositoryMock.save(Mockito.any(ReservationList.class))).thenAnswer(i -> i.getArguments()[0]);
        Mockito.when(reservationRepositoryMock.findById(10L)).thenReturn(Optional.empty());
        try {
            Mockito.doNothing().when(emailServiceMock).sendEmailAsync("",
                    mockPatient.getEmail(),
                    "Rezervacija leka",
                    "Uspesno ste rezervisali lekove. Broj vase rezervacije: " + 1L + ".");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        boolean rez = reservationServiceMock.cancelReservation(10L);
        assertFalse(rez);
    }
}
