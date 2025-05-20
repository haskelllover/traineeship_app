package com.myy803.traineeship_app.service;

import com.myy803.traineeship_app.domainmodel.Student;
import com.myy803.traineeship_app.domainmodel.TraineeshipPosition;
import com.myy803.traineeship_app.mapper.StudentMapper;
import com.myy803.traineeship_app.mapper.TraineeshipPositionMapper;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SearchBasedOnLocation implements PositionsSearchStrategy {
    private final StudentMapper studentMapper;
    private final TraineeshipPositionMapper positionMapper;

    public SearchBasedOnLocation(StudentMapper studentMapper, 
                               TraineeshipPositionMapper positionMapper) {
        this.studentMapper = studentMapper;
        this.positionMapper = positionMapper;
    }

    @Override
    public List<TraineeshipPosition> search(String applicantUsername) {
        Student student = studentMapper.findByUsername(applicantUsername)
            .orElseThrow(() -> new IllegalArgumentException("Student not found"));
        
        if (student.getPreferredLocation() == null || student.getPreferredLocation().isEmpty()) {
            throw new IllegalStateException("Student has no preferred location set");
        }

        // Get all available positions
        List<TraineeshipPosition> allPositions = positionMapper.findByIsAssigned(false);
        
        return allPositions.stream()
            .filter(position -> position.getCompany() != null)
            .filter(position -> position.getCompany().getCompanyLocation() != null)
            .filter(position -> position.getCompany().getCompanyLocation()
                .equalsIgnoreCase(student.getPreferredLocation()))
            .collect(Collectors.toList());
    }
}