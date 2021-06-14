package com.isamrs.onlinehealth.student4.integration;

import com.isamrs.onlinehealth.dto.EqrPharmacyDTO;
import com.isamrs.onlinehealth.dto.EqrPharmacyPriceListDTO;
import com.isamrs.onlinehealth.dto.EqrPrescriptionDTO;
import com.isamrs.onlinehealth.dto.EqrPrescriptionItemDTO;
import com.isamrs.onlinehealth.repository.EPrescriptionRepository;
import com.isamrs.onlinehealth.repository.PharmacyRepository;
import com.isamrs.onlinehealth.util.TestUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.integration.test.context.SpringIntegrationTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@SpringBootTest()
@AutoConfigureMockMvc
@SpringIntegrationTest
@WebAppConfiguration
public class EPrescriptionTest {
    private static final String URL_PREFIX = "/api/";

    private final MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype());

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private EPrescriptionRepository ePrescriptionRepository;

    @Autowired
    private PharmacyRepository pharmacyRepository;

    @Before
    public void setup(){
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(username = "pacijent2", roles = {"PATIENT"})
    public void getCompleteEqrPrescriptionInvalid() throws Exception{
        mockMvc.perform(post(URL_PREFIX + "qrCodeUpload?qrPath=QRcode.png&username=pacijent2")).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "pacijent1", roles = {"PATIENT"})
    public void getCompleteEqrPrescriptionValid() throws Exception{
        mockMvc.perform(post(URL_PREFIX + "qrCodeUpload?qrPath=QRcode.png&username=pacijent1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType));
    }

    @Test
    @WithMockUser(username = "pacijent1", roles = {"PATIENT"})
    public void getSearchResults() throws Exception{
        EqrPrescriptionDTO dto = new EqrPrescriptionDTO();
        dto.setId(ePrescriptionRepository.findAll().get(0).getId().toString());
        String json = TestUtil.json(dto);
        mockMvc.perform(post(URL_PREFIX + "qrSearchResults").contentType(contentType).content(json))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
    }

    @Test
    @WithMockUser(username = "pacijent1", roles = {"PATIENT"})
    public void acceptPrescription() throws Exception{
        Long id = ePrescriptionRepository.findAll().get(0).getId();
        String username= "pacijent1";
        //-------------------------------------------------------------
        EqrPharmacyDTO dto = new EqrPharmacyDTO();
        dto.setId(pharmacyRepository.findAll().get(0).getId().toString());
        List<EqrPharmacyPriceListDTO> list = new ArrayList<>();
        EqrPharmacyPriceListDTO listDTO = new EqrPharmacyPriceListDTO();
        listDTO.setName("Paracetamol");
        listDTO.setIdentifier("LK116116");
        listDTO.setAmount("3");
        listDTO.setCost("1000");
        list.add(listDTO);
        listDTO = new EqrPharmacyPriceListDTO();
        listDTO.setName("Tylenol");
        listDTO.setIdentifier("LK181181");
        listDTO.setAmount("5");
        listDTO.setCost("1500");
        dto.setPrices(list);
        //-------------------------------------------------------------
        String json = TestUtil.json(dto);
        mockMvc.perform(post(URL_PREFIX + "acceptPrescription?username=pacijent1&id=" + id.toString())
                .contentType(contentType).content(json)).andExpect(status().isOk());
    }
}
