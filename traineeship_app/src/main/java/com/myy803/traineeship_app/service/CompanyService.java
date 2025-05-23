package com.myy803.traineeship_app.service;

import com.myy803.traineeship_app.domainmodel.Company;
import com.myy803.traineeship_app.domainmodel.TraineeshipPosition;
import com.myy803.traineeship_app.domainmodel.Evaluation;

import org.springframework.ui.Model;


public interface CompanyService {
    String getCompanyDashboard();
    String retrieveProfile(Model model);
    String saveProfile(Company company, Model model);
    String listAvailablePosition(Model model);
    String showPositionForm(Model model);
    //String savePosition(TraineeshipPosition position, Model model);
    String listAssigneePostHours(Model model);
    String evaluateAssignedTraineeship(Integer positionId, Model model);
    String saveEvaluation(Integer positionId, Evaluation evaluation, Model model);
    String deletePosition(Integer positionId, Model model);
	String savePosition(TraineeshipPosition position, Model model, String username);
}
