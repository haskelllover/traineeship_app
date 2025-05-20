package com.myy803.traineeship_app.service;

import com.myy803.traineeship_app.domainmodel.Professor;
import com.myy803.traineeship_app.domainmodel.TraineeshipPosition;
import com.myy803.traineeship_app.mapper.ProfessorMapper;
import com.myy803.traineeship_app.mapper.TraineeshipPositionMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AssignmentBasedOnLoad implements SupervisorAssignmentStrategy {
    private final TraineeshipPositionMapper positionMapper;
    private final ProfessorMapper professorMapper;

    public AssignmentBasedOnLoad(TraineeshipPositionMapper positionMapper, 
                               ProfessorMapper professorMapper) {
        this.positionMapper = positionMapper;
        this.professorMapper = professorMapper;
    }

    @Override
    public void assign(Integer positionId) {
        TraineeshipPosition position = positionMapper.findById(positionId)
            .orElseThrow(() -> new IllegalArgumentException("Position not found"));
        
        if (position.getSupervisor() != null) {
            throw new IllegalStateException("Position already has a supervisor");
        }

        List<Professor> professors = professorMapper.findAll();
        
        Professor leastBusy = null;
        int minPositions = Integer.MAX_VALUE;
        
        for (Professor professor : professors) {
            int currentLoad = professor.getSupervisedPositions() != null ? 
                professor.getSupervisedPositions().size() : 0;
                
            if (currentLoad < minPositions) {
                minPositions = currentLoad;
                leastBusy = professor;
            }
        }
        
        if (leastBusy == null) {
            throw new IllegalStateException("No professors available");
        }
        
        position.setSupervisor(leastBusy);
        positionMapper.save(position);
    }
}