package com.myy803.traineeship_app.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
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

import com.myy803.traineeship_app.service.ProfessorService;

@ExtendWith(MockitoExtension.class)
public class ProfessorControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProfessorService professorService;

    @InjectMocks
    private ProfessorController professorController;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(professorController).build();
    }

    @Test
    @WithMockUser(roles = "PROFESSOR")
    public void whenGetPositions_thenReturnPositionsView() throws Exception {
        mockMvc.perform(get("/professor/positions"))
               .andExpect(status().isOk())
               .andExpect(view().name("professor/positions"));
    }

    @Test
    @WithMockUser(roles = "PROFESSOR")
    public void whenSubmitEvaluation_thenSaveEvaluationAndRedirect() throws Exception {
    	mockMvc.perform(post("/professor/positions/{id}/submit-evaluation", 1)
    	        .param("studentMotivation", "8")
    	        .param("studentEffectiveness", "9")
    	        .param("studentEfficiency", "7")
    	        .param("companyFacilities", "10")
    	        .param("companyGuidance", "9"))
    	    .andExpect(status().is3xxRedirection())
    	    .andExpect(redirectedUrl("/professor/positions/1/evaluate"));
    }
}