package com.myy803.traineeship_app.service;

import com.myy803.traineeship_app.domainmodel.Evaluation;
import com.myy803.traineeship_app.domainmodel.EvaluationType;
import com.myy803.traineeship_app.domainmodel.Professor;
import com.myy803.traineeship_app.domainmodel.Student;
import com.myy803.traineeship_app.domainmodel.TraineeshipPosition;
import com.myy803.traineeship_app.mapper.EvaluationMapper;
import com.myy803.traineeship_app.mapper.ProfessorMapper;
import com.myy803.traineeship_app.mapper.TraineeshipPositionMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Service
@Transactional
public class ProfessorServiceImpl implements ProfessorService {
    
    private final ProfessorMapper professorMapper;
    private final TraineeshipPositionMapper positionMapper;
    private final EvaluationMapper evaluationMapper;

    @Autowired
    public ProfessorServiceImpl(ProfessorMapper professorMapper, 
                              TraineeshipPositionMapper positionMapper,
                              EvaluationMapper evaluationMapper) {
        this.professorMapper = professorMapper;
        this.positionMapper = positionMapper;
        this.evaluationMapper = evaluationMapper;
    }

    @Override
    public String getProfessorDashboard() {
        return "professor/dashboard";
    }

    @Override
    public String retrieveProfile(Model model) {
        String username = getCurrentUsername();
        Professor professor = professorMapper.findByUsername(username)
                .orElseGet(() -> {
                    Professor newProfessor = new Professor();
                    newProfessor.setUsername(username);
                    return professorMapper.save(newProfessor);
                });

        model.addAttribute("professorName", professor.getProfessorName());
        model.addAttribute("professor", professor);
        return "professor/profile";
    }
    
    @Override
    public String saveProfile(@ModelAttribute("professor") Professor professor, Model model) {
        try {
            Professor existingProfessor = professorMapper.findByUsername(professor.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Professor not found"));
            
            // Update fields
            existingProfessor.setProfessorName(professor.getProfessorName());
            existingProfessor.setInterests(professor.getInterests());

            
            professorMapper.save(existingProfessor);
            
            // Add flash attribute for success message
            model.addAttribute("successMessage", "Profile updated successfully!");
            return "redirect:/professor/profile";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error updating profile: " + e.getMessage());
            return "professor/profile";
        }
    }

    @Override
    public String listAssigneeTraineeships(Model model) {
        List<TraineeshipPosition> positions = positionMapper.findByIsAssigned(true);
        
        // Explicitly load evaluations if they're not being loaded by default
        positions.forEach(position -> {
            if (position.getEvaluations() == null) {
                position.setEvaluations(evaluationMapper.findByTraineeshipPositionId(position.getId()));
            }
        });
        
        model.addAttribute("positions", positions);
        return "committee/assignees";
    }

    @Override
    public String evaluateAssignedTraineeship(Integer positionId, Model model) {
        try {
            TraineeshipPosition position = positionMapper.findById(positionId)
                .orElseThrow(() -> new IllegalArgumentException("Position not found"));
            
            model.addAttribute("position", position);
            
            // Create new evaluation with 1-5 scale fields
            Evaluation evaluation = new Evaluation();
            model.addAttribute("evaluation", evaluation);
            
            return "professor/evaluation-form";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "redirect:/professor/assignees";
        }
    }

    @Override
    public String saveEvaluation(Integer positionId, Evaluation evaluation, Model model) {
        try {
            validateEvaluation(evaluation);
            evaluation.setEvaluationType(EvaluationType.PROFESSOR_EVALUATION);
            
            TraineeshipPosition position = positionMapper.findById(positionId)
                .orElseThrow(() -> new IllegalArgumentException("Position not found"));
            
            evaluation.setEvaluationType(EvaluationType.PROFESSOR_EVALUATION);
            evaluation.setTraineeshipPosition(position);
            position.getEvaluations().add(evaluation);
            positionMapper.save(position);

            model.addAttribute("successMessage", "Evaluation saved successfully");
            return "redirect:/professor/assignees";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error saving evaluation: " + e.getMessage());
            return "professor/evaluation-form";
        }
    }

    private String getCurrentUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }


    private void validateEvaluation(Evaluation evaluation) {
        // Adjust validation for 1-5 scale as per US15
        if (evaluation.getStudentMotivation() < 1 || evaluation.getStudentMotivation() > 5 ||
            evaluation.getStudentEffectiveness() < 1 || evaluation.getStudentEffectiveness() > 5 ||
            evaluation.getStudentEfficiency() < 1 || evaluation.getStudentEfficiency() > 5 ||
            evaluation.getCompanyFacilities() < 1 || evaluation.getCompanyFacilities() > 5 ||
            evaluation.getCompanyGuidance() < 1 || evaluation.getCompanyGuidance() > 5) {
            throw new IllegalArgumentException("All evaluation scores must be between 1 and 5");
        }
    }
}