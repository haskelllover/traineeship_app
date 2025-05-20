package com.myy803.traineeship_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.myy803.traineeship_app.domainmodel.Student;
import com.myy803.traineeship_app.domainmodel.TraineeshipPosition;
import com.myy803.traineeship_app.service.StudentService;

@Controller
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        return studentService.getStudentDashboard();
    }

    @GetMapping("/profile")
    public String viewProfile(Model model) {
        return studentService.retrieveProfile(model);
    }

    @PostMapping("/save-profile")
    public String saveProfile(@ModelAttribute("student") Student student, Model model) {
        return studentService.saveProfile(student, model);
    }

    @GetMapping("/apply-traineeship")
    public String showApplicationForm(Model model) {
        return studentService.applyForTraineeship(model);
    }

    @PostMapping("/submit-application")
    public String submitApplication(Model model) {
        return studentService.submitApplication(model);
    }
    
    @GetMapping("/logbook")
    public String viewLogbook(Model model) {
        return studentService.initLogbook(model);
    }

    @PostMapping("/save-logbook")
    public String saveLogbook(@ModelAttribute("position") TraineeshipPosition position, Model model) {
        return studentService.saveLogbook(position, model);
    }
    
    @GetMapping("/application-status")
    public String viewApplicationStatus(Model model) {
        return studentService.getApplicationStatus(model);
    }
    
    
}