package com.myy803.traineeship_app.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.myy803.traineeship_app.domainmodel.Professor;
import com.myy803.traineeship_app.domainmodel.TraineeshipPosition;
import com.myy803.traineeship_app.mapper.EvaluationMapper;
import com.myy803.traineeship_app.mapper.ProfessorMapper;
import com.myy803.traineeship_app.mapper.TraineeshipPositionMapper;
import com.myy803.traineeship_app.domainmodel.Evaluation;
import com.myy803.traineeship_app.service.ProfessorService;

@Controller
@RequestMapping("/professor")
public class ProfessorController {
    
    private final ProfessorService professorService; 
    private final ProfessorMapper professorMapper;
    private final TraineeshipPositionMapper positionMapper;
    private final EvaluationMapper evaluationMapper;
    
    @Autowired
    public ProfessorController(ProfessorService professorService,
                             ProfessorMapper professorMapper,
                             TraineeshipPositionMapper positionMapper, 
                             EvaluationMapper evaluationMapper) {
        this.professorService = professorService;
        this.professorMapper = professorMapper;
        this.positionMapper = positionMapper;
        this.evaluationMapper = evaluationMapper;
    }
    
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        return professorService.getProfessorDashboard();
    }

    @GetMapping("/profile")
    public String viewProfile(Model model) {
        return professorService.retrieveProfile(model);
    }

    @PostMapping("/profile")
    public String saveProfile(@ModelAttribute("professor") Professor professor, Model model) {
        return professorService.saveProfile(professor, model);
    }
    
    @GetMapping("/assignees")
    public String listAssignedTraineeships(Model model, Authentication authentication) {
    	String professorUsername = authentication.getName();
        model.addAttribute("positions", positionMapper.findBySupervisorUsername(professorUsername));
        return "professor/assignees";
    }    
    
    @GetMapping("/positions")
    public String getSupervisedPositions(
        Authentication authentication,
        Model model
    ) {
        try {
            // Verify professor exists
            Professor professor = professorMapper.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Professor not found"));
            
            // Get positions supervised by this professor
            List<TraineeshipPosition> positions = positionMapper.findBySupervisorUsername(authentication.getName());
            
            // Add to model
            model.addAttribute("positions", positions);
            model.addAttribute("professorName", professor.getProfessorName());
            
            return "professor/positions";
            
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error: " + e.getMessage());
            return "professor/positions";
        }
    }
     
    @GetMapping("/positions/{id}/evaluate")
    public String showEvaluationForm(@PathVariable Integer id, Model model) {
        TraineeshipPosition position = positionMapper.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid position ID"));
        
        // Check if evaluation already exists
        if (evaluationMapper.existsById(id)) {
            model.addAttribute("errorMessage", "This position has already been evaluated");
            return "redirect:/professor/positions/" + id + "/details";
        }
        
        model.addAttribute("position", position);
        return "professor/evaluation-form";
    }

    @PostMapping("/positions/{id}/submit-evaluation")
    public String submitEvaluation(
            @PathVariable Integer id,
            @RequestParam Integer studentMotivation,
            @RequestParam Integer studentEffectiveness,
            @RequestParam Integer studentEfficiency,
            @RequestParam Integer companyFacilities,
            @RequestParam Integer companyGuidance,
            RedirectAttributes redirectAttributes) {
        
        try {
            Evaluation evaluation = new Evaluation();
            evaluation.setStudentMotivation(studentMotivation);
            evaluation.setStudentEffectiveness(studentEffectiveness);
            evaluation.setStudentEfficiency(studentEfficiency);
            evaluation.setCompanyFacilities(companyFacilities);
            evaluation.setCompanyGuidance(companyGuidance);

            TraineeshipPosition position = positionMapper.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid position ID"));
            evaluation.setTraineeshipPosition(position);
                       
            evaluationMapper.save(evaluation);
            
            redirectAttributes.addFlashAttribute("successMessage", "Evaluation submitted successfully");
            return "redirect:/professor/positions/" + id + "/details";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error submitting evaluation: " + e.getMessage());
            return "redirect:/professor/positions/" + id + "/evaluate";
        }
    }


    @GetMapping("/positions/{id}/details")
    public String viewPositionDetails(@PathVariable Integer id, Model model) {
        TraineeshipPosition position = positionMapper.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid position ID"));
        
        model.addAttribute("position", position);
        return "professor/position-details"; // Create this view template
    }
}