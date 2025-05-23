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

import com.myy803.traineeship_app.service.StudentService;

@ExtendWith(MockitoExtension.class)
public class StudentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(studentController).build();
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    public void whenGetProfile_thenReturnProfileView() throws Exception {
        mockMvc.perform(get("/student/profile"))
               .andExpect(status().isOk())
               .andExpect(view().name("student/profile"));
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    public void whenSaveProfile_thenCallService() throws Exception {
        mockMvc.perform(post("/student/profile"))
               .andExpect(status().isOk());
    }
}