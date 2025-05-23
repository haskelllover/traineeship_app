package com.myy803.traineeship_app.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;


import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.myy803.traineeship_app.domainmodel.TraineeshipPosition;
import com.myy803.traineeship_app.mapper.TraineeshipPositionMapper;
import com.myy803.traineeship_app.service.CommitteeService;

@WebMvcTest(CommitteeController.class)
@WithMockUser(roles = "COMMITTEE")
class CommitteeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommitteeService committeeService;

    @MockBean
    private TraineeshipPositionMapper positionMapper;

    private TraineeshipPosition testPosition;

    @BeforeEach
    void setUp() {
        testPosition = new TraineeshipPosition();
        testPosition.setId(1);
        testPosition.setTitle("Test Position");
    }

    @Test
    void whenGetDashboard_thenReturnDashboard() throws Exception {
        when(committeeService.getCommitteeDashboard()).thenReturn("committee/dashboard");

        mockMvc.perform(get("/committee/dashboard"))
               .andExpect(status().isOk())
               .andExpect(view().name("committee/dashboard"));
    }

    @Test
    void whenGetApplications_thenReturnApplicationsView() throws Exception {
        when(committeeService.listTraineeshipApplication(any())).thenReturn("committee/applications");

        mockMvc.perform(get("/committee/applications"))
               .andExpect(status().isOk())
               .andExpect(view().name("committee/applications"));
    }


    @Test
    void whenAssignPosition_thenRedirectWithFlashAttributes() throws Exception {
        when(committeeService.assignPosition(anyInt(), anyString(), any()))
            .thenReturn("redirect:/committee/applications");

        mockMvc.perform(post("/committee/assign-position")
               .with(csrf()) // ← add this line
               .param("positionId", "1")
               .param("studentUsername", "testuser"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/committee/applications"));
    }


    @Test
    void whenShowAssignSupervisorView_thenReturnViewWithPosition() throws Exception {
        when(positionMapper.findById(1)).thenReturn(Optional.of(testPosition));

        mockMvc.perform(get("/committee/assign-supervisor")
               .param("positionId", "1"))
               .andExpect(status().isOk())
               .andExpect(view().name("committee/assign-supervisor"))
               .andExpect(model().attributeExists("position"));
    }
}