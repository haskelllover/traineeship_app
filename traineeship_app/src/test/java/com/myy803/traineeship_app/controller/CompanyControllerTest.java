package com.myy803.traineeship_app.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.myy803.traineeship_app.service.CompanyService;

@ExtendWith(MockitoExtension.class)
public class CompanyControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CompanyService companyService;

    @InjectMocks
    private CompanyController companyController;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(companyController).build();
    }

    @Test
    @WithMockUser(roles = "COMPANY")
    public void whenGetProfile_thenReturnProfileView() throws Exception {
        mockMvc.perform(get("/company/profile"))
               .andExpect(status().isOk())
               .andExpect(view().name("company/profile"));
    }

    @Test
    @WithMockUser(roles = "COMPANY")
    public void whenSavePosition_thenCallService() throws Exception {
        mockMvc.perform(post("/company/positions"))
        	.andExpect(view().name("company/position-form"));
    }
}