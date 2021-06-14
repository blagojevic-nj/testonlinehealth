package com.isamrs.onlinehealth.student3.unit;


import com.isamrs.onlinehealth.model.*;
import com.isamrs.onlinehealth.repository.*;
import com.isamrs.onlinehealth.service.EmailService;
import com.isamrs.onlinehealth.service.ExaminationsService;
import com.isamrs.onlinehealth.service.LoyaltyProgramService;
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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ScheduleExaminationServiceTest {

    @Mock
    private ExaminationsRepository examinationsRepositoryMock;
    @Mock
    private PatientRepository patientRepositoryMock;

    @Mock
    private ConsultationsRepository consultationsRepositoryMock;

    @InjectMocks
    private ExaminationsService examinationsServiceMock;

    @Mock
    private PharmacyRepository pharmacyRepositoryMock;

    @Mock
    private DermatologistRepository dermatologistRepositoryMock;

    @Mock
    private LoyaltyProgramService loyaltyProgramServiceMock;

    @Mock
    private EmailService emailServiceMock;


    private Patient mockPatient;
    private Dermatologist mockDermatologist;
    private WorkHours mockWorhHours;
    private Pharmacy mockPharmacy;
    private Collection<Examination> mockExaminations;
    private Examination mockExamination;
    private Examination mockNewValidExamination;
    private Examination mockNewInvalidExaminationWorkHours;
    private Examination mockNewInvalidLeaveRequest;


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


        mockDermatologist = new Dermatologist();
        mockDermatologist.setId(4L);
        mockDermatologist.setPassword("password");
        mockDermatologist.setUsername("dermatolog");
        mockDermatologist.setEmail("dermatolog" + "@gmail.com");
        mockDermatologist.setFirstName("Srboljub");
        mockDermatologist.setLastName("Srbanovic");
        mockDermatologist.setPasswordChanged(true);
        mockDermatologist.setAddress("Ulica");
        mockDermatologist.setCity("Novi Sad");
        mockDermatologist.setCountry("Srbija");
        mockDermatologist.setPhoneNumber("06" + "3487611");
        mockDermatologist.setUser(USER_TYPE.DERMATOLOGIST);
        mockDermatologist.setDeleted(false);
        mockDermatologist.setEnabled(true);

        mockPharmacy = new Pharmacy();
        mockPharmacy.setId(2L);
        mockPharmacy.setName("Benu");
        mockPharmacy.setDescription("Neka apoteka");
        mockPharmacy.setDeleted(false);
        mockPharmacy.setRating(3.7);
        mockPharmacy.setConsultation_price(2000);
        mockPharmacy.setRatings(new LinkedHashSet<>(new ArrayList<>()));
        mockPharmacy.setDermatologists(new LinkedHashSet<>());
        mockPharmacy.getDermatologists().add(mockDermatologist);
        mockWorhHours = new WorkHours(LocalTime.of(2, 0, 0), LocalTime.of(23, 59, 59), false);
        mockWorhHours.setId(3L);
        mockDermatologist.getWorkHoursPharmacies().put(mockPharmacy, mockWorhHours);

        mockExamination = new Examination();
        mockExamination.setId(1L);
        mockExamination.setDeleted(false);
        mockExamination.setStart(LocalDateTime.parse("2021-07-29T07:50:00.00"));
        mockExamination.setReport("Ovde ide kao neki izvestaj o toku pregleda!");
        mockExamination.setEnd(LocalDateTime.parse("2021-07-29T19:10:10.00"));
        mockExamination.setPrice(1000.0);
        mockExamination.setDermatologist(mockDermatologist);
        mockExamination.setPatient(mockPatient);
        mockExamination.setPharmacy(mockPharmacy);
        mockExaminations = new HashSet<>();
        mockExaminations.add(mockExamination);

        mockNewValidExamination = new Examination();
        mockNewValidExamination.setId(6L);
        mockNewValidExamination.setDeleted(false);
        mockNewValidExamination.setStart(LocalDateTime.parse("2021-08-29T07:50:00.00"));
        mockNewValidExamination.setReport("Ovde ide kao neki izvestaj o toku pregleda!");
        mockNewValidExamination.setEnd(LocalDateTime.parse("2021-08-29T19:10:10.00"));
        mockNewValidExamination.setPrice(1000.0);
        mockNewValidExamination.setDermatologist(mockDermatologist);
        mockNewValidExamination.setPharmacy(mockPharmacy);
        mockExaminations.add(mockNewValidExamination);

        mockNewInvalidExaminationWorkHours = new Examination();
        mockNewInvalidExaminationWorkHours.setId(7L);
        mockNewInvalidExaminationWorkHours.setDeleted(false);
        mockNewInvalidExaminationWorkHours.setStart(LocalDateTime.parse("2021-08-29T00:50:00.00"));
        mockNewInvalidExaminationWorkHours.setReport("Ovde ide kao neki izvestaj o toku pregleda!");
        mockNewInvalidExaminationWorkHours.setEnd(LocalDateTime.parse("2021-08-29T19:10:10.00"));
        mockNewInvalidExaminationWorkHours.setPrice(1000.0);
        mockNewInvalidExaminationWorkHours.setDermatologist(mockDermatologist);
        mockNewInvalidExaminationWorkHours.setPharmacy(mockPharmacy);
        mockExaminations.add(mockNewInvalidExaminationWorkHours);

        mockNewInvalidLeaveRequest = new Examination();
        mockNewInvalidLeaveRequest.setId(8L);
        mockNewInvalidLeaveRequest.setDeleted(false);
        mockNewInvalidLeaveRequest.setStart(LocalDateTime.parse("2021-09-29T10:50:00.00"));
        mockNewInvalidLeaveRequest.setReport("Ovde ide kao neki izvestaj o toku pregleda!");
        mockNewInvalidLeaveRequest.setEnd(LocalDateTime.parse("2021-09-29T14:10:10.00"));
        mockNewInvalidLeaveRequest.setPrice(1000.0);
        mockNewInvalidLeaveRequest.setPharmacy(mockPharmacy);

        mockDermatologist.getLeaveRequests().add(new LeaveRequest(10L, LocalDateTime.of(2021, 9, 1, 0, 0, 0), LocalDateTime.of(2021, 10, 15, 0, 0, 0), LEAVE_REQUEST_STATUS.ACCEPTED));

        mockExaminations.add(mockNewInvalidLeaveRequest);

    }


    @Test()
    @Transactional
    @Rollback()
    public void checkInvalidWorkHours() {
        Mockito.when(pharmacyRepositoryMock.findOneById(2L)).thenReturn(mockPharmacy);
        Mockito.when(examinationsRepositoryMock.findByIdAndLock(7L)).thenReturn(Optional.of(mockNewInvalidExaminationWorkHours));
        Mockito.when(dermatologistRepositoryMock.findOneByUsername("dermatolog")).thenReturn(mockDermatologist);
        Mockito.when(patientRepositoryMock.findById(5L)).thenReturn(Optional.of(mockPatient));

        Boolean result = examinationsServiceMock.scheduleExisting(5L, "dermatolog", 7L);

        assertEquals(false, result);

    }

    @Test()
    @Transactional
    @Rollback()
    public void checkValidWorkHours() {
        Mockito.when(pharmacyRepositoryMock.findOneById(2L)).thenReturn(mockPharmacy);
        Mockito.when(examinationsRepositoryMock.findByIdAndLock(6L)).thenReturn(Optional.of(mockNewValidExamination));
        Mockito.when(dermatologistRepositoryMock.findOneByUsername("dermatolog")).thenReturn(mockDermatologist);
        Mockito.when(patientRepositoryMock.findById(5L)).thenReturn(Optional.of(mockPatient));
        Mockito.when(examinationsRepositoryMock.save(mockNewValidExamination)).thenReturn(mockNewValidExamination);
        Mockito.when(dermatologistRepositoryMock.save(mockDermatologist)).thenReturn(mockDermatologist);
        Mockito.when(patientRepositoryMock.save(mockPatient)).thenReturn(mockPatient);
        try {
            Mockito.doNothing().when(emailServiceMock).sendEmailAsync("",
                    mockPatient.getEmail(),
                    "Zakazivanje pregleda",
                    "Uspesno ste zakazali termin za " + mockNewValidExamination.getStart().toString() + ".");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Boolean result = examinationsServiceMock.scheduleExisting(5L, "dermatolog", 6L);

        assertEquals(true, result);
    }


    @Test()
    @Transactional
    @Rollback()
    public void checkInvalidLeaveRequest() {
        Mockito.when(pharmacyRepositoryMock.findOneById(2L)).thenReturn(mockPharmacy);
        Mockito.when(examinationsRepositoryMock.findByIdAndLock(8L)).thenReturn(Optional.of(mockNewInvalidLeaveRequest));
        Mockito.when(dermatologistRepositoryMock.findOneByUsername("dermatolog")).thenReturn(mockDermatologist);
        Mockito.when(patientRepositoryMock.findById(5L)).thenReturn(Optional.of(mockPatient));


        Boolean result = examinationsServiceMock.scheduleExisting(5L, "dermatolog", 8L);

        assertEquals(false, result);

    }

}
