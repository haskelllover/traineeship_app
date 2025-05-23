package com.myy803.traineeship_app.service;

import com.myy803.traineeship_app.domainmodel.Professor;
import com.myy803.traineeship_app.domainmodel.TraineeshipPosition;
import com.myy803.traineeship_app.mapper.ProfessorMapper;
import com.myy803.traineeship_app.mapper.TraineeshipPositionMapper;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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
    @Transactional
    public void assign(Integer positionId) {
        // Refresh entities to ensure we're working with current database state
        TraineeshipPosition position = positionMapper.findById(positionId)
            .orElseThrow(() -> new IllegalArgumentException("Position not found"));
        
        if (position.getSupervisor() != null) {
            throw new IllegalStateException("Position already has a supervisor");
        }

        // Load professors with their supervised positions
        List<Professor> professors = professorMapper.findAll();
        
        Professor leastBusy = findLeastBusyProfessor(professors);
        
        // Set both sides of the relationship
        position.setSupervisor(leastBusy);
        if (leastBusy.getSupervisedPositions() == null) {
            leastBusy.setSupervisedPositions(new ArrayList<>());
        }
        leastBusy.getSupervisedPositions().add(position);
        
        // Explicitly save both entities
        positionMapper.save(position);
        professorMapper.save(leastBusy);
        
        // Force immediate flush to database
        positionMapper.flush();
        professorMapper.flush();
    }

    public Professor findLeastBusyProfessor(List<Professor> professors) {
        if (professors.isEmpty()) {
            throw new IllegalStateException("No professors available");
        }
        
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
        
        return leastBusy;
    }
}