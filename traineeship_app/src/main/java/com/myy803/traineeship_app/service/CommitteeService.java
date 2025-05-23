package com.myy803.traineeship_app.service;

import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public interface CommitteeService {
    // View methods
    String getCommitteeDashboard();
    String listTraineeshipApplication(Model model);
    String instructions(String studentUsername, String strategy, Model model);
    String assignPosition(Integer positionId, String studentUsername, Model model);
    String assignSupervisor(Integer positionId, String strategy, Model model);
    String listAssigneeTraineeships(Model model);
    String completeAssignedTraineeships(Integer positionId, Model model, RedirectAttributes redirectAttributes);
}
