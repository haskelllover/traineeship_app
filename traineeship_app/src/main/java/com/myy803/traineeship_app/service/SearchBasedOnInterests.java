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
public class SearchBasedOnInterests implements PositionsSearchStrategy {
    private final TraineeshipPositionMapper positionMapper;
    private final StudentMapper studentMapper;

    public SearchBasedOnInterests(TraineeshipPositionMapper positionMapper, 
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
        String studentInterests = student.getInterests(); // Assuming Student has getInterests()
        
        return positionMapper.findByIsAssigned(false).stream()
            .filter(position -> position.getTopics() != null && 
                              studentInterests != null && 
                              containsAny(position.getTopics(), studentInterests))
            .collect(Collectors.toList());
    }

    private boolean containsAny(String positionTopics, String studentInterests) {
        String[] positionTopicArray = positionTopics.split(",\\s*");
        String[] studentInterestArray = studentInterests.split(",\\s*");
        
        for (String posTopic : positionTopicArray) {
            for (String stuInterest : studentInterestArray) {
                if (posTopic.trim().equalsIgnoreCase(stuInterest.trim())) {
                    return true;
                }
            }
        }
        return false;
    }
}