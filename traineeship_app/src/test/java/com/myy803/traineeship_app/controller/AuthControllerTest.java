package com.myy803.traineeship_app.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.myy803.traineeship_app.mapper.StudentMapper;
import com.myy803.traineeship_app.service.UserService;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Mock
    private StudentMapper studentMapper;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    public void whenGetLogin_thenReturnLoginView() throws Exception {
        mockMvc.perform(get("/login"))  // Changed from "/auth/signin" to "/login"
               .andExpect(status().isOk())
               .andExpect(view().name("auth/signin"));  // Removed leading slash
    }

    @Test
    public void whenGetRegister_thenReturnRegisterView() throws Exception {
        mockMvc.perform(get("/register"))  // Changed from "/auth/register" to "/register"
               .andExpect(status().isOk())
               .andExpect(view().name("auth/signup"));  // Changed to match controller's return value
    }
}