package com.myy803.traineeship_app.service;

import com.myy803.traineeship_app.domainmodel.Professor;
import com.myy803.traineeship_app.domainmodel.TraineeshipPosition;
import com.myy803.traineeship_app.mapper.ProfessorMapper;
import com.myy803.traineeship_app.mapper.TraineeshipPositionMapper;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AssignmentBasedOnInterests implements SupervisorAssignmentStrategy {
    private final TraineeshipPositionMapper positionMapper;
    private final ProfessorMapper professorMapper;

    public AssignmentBasedOnInterests(TraineeshipPositionMapper positionMapper, 
                                    ProfessorMapper professorMapper) {
        this.positionMapper = positionMapper;
        this.professorMapper = professorMapper;
    }

    @Override
    @Transactional 
    public void assign(Integer positionId) {
        // Get fresh position data
        TraineeshipPosition position = positionMapper.findById(positionId)
            .orElseThrow(() -> new IllegalArgumentException("Position not found"));
        
        if (position.getSupervisor() != null) {
            throw new IllegalStateException("Position already has a supervisor");
        }

        // Get fresh professors list
        List<Professor> professors = professorMapper.findAll();
        
        Professor bestMatch = findBestMatch(position, professors);
        
        // Initialize collections if null
        if (bestMatch.getSupervisedPositions() == null) {
            bestMatch.setSupervisedPositions(new ArrayList<>());
        }
        
        // Set both sides of relationship
        position.setSupervisor(bestMatch);
        bestMatch.getSupervisedPositions().add(position);
        
        // Save both entities
        positionMapper.save(position);
        professorMapper.save(bestMatch);
        
        // Ensure immediate database update
        positionMapper.flush();
        professorMapper.flush();
    }

    public Professor findBestMatch(TraineeshipPosition position, List<Professor> professors) {
        if (professors.isEmpty()) {
            throw new IllegalStateException("No professors available for assignment");
        }
        
        List<String> positionKeywords = combinePositionKeywords(position);
        
        System.out.println("Matching position: " + position.getTitle() + 
                          " with keywords: " + positionKeywords);
        System.out.println("Available professors: " + professors.size());
        
        Professor bestMatch = null;
        int maxMatches = 0;
        
        for (Professor professor : professors) {
            List<String> interests = professor.getInterestsList();
            System.out.println("Professor: " + professor.getProfessorName() + 
                             " | Interests: " + interests);
            
            int matches = countMatchingKeywords(interests, positionKeywords);
            System.out.println("Matches: " + matches);
            
            if (matches > maxMatches) {
                maxMatches = matches;
                bestMatch = professor;
            }
        }
        
        if (bestMatch == null) {
            throw new IllegalStateException("No professor found with matching interests for position: " + 
                                          position.getTitle());
        }
        
        System.out.println("Selected professor: " + bestMatch.getProfessorName() + 
                         " with " + maxMatches + " matching interests");
        return bestMatch;
    }

    public List<String> combinePositionKeywords(TraineeshipPosition position) {
        String combined = (position.getTopics() != null ? position.getTopics() : "") + "," + 
                         (position.getSkills() != null ? position.getSkills() : "");
        return Arrays.stream(combined.split(",\\s*"))
                   .map(String::trim)
                   .filter(s -> !s.isEmpty())
                   .collect(Collectors.toList());
    }

    public int countMatchingKeywords(List<String> professorInterests, List<String> positionKeywords) {
        if (professorInterests == null || positionKeywords == null) return 0;
        
        return (int) professorInterests.stream()
            .filter(interest -> positionKeywords.stream()
                .anyMatch(keyword -> keyword.equalsIgnoreCase(interest)))
            .count();
    }
}