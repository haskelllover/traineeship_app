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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class AssignmentBasedOnLoadTest {

    @Mock
    private TraineeshipPositionMapper positionMapper;

    @Mock
    private ProfessorMapper professorMapper;

    @InjectMocks
    private AssignmentBasedOnLoad assignmentStrategy;

    private TraineeshipPosition position;
    private Professor professorWithNoLoad;
    private Professor professorWithOnePosition;
    private Professor professorWithTwoPositions;

    @BeforeEach
    void setUp() {
        position = new TraineeshipPosition();
        position.setId(1);
        position.setTitle("Test Position");

        // Professor with no supervised positions
        professorWithNoLoad = new Professor();
        professorWithNoLoad.setId(101);
        professorWithNoLoad.setProfessorName("Dr. No Load");
        professorWithNoLoad.setSupervisedPositions(new ArrayList<>());

        // Professor with one supervised position
        professorWithOnePosition = new Professor();
        professorWithOnePosition.setId(102);
        professorWithOnePosition.setProfessorName("Dr. One Position");
        professorWithOnePosition.setSupervisedPositions(new ArrayList<>());
        professorWithOnePosition.getSupervisedPositions().add(new TraineeshipPosition());

        // Professor with two supervised positions
        professorWithTwoPositions = new Professor();
        professorWithTwoPositions.setId(103);
        professorWithTwoPositions.setProfessorName("Dr. Two Positions");
        professorWithTwoPositions.setSupervisedPositions(new ArrayList<>());
        professorWithTwoPositions.getSupervisedPositions().add(new TraineeshipPosition());
        professorWithTwoPositions.getSupervisedPositions().add(new TraineeshipPosition());
    }

    @Test
    void assign_ShouldAssignLeastBusyProfessor() {
        // Arrange
        when(positionMapper.findById(1)).thenReturn(Optional.of(position));
        when(professorMapper.findAll()).thenReturn(Arrays.asList(
            professorWithOnePosition, 
            professorWithNoLoad,
            professorWithTwoPositions
        ));
        
        // Act
        assignmentStrategy.assign(1);

        // Assert
        assertEquals(professorWithNoLoad, position.getSupervisor());
        assertTrue(professorWithNoLoad.getSupervisedPositions().contains(position));
        verify(positionMapper).save(position);
        verify(professorMapper).save(professorWithNoLoad);
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
        position.setSupervisor(professorWithNoLoad);
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
    void findLeastBusyProfessor_ShouldReturnProfessorWithFewestPositions() {
        List<Professor> professors = Arrays.asList(
            professorWithOnePosition,
            professorWithNoLoad,
            professorWithTwoPositions
        );
        
        Professor result = assignmentStrategy.findLeastBusyProfessor(professors);
        assertEquals(professorWithNoLoad, result);
    }

    @Test
    void findLeastBusyProfessor_WhenAllEqual_ShouldReturnFirstProfessor() {
        // All professors have 1 position
        professorWithNoLoad.getSupervisedPositions().add(new TraineeshipPosition());
        
        List<Professor> professors = Arrays.asList(
            professorWithNoLoad,
            professorWithOnePosition
        );
        
        Professor result = assignmentStrategy.findLeastBusyProfessor(professors);
        assertEquals(professorWithNoLoad, result);
    }

    @Test
    void findLeastBusyProfessor_WhenNullPositionsList_ShouldTreatAsZero() {
        Professor professorWithNullList = new Professor();
        professorWithNullList.setId(104);
        professorWithNullList.setProfessorName("Dr. Null List");
        professorWithNullList.setSupervisedPositions(null);
        
        List<Professor> professors = Arrays.asList(
            professorWithOnePosition,
            professorWithNullList
        );
        
        Professor result = assignmentStrategy.findLeastBusyProfessor(professors);
        assertEquals(professorWithNullList, result);
    }

    @Test
    void findLeastBusyProfessor_WhenEmptyProfessorList_ShouldThrowException() {
        assertThrows(IllegalStateException.class, () -> {
            assignmentStrategy.findLeastBusyProfessor(Collections.emptyList());
        });
    }
}