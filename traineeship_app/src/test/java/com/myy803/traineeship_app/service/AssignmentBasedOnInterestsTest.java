package com.myy803.traineeship_app.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.myy803.traineeship_app.domainmodel.Professor;
import com.myy803.traineeship_app.domainmodel.TraineeshipPosition;
import com.myy803.traineeship_app.mapper.ProfessorMapper;
import com.myy803.traineeship_app.mapper.TraineeshipPositionMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class AssignmentBasedOnInterestsTest {

    @Mock
    private TraineeshipPositionMapper positionMapper;

    @Mock
    private ProfessorMapper professorMapper;

    @InjectMocks
    private AssignmentBasedOnInterests assignmentStrategy;

    private TraineeshipPosition position;
    private Professor professor1;
    private Professor professor2;

    @BeforeEach
    void setUp() {
        position = new TraineeshipPosition();
        position.setId(1);
        position.setTitle("Software Development");
        position.setTopics("Java, Spring Boot");
        position.setSkills("Programming, Testing");

        professor1 = new Professor();
        professor1.setId(101);
        professor1.setProfessorName("Dr. Smith");
        professor1.setInterests("Java, Algorithms, Programming");

        professor2 = new Professor();
        professor2.setId(102);
        professor2.setProfessorName("Dr. Johnson");
        professor2.setInterests("Databases, Machine Learning");
    }

    @Test
    void assign_ShouldAssignBestMatchingProfessor() {
        // Arrange
        when(positionMapper.findById(1)).thenReturn(Optional.of(position));
        when(professorMapper.findAll()).thenReturn(Arrays.asList(professor1, professor2));
        
        // Act
        assignmentStrategy.assign(1);

        // Assert
        assertEquals(professor1, position.getSupervisor());
        assertTrue(professor1.getSupervisedPositions().contains(position));
        verify(positionMapper).save(position);
        verify(professorMapper).save(professor1);
        verify(positionMapper).flush();
        verify(professorMapper).flush();
    }

    @Test
    void assign_WhenPositionNotFound_ShouldThrowException() {
        when(positionMapper.findById(1)).thenReturn(Optional.empty());
        
        assertThrows(IllegalArgumentException.class, () -> {
            assignmentStrategy.assign(1);
        });
    }

    @Test
    void assign_WhenPositionAlreadyHasSupervisor_ShouldThrowException() {
        position.setSupervisor(professor1);
        when(positionMapper.findById(1)).thenReturn(Optional.of(position));
        
        assertThrows(IllegalStateException.class, () -> {
            assignmentStrategy.assign(1);
        });
    }

    @Test
    void assign_WhenNoProfessorsAvailable_ShouldThrowException() {
        when(positionMapper.findById(1)).thenReturn(Optional.of(position));
        when(professorMapper.findAll()).thenReturn(Collections.emptyList());
        
        assertThrows(IllegalStateException.class, () -> {
            assignmentStrategy.assign(1);
        });
    }

    @Test
    void findBestMatch_ShouldReturnProfessorWithMostMatchingInterests() {
        List<Professor> professors = Arrays.asList(professor1, professor2);
        Professor result = assignmentStrategy.findBestMatch(position, professors);
        
        assertEquals(professor1, result);
    }

    @Test
    void findBestMatch_WhenNoMatches_ShouldThrowException() {
        position.setTopics("Quantum Computing");
        position.setSkills("Physics");
        List<Professor> professors = Arrays.asList(professor1, professor2);
        
        assertThrows(IllegalStateException.class, () -> {
            assignmentStrategy.findBestMatch(position, professors);
        });
    }

    @Test
    void combinePositionKeywords_ShouldReturnCorrectKeywords() {
        List<String> expected = Arrays.asList("Java", "Spring Boot", "Programming", "Testing");
        List<String> result = assignmentStrategy.combinePositionKeywords(position);
        
        assertEquals(expected, result);
    }

    @Test
    void combinePositionKeywords_WhenEmpty_ShouldReturnEmptyList() {
        position.setTopics("");
        position.setSkills("");
        List<String> result = assignmentStrategy.combinePositionKeywords(position);
        
        assertTrue(result.isEmpty());
    }

    @Test
    void countMatchingKeywords_ShouldReturnCorrectCount() {
        List<String> interests = Arrays.asList("Java", "Algorithms", "Programming");
        List<String> keywords = Arrays.asList("Java", "Spring Boot", "Programming", "Testing");
        
        int result = assignmentStrategy.countMatchingKeywords(interests, keywords);
        
        assertEquals(2, result);
    }

    @Test
    void countMatchingKeywords_WhenNoMatches_ShouldReturnZero() {
        List<String> interests = Arrays.asList("Databases", "Machine Learning");
        List<String> keywords = Arrays.asList("Java", "Spring Boot");
        
        int result = assignmentStrategy.countMatchingKeywords(interests, keywords);
        
        assertEquals(0, result);
    }

    @Test
    void countMatchingKeywords_WhenNullInput_ShouldReturnZero() {
        assertEquals(0, assignmentStrategy.countMatchingKeywords(null, Arrays.asList("Java")));
        assertEquals(0, assignmentStrategy.countMatchingKeywords(Arrays.asList("Java"), null));
        assertEquals(0, assignmentStrategy.countMatchingKeywords(null, null));
    }
}