package com.myy803.traineeship_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.myy803.traineeship_app.domainmodel.Company;
import com.myy803.traineeship_app.domainmodel.Professor;
import com.myy803.traineeship_app.domainmodel.Role;
import com.myy803.traineeship_app.domainmodel.Student;
import com.myy803.traineeship_app.domainmodel.User;
import com.myy803.traineeship_app.mapper.CompanyMapper;
import com.myy803.traineeship_app.mapper.ProfessorMapper;
import com.myy803.traineeship_app.mapper.StudentMapper;
import com.myy803.traineeship_app.service.UserService;

@Controller
public class AuthController {
    @Autowired
    private UserService userService;
    
    @Autowired
    private StudentMapper studentMapper; 
    
    @Autowired
    private ProfessorMapper professorMapper; 
    
    @Autowired
    private CompanyMapper companyMapper; 
    
    @Autowired
    //private CommitteeMapper committeeMapper;
    
    @GetMapping("/login")
    public String login(){
        return "auth/signin";
    }

    @GetMapping("/register")
    public String register(Model model){
        model.addAttribute("user", new User());
        return "auth/signup";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, Model model) {
        if(userService.isUserPresent(user)){
            model.addAttribute("errorMessage", "User already registered!");
            return "auth/signup";
        }
        
        // Set default role if not specified
        if(user.getRole() == null) {
            user.setRole(Role.USER);
        }
        
        userService.saveUser(user);

        if(user.getRole() == Role.STUDENT) {
            Student student = new Student();
            student.setUsername(user.getUsername());
            studentMapper.save(student); 
        }
        
        if(user.getRole() == Role.PROFESSOR) {
            Professor professor = new Professor();
            professor.setUsername(user.getUsername());
            professorMapper.save(professor); 
        }
        
        if(user.getRole() == Role.COMPANY) {
            Company company = new Company();
            company.setUsername(user.getUsername());
            companyMapper.save(company); 
        }
        
        
        model.addAttribute("successMessage", "User registered successfully!");
        return "redirect:/login";  // Changed to redirect
    }
}