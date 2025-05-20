package com.myy803.traineeship_app.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.myy803.traineeship_app.domainmodel.User;
import com.myy803.traineeship_app.service.UserService;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    
    private final UserService userService;
    
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        // Add any admin-specific data to the model
        return "admin/dashboard";
    }
    
    @GetMapping("/create-user")
    public String showCreateUserForm(Model model) {
        model.addAttribute("user", new User());
        return "admin/create-user";
    }
    
    @PostMapping("/create-user")
    public String createUser(User user, Model model) {
        // Admin can create users with any role
        userService.saveUser(user);
        model.addAttribute("successMessage", "User created successfully!");
        return "admin/create-user";
    }
}