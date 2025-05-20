package com.myy803.traineeship_app.controller;

import com.myy803.traineeship_app.service.CommitteeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/committee")
public class CommitteeController {
    private final CommitteeService committeeService;

    public CommitteeController(CommitteeService committeeService) {
        this.committeeService = committeeService;
    }

    @GetMapping("/dashboard")
    public String getDashboard() {
        return committeeService.getCommitteeDashboard();
    }

    @GetMapping("/applications")
    public String listApplications(Model model) {
        return committeeService.listTraineeshipApplication(model);
    }

    @GetMapping("/instructions")
    public String showInstructions(@RequestParam String studentUsername, 
                                 @RequestParam String strategy,
                                 Model model) {
        return committeeService.instructions(studentUsername, strategy, model);
    }

    @PostMapping("/assign-position")
    public String assignPosition(@RequestParam Integer positionId,
                               @RequestParam String studentUsername,
                               Model model) {
        return committeeService.assignPosition(positionId, studentUsername, model);
    }
    
    @GetMapping("/assign-supervisor")
    public String assignSupervisor(@RequestParam Integer positionId,
                                 @RequestParam String strategy,
                                 Model model) {
        return committeeService.assignSupervisor(positionId, strategy, model);
    }
    
    @GetMapping("/assignees")
    public String listAssignees(Model model) {
        return committeeService.listAssigneeTraineeships(model);
    }

    @PostMapping("/complete-traineeship")
    public String completeTraineeship(@RequestParam Integer positionId,
                                    Model model) {
        return committeeService.completeAssignedTraineeships(positionId, model);
    }
}