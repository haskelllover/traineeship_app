package com.myy803.traineeship_app.service;

import com.myy803.traineeship_app.domainmodel.Student;
import com.myy803.traineeship_app.domainmodel.TraineeshipPosition;
import com.myy803.traineeship_app.mapper.StudentMapper;
import com.myy803.traineeship_app.mapper.TraineeshipPositionMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class SearchBasedOnSkills implements PositionsSearchStrategy{

	private final TraineeshipPositionMapper positionMapper;
    private final StudentMapper studentMapper;

    public SearchBasedOnSkills(TraineeshipPositionMapper positionMapper, 
                           StudentMapper studentMapper) {
        this.positionMapper = positionMapper;
        this.studentMapper = studentMapper;
    }

    @Override
    public List<TraineeshipPosition> search(String applicantUsername) {
        Optional<Student> studentOpt = studentMapper.findByUsername(applicantUsername);
        if (studentOpt.isEmpty()) {
            throw new IllegalArgumentException("Student not found");
        }
        
        Student student = studentOpt.get();
        String studentSkills = student.getSkills(); // Assuming Student has getSkills()
        
        return positionMapper.findByIsAssigned(false).stream()
            .filter(position -> position.getSkills() != null && 
                              studentSkills != null && 
                              containsAny(position.getSkills(), studentSkills))
            .collect(Collectors.toList());
    }

    private boolean containsAny(String positionSkills, String studentSkills) {
        String[] positionSkillArray = positionSkills.split(",\\s*");
        String[] studentSkillArray = studentSkills.split(",\\s*");
        
        for (String posSkill : positionSkillArray) {
            for (String stuSkill : studentSkillArray) {
                if (posSkill.trim().equalsIgnoreCase(stuSkill.trim())) {
                    return true;
                }
            }
        }
        return false;
    }
}