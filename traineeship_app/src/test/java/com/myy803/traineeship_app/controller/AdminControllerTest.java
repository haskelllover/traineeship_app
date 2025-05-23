package com.myy803.traineeship_app.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import com.myy803.traineeship_app.domainmodel.Role;
import com.myy803.traineeship_app.domainmodel.User;
import com.myy803.traineeship_app.service.UserService;

class AdminControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @InjectMocks
    private AdminController adminController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(adminController).build();
    }

    @Test
    void adminDashboard_ShouldReturnDashboardView() throws Exception {
        mockMvc.perform(get("/admin/dashboard"))
               .andExpect(status().isOk())
               .andExpect(view().name("admin/dashboard"));
    }

    @Test
    void showCreateUserForm_ShouldReturnCreateUserViewWithUserAttribute() throws Exception {
        mockMvc.perform(get("/admin/create-user"))
               .andExpect(status().isOk())
               .andExpect(view().name("admin/create-user"))
               .andExpect(model().attributeExists("user"));
    }

    @Test
    void createUser_ShouldSaveUserAndReturnSuccessMessage() throws Exception {
        User testUser = new User();
        testUser.setUsername("testUser");
        testUser.setPassword("password");
        testUser.setRole(Role.STUDENT);  // Using enum value instead of string

        mockMvc.perform(post("/admin/create-user")
                .flashAttr("user", testUser))
               .andExpect(status().isOk())
               .andExpect(view().name("admin/create-user"))
               .andExpect(model().attributeExists("successMessage"));

        verify(userService, times(1)).saveUser(testUser);
    }

    @Test
    void createUser_WithInvalidData_ShouldStillSaveUser() throws Exception {
        User invalidUser = new User(); // Missing required fields

        mockMvc.perform(post("/admin/create-user")
                .flashAttr("user", invalidUser))
               .andExpect(status().isOk())
               .andExpect(view().name("admin/create-user"));

        verify(userService, times(1)).saveUser(invalidUser);
    }
}