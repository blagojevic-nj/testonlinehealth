package com.isamrs.onlinehealth.config;

import com.isamrs.onlinehealth.auth.RestAuthenticationEntryPoint;
import com.isamrs.onlinehealth.auth.TokenAuthenticationFilter;
import com.isamrs.onlinehealth.service.CustomUserDetailsService;
import com.isamrs.onlinehealth.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.HttpMethod;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Properties;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // komunikacija izmedju klijenta i servera je stateless posto je u pitanju REST aplikacija
                // ovo znaci da server ne pamti nikakvo stanje, tokeni se ne cuvaju na serveru
                // ovo nije slucaj kao sa sesijama koje se cuvaju na serverskoj strani - STATEFULL aplikacija
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()

                // sve neautentifikovane zahteve obradi uniformno i posalji 401 gresku
                .exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint).and()

                // svim korisnicima dopusti da pristupe sledecim putanjama:
                .authorizeRequests().antMatchers("/auth/**").permitAll()		// /auth/**
                .antMatchers("/h2-console/**").permitAll()	// /h2-console/** ako se koristi H2 baza)
                .antMatchers("/api/foo").permitAll()		// /api/foo

                // ukoliko ne zelimo da koristimo @PreAuthorize anotacije nad metodama kontrolera, moze se iskoristiti hasRole() metoda da se ogranici
                // koji tip korisnika moze da pristupi odgovarajucoj ruti. Npr. ukoliko zelimo da definisemo da ruti 'admin' moze da pristupi
                // samo korisnik koji ima rolu 'ADMIN', navodimo na sledeci nacin:
                // .antMatchers("/admin").hasRole("ADMIN") ili .antMatchers("/admin").hasAuthority("ROLE_ADMIN")

                // za svaki drugi zahtev korisnik mora biti autentifikovan
                .anyRequest().authenticated().and()

                // za development svrhe ukljuci konfiguraciju za CORS iz WebConfig klase
                .cors().and()

                // umetni custom filter TokenAuthenticationFilter kako bi se vrsila provera JWT tokena umesto cistih korisnickog imena i lozinke (koje radi BasicAuthenticationFilter)
                .addFilterBefore(new TokenAuthenticationFilter(tokenUtils, customUserDetailsService), BasicAuthenticationFilter.class);

        //http.csrf().disable().cors().disable();
        http.csrf().disable();
        //komentarasddasasdasd
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }

    // Implementacija PasswordEncoder-a koriscenjem BCrypt hashing funkcije.
    // BCrypt po defalt-u radi 10 rundi hesiranja prosledjene vrednosti.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    // Handler za vracanje 401 kada klijent sa neodogovarajucim korisnickim imenom i lozinkom pokusa da pristupi resursu
    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    // Registrujemo authentication manager koji ce da uradi autentifikaciju korisnika za nas
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    // Definisemo nacin utvrdjivanja korisnika pri autentifikaciji
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                // Definisemo uputstva AuthenticationManager-u:

                // 1. koji servis da koristi da izvuce podatke o korisniku koji zeli da se autentifikuje
                // prilikom autentifikacije, AuthenticationManager ce sam pozivati loadUserByUsername() metodu ovog servisa
                .userDetailsService(customUserDetailsService)

                // 2. kroz koji enkoder da provuce lozinku koju je dobio od klijenta u zahtevu
                // da bi adekvatan hash koji dobije kao rezultat hash algoritma uporedio sa onim koji se nalazi u bazi (posto se u bazi ne cuva plain lozinka)
                .passwordEncoder(passwordEncoder());
    }

    // Injektujemo implementaciju iz TokenUtils klase kako bismo mogli da koristimo njene metode za rad sa JWT u TokenAuthenticationFilteru
    @Autowired
    private TokenUtils tokenUtils;
    @Override
    public void configure(WebSecurity web) throws Exception {
        // Autentifikacija ce biti ignorisana ispod navedenih putanja (kako bismo ubrzali pristup resursima)
        // Zahtevi koji se mecuju za web.ignoring().antMatchers() nemaju pristup SecurityContext-u

        // Dozvoljena metoda ... na ruti /api/... , za svaki drugi tip HTTP metode greska je 401 Unauthorized
        web.ignoring().antMatchers(HttpMethod.POST, "/api/login");
        web.ignoring().antMatchers(HttpMethod.POST, "/api/registration");
        web.ignoring().antMatchers(HttpMethod.GET, "/api/confirmRegistration");

        /* OVDE DODAJTE SVE VASE ADRESE KOJE SVI NEULOGOVANI KORISNICI MOGU KORISTITI*/
        web.ignoring().antMatchers(HttpMethod.GET, "/api/pharmacy/getAll");
        web.ignoring().antMatchers(HttpMethod.GET, "/api/pharmacy");
        web.ignoring().antMatchers(HttpMethod.GET, "/api/pharmacyById");
        web.ignoring().antMatchers(HttpMethod.GET, "/api/pharmacy/getMedicines");
        web.ignoring().antMatchers(HttpMethod.GET, "/api/medicine/getAll");
        web.ignoring().antMatchers(HttpMethod.GET, "/api/medicine");
        web.ignoring().antMatchers(HttpMethod.GET, "/api/medicine/{id}");
        web.ignoring().antMatchers(HttpMethod.GET, "/api/pharmacy/getMedicines");

        //web.ignoring().antMatchers(HttpMethod.GET, "/api/dermatologists/getAllDermatologistsForPharmacy");
        //web.ignoring().antMatchers(HttpMethod.GET, "/api/examinations/getFreeExaminations");
        web.ignoring().antMatchers(HttpMethod.GET, "/api/dermatologist/getAllForSearch");
        web.ignoring().antMatchers(HttpMethod.GET, "/api/dermatologist/listOfAll/search");
        web.ignoring().antMatchers(HttpMethod.GET, "/api/pharmacists/getAllForSearch");
        web.ignoring().antMatchers(HttpMethod.GET, "/api/pharmacists/listOfAll/search");

        web.ignoring().antMatchers(HttpMethod.POST, "/api/reservations/add");

        web.ignoring().antMatchers(HttpMethod.GET, "/api/getInfo");
    //web.ignoring().antMatchers(HttpMethod.GET, "/api/pharmacyAdminProfile");
    //web.ignoring().antMatchers(HttpMethod.GET, "/api/drugs");
        //web.ignoring().antMatchers(HttpMethod.GET, "/api/medicine/getAllForPharmacy");
        //web.ignoring().antMatchers(HttpMethod.GET, "/api/medicine/deleteFromPharmacy");
        //web.ignoring().antMatchers(HttpMethod.GET, "/api/medicine/addToPharmacy");
        //web.ignoring().antMatchers(HttpMethod.GET, "/api/medicineSearch");
    //web.ignoring().antMatchers(HttpMethod.GET, "/api/pharmacistManagement");

        //web.ignoring().antMatchers(HttpMethod.GET, "/api/pharmacists/getAllPharmacistsForPharmacy");
        //web.ignoring().antMatchers(HttpMethod.GET, "/api/pharmacists/search");
        //web.ignoring().antMatchers(HttpMethod.GET, "/api/pharmacists/addToPharmacy");
        //web.ignoring().antMatchers(HttpMethod.GET, "/api/pharmacists/deleteFromPharmacy");
        //web.ignoring().antMatchers(HttpMethod.GET, "/api/pharmacyAndAdmin/getUser");
        //web.ignoring().antMatchers(HttpMethod.POST, "/api/pharmacyAndAdmin/updateUser");
        //web.ignoring().antMatchers(HttpMethod.GET, "/api/dermatologistAppointment");
        //web.ignoring().antMatchers(HttpMethod.GET, "/api/dermatologistManagement");
        //web.ignoring().antMatchers(HttpMethod.GET, "/api/dermatologist/checkWorkTime");
        //web.ignoring().antMatchers(HttpMethod.GET, "/api/dermatologist/addToPharmacy");
        //web.ignoring().antMatchers(HttpMethod.GET, "/api/dermatologist/deleteFromPharmacy");
        //web.ignoring().antMatchers(HttpMethod.GET, "/api/dermatologist/addApointment");
        //web.ignoring().antMatchers(HttpMethod.GET, "/api/examinations/getFilterExaminations"); pacijent na strani apoteke
        //web.ignoring().antMatchers(HttpMethod.GET, "/api/pharmacyByAdminUsername");
        //web.ignoring().antMatchers(HttpMethod.GET, "/api/pharmacy/updatePharmacyInfo"); kao dole
        //web.ignoring().antMatchers(HttpMethod.GET, "/api/pharmacy/testAddress"); pharmacyInfoUpdate
        //web.ignoring().antMatchers(HttpMethod.GET, "/api/medicine/getAllBelongsToPharmacy"); AddUpdateLek, DiscountsPromotions.jsx
        //web.ignoring().antMatchers(HttpMethod.POST,"/api/pharmacy/newPriceList");
        //web.ignoring().antMatchers(HttpMethod.POST,"/api/pharmacy/updatePriceList");
        //web.ignoring().antMatchers(HttpMethod.GET, "/api/pharmacy/getMedicinesForUpdatePriceList"); AddUpdateLek
        //web.ignoring().antMatchers(HttpMethod.POST,"/api/pharmacy/newDiscount");
        //web.ignoring().antMatchers(HttpMethod.POST,"/api/pharmacy/newPromotion");
        //web.ignoring().antMatchers(HttpMethod.GET,"/api/pharmacy/getAllPromotions");
        //web.ignoring().antMatchers(HttpMethod.GET,"/api/pharmacy/getAllDiscounts");
        //web.ignoring().antMatchers(HttpMethod.POST,"/api/pharmacyAndAdmin/newOrder"); OrderList
        //web.ignoring().antMatchers(HttpMethod.GET,"/api/pharmacyAndAdmin/getAllOrders");  allOrders.jsx, sveNarudzbe u lekovima
        //web.ignoring().antMatchers(HttpMethod.GET,"/api/pharmacyAndAdmin/deleteOrder"); -||-
        web.ignoring().antMatchers(HttpMethod.GET, "/api/searchftlMedicine");
        //web.ignoring().antMatchers(HttpMethod.GET,"/api/pharmacyAndAdmin/getSupplies");  Ponude dugme u AllOrders, ViewPonuda.jsx
        //web.ignoring().antMatchers(HttpMethod.GET,"/api/pharmacyAndAdmin/acceptSupply"); -||-
        //web.ignoring().antMatchers(HttpMethod.GET,"/api/pharmacyAndAdmin/getAllFailedQueries"); Neuspesni upiti















        // Ovim smo dozvolili pristup statickim resursima aplikacije
        web.ignoring().antMatchers(HttpMethod.GET, "/", "/webjars/**", "/*.html", "favicon.ico", "/**/*.html",
                "/**/*.css", "/**/*.js");
    }

}