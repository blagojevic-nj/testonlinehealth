package com.isamrs.onlinehealth.student1.integration;

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

import java.util.HashMap;

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest()
@AutoConfigureMockMvc
@SpringIntegrationTest
@WebAppConfiguration
public class ExaminationsTest {
    private static final String URL_PREFIX = "/api/examinations";

    private final MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype());

    private MockMvc mockMvc;


    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(username = "pacijent1", roles = {"PATIENT"})
    public void getFinishedExaminations() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/getFinishedExaminations?username=pacijent1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(6)))
                .andExpect(jsonPath("$.[*].id").value(hasItem(7)))
                .andExpect(jsonPath("$.[*].patient").value(hasItem("pacijent1")));
    }

    @Test
    @WithMockUser(username = "pacijent1", roles = {"PATIENT"})
    public void getScheduledAppointments() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/getScheduledAppointments?username=pacijent1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @WithMockUser(username = "dermatolog1", roles = {"DERMATOLOGIST"})
    public void getOneExamination() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "?id=10&username=dermatolog2"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.patient").value("Miroslav1 Milojevic1"));
    }

    @Test
    @WithMockUser(username = "pacijent1", roles = {"PATIENT"})
    public void scheduling() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/scheduling?id=1&username=pacijent1")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "pacijent1", roles = {"PATIENT"})
    public void invalidScheduling() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/scheduling?id=1&username=pacidasdsfjent1")).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "pacijent1", roles = {"PATIENT"})
    public void canceling() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/canceling?id=1&username=pacijent1"))
                .andExpect(status().isOk())
                ;
    }

    @Test
    @WithMockUser(username = "pacijent1", roles = {"PATIENT"})
    public void invalidCanceling() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/canceling?id=312213&username=pacijendfssd"))
                .andExpect(status().isBadRequest())
        ;
    }
}
