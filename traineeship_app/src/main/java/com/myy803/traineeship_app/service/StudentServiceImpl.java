package com.myy803.traineeship_app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.myy803.traineeship_app.domainmodel.Student;
import com.myy803.traineeship_app.domainmodel.TraineeshipPosition;
import com.myy803.traineeship_app.mapper.StudentMapper;
import com.myy803.traineeship_app.mapper.TraineeshipPositionMapper;

@Service
@Transactional
public class StudentServiceImpl implements StudentService {
    
    private final StudentMapper studentMapper;
    private final TraineeshipPositionMapper positionMapper;

    @Autowired
    public StudentServiceImpl(StudentMapper studentMapper,
                            TraineeshipPositionMapper positionMapper) {
        this.studentMapper = studentMapper;
        this.positionMapper = positionMapper;
    }

    @Override
    public String getStudentDashboard() {
        return "student/dashboard";
    }

    @Override
    public String retrieveProfile(Model model) {
        String username = getCurrentUsername();
        Student student = studentMapper.findByUsername(username)
            .orElseThrow(() -> new IllegalArgumentException("Student not found"));
        
        // Add studentName as fullName to the model
        model.addAttribute("studentName", student.getStudentName());
        model.addAttribute("student", student);
        return "student/profile";
    }

    @Override
    public String saveProfile(@ModelAttribute("student") Student student, Model model) {
        try {
            Student existingStudent = studentMapper.findByUsername(getCurrentUsername())
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));
            
            // Update only the allowed fields
            existingStudent.setStudentName(student.getStudentName());
            existingStudent.setAM(student.getAM());
            existingStudent.setAvgGrade(student.getAvgGrade());
            existingStudent.setPreferredLocation(student.getPreferredLocation());
            existingStudent.setInterests(student.getInterests());
            existingStudent.setSkills(student.getSkills());
            existingStudent.setYearOfStudies(student.getYearOfStudies());
            existingStudent.setDepartment(student.getDepartment());
            
            studentMapper.save(existingStudent);
            model.addAttribute("successMessage", "Profile updated successfully!");
            return "student/profile";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error updating profile: " + e.getMessage());
            return "student/profile";
        }
    }

    @Override
    public String initLogbook(Model model) {
        String username = getCurrentUsername();
        Student student = studentMapper.findByUsername(username)
            .orElseThrow(() -> new IllegalArgumentException("Student not found"));

        // Initialize position if null
        if (student.getAssignedTraineeship() == null) {
            TraineeshipPosition position = new TraineeshipPosition();
            position.setStudent(student);  // Critical: set the relationship
            student.setAssignedTraineeship(position);
            // Don't save here - let the saveLogbook handle persistence
        }

        model.addAttribute("position", student.getAssignedTraineeship());
        model.addAttribute("student", student);
        return "student/logbook";
    }

    @Override
    @Transactional
    public String saveLogbook(@ModelAttribute("position") TraineeshipPosition position, Model model) {
        try {
            String username = getCurrentUsername();
            Student student = studentMapper.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));

            // Always set the student relationship
            position.setStudent(student);

            // Handle new vs existing position
            if (position.getId() == null) {
                // New position
                TraineeshipPosition savedPosition = positionMapper.save(position);
                student.setAssignedTraineeship(savedPosition);
                studentMapper.save(student);
            } else {
                // Existing position - verify it belongs to this student
                TraineeshipPosition existingPosition = positionMapper.findById(position.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Position not found"));
                
                if (existingPosition.getStudent() == null || 
                    !existingPosition.getStudent().getId().equals(student.getId())) {
                    throw new SecurityException("You don't have permission to modify this position");
                }
                
                // Update fields from the form
                updatePositionFields(existingPosition, position);
                positionMapper.save(existingPosition);
            }

            model.addAttribute("successMessage", "Logbook updated successfully");
            return "redirect:/student/logbook";
        } catch (Exception e) {
            // Return to logbook with error message and current data
            String username = getCurrentUsername();
            Student student = studentMapper.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));
            
            model.addAttribute("errorMessage", "Error saving logbook: " + e.getMessage());
            model.addAttribute("position", student.getAssignedTraineeship());
            model.addAttribute("student", student);
            return "student/logbook";
        }
    }

    public void updatePositionFields(TraineeshipPosition existing, TraineeshipPosition newData) {
        if (newData.getWeeklyReport() != null) {
            existing.setWeeklyReport(newData.getWeeklyReport());
        }
        if (newData.getAchievements() != null) {
            existing.setAchievements(newData.getAchievements());
        }
        if (newData.getSkillsGained() != null) {
            existing.setSkillsGained(newData.getSkillsGained());
        }
        // Update other fields as needed
    }

    @Override
    public String applyForTraineeship(Model model) {
        String username = getCurrentUsername();
        Student student = studentMapper.findByUsername(username)
            .orElseThrow(() -> new IllegalArgumentException("Student not found"));
        
        model.addAttribute("student", student);
        return "student/apply-traineeship";
    }

    @Override
    public String submitApplication(Model model) {
        String username = getCurrentUsername();
        Student student = studentMapper.findByUsername(username)
            .orElseThrow(() -> new IllegalArgumentException("Student not found"));
        
        student.setLookingForTraineeship(true);
        studentMapper.save(student);
        
        model.addAttribute("successMessage", "Application submitted successfully!");
        return "redirect:/student/application-status";
    }

    private String getCurrentUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @Override
    public String getApplicationStatus(Model model) {
        String username = getCurrentUsername();
        Student student = studentMapper.findByUsername(username)
            .orElseThrow(() -> new IllegalArgumentException("Student not found"));
        
        model.addAttribute("isLooking", student.isLookingForTraineeship());
        model.addAttribute("position", student.getAssignedTraineeship());
        model.addAttribute("student", student);
        
        return "student/application-status";
    }
}