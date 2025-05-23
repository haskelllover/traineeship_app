package com.myy803.traineeship_app.service;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.myy803.traineeship_app.domainmodel.Student;
import com.myy803.traineeship_app.domainmodel.TraineeshipPosition;

public interface StudentService {
    // Keep these as they match controllers
    String getStudentDashboard();
    String retrieveProfile(Model model);
    String saveProfile(@ModelAttribute("student") Student student, Model model);
    String initLogbook(Model model);
    String saveLogbook(@ModelAttribute("position") TraineeshipPosition position, Model model);
    String applyForTraineeship(Model model);
    String submitApplication(Model model);
	String getApplicationStatus(Model model);
}