package com.myy803.traineeship_app.service;

import com.myy803.traineeship_app.domainmodel.Evaluation;
import com.myy803.traineeship_app.domainmodel.Professor;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

public interface ProfessorService {
    // Controller-facing methods
    String getProfessorDashboard();
    String retrieveProfile(Model model);
    String saveProfile(@ModelAttribute("professor") Professor professor, Model model);
    String listAssigneeTraineeships(Model model);
    String evaluateAssignedTraineeship(Integer positionId, Model model);
	String saveEvaluation(Integer positionId, Evaluation evaluation, Model model);
}