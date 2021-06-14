INSERT INTO role (id, name)
VALUES (1, 'ROLE_PATIENT');
INSERT INTO role (id, name)
VALUES (2, 'ROLE_DERMATOLOGIST');
INSERT INTO role (id, name)
VALUES (3, 'ROLE_PHARMACY_ADMIN');
INSERT INTO role (id, name)
VALUES (4, 'ROLE_SYSTEM_ADMIN');
INSERT INTO role (id, name)
VALUES (5, 'ROLE_PHARMACIST');
INSERT INTO role (id, name)
VALUES (6, 'ROLE_SUPPLIER');



INSERT INTO work_hours (work_hours_id, deleted, end_time, start_time)
VALUES (1, false, '15:00:00', '07:00:00');
INSERT INTO work_hours (work_hours_id, deleted, end_time, start_time)
VALUES (2, false, '16:00:00', '08:00:00');
INSERT INTO work_hours (work_hours_id, deleted, end_time, start_time)
VALUES (3, false, '17:00:00', '09:00:00');
INSERT INTO work_hours (work_hours_id, deleted, end_time, start_time)
VALUES (4, false, '18:00:00', '10:00:00');
INSERT INTO work_hours (work_hours_id, deleted, end_time, start_time)
VALUES (5, false, '19:00:00', '11:00:00');
INSERT INTO work_hours (work_hours_id, deleted, end_time, start_time)
VALUES (6, false, '23:59:59', '02:00:00');

INSERT INTO locations (location_id, address, deleted, latitude, longitude)
VALUES (1, 'Novi Sad, Bulevar Oslobodjenja', false, 45.25505014411735, 19.835028744264086);
INSERT INTO locations (location_id, address, deleted, latitude, longitude)
VALUES (2, 'Novi Sad, Bulevar Cara Lazara', false, 45.25022169026069, 19.84839843046331);

INSERT INTO price_lists (price_list_id, deleted, version)
VALUES (1, false, 0);
INSERT INTO price_lists (price_list_id, deleted, version)
VALUES (2, false, 0);

INSERT INTO pharmacies (pharmacy_id, consultation_price, deleted, description, name, rating, pharmacy_location,
                        pricelist_price_list_id)
VALUES (1, 2000, false, 'Neka apoteka', 'Benu', 3.7, 1, 2);
INSERT INTO pharmacies (pharmacy_id, consultation_price, deleted, description, name, rating, pharmacy_location,
                        pricelist_price_list_id)
VALUES (2, 1500, false, 'Najbolja apoteka u gradu', 'DrMax', 0, 2, 1);

INSERT INTO dermatologists (user_id, address, city, country, deleted, email, enabled, first_name, last_name, password,
                            password_changed, phone_number, user_type, username, rating)
VALUES (1, 'Ulica0', 'Novi Sad', 'Srbija', false, 'dermatolog0@gmail.com', true, 'Srboljub0', 'Srbanovic0',
        '$2a$10$nbmPYz7qZZ76lW3qBbdmU.L5UQ9cA5rLSUoHPgwUMPkmlzn9kIRra', true, '0603487611', 'DERMATOLOGIST',
        'dermatolog0', 0);
INSERT INTO dermatologists (user_id, address, city, country, deleted, email, enabled, first_name, last_name, password,
                            password_changed, phone_number, user_type, username, rating)
VALUES (7, 'Ulica1', 'Novi Sad', 'Srbija', false, 'dermatolog1@gmail.com', true, 'Srboljub1', 'Srbanovic1',
        '$2a$10$xbUQlKP/j9CyIaP23MRS/.dVHUBYhHgm.Y/KkBuPlMnzkwTJy/rBm', true, '0613487611', 'DERMATOLOGIST',
        'dermatolog1', 0);
INSERT INTO dermatologists (user_id, address, city, country, deleted, email, enabled, first_name, last_name, password,
                            password_changed, phone_number, user_type, username, rating)
VALUES (13, 'Ulica2', 'Novi Sad', 'Srbija', false, 'dermatolog2@gmail.com', true, 'Srboljub2', 'Srbanovic2',
        '$2a$10$gK/O7Ky80UmJpNwRM2XmyOETTxcmHRNPjHXKLpNsgSM9EDpHYfyja', true, '0623487611', 'DERMATOLOGIST',
        'dermatolog2', 0);
INSERT INTO dermatologists (user_id, address, city, country, deleted, email, enabled, first_name, last_name, password,
                            password_changed, phone_number, user_type, username, rating)
VALUES (19, 'Ulica3', 'Novi Sad', 'Srbija', false, 'dermatolog3@gmail.com', true, 'Srboljub3', 'Srbanovic3',
        '$2a$10$hQIykn3CbBVLrB0w9dBiTu6hcW/ykb8soJlq5ewXTli1NzoTJntEC', true, '0633487611', 'DERMATOLOGIST',
        'dermatolog3', 0);
INSERT INTO dermatologists (user_id, address, city, country, deleted, email, enabled, first_name, last_name, password,
                            password_changed, phone_number, user_type, username, rating)
VALUES (25, 'Ulica4', 'Novi Sad', 'Srbija', false, 'dermatolog4@gmail.com', true, 'Srboljub4', 'Srbanovic4',
        '$2a$10$5uZo3sfZV0MWJCHHUsh7b.9SeeiAMVboVqknvUHEFsiSdZHnadeXK', true, '0643487611', 'DERMATOLOGIST',
        'dermatolog4', 0);

INSERT INTO system_admins (user_id, address, city, country, deleted, email, enabled, first_name, last_name, password,
                           password_changed, phone_number, user_type, username, loyalty_program_loyalty_program_id)
VALUES (5, 'Ulica0', 'Novi Sad', 'Srbija', false, 'sistemskiAdmin0@gmail.com', true, 'Nikola0', 'Nikolic0',
        '$2a$10$KoaesG22mGjOgJMu3nQs3.3b9Lp8Zq7JYgjCmtNDNkSvpWKNWq6H.', true, '0605641321', 'SYSTEM_ADMIN',
        'sistemskiAdmin0', null);
INSERT INTO system_admins (user_id, address, city, country, deleted, email, enabled, first_name, last_name, password,
                           password_changed, phone_number, user_type, username, loyalty_program_loyalty_program_id)
VALUES (11, 'Ulica1', 'Novi Sad', 'Srbija', false, 'sistemskiAdmin1@gmail.com', true, 'Nikola1', 'Nikolic1',
        '$2a$10$iXChfe2fdfdOZwhv9m.nvujsMaVwZMIDJDt5gOU5hjsIvzWJgWn.i', true, '0615641321', 'SYSTEM_ADMIN',
        'sistemskiAdmin1', null);
INSERT INTO system_admins (user_id, address, city, country, deleted, email, enabled, first_name, last_name, password,
                           password_changed, phone_number, user_type, username, loyalty_program_loyalty_program_id)
VALUES (17, 'Ulica2', 'Novi Sad', 'Srbija', false, 'sistemskiAdmin2@gmail.com', true, 'Nikola2', 'Nikolic2',
        '$2a$10$ii8jDlhPmzle7Mlld5kT3eFYrA41ykkkz4Qta2rOg8flCdLkCRogW', true, '0625641321', 'SYSTEM_ADMIN',
        'sistemskiAdmin2', null);
INSERT INTO system_admins (user_id, address, city, country, deleted, email, enabled, first_name, last_name, password,
                           password_changed, phone_number, user_type, username, loyalty_program_loyalty_program_id)
VALUES (23, 'Ulica3', 'Novi Sad', 'Srbija', false, 'sistemskiAdmin3@gmail.com', true, 'Nikola3', 'Nikolic3',
        '$2a$10$TRBdn6o2Fzf/GR7hiEgTVeFhEZ3qkUKp/.W7Pl/RyIROoQt/3iubK', true, '0635641321', 'SYSTEM_ADMIN',
        'sistemskiAdmin3', null);
INSERT INTO system_admins (user_id, address, city, country, deleted, email, enabled, first_name, last_name, password,
                           password_changed, phone_number, user_type, username, loyalty_program_loyalty_program_id)
VALUES (29, 'Ulica4', 'Novi Sad', 'Srbija', false, 'sistemskiAdmin4@gmail.com', true, 'Nikola4', 'Nikolic4',
        '$2a$10$odKhttaLOL9hZxD9tP2RQOMr4dXePJ7mAX2BrTK2BbSkecRBRyOHS', true, '0645641321', 'SYSTEM_ADMIN',
        'sistemskiAdmin4', null);

INSERT INTO suppliers (user_id, address, city, country, deleted, email, enabled, first_name, last_name, password,
                       password_changed, phone_number, user_type, username)
VALUES (6, 'Ulica0', 'Novi Sad', 'Srbija', false, 'dobavljac0@gmail.com', true, 'Svetozar0', 'Dangubic0',
        '$2a$10$koi0Gk3DPlmYkd2Mn8ULGu41sLspMcIIzzg8ZhOVolShH2be7cFpy', true, '0607652121', 'SUPPLIER', 'dobavljac0');
INSERT INTO suppliers (user_id, address, city, country, deleted, email, enabled, first_name, last_name, password,
                       password_changed, phone_number, user_type, username)
VALUES (12, 'Ulica1', 'Novi Sad', 'Srbija', false, 'dobavljac1@gmail.com', true, 'Svetozar1', 'Dangubic1',
        '$2a$10$DfHHEHGRHfY3IdRH5STm.uI0esiyF2BcN/yZA/0FdT7938ujnkCpS', true, '0617652121', 'SUPPLIER', 'dobavljac1');
INSERT INTO suppliers (user_id, address, city, country, deleted, email, enabled, first_name, last_name, password,
                       password_changed, phone_number, user_type, username)
VALUES (18, 'Ulica2', 'Novi Sad', 'Srbija', false, 'dobavljac2@gmail.com', true, 'Svetozar2', 'Dangubic2',
        '$2a$10$moaDzIbH2wTPIY9ZEcuf1OCj/GzegG/L.uTAGTlQ8b42IPTFgb.Gu', true, '0627652121', 'SUPPLIER', 'dobavljac2');
INSERT INTO suppliers (user_id, address, city, country, deleted, email, enabled, first_name, last_name, password,
                       password_changed, phone_number, user_type, username)
VALUES (24, 'Ulica3', 'Novi Sad', 'Srbija', false, 'dobavljac3@gmail.com', true, 'Svetozar3', 'Dangubic3',
        '$2a$10$LqxfWcXLdS8sC9LgoM9UIeyPq2rPiL2LW4Sb9UR772g91XDh8AmLa', true, '0637652121', 'SUPPLIER', 'dobavljac3');
INSERT INTO suppliers (user_id, address, city, country, deleted, email, enabled, first_name, last_name, password,
                       password_changed, phone_number, user_type, username)
VALUES (30, 'Ulica4', 'Novi Sad', 'Srbija', false, 'dobavljac4@gmail.com', true, 'Svetozar4', 'Dangubic4',
        '$2a$10$H/Vv4quQ/xTvLM6kWIea3.LA8TOE7PrH5/igV0G/ud8VtYwD.DhLG', true, '0647652121', 'SUPPLIER', 'dobavljac4');

INSERT INTO pharmacy_admins (user_id, address, city, country, deleted, email, enabled, first_name, last_name, password,
                             password_changed, phone_number, user_type, username, pharmacy_pharmacy_id)
VALUES (4, 'Ulica0', 'Novi Sad', 'Srbija', false, 'adminApoteka0@gmail.com', true, 'Dragoljub0', 'Draganovic0',
        '$2a$10$DVZyiz7m0EcVNfw4R59q2erx0Sso2Z86xBjwf8Kw2T7lAtS1pZNoy', true, '0602376551', 'PHARMACY_ADMIN',
        'adminApoteka0', null);
INSERT INTO pharmacy_admins (user_id, address, city, country, deleted, email, enabled, first_name, last_name, password,
                             password_changed, phone_number, user_type, username, pharmacy_pharmacy_id)
VALUES (16, 'Ulica2', 'Novi Sad', 'Srbija', false, 'adminApoteka2@gmail.com', true, 'Dragoljub2', 'Draganovic2',
        '$2a$10$uvZJDKUOMvfLv27WTRlJc.kb3vOvBUgpqUZn78h2B3DH2RA33G34O', true, '0622376551', 'PHARMACY_ADMIN',
        'adminApoteka2', null);
INSERT INTO pharmacy_admins (user_id, address, city, country, deleted, email, enabled, first_name, last_name, password,
                             password_changed, phone_number, user_type, username, pharmacy_pharmacy_id)
VALUES (22, 'Ulica3', 'Novi Sad', 'Srbija', false, 'adminApoteka3@gmail.com', true, 'Dragoljub3', 'Draganovic3',
        '$2a$10$/jI/VNZfS2uLYWWmdPDaF.zX/tm0RqePPTaafmWA1wpmDfFrBOu/W', true, '0632376551', 'PHARMACY_ADMIN',
        'adminApoteka3', null);
INSERT INTO pharmacy_admins (user_id, address, city, country, deleted, email, enabled, first_name, last_name, password,
                             password_changed, phone_number, user_type, username, pharmacy_pharmacy_id)
VALUES (28, 'Ulica4', 'Novi Sad', 'Srbija', false, 'adminApoteka4@gmail.com', true, 'Dragoljub4', 'Draganovic4',
        '$2a$10$cS4OShAtGTHHd.SO2VAuLODj4.gblR4bZzEZ3icOaJDuruVW5kqm6', true, '0642376551', 'PHARMACY_ADMIN',
        'adminApoteka4', null);
INSERT INTO pharmacy_admins (user_id, address, city, country, deleted, email, enabled, first_name, last_name, password,
                             password_changed, phone_number, user_type, username, pharmacy_pharmacy_id)
VALUES (10, 'Ulica1', 'Novi Sad', 'Srbija', false, 'adminApoteka1@gmail.com', true, 'Dragoljub1', 'Draganovic1',
        '$2a$10$dSyszMGnfLkSxgmm0itipe8ef/vnrKhgz30DdMYWR/xCniQGOTniu', true, '0612376551', 'PHARMACY_ADMIN',
        'adminApoteka1', 1);

INSERT INTO pharmacists (user_id, address, city, country, deleted, email, enabled, first_name, last_name, password,
                         password_changed, phone_number, user_type, username, rating, work_hours_work_hours_id)
VALUES (2, 'Ulica0', 'Novi Sad', 'Srbija', false, 'farmaceut0@gmail.com', true, 'Petar0', 'Petrusic0',
        '$2a$10$3DmGOrWKAOp4w3U41PfPjOnvRtk63NjoudsJHsOFEsvLe6W.CGLEK', true, '0604554731', 'PHARMACIST', 'farmaceut0',
        0, 1);
INSERT INTO pharmacists (user_id, address, city, country, deleted, email, enabled, first_name, last_name, password,
                         password_changed, phone_number, user_type, username, rating, work_hours_work_hours_id)
VALUES (8, 'Ulica1', 'Novi Sad', 'Srbija', false, 'farmaceut1@gmail.com', true, 'Petar1', 'Petrusic1',
        '$2a$10$pnXQVvscRaPay7GTxDDlc.E9G12i5kVBq2trzK2JwVf4jQCuIZne.', true, '0614554731', 'PHARMACIST', 'farmaceut1',
        0, 2);
INSERT INTO pharmacists (user_id, address, city, country, deleted, email, enabled, first_name, last_name, password,
                         password_changed, phone_number, user_type, username, rating, work_hours_work_hours_id)
VALUES (14, 'Ulica2', 'Novi Sad', 'Srbija', false, 'farmaceut2@gmail.com', true, 'Petar2', 'Petrusic2',
        '$2a$10$VZm/O/OJ3Bp/m4ZewS/ue.laudcQv3u/LoVxw5ERJ5Qe81gXKLCZi', true, '0624554731', 'PHARMACIST', 'farmaceut2',
        0, 3);
INSERT INTO pharmacists (user_id, address, city, country, deleted, email, enabled, first_name, last_name, password,
                         password_changed, phone_number, user_type, username, rating, work_hours_work_hours_id)
VALUES (20, 'Ulica3', 'Novi Sad', 'Srbija', false, 'farmaceut3@gmail.com', true, 'Petar3', 'Petrusic3',
        '$2a$10$BE0jUQc2fl7mex9LL3R60OihDwD6qwr1X.eSfKkoJnGfnu/BIHFRW', true, '0634554731', 'PHARMACIST', 'farmaceut3',
        0, 4);
INSERT INTO pharmacists (user_id, address, city, country, deleted, email, enabled, first_name, last_name, password,
                         password_changed, phone_number, user_type, username, rating, work_hours_work_hours_id)
VALUES (26, 'Ulica4', 'Novi Sad', 'Srbija', false, 'farmaceut4@gmail.com', true, 'Petar4', 'Petrusic4',
        '$2a$10$TaDpJGnxhkO5oLUu1U21q.0/FTx3Umg8uDcpYB5hFB3Qpp6Gg6Oku', true, '0644554731', 'PHARMACIST', 'farmaceut4',
        0, 5);

INSERT INTO patients (user_id, address, city, country, deleted, email, enabled, first_name, last_name, password,
                      password_changed, phone_number, user_type, username, penalties, points)
VALUES (3, 'Ulica0', 'Novi Sad', 'Srbija', false, 'pacijent0@gmail.com', true, 'Miroslav0', 'Milojevic0',
        '$2a$10$NgzuroOlp.M8P8tw.Fpyl.L8HF8UHtPywX2AVBD3j03fisPPRJeMe', true, '0609876412', 'PATIENT', 'pacijent0', 1,
        30);
INSERT INTO patients (user_id, address, city, country, deleted, email, enabled, first_name, last_name, password,
                      password_changed, phone_number, user_type, username, penalties, points)
VALUES (15, 'Ulica2', 'Novi Sad', 'Srbija', false, 'pacijent2@gmail.com', true, 'Miroslav2', 'Milojevic2',
        '$2a$10$DE7Wt69rnwu1schfIkv8YOnMwiJnzKhgtB2j6hZk0w3C8XJ8T1ch6', true, '0629876412', 'PATIENT', 'pacijent2', 1,
        30);
INSERT INTO patients (user_id, address, city, country, deleted, email, enabled, first_name, last_name, password,
                      password_changed, phone_number, user_type, username, penalties, points)
VALUES (21, 'Ulica3', 'Novi Sad', 'Srbija', false, 'pacijent3@gmail.com', true, 'Miroslav3', 'Milojevic3',
        '$2a$10$gvPTKQrVGfjX0mjCRIJ9kuZLO0Fne10cIxSyySne9tyQ.acWTpRgS', true, '0639876412', 'PATIENT', 'pacijent3', 1,
        30);
INSERT INTO patients (user_id, address, city, country, deleted, email, enabled, first_name, last_name, password,
                      password_changed, phone_number, user_type, username, penalties, points)
VALUES (27, 'Ulica4', 'Novi Sad', 'Srbija', false, 'pacijent4@gmail.com', true, 'Miroslav4', 'Milojevic4',
        '$2a$10$tFC1gfHos5rjrXueR.3sAuUUDV0rpAnqoUvfT4E202sicdMYyU8.a', true, '0649876412', 'PATIENT', 'pacijent4', 1,
        30);
INSERT INTO patients (user_id, address, city, country, deleted, email, enabled, first_name, last_name, password,
                      password_changed, phone_number, user_type, username, penalties, points)
VALUES (9, 'Ulica1', 'Novi Sad', 'Srbija', false, 'tim10isamrs2021@gmail.com', true, 'Miroslav1', 'Milojevic1',
        '$2a$10$3Ro2zIQ8BZYpRGEuLolIu.Vf1Q2SmvUuhV5k/Q9cyfXbKUyCerzzS', true, '0619876412', 'PATIENT', 'pacijent1', 1,
        30);

INSERT INTO medicines (medicine_id, daily_dose, deleted, description, medicine_identifier, is_prescription,
                       manufacturer, medicine_name, rating, remarks, medicine_type)
VALUES (1, '4x3', false, 'noga', 'LK141141', false, 'galenika', 'Rapidol', 4.2, 'opasno', 'NSAID');
INSERT INTO medicines (medicine_id, daily_dose, deleted, description, medicine_identifier, is_prescription,
                       manufacturer, medicine_name, rating, remarks, medicine_type)
VALUES (2, '10x3', false, 'zglobovi', 'LK511511', false, 'hemofarm', 'Brufen', 0.3, 'cudno', 'intravenski');
INSERT INTO medicines (medicine_id, daily_dose, deleted, description, medicine_identifier, is_prescription,
                       manufacturer, medicine_name, rating, remarks, medicine_type)
VALUES (3, '1x3', false, 'oko', 'LK116116', false, 'galenika', 'Paracetamol', 5, 'sjajno', 'NSAID');
INSERT INTO medicines (medicine_id, daily_dose, deleted, description, medicine_identifier, is_prescription,
                       manufacturer, medicine_name, rating, remarks, medicine_type)
VALUES (4, '1x1', false, 'zub', 'LK181181', false, 'hemofarm', 'Tylenol', 3.8, 'beskorisno', 'NSAID');
INSERT INTO medicines (medicine_id, daily_dose, deleted, description, medicine_identifier, is_prescription,
                       manufacturer, medicine_name, rating, remarks, medicine_type)
VALUES (5, '5x5', false, 'kosa', 'LK911911', false, 'galenika', 'Panadol', 2.6, 'korisno', 'intravenski');
INSERT INTO medicines (medicine_id, daily_dose, deleted, description, medicine_identifier, is_prescription,
                       manufacturer, medicine_name, rating, remarks, medicine_type)
VALUES (6, '8x8', false, 'koleno', 'LK811811', false, 'hemofarm', 'Acetaminophen', 4.9, 'useless', 'NSAID');
INSERT INTO medicines (medicine_id, daily_dose, deleted, description, medicine_identifier, is_prescription,
                       manufacturer, medicine_name, rating, remarks, medicine_type)
VALUES (7, '2x3', false, 'glava', 'LK111111', false, 'galenika', 'Ibuprofen', 1.5, 'otrovno', 'NSAID');
INSERT INTO medicines (medicine_id, daily_dose, deleted, description, medicine_identifier, is_prescription,
                       manufacturer, medicine_name, rating, remarks, medicine_type)
VALUES (8, '2x4', false, 'stomak', 'LK121121', false, 'hemofarm', 'Advil', 3.1, 'toksicno', 'intravenski');

INSERT INTO consultations (appointment_id, deleted, end_time, finished, ongoing, report, start_time, version, price,
                           patient, pharmacist, pharmacy_id)
VALUES (1, false, '2021-04-16 23:50:00.000000', false, false, 'Pokazuje znake agresije', '2021-04-25 19:10:10.000000',
        0, 2000, 9, 2, 1);
INSERT INTO consultations (appointment_id, deleted, end_time, finished, ongoing, report, start_time, version, price,
                           patient, pharmacist, pharmacy_id)
VALUES (2, false, '2021-05-18 11:50:00.000000', false, false, 'Pokazuje znake agresije', '2021-05-18 07:10:10.000000',
        0, 2000, 9, 8, 1);
INSERT INTO consultations (appointment_id, deleted, end_time, finished, ongoing, report, start_time, version, price,
                           patient, pharmacist, pharmacy_id)
VALUES (3, false, '2021-05-10 23:50:00.000000', false, false, 'Pokazuje znake agresije', '2021-05-10 15:10:10.000000',
        0, 2000, 9, 8, 1);
INSERT INTO consultations (appointment_id, deleted, end_time, finished, ongoing, report, start_time, version, price,
                           patient, pharmacist, pharmacy_id)
VALUES (4, false, '2021-05-16 19:50:00.000000', false, false, 'Pokazuje znake agresije', '2021-05-16 01:10:10.000000',
        0, 2000, 9, 8, 1);


INSERT INTO examinations (appointment_id, deleted, end_time, finished, ongoing, report, start_time, version, price,
                          dermatologist, patient, pharmacy_id)
VALUES (1, false, '2021-04-25 09:50:00.000000', false, false, '', '2021-04-25 09:10:10.000000', 0, 2000, 25, null, 1);
INSERT INTO examinations (appointment_id, deleted, end_time, finished, ongoing, report, start_time, version, price,
                          dermatologist, patient, pharmacy_id)
VALUES (2, false, '2021-04-26 19:50:00.000000', false, false, '', '2021-04-26 09:10:10.000000', 0, 1000, 13, 15, 1);
INSERT INTO examinations (appointment_id, deleted, end_time, finished, ongoing, report, start_time, version, price,
                          dermatologist, patient, pharmacy_id)
VALUES (3, false, '2021-04-27 10:50:00.000000', false, false, 'Ovde ide kao neki izvestaj o toku pregleda!',
        '2021-04-27 07:10:10.000000', 0, 1000, 13, 9, 1);
INSERT INTO examinations (appointment_id, deleted, end_time, finished, ongoing, report, start_time, version, price,
                          dermatologist, patient, pharmacy_id)
VALUES (4, false, '2021-05-17 19:50:00.000000', false, false, '', '2021-05-17 09:10:10.000000', 0, 1000, 13, 3, 1);
INSERT INTO examinations (appointment_id, deleted, end_time, finished, ongoing, report, start_time, version, price,
                          dermatologist, patient, pharmacy_id)
VALUES (5, false, '2021-07-14 09:50:00.000000', false, false, '', '2021-07-14 09:10:10.000000', 0, 500, 25, null, 2);
INSERT INTO examinations (appointment_id, deleted, end_time, finished, ongoing, report, start_time, version, price,
                          dermatologist, patient, pharmacy_id)
VALUES (6, false, '2021-04-16 23:50:00.000000', false, false,
        'Potrebno raditi dalje preglede, pacijent pokazuje agresiju!', '2021-04-16 19:10:10.000000', 0, 500, 25, 9, 2);
INSERT INTO examinations (appointment_id, deleted, end_time, finished, ongoing, report, start_time, version, price,
                          dermatologist, patient, pharmacy_id)
VALUES (7, false, '2021-05-03 19:50:00.000000', false, false, 'Ovde ide kao neki izvestaj o toku pregleda!',
        '2021-05-03 07:10:10.000000', 0, 1000, 13, 9, 1);
INSERT INTO examinations (appointment_id, deleted, end_time, finished, ongoing, report, start_time, version, price,
                          dermatologist, patient, pharmacy_id)
VALUES (8, false, '2021-05-25 19:50:00.000000', false, false, 'Ovde ide kao neki izvestaj o toku pregleda!',
        '2021-05-25 07:10:10.000000', 0, 1000, 13, 9, 1);
INSERT INTO examinations (appointment_id, deleted, end_time, finished, ongoing, report, start_time, version, price,
                          dermatologist, patient, pharmacy_id)
VALUES (9, false, '2021-05-29 19:10:10.000000', false, false, 'Ovde ide kao neki izvestaj o toku pregleda!',
        '2021-05-29 00:50:00.000000', 0, 1000, 13, 9, 1);
INSERT INTO examinations (appointment_id, deleted, end_time, finished, ongoing, report, start_time, version, price,
                          dermatologist, patient, pharmacy_id)
VALUES (10, false, '2021-05-11 19:10:10.000000', false, false, 'Ovde ide kao neki izvestaj o toku pregleda!',
        '2021-05-11 00:50:00.000000', 0, 1000, 13, 9, 1);


INSERT INTO leave_requests (leave_request_id, accepted, end_time, start_time, status)
VALUES (1, false, '2021-05-30 17:41:50.575202', '2021-05-30 17:41:50.575202', 0);

INSERT INTO dermatologist_pharmacy_work_hours (dermatologist_id, work_hour_id, pharmacy_id)
VALUES (13, 6, 1);



INSERT INTO loyalty_categories (loyalty_category_id, deleted, discount_rate, high_limit, low_limit, category_name)
VALUES (1, false, 0, 20, 0, 'GVOZDENI');
INSERT INTO loyalty_categories (loyalty_category_id, deleted, discount_rate, high_limit, low_limit, category_name)
VALUES (2, false, 10, 50, 20, 'BRONZANI');
INSERT INTO loyalty_categories (loyalty_category_id, deleted, discount_rate, high_limit, low_limit, category_name)
VALUES (3, false, 15, 100, 50, 'SREBRNI');
INSERT INTO loyalty_categories (loyalty_category_id, deleted, discount_rate, high_limit, low_limit, category_name)
VALUES (4, false, 25, 200, 100, 'ZLATNI');
INSERT INTO loyalty_categories (loyalty_category_id, deleted, discount_rate, high_limit, low_limit, category_name)
VALUES (5, false, 50, 500, 200, 'PLATINASTI');

INSERT INTO loyalty_programs (loyalty_program_id, consulting_points, deleted, examination_points)
VALUES (1, 3, false, 3);

INSERT INTO loyalty_programs_loyalty_categories (loyalty_program_loyalty_program_id,
                                                 loyalty_categories_loyalty_category_id)
VALUES (1, 1);
INSERT INTO loyalty_programs_loyalty_categories (loyalty_program_loyalty_program_id,
                                                 loyalty_categories_loyalty_category_id)
VALUES (1, 2);
INSERT INTO loyalty_programs_loyalty_categories (loyalty_program_loyalty_program_id,
                                                 loyalty_categories_loyalty_category_id)
VALUES (1, 3);
INSERT INTO loyalty_programs_loyalty_categories (loyalty_program_loyalty_program_id,
                                                 loyalty_categories_loyalty_category_id)
VALUES (1, 4);
INSERT INTO loyalty_programs_loyalty_categories (loyalty_program_loyalty_program_id,
                                                 loyalty_categories_loyalty_category_id)
VALUES (1, 5);

INSERT INTO pharmacies_dermatologists (pharmacy_pharmacy_id, dermatologists_user_id)
VALUES (1, 25);
INSERT INTO pharmacies_dermatologists (pharmacy_pharmacy_id, dermatologists_user_id)
VALUES (1, 13);
INSERT INTO pharmacies_dermatologists (pharmacy_pharmacy_id, dermatologists_user_id)
VALUES (1, 19);
INSERT INTO pharmacies_dermatologists (pharmacy_pharmacy_id, dermatologists_user_id)
VALUES (2, 1);
INSERT INTO pharmacies_dermatologists (pharmacy_pharmacy_id, dermatologists_user_id)
VALUES (2, 7);

INSERT INTO pharmacies_pharmacists (pharmacy_pharmacy_id, pharmacists_user_id)
VALUES (1, 8);
INSERT INTO pharmacies_pharmacists (pharmacy_pharmacy_id, pharmacists_user_id)
VALUES (1, 2);
INSERT INTO pharmacies_pharmacists (pharmacy_pharmacy_id, pharmacists_user_id)
VALUES (2, 20);
INSERT INTO pharmacies_pharmacists (pharmacy_pharmacy_id, pharmacists_user_id)
VALUES (2, 26);


INSERT INTO price_list_items (price_list_item_id, available_quantity, deleted, end_date, price, start_date, version,
                              medicine_medicine_id)
VALUES (1, 11, false, '2020-08-07 18:00:00.000000', 1001, '2020-08-07 18:00:00.000000', 0, 1);
INSERT INTO price_list_items (price_list_item_id, available_quantity, deleted, end_date, price, start_date, version,
                              medicine_medicine_id)
VALUES (2, 12, false, '2020-08-07 18:00:00.000000', 1004, '2020-08-07 18:00:00.000000', 0, 2);
INSERT INTO price_list_items (price_list_item_id, available_quantity, deleted, end_date, price, start_date, version,
                              medicine_medicine_id)
VALUES (3, 13, false, '2020-08-07 18:00:00.000000', 1009, '2020-08-07 18:00:00.000000', 0, 3);
INSERT INTO price_list_items (price_list_item_id, available_quantity, deleted, end_date, price, start_date, version,
                              medicine_medicine_id)
VALUES (4, 14, false, '2020-08-07 18:00:00.000000', 1016, '2020-08-07 18:00:00.000000', 0, 4);
INSERT INTO price_list_items (price_list_item_id, available_quantity, deleted, end_date, price, start_date, version,
                              medicine_medicine_id)
VALUES (5, 15, false, '2020-08-07 18:00:00.000000', 1025, '2020-08-07 18:00:00.000000', 0, 5);
INSERT INTO price_list_items (price_list_item_id, available_quantity, deleted, end_date, price, start_date, version,
                              medicine_medicine_id)
VALUES (6, 16, false, '2020-08-07 18:00:00.000000', 1036, '2020-08-07 18:00:00.000000', 0, 6);
INSERT INTO price_list_items (price_list_item_id, available_quantity, deleted, end_date, price, start_date, version,
                              medicine_medicine_id)
VALUES (7, 17, false, '2020-08-07 18:00:00.000000', 1049, '2020-08-07 18:00:00.000000', 0, 7);
INSERT INTO price_list_items (price_list_item_id, available_quantity, deleted, end_date, price, start_date, version,
                              medicine_medicine_id)
VALUES (8, 18, false, '2020-08-07 18:00:00.000000', 1064, '2020-08-07 18:00:00.000000', 0, 8);



INSERT INTO price_lists_price_list_items (price_list_price_list_id, price_list_items_price_list_item_id)
VALUES (1, 3);
INSERT INTO price_lists_price_list_items (price_list_price_list_id, price_list_items_price_list_item_id)
VALUES (1, 4);
INSERT INTO price_lists_price_list_items (price_list_price_list_id, price_list_items_price_list_item_id)
VALUES (2, 1);
INSERT INTO price_lists_price_list_items (price_list_price_list_id, price_list_items_price_list_item_id)
VALUES (2, 2);

INSERT INTO supplier_medicine_quantity (supplier_id, available_quantity, medicine_quantity_key)
VALUES (6, 20, 1);
INSERT INTO supplier_medicine_quantity (supplier_id, available_quantity, medicine_quantity_key)
VALUES (6, 20, 2);
INSERT INTO supplier_medicine_quantity (supplier_id, available_quantity, medicine_quantity_key)
VALUES (6, 20, 3);
INSERT INTO supplier_medicine_quantity (supplier_id, available_quantity, medicine_quantity_key)
VALUES (6, 20, 4);
INSERT INTO supplier_medicine_quantity (supplier_id, available_quantity, medicine_quantity_key)
VALUES (6, 20, 5);
INSERT INTO supplier_medicine_quantity (supplier_id, available_quantity, medicine_quantity_key)
VALUES (6, 20, 6);
INSERT INTO supplier_medicine_quantity (supplier_id, available_quantity, medicine_quantity_key)
VALUES (6, 20, 7);
INSERT INTO supplier_medicine_quantity (supplier_id, available_quantity, medicine_quantity_key)
VALUES (6, 20, 8);
INSERT INTO supplier_medicine_quantity (supplier_id, available_quantity, medicine_quantity_key)
VALUES (12, 20, 1);
INSERT INTO supplier_medicine_quantity (supplier_id, available_quantity, medicine_quantity_key)
VALUES (12, 20, 2);
INSERT INTO supplier_medicine_quantity (supplier_id, available_quantity, medicine_quantity_key)
VALUES (12, 20, 3);
INSERT INTO supplier_medicine_quantity (supplier_id, available_quantity, medicine_quantity_key)
VALUES (12, 20, 4);
INSERT INTO supplier_medicine_quantity (supplier_id, available_quantity, medicine_quantity_key)
VALUES (12, 20, 5);
INSERT INTO supplier_medicine_quantity (supplier_id, available_quantity, medicine_quantity_key)
VALUES (12, 20, 6);
INSERT INTO supplier_medicine_quantity (supplier_id, available_quantity, medicine_quantity_key)
VALUES (12, 20, 7);
INSERT INTO supplier_medicine_quantity (supplier_id, available_quantity, medicine_quantity_key)
VALUES (12, 20, 8);

INSERT INTO user_role (user_id, role_id)
VALUES ('dermatolog0', 2);
INSERT INTO user_role (user_id, role_id)
VALUES ('farmaceut0', 5);
INSERT INTO user_role (user_id, role_id)
VALUES ('pacijent0', 1);
INSERT INTO user_role (user_id, role_id)
VALUES ('adminApoteka0', 3);
INSERT INTO user_role (user_id, role_id)
VALUES ('sistemskiAdmin0', 4);
INSERT INTO user_role (user_id, role_id)
VALUES ('dobavljac0', 6);
INSERT INTO user_role (user_id, role_id)
VALUES ('dermatolog1', 2);
INSERT INTO user_role (user_id, role_id)
VALUES ('farmaceut1', 5);
INSERT INTO user_role (user_id, role_id)
VALUES ('pacijent1', 1);
INSERT INTO user_role (user_id, role_id)
VALUES ('adminApoteka1', 3);
INSERT INTO user_role (user_id, role_id)
VALUES ('sistemskiAdmin1', 4);
INSERT INTO user_role (user_id, role_id)
VALUES ('dobavljac1', 6);
INSERT INTO user_role (user_id, role_id)
VALUES ('dermatolog2', 2);
INSERT INTO user_role (user_id, role_id)
VALUES ('farmaceut2', 5);
INSERT INTO user_role (user_id, role_id)
VALUES ('pacijent2', 1);
INSERT INTO user_role (user_id, role_id)
VALUES ('adminApoteka2', 3);
INSERT INTO user_role (user_id, role_id)
VALUES ('sistemskiAdmin2', 4);
INSERT INTO user_role (user_id, role_id)
VALUES ('dobavljac2', 6);
INSERT INTO user_role (user_id, role_id)
VALUES ('dermatolog3', 2);
INSERT INTO user_role (user_id, role_id)
VALUES ('farmaceut3', 5);
INSERT INTO user_role (user_id, role_id)
VALUES ('pacijent3', 1);
INSERT INTO user_role (user_id, role_id)
VALUES ('adminApoteka3', 3);
INSERT INTO user_role (user_id, role_id)
VALUES ('sistemskiAdmin3', 4);
INSERT INTO user_role (user_id, role_id)
VALUES ('dobavljac3', 6);
INSERT INTO user_role (user_id, role_id)
VALUES ('dermatolog4', 2);
INSERT INTO user_role (user_id, role_id)
VALUES ('farmaceut4', 5);
INSERT INTO user_role (user_id, role_id)
VALUES ('pacijent4', 1);
INSERT INTO user_role (user_id, role_id)
VALUES ('adminApoteka4', 3);
INSERT INTO user_role (user_id, role_id)
VALUES ('sistemskiAdmin4', 4);
INSERT INTO user_role (user_id, role_id)
VALUES ('dobavljac4', 6);