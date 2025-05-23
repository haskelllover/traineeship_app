package com.myy803.traineeship_app.service;

import com.myy803.traineeship_app.domainmodel.Professor;
import com.myy803.traineeship_app.domainmodel.Student;
import com.myy803.traineeship_app.domainmodel.TraineeshipPosition;
import com.myy803.traineeship_app.mapper.ProfessorMapper;
import com.myy803.traineeship_app.mapper.StudentMapper;
import com.myy803.traineeship_app.mapper.TraineeshipPositionMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CommitteeServiceImpl implements CommitteeService {
    
    private final PositionsSearchFactory positionsSearchFactory;
    private final SupervisorAssignmentFactory supervisorAssignmentFactory;
    private final StudentMapper studentMapper;
    private final ProfessorMapper professorMapper;
    private final TraineeshipPositionMapper positionMapper;

    @Autowired
    public CommitteeServiceImpl(PositionsSearchFactory positionsSearchFactory,
                              SupervisorAssignmentFactory supervisorAssignmentFactory,
                              StudentMapper studentMapper, ProfessorMapper professorMapper,
                              TraineeshipPositionMapper positionMapper) {
        this.positionsSearchFactory = positionsSearchFactory;
        this.supervisorAssignmentFactory = supervisorAssignmentFactory;
        this.studentMapper = studentMapper;
        this.professorMapper = professorMapper;
        this.positionMapper = positionMapper;
    }

    @Override
    public String getCommitteeDashboard() {
        return "committee/dashboard";
    }

    @Override
    public String listTraineeshipApplication(Model model) {
        List<Student> applicants = studentMapper.findByLookingForTraineeship(true);
        model.addAttribute("applicants", applicants);
        return "committee/applications";
    }

    @Override
    public String instructions(String studentUsername, String strategy, Model model) {
        List<TraineeshipPosition> positions = positionsSearchFactory.create(strategy)
            .search(studentUsername);
        
        model.addAttribute("positions", positions);
        model.addAttribute("studentUsername", studentUsername);
        model.addAttribute("strategy", strategy);
        
        return "committee/instructions";
    }

    @Override
    public String assignPosition(Integer positionId, String studentUsername, Model model) {
        try {
            TraineeshipPosition position = positionMapper.findById(positionId)
                .orElseThrow(() -> new IllegalArgumentException("Position not found"));
            
            Student student = studentMapper.findByUsername(studentUsername)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));
            
            // Check if student is already assigned to another position
            Optional<TraineeshipPosition> existingPosition = positionMapper.findById(student.getId());
            if (existingPosition.isPresent()) {
                model.addAttribute("errorMessage", 
                    "Cannot assign - Student is already assigned to position: " + 
                    existingPosition.get().getTitle());
                return "redirect:/committee/instructions?studentUsername=" + studentUsername + 
                       "&strategy=combined";
            }
            
            position.setStudent(student);
            position.setIsAssigned(true);
            student.setLookingForTraineeship(false);
            
            positionMapper.save(position);
            studentMapper.save(student);
            
            model.addAttribute("successMessage", "Position assigned successfully");
            return "redirect:/committee/applications";
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("errorMessage", "This student is already assigned to another position");
            return "committee/applications";
        
        }
    }

    public String assignSupervisor(Integer positionId, String strategy, Model model) {
        try {
            supervisorAssignmentFactory.create(strategy).assign(positionId);
            model.addAttribute("successMessage", "Supervisor assigned successfully");
            return "redirect:/committee/assignees";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error assigning supervisor: " + e.getMessage());
            return "committee/assignees";
        }
    }
    
    @Override
    public String completeAssignedTraineeships(Integer positionId, Model model, RedirectAttributes redirectAttributes) {
        try {
            TraineeshipPosition position = positionMapper.findById(positionId)
                .orElseThrow(() -> new IllegalArgumentException("Position not found"));
            
            // Validate evaluations exist
            if (position.getEvaluations() == null || position.getEvaluations().isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", 
                    "Cannot complete - No evaluations exist for this position");
                return "redirect:/committee/assignees";
            }
            
            double finalGrade = calculateFinalGrade(position);
            boolean passed = finalGrade >= 2.5;
            position.setPassFailGrade(passed);
            
            Student student = position.getStudent();
            student.setAssignedTraineeship(null);
            student.setLookingForTraineeship(true);
            
            positionMapper.save(position);
            studentMapper.save(student);
            
            redirectAttributes.addFlashAttribute("successMessage", 
                "Traineeship completed successfully. Result: " + (passed ? "PASS" : "FAIL") + 
                " (Grade: " + String.format("%.1f", finalGrade) + ")");
            return "redirect:/committee/assignees";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Error completing traineeship: " + e.getMessage());
            return "redirect:/committee/assignees";
        }
    }

    public double calculateFinalGrade(TraineeshipPosition position) {
        // Calculate weighted average if needed
        return position.getEvaluations().stream()
            .mapToDouble(e -> (e.getStudentMotivation() * 0.3 + 
                              e.getStudentEfficiency() * 0.4 + 
                              e.getStudentEffectiveness() * 0.3))
            .average()
            .orElseThrow(() -> new IllegalStateException("No evaluations available"));
    }

    @Override
    public String listAssigneeTraineeships(Model model) {
        // 1. Fetch positions with supervisor and student relationships loaded
        List<TraineeshipPosition> positions = positionMapper.findByIsAssigned(true);
        
        // 2. Verify data integrity
        positions.forEach(position -> {
            if (position.getSupervisor() != null && position.getSupervisor().getProfessorName() == null) {
                // Handle cases where supervisor exists but name isn't loaded
                Professor supervisor = professorMapper.findById(position.getSupervisor().getId())
                    .orElseThrow(() -> new IllegalStateException(
                        "Supervisor not found for position ID: " + position.getId()));
                position.setSupervisor(supervisor);
            }
        });
        
        // 3. Add attributes to model
        model.addAttribute("positions", positions);
        
        // 4. Debug output (optional)
        System.out.println("Loaded positions with supervisors:");
        positions.forEach(p -> System.out.println(
            "Position ID: " + p.getId() + 
            " | Supervisor: " + (p.getSupervisor() != null ? 
                p.getSupervisor().getProfessorName() : "null")));
        
        return "committee/assignees";
    }

}