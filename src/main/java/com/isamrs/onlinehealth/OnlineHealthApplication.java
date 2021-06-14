package com.isamrs.onlinehealth;

import com.isamrs.onlinehealth.dto.EqrPrescriptionDTO;
import com.isamrs.onlinehealth.dto.EqrPrescriptionItemDTO;
import com.isamrs.onlinehealth.model.*;
import com.isamrs.onlinehealth.repository.*;
import com.isamrs.onlinehealth.service.EPrescriptionService;
import com.isamrs.onlinehealth.service.RoleService;
import com.isamrs.onlinehealth.service.SupplierService;
import com.isamrs.onlinehealth.utils.QRCodeGen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@EnableScheduling
@SpringBootApplication
public class OnlineHealthApplication implements CommandLineRunner {
	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	RoleService roleService;

	@Autowired
	DermatologistRepository dermatologistRepository;

	@Autowired
	PharmacistRepository pharmacistRepository;

	@Autowired
	PatientRepository patientRepository;

	@Autowired
	PharmacyAdminRepository pharmacyAdminRepository;

	@Autowired
	SystemAdminRepository systemAdminRepository;

	@Autowired
	SupplierRepository supplierRepository;

	@Autowired
	MedicineRepository medicineRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PriceListRepository priceListRepository;
	@Autowired
	PriceListItemRepository priceListItemRepository;
	@Autowired
	LocationRepositrory locationRepositrory;
	@Autowired
	RatingItemRepository ratingItemRepository;
	@Autowired
	PharmacyRepository pharmacyRepository;
	@Autowired
	ExaminationsRepository examinationsRepository;
	@Autowired
	ConsultationsRepository consultationsRepository;
	@Autowired
	EPrescriptionService ePrescriptionService;

	@Autowired
	LoyaltyCategoryRepository loyaltyCategoryRepository;
	@Autowired
	LoyaltyProgramRepository loyaltyProgramRepository;
	@Autowired
	SupplierService supplierService;
	@Autowired
	LeaveRequestRepository leaveRequestRepository;

	@Autowired
	WorkHoursRepository workHoursRepository;

	public static void main(String[] args) {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		SpringApplication.run(OnlineHealthApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {
		Role role = new Role();
		role.setName("ROLE_PATIENT");
		roleRepository.save(role);
		role = new Role();
		role.setName("ROLE_DERMATOLOGIST");
		roleRepository.save(role);
		role = new Role();
		role.setName("ROLE_PHARMACY_ADMIN");
		roleRepository.save(role);
		role = new Role();
		role.setName("ROLE_SYSTEM_ADMIN");
		roleRepository.save(role);
		role = new Role();
		role.setName("ROLE_PHARMACIST");
		roleRepository.save(role);
		role = new Role();
		role.setName("ROLE_SUPPLIER");
		roleRepository.save(role);

		List<Patient> patients = new ArrayList<>();
		Set<Dermatologist> dermatologists = new HashSet<>();
		List<Pharmacist> pharmacists = new ArrayList<>();
		List<PharmacyAdmin> admins = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			Dermatologist dermatologist = new Dermatologist();
			List<Role> roles = roleService.findByName("ROLE_DERMATOLOGIST");
			dermatologist.setPassword(passwordEncoder.encode("ringispil" + i));
			dermatologist.setRoles(roles);
			dermatologist.setUsername("dermatolog" + i);
			dermatologist.setEmail("dermatolog" + i + "@gmail.com");
			dermatologist.setFirstName("Srboljub" + i);
			dermatologist.setLastName("Srbanovic" + i);
			dermatologist.setPasswordChanged(true);
			dermatologist.setAddress("Ulica" + i);
			dermatologist.setCity("Novi Sad");
			dermatologist.setCountry("Srbija");
			dermatologist.setPhoneNumber("06" + i + "3487611");
			dermatologist.setUser(USER_TYPE.DERMATOLOGIST);
			dermatologist.setDeleted(false);
			dermatologist.setEnabled(true);
			dermatologistRepository.save(dermatologist);
			dermatologists.add(dermatologist);
			/*-------------------------------------------------------*/
			Pharmacist pharmacist = new Pharmacist();
			roles = roleService.findByName("ROLE_PHARMACIST");
			pharmacist.setPassword(passwordEncoder.encode("vetrenaca" + i));
			pharmacist.setRoles(roles);
			pharmacist.setUsername("farmaceut" + i);
			pharmacist.setEmail("farmaceut" + i + "@gmail.com");
			pharmacist.setFirstName("Petar" + i);
			pharmacist.setLastName("Petrusic" + i);
			pharmacist.setPasswordChanged(true);
			pharmacist.setAddress("Ulica" + i);
			pharmacist.setCity("Novi Sad");
			pharmacist.setCountry("Srbija");
			pharmacist.setPhoneNumber("06" + i + "4554731");
			pharmacist.setUser(USER_TYPE.PHARMACIST);
			pharmacist.setDeleted(false);
			pharmacist.setEnabled(true);
			WorkHours wH1 = new WorkHours(LocalTime.of(7 + i, 0), LocalTime.of(15 + i, 0), false);
			pharmacist.setWorkHours(workHoursRepository.save(wH1));
			pharmacistRepository.save(pharmacist);
			pharmacists.add(pharmacist);
			/*------------------------------------------------------*/
			Patient patient = new Patient();
			roles = roleService.findByName("ROLE_PATIENT");
			patient.setPassword(passwordEncoder.encode("password" + i));
			patient.setRoles(roles);
			patient.setUsername("pacijent" + i);
			patient.setEmail("pacijent" + i + "@gmail.com");
			patient.setFirstName("Miroslav" + i);
			patient.setLastName("Milojevic" + i);
			patient.setPasswordChanged(true);
			patient.setAddress("Ulica" + i);
			patient.setCity("Novi Sad");
			patient.setCountry("Srbija");
			patient.setPhoneNumber("06" + i + "9876412");
			patient.setUser(USER_TYPE.PATIENT);
			patient.setDeleted(false);
			patient.setEnabled(true);
			patient.setPenalties(1.0);
			patient.setPoints(30.0);
			patientRepository.save(patient);
			patients.add(patient);
			/*------------------------------------------------------*/
			PharmacyAdmin pharmacyAdmin = new PharmacyAdmin();
			roles = roleService.findByName("ROLE_PHARMACY_ADMIN");
			pharmacyAdmin.setPassword(passwordEncoder.encode("password" + i));
			pharmacyAdmin.setRoles(roles);
			pharmacyAdmin.setUsername("adminApoteka" + i);
			pharmacyAdmin.setEmail("adminApoteka" + i + "@gmail.com");
			pharmacyAdmin.setFirstName("Dragoljub" + i);
			pharmacyAdmin.setLastName("Draganovic" + i);
			pharmacyAdmin.setPasswordChanged(true);
			pharmacyAdmin.setAddress("Ulica" + i);
			pharmacyAdmin.setCity("Novi Sad");
			pharmacyAdmin.setCountry("Srbija");
			pharmacyAdmin.setPhoneNumber("06" + i + "2376551");
			pharmacyAdmin.setUser(USER_TYPE.PHARMACY_ADMIN);
			pharmacyAdmin.setDeleted(false);
			pharmacyAdmin.setEnabled(true);
			admins.add(pharmacyAdmin);
			pharmacyAdminRepository.save(pharmacyAdmin);
			/*------------------------------------------------------*/
			SystemAdmin systemAdmin = new SystemAdmin();
			roles = roleService.findByName("ROLE_SYSTEM_ADMIN");
			systemAdmin.setPassword(passwordEncoder.encode("centrifuga" + i));
			systemAdmin.setRoles(roles);
			systemAdmin.setUsername("sistemskiAdmin" + i);
			systemAdmin.setEmail("sistemskiAdmin" + i + "@gmail.com");
			systemAdmin.setFirstName("Nikola" + i);
			systemAdmin.setLastName("Nikolic" + i);
			systemAdmin.setPasswordChanged(true);
			systemAdmin.setAddress("Ulica" + i);
			systemAdmin.setCity("Novi Sad");
			systemAdmin.setCountry("Srbija");
			systemAdmin.setPhoneNumber("06" + i + "5641321");
			systemAdmin.setUser(USER_TYPE.SYSTEM_ADMIN);
			systemAdmin.setDeleted(false);
			systemAdmin.setEnabled(true);
			systemAdminRepository.save(systemAdmin);
			/*------------------------------------------------------*/
			Supplier supplier = new Supplier();
			roles = roleService.findByName("ROLE_SUPPLIER");
			supplier.setPassword(passwordEncoder.encode("vesmasina" + i));
			supplier.setRoles(roles);
			supplier.setUsername("dobavljac" + i);
			supplier.setEmail("dobavljac" + i + "@gmail.com");
			supplier.setFirstName("Svetozar" + i);
			supplier.setLastName("Dangubic" + i);
			supplier.setPasswordChanged(true);
			supplier.setAddress("Ulica" + i);
			supplier.setCity("Novi Sad");
			supplier.setCountry("Srbija");
			supplier.setPhoneNumber("06" + i + "7652121");
			supplier.setUser(USER_TYPE.SUPPLIER);
			supplier.setDeleted(false);
			supplier.setEnabled(true);
			supplierRepository.save(supplier);
		}

		Medicine medicine1 = new Medicine();
		medicine1.setDailyDose("2x3");
		medicine1.setDescription("glava");
		medicine1.setIdentifier("LK111111");
		medicine1.setIsPrescription(false);
		medicine1.setManufacturer("galenika");
		medicine1.setName("Ibuprofen");
		medicine1.setRemarks("otrovno");
		medicine1.setType("NSAID");
		medicine1.setRating(1.5);

		Medicine medicine2 = new Medicine();
		medicine2.setDailyDose("2x4");
		medicine2.setDescription("stomak");
		medicine2.setIdentifier("LK121121");
		medicine2.setIsPrescription(false);
		medicine2.setManufacturer("hemofarm");
		medicine2.setName("Advil");
		medicine2.setRemarks("toksicno");
		medicine2.setType("intravenski");
		medicine2.setRating(3.1);

		Medicine medicine3 = new Medicine();
		medicine3.setDailyDose("4x3");
		medicine3.setDescription("noga");
		medicine3.setIdentifier("LK141141");
		medicine3.setIsPrescription(false);
		medicine3.setManufacturer("galenika");
		medicine3.setName("Rapidol");
		medicine3.setRemarks("opasno");
		medicine3.setType("NSAID");
		medicine3.setRating(4.2);

		Medicine medicine4 = new Medicine();
		medicine4.setDailyDose("10x3");
		medicine4.setDescription("zglobovi");
		medicine4.setIdentifier("LK511511");
		medicine4.setIsPrescription(false);
		medicine4.setManufacturer("hemofarm");
		medicine4.setName("Brufen");
		medicine4.setRemarks("cudno");
		medicine4.setType("intravenski");
		medicine4.setRating(0.3);

		Medicine medicine5 = new Medicine();
		medicine5.setDailyDose("1x3");
		medicine5.setDescription("oko");
		medicine5.setIdentifier("LK116116");
		medicine5.setIsPrescription(false);
		medicine5.setManufacturer("galenika");
		medicine5.setName("Paracetamol");
		medicine5.setRemarks("sjajno");
		medicine5.setType("NSAID");
		medicine5.setRating(5.0);

		Medicine medicine6 = new Medicine();
		medicine6.setDailyDose("1x1");
		medicine6.setDescription("zub");
		medicine6.setIdentifier("LK181181");
		medicine6.setIsPrescription(false);
		medicine6.setManufacturer("hemofarm");
		medicine6.setName("Tylenol");
		medicine6.setRemarks("beskorisno");
		medicine6.setType("NSAID");
		medicine6.setRating(3.8);

		Medicine medicine7 = new Medicine();
		medicine7.setDailyDose("5x5");
		medicine7.setDescription("kosa");
		medicine7.setIdentifier("LK911911");
		medicine7.setIsPrescription(false);
		medicine7.setManufacturer("galenika");
		medicine7.setName("Panadol");
		medicine7.setRemarks("korisno");
		medicine7.setType("intravenski");
		medicine7.setRating(2.6);

		Medicine medicine8 = new Medicine();
		medicine8.setDailyDose("8x8");
		medicine8.setDescription("koleno");
		medicine8.setIdentifier("LK811811");
		medicine8.setIsPrescription(false);
		medicine8.setManufacturer("hemofarm");
		medicine8.setName("Acetaminophen");
		medicine8.setRemarks("useless");
		medicine8.setType("NSAID");
		medicine8.setRating(4.9);



		medicineRepository.save(medicine3);
		medicineRepository.save(medicine4);
		medicineRepository.save(medicine5);
		medicineRepository.save(medicine6);
		medicineRepository.save(medicine7);
		medicineRepository.save(medicine8);

		Set<Medicine> medicines = new HashSet<>();
		medicines.add(medicineRepository.save(medicine1));
		medicines.add(medicineRepository.save(medicine2));
		medicines.add(medicine3);
		medicines.add(medicine4);
		medicines.add(medicine5);
		medicines.add(medicine6);
		medicines.add(medicine7);
		medicines.add(medicine8);

		LocalDateTime convertedDateTime = LocalDateTime.parse("2020-08-07T18:00:00.00");
		Set<PriceListItem> priceListItems = new HashSet<>();
		int i = 0;
		for (Medicine m : medicines) {
			i++;
			PriceListItem priceListItem = new PriceListItem();
			priceListItem.setLek(m);
			priceListItem.setAvailableQuantity(10 + i);
			priceListItem.setEnd(convertedDateTime);
			priceListItem.setStart(convertedDateTime);
			priceListItem.setDeleted(false);
			priceListItem.setPrice(1000.0 + i*i);
			priceListItems.add(priceListItemRepository.save(priceListItem));
		}

		PriceList priceList1 = new PriceList();
		priceList1.setPriceListItems(new LinkedHashSet<>(new ArrayList<>(priceListItems).subList(0,2)));
		priceList1.setDeleted(false);
		PriceList priceList2 = new PriceList();
		priceList2.setPriceListItems(new LinkedHashSet<>(new ArrayList<>(priceListItems).subList(2,4)));
		priceList2.setDeleted(false);
		priceListRepository.save(priceList2);

		Location location1 = new Location();
		location1.setDeleted(false);
		location1.setAddress("Novi Sad, Bulevar Oslobodjenja");
		location1.setLatitude(45.25505014411735);
		location1.setLongitude(19.835028744264086);
		locationRepositrory.save(location1);
		Location location2 = new Location();
		location2.setDeleted(false);
		location2.setAddress("Novi Sad, Bulevar Cara Lazara");
		location2.setLatitude(45.25022169026069);
		location2.setLongitude(19.84839843046331);
		locationRepositrory.save(location2);

//		Set<RatingItem> ratingItems = new HashSet<>();
//		for (int j = 0; j < 5; j++) {
//			RatingItem ratingItem = new RatingItem();
//			ratingItem.setDeleted(false);
//			ratingItem.setRating(j*2.0);
//			ratingItem.setPatient(patients.get(j));
//			ratingItems.add(ratingItemRepository.save(ratingItem));
//		}

		Pharmacy pharmacy1 = new Pharmacy();
		pharmacy1.setName("Benu");
		pharmacy1.setDescription("Neka apoteka");
		pharmacy1.setDeleted(false);
		pharmacy1.setPricelist(priceListRepository.save(priceList1));
		pharmacy1.setLocation(location1);
		pharmacy1.setRating(3.7);
		pharmacy1.setConsultation_price(2000);
		pharmacy1.setRatings(new LinkedHashSet<>(new ArrayList<>()));
		pharmacy1.setDermatologists(new LinkedHashSet<>(new ArrayList<>(dermatologists).subList(0, 3)));
		pharmacy1.setPharmacists(new HashSet<Pharmacist>(pharmacists.subList(0, 2)));
		pharmacyRepository.save(pharmacy1);

		Dermatologist dermatologist = dermatologistRepository.findOneByUsername("dermatolog2");
		dermatologist.getWorkHoursPharmacies().put(pharmacyRepository.findOneById(1L), workHoursRepository.save(new WorkHours(LocalTime.of(2, 0, 0), LocalTime.of(23, 59, 59), false)));
		dermatologistRepository.save(dermatologist);

		Dermatologist dermatologist1 = dermatologistRepository.findOneByUsername("dermatolog1");
		dermatologist.getWorkHoursPharmacies().put(pharmacyRepository.findOneById(1L), workHoursRepository.save(new WorkHours(LocalTime.of(2, 0, 0), LocalTime.of(23, 59, 59), false)));
		dermatologistRepository.save(dermatologist1);

		Pharmacy pharmacy2 = new Pharmacy();
		pharmacy2.setName("DrMax");
		pharmacy2.setDescription("Najbolja apoteka u gradu");
		pharmacy2.setDeleted(false);
		pharmacy2.setPricelist(priceList2);
		pharmacy2.setLocation(location2);
		pharmacy2.setRating(0);
		pharmacy2.setConsultation_price(1500);
		pharmacy2.setRatings(new LinkedHashSet<>(new ArrayList<>()));
		pharmacy2.setDermatologists(new LinkedHashSet<>(new ArrayList<>(dermatologists).subList(3,5)));
		pharmacy2.setPharmacists(new HashSet<Pharmacist>(pharmacists.subList(3,5)));
		pharmacyRepository.save(pharmacy2);

		List<Dermatologist> dermatologistList = new ArrayList<>(dermatologists);

		Examination examination = new Examination();
		examination.setDeleted(false);
		examination.setEnd(LocalDateTime.parse("2021-04-25T09:50:00.00"));
		examination.setReport("");
		examination.setStart(LocalDateTime.parse("2021-04-25T09:10:10.00"));
		examination.setPrice(2000.0);
		examination.setDermatologist(dermatologistList.get(0));
		examination.setPatient(null);
		examination.setPharmacy(pharmacy1);
		examinationsRepository.save(examination);
		examination = new Examination();
		examination.setDeleted(false);
		examination.setEnd(LocalDateTime.parse("2021-04-26T19:50:00.00"));
		examination.setReport("");
		examination.setStart(LocalDateTime.parse("2021-04-26T09:10:10.00"));
		examination.setPrice(1000.0);
		examination.setDermatologist(dermatologistList.get(1));
		examination.setPatient(patients.get(2));
		examination.setPharmacy(pharmacy1);
		examinationsRepository.save(examination);
		examination = new Examination();
		examination.setDeleted(false);
		examination.setEnd(LocalDateTime.parse("2021-04-27T10:50:00.00"));
		examination.setReport("Ovde ide kao neki izvestaj o toku pregleda!");
		examination.setStart(LocalDateTime.parse("2021-04-27T07:10:10.00"));
		examination.setPrice(1000.0);
		examination.setDermatologist(dermatologistList.get(1));
		examination.setPatient(patients.get(1));
		examination.setPharmacy(pharmacy1);
		examinationsRepository.save(examination);
		examination = new Examination();
		examination.setDeleted(false);
		examination.setEnd(LocalDateTime.parse("2021-05-17T19:50:00.00"));
		examination.setReport("");
		examination.setStart(LocalDateTime.parse("2021-05-17T09:10:10.00"));
		examination.setPrice(1000.0);
		examination.setDermatologist(dermatologistList.get(1));
		examination.setPharmacy(pharmacy1);
		examination.setPatient(patients.get(0));
		examinationsRepository.save(examination);
		examination = new Examination();
		examination.setDeleted(false);
		examination.setEnd(LocalDateTime.parse("2021-07-14T09:50:00.00"));
		examination.setReport("");
		examination.setStart(LocalDateTime.parse("2021-07-14T09:10:10.00"));
		examination.setPrice(500.0);
		examination.setDermatologist(dermatologistList.get(0));
		examination.setPatient(null);
		examination.setPharmacy(pharmacy2);
		examinationsRepository.save(examination);
		examination = new Examination();
		examination.setDeleted(false);
		examination.setEnd(LocalDateTime.parse("2021-04-16T23:50:00.00"));
		examination.setReport("Potrebno raditi dalje preglede, pacijent pokazuje agresiju!");
		examination.setStart(LocalDateTime.parse("2021-04-16T19:10:10.00"));
		examination.setPrice(500.0);
		examination.setDermatologist(dermatologistList.get(0));
		examination.setPatient(patients.get(1));
		examination.setPharmacy(pharmacy2);
		examinationsRepository.save(examination);
		examination = new Examination();
		examination.setDeleted(false);
		examination.setEnd(LocalDateTime.parse("2021-05-03T19:50:00.00"));
		examination.setReport("Ovde ide kao neki izvestaj o toku pregleda!");
		examination.setStart(LocalDateTime.parse("2021-05-03T07:10:10.00"));
		examination.setPrice(1000.0);
		examination.setDermatologist(dermatologistList.get(1));
		examination.setPatient(patients.get(1));
		examination.setPharmacy(pharmacy1);
		examinationsRepository.save(examination);


		examination = new Examination();
		examination.setDeleted(false);
		examination.setEnd(LocalDateTime.parse("2021-05-25T19:50:00.00"));
		examination.setReport("Ovde ide kao neki izvestaj o toku pregleda!");
		examination.setStart(LocalDateTime.parse("2021-05-25T07:10:10.00"));
		examination.setPrice(1000.0);
		examination.setDermatologist(dermatologistList.get(1));
		examination.setPatient(patients.get(1));
		examination.setPharmacy(pharmacy1);
		examinationsRepository.save(examination);


		examination = new Examination();
		examination.setDeleted(false);
		examination.setStart(LocalDateTime.parse("2021-05-29T00:50:00.00"));
		examination.setReport("Ovde ide kao neki izvestaj o toku pregleda!");
		examination.setEnd(LocalDateTime.parse("2021-05-29T19:10:10.00"));
		examination.setPrice(1000.0);
		examination.setDermatologist(dermatologistList.get(1));
		examination.setPatient(patients.get(1));
		examination.setPharmacy(pharmacy1);
		examinationsRepository.save(examination);

		examination = new Examination();
		examination.setDeleted(false);
		examination.setStart(LocalDateTime.parse("2021-05-11T00:50:00.00"));
		examination.setReport("Ovde ide kao neki izvestaj o toku pregleda!");
		examination.setEnd(LocalDateTime.parse("2021-05-11T19:10:10.00"));
		examination.setPrice(1000.0);
		examination.setDermatologist(dermatologistList.get(1));
		examination.setPatient(patients.get(1));
		examination.setPharmacy(pharmacy1);
		examinationsRepository.save(examination);

		examination = new Examination();
		examination.setDeleted(false);
		examination.setEnd(LocalDateTime.parse("2021-08-25T09:50:00.00"));
		examination.setReport("");
		examination.setStart(LocalDateTime.parse("2021-08-25T09:10:10.00"));
		examination.setPrice(2000.0);
		examination.setDermatologist(dermatologistList.get(1));
		examination.setPatient(null);
		examination.setPharmacy(pharmacy1);
		examinationsRepository.save(examination);

		examination = new Examination();
		examination.setDeleted(false);
		examination.setEnd(LocalDateTime.parse("2021-08-20T09:50:00.00"));
		examination.setReport("");
		examination.setStart(LocalDateTime.parse("2021-08-20T09:10:10.00"));
		examination.setPrice(2000.0);
		examination.setDermatologist(dermatologistList.get(1));
		examination.setPatient(null);
		examination.setPharmacy(pharmacy1);
		examinationsRepository.save(examination);

		examination = new Examination();
		examination.setDeleted(false);
		examination.setEnd(LocalDateTime.parse("2021-06-01T09:50:00.00"));
		examination.setReport("");
		examination.setStart(LocalDateTime.parse("2021-06-01T04:10:10.00"));
		examination.setPrice(2000.0);
		examination.setDermatologist(dermatologistList.get(1));
		examination.setPatient(patients.get(2));
		examination.setPharmacy(pharmacy1);
		examinationsRepository.save(examination);

		examination = new Examination();
		examination.setDeleted(false);
		examination.setEnd(LocalDateTime.parse("2021-05-31T15:50:00.00"));
		examination.setReport("");
		examination.setStart(LocalDateTime.parse("2021-05-31T06:10:10.00"));
		examination.setPrice(2000.0);
		examination.setDermatologist(dermatologistList.get(1));
		examination.setPatient(patients.get(3));
		examination.setPharmacy(pharmacy1);
		examinationsRepository.save(examination);

		Consultation consultation = new Consultation();
		consultation.setPatient(patients.get(1));
		consultation.setPharmacist(pharmacists.get(0));
		consultation.setPharmacy(pharmacy1);
		consultation.setEnd(LocalDateTime.parse("2021-04-16T23:50:00.00"));
		consultation.setStart(LocalDateTime.parse("2021-04-25T19:10:10.00"));
		consultation.setReport("Pokazuje znake agresije");
		consultation.setPrice(pharmacy1.getConsultation_price());
		consultationsRepository.save(consultation);

		consultation = new Consultation();
		consultation.setPatient(patients.get(1));
		consultation.setPharmacist(pharmacists.get(1));
		consultation.setPharmacy(pharmacy1);
		consultation.setEnd(LocalDateTime.parse("2021-05-18T11:50:00.00"));
		consultation.setStart(LocalDateTime.parse("2021-05-18T07:10:10.00"));
		consultation.setReport("Pokazuje znake agresije");
		consultation.setPrice(pharmacy1.getConsultation_price());
		consultationsRepository.save(consultation);

		consultation = new Consultation();
		consultation.setPatient(patients.get(1));
		consultation.setPharmacist(pharmacists.get(1));
		consultation.setPharmacy(pharmacy1);
		consultation.setEnd(LocalDateTime.parse("2021-05-10T23:50:00.00"));
		consultation.setStart(LocalDateTime.parse("2021-05-10T15:10:10.00"));
		consultation.setReport("Pokazuje znake agresije");
		consultation.setPrice(pharmacy1.getConsultation_price());
		consultationsRepository.save(consultation);


		consultation = new Consultation();
		consultation.setPatient(patients.get(1));
		consultation.setPharmacist(pharmacists.get(1));
		consultation.setPharmacy(pharmacy1);
		consultation.setEnd(LocalDateTime.parse("2021-05-16T19:50:00.00"));
		consultation.setStart(LocalDateTime.parse("2021-05-16T01:10:10.00"));
		consultation.setReport("Pokazuje znake agresije");
		consultation.setPrice(pharmacy1.getConsultation_price());
		consultationsRepository.save(consultation);

		PharmacyAdmin pa1 = pharmacyAdminRepository.findOneByUsername("adminApoteka1");
		pa1.setPharmacy(pharmacy1);
		pharmacyAdminRepository.save(pa1);

		PharmacyAdmin pa2 = pharmacyAdminRepository.findOneByUsername("adminApoteka2");
		pa1.setPharmacy(pharmacy2);
		pharmacyAdminRepository.save(pa2);
		LoyaltyCategory lc1 = new LoyaltyCategory();
		lc1.setDeleted(false);
		lc1.setLowLimit(0.0);
		lc1.setHighLimit(20.0);
		lc1.setDiscountRate(0.0);
		lc1.setName("GVOZDENI");

		LoyaltyCategory lc2 = new LoyaltyCategory();
		lc2.setDeleted(false);
		lc2.setLowLimit(20.0);
		lc2.setHighLimit(50.0);
		lc2.setDiscountRate(10.0);
		lc2.setName("BRONZANI");

		LoyaltyCategory lc3 = new LoyaltyCategory();
		lc3.setDeleted(false);
		lc3.setLowLimit(50.0);
		lc3.setHighLimit(100.0);
		lc3.setDiscountRate(15.0);
		lc3.setName("SREBRNI");

		LoyaltyCategory lc4 = new LoyaltyCategory();
		lc4.setDeleted(false);
		lc4.setLowLimit(100.0);
		lc4.setHighLimit(200.0);
		lc4.setDiscountRate(25.0);
		lc4.setName("ZLATNI");

		LoyaltyCategory lc5 = new LoyaltyCategory();
		lc5.setDeleted(false);
		lc5.setLowLimit(200.0);
		lc5.setHighLimit(500.0);
		lc5.setDiscountRate(50.0);
		lc5.setName("PLATINASTI");

		loyaltyCategoryRepository.save(lc1);
		loyaltyCategoryRepository.save(lc2);
		loyaltyCategoryRepository.save(lc3);
		loyaltyCategoryRepository.save(lc4);
		loyaltyCategoryRepository.save(lc5);

		LoyaltyProgram lp = new LoyaltyProgram();
		lp.setMedicinePoints(new HashMap<>());
		lp.setDeleted(false);
		lp.setConsultingPoints(3.0);
		lp.setExaminationPoints(3.0);
		HashSet<LoyaltyCategory> hs = new HashSet<>();
		hs.add(lc1);
		hs.add(lc2);
		hs.add(lc3);
		hs.add(lc4);
		hs.add(lc5);
		lp.setLoyaltyCategories(hs);
		loyaltyProgramRepository.save(lp);

		Map<Medicine, Integer> medQuant = new HashMap<>();
		medQuant.put(medicine1, 20);
		medQuant.put(medicine2, 20);
		medQuant.put(medicine3, 20);
		medQuant.put(medicine4, 20);
		medQuant.put(medicine5, 20);
		medQuant.put(medicine6, 20);
		medQuant.put(medicine7, 20);
		medQuant.put(medicine8, 20);
		supplierService.setQuantityMedicines(medQuant, supplierRepository.findAll().get(0));
		Map<Medicine, Integer> medQuant1 = new HashMap<>();
		medQuant1.put(medicine1, 10);
		medQuant1.put(medicine2, 10);
		medQuant1.put(medicine3, 10);
		medQuant1.put(medicine4, 10);
		medQuant1.put(medicine5, 10);
		medQuant1.put(medicine6, 10);
		medQuant1.put(medicine7, 10);
		medQuant1.put(medicine8, 10);
		supplierService.setQuantityMedicines(medQuant, supplierRepository.findAll().get(1));

    	Set<LeaveRequest> requests = new HashSet<>();
    	LeaveRequest leaveRequest = new LeaveRequest();
    	leaveRequest.setStatus(LEAVE_REQUEST_STATUS.PENDING);
    	leaveRequest.setStart(LocalDateTime.now());
    	leaveRequest.setEnd(LocalDateTime.now());
    	leaveRequest.setDeleted(false);
    	leaveRequestRepository.save(leaveRequest);
    	requests.add(leaveRequest);
    	pharmacists.get(0).setLeaveRequests(requests);
    	pharmacistRepository.save(pharmacists.get(0));

		qrcodeInit();

	}

	//---------------------- [ QR CODE GENERATION ] -------------------------
	public void qrcodeInit(){
		Patient patient = patientRepository.findOneByUsername("pacijent1");
		patient.setEmail("tim10isamrs2021@gmail.com");
		patientRepository.save(patient);
		String path = "src/main/java/com/isamrs/onlinehealth/public/QRcode.png";
		//-------------------------------------------------------------
		EqrPrescriptionItemDTO itemDTO1 = new EqrPrescriptionItemDTO();
		itemDTO1.setAmount("3");
		itemDTO1.setIdentifier("LK116116");
		itemDTO1.setName("Paracetamol");
		//-------------------------------------------------------------
		EqrPrescriptionItemDTO itemDTO2 = new EqrPrescriptionItemDTO();
		itemDTO2.setAmount("5");
		itemDTO2.setIdentifier("LK181181");
		itemDTO2.setName("Tylenol");
		//-------------------------------------------------------------
		List<EqrPrescriptionItemDTO> itemsDTO = new ArrayList<>();
		itemsDTO.add(itemDTO1);
		itemsDTO.add(itemDTO2);
		//-------------------------------------------------------------
		EqrPrescriptionDTO dto = new EqrPrescriptionDTO();
		dto.setDate(LocalDateTime.now().toString());
		dto.setMedicines(itemsDTO);
		dto.setUsername("pacijent1");
		dto.setFirstName("Miroslav1");
		dto.setLastName("Milojevic1");
		EPrescription ePrescription = ePrescriptionService.saveDTO(dto);
		dto.setId(ePrescription.getId().toString());
		String resonse = QRCodeGen.generate(dto.serialize(), path);
		System.out.println("QR CODE GENERATION STATUS ---------> " + resonse);
	}


}
