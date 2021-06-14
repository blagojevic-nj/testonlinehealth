package com.isamrs.onlinehealth.student3.integration;

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
public class LeaveRequestsTest {
    private static final String URL_PREFIX = "/api/dermatologist";

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
    @WithMockUser(username = "dermatolog2", roles = {"DERMATOLOGIST"})
    public void getLeaveRequests() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/getLeaveRequests?username=dermatolog2")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "dermatolog2", roles = {"DERMATOLOGIST"})
    public void newLeaveRequests() throws Exception {

        HashMap<String, String> dto = new HashMap<>();
        dto.put("start", "2020-08-07T18:00:00.00");
        dto.put("end", "2020-09-07T18:00:00.00");
        dto.put("username", "dermatolog2");
        String json = TestUtil.json(dto);
        mockMvc.perform(post(URL_PREFIX + "/newLeaveRequest").contentType(contentType).content(json)).andExpect(status().isOk());

        mockMvc.perform(get(URL_PREFIX + "/getLeaveRequests?username=dermatolog2"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
    }

    @Test
    @WithMockUser(username = "dermatolog2", roles = {"DERMATOLOGIST"})
    public void newOverlapLeaveRequests() throws Exception {

        HashMap<String, String> dto = new HashMap<>();
        dto.put("start", "2020-08-07T18:00:00.00");
        dto.put("end", "2020-09-07T18:00:00.00");
        dto.put("username", "dermatolog2");
        String json = TestUtil.json(dto);
        mockMvc.perform(post(URL_PREFIX + "/newLeaveRequest").contentType(contentType).content(json)).andExpect(status().isOk());

        dto.put("start", "2020-08-17T18:00:00.00");
        dto.put("end", "2020-09-17T18:00:00.00");
        dto.put("username", "dermatolog2");
        json = TestUtil.json(dto);
        mockMvc.perform(post(URL_PREFIX + "/newLeaveRequest").contentType(contentType).content(json)).andExpect(status().isBadRequest());

    }


}
