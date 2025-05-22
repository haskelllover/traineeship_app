package com.myy803.traineeship_app.controller;

import com.myy803.traineeship_app.domainmodel.TraineeshipPosition;
import com.myy803.traineeship_app.mapper.TraineeshipPositionMapper;
import com.myy803.traineeship_app.service.CommitteeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/committee")
public class CommitteeController {
    private final CommitteeService committeeService;
    private final TraineeshipPositionMapper positionMapper;

    @Autowired 
    public CommitteeController(CommitteeService committeeService,
                             TraineeshipPositionMapper positionMapper) {
        this.committeeService = committeeService;
        this.positionMapper = positionMapper;
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
    public String showInstructions(
            @RequestParam String studentUsername, 
            @RequestParam(defaultValue = "combined") String strategy,
            Model model) {
        return committeeService.instructions(studentUsername, strategy, model);
    }

    @PostMapping("/assign-position")
    public String assignPosition(
            @RequestParam Integer positionId,
            @RequestParam String studentUsername,
            RedirectAttributes redirectAttributes) {
        String result = committeeService.assignPosition(positionId, studentUsername, redirectAttributes);
        
        // Preserve flash attributes for redirects
        if (result.startsWith("redirect:")) {
            redirectAttributes.addFlashAttribute("successMessage", 
                redirectAttributes.getFlashAttributes().get("successMessage"));
            redirectAttributes.addFlashAttribute("errorMessage", 
                redirectAttributes.getFlashAttributes().get("errorMessage"));
        }
        return result;
    }
    
    @GetMapping("/assign-supervisor")
    public String showAssignView(@RequestParam Integer positionId, Model model) {
        TraineeshipPosition position = positionMapper.findById(positionId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid position ID"));
        model.addAttribute("position", position);
        return "committee/assign-supervisor";
    }

    @PostMapping("/assign-supervisor")
    public String assignSupervisor(
            @RequestParam Integer positionId,
            @RequestParam String strategy,
            RedirectAttributes redirectAttributes) {
        try {
            // Perform assignment
            committeeService.assignSupervisor(positionId, strategy, redirectAttributes);
            
            // Verify assignment
            TraineeshipPosition position = positionMapper.findById(positionId)
                .orElseThrow(() -> new IllegalArgumentException("Position not found"));
            
            if (position.getSupervisor() == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "Supervisor assignment failed");
                return "redirect:/committee/assign-supervisor?positionId=" + positionId;
            }
            
            redirectAttributes.addFlashAttribute("successMessage", 
                "Successfully assigned supervisor: " + position.getSupervisor().getProfessorName());
            return "redirect:/committee/assignees";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Error assigning supervisor: " + e.getMessage());
            return "redirect:/committee/assign-supervisor?positionId=" + positionId;
        }
    }

    @GetMapping("/assignees")
    public String listAssignees(Model model) {
        return committeeService.listAssigneeTraineeships(model);
    }

    @PostMapping("/complete-traineeship")
    public String completeTraineeship(
            @RequestParam Integer positionId,
            RedirectAttributes redirectAttributes) {
        String result = committeeService.completeAssignedTraineeships(positionId, redirectAttributes);
        
        if (result.startsWith("redirect:")) {
            redirectAttributes.addFlashAttribute("successMessage", 
                redirectAttributes.getFlashAttributes().get("successMessage"));
            redirectAttributes.addFlashAttribute("errorMessage", 
                redirectAttributes.getFlashAttributes().get("errorMessage"));
        }
        return result;
    }
}