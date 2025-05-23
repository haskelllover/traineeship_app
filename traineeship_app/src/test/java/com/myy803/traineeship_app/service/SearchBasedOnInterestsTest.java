package com.myy803.traineeship_app.service;

import com.myy803.traineeship_app.domainmodel.Student;
import com.myy803.traineeship_app.domainmodel.TraineeshipPosition;
import com.myy803.traineeship_app.mapper.StudentMapper;
import com.myy803.traineeship_app.mapper.TraineeshipPositionMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SearchBasedOnInterestsTest {

    @Mock private StudentMapper studentMapper;
    @Mock private TraineeshipPositionMapper positionMapper;

    @InjectMocks private SearchBasedOnInterests searchService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSearch_ReturnsMatchingPositions() {
        Student student = new Student();
        student.setInterests("AI, Machine Learning");

        TraineeshipPosition pos1 = new TraineeshipPosition();
        pos1.setTopics("Machine Learning, Data Science");

        TraineeshipPosition pos2 = new TraineeshipPosition();
        pos2.setTopics("Marketing, Business");

        when(studentMapper.findByUsername("john")).thenReturn(Optional.of(student));
        when(positionMapper.findByIsAssigned(false)).thenReturn(List.of(pos1, pos2));

        List<TraineeshipPosition> result = searchService.search("john");

        assertEquals(1, result.size());
        assertTrue(result.contains(pos1));
    }

    @Test
    void testSearch_CaseInsensitiveMatch() {
        Student student = new Student();
        student.setInterests("cybersecurity");

        TraineeshipPosition pos = new TraineeshipPosition();
        pos.setTopics("CyberSecurity, Networking");

        when(studentMapper.findByUsername("alice")).thenReturn(Optional.of(student));
        when(positionMapper.findByIsAssigned(false)).thenReturn(List.of(pos));

        List<TraineeshipPosition> result = searchService.search("alice");

        assertEquals(1, result.size());
        assertEquals(pos, result.get(0));
    }

    @Test
    void testSearch_NoMatch() {
        Student student = new Student();
        student.setInterests("Robotics");

        TraineeshipPosition pos = new TraineeshipPosition();
        pos.setTopics("Finance, Marketing");

        when(studentMapper.findByUsername("mike")).thenReturn(Optional.of(student));
        when(positionMapper.findByIsAssigned(false)).thenReturn(List.of(pos));

        List<TraineeshipPosition> result = searchService.search("mike");

        assertTrue(result.isEmpty());
    }

    @Test
    void testSearch_StudentNotFound_ThrowsException() {
        when(studentMapper.findByUsername("unknown")).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                searchService.search("unknown"));

        assertEquals("Student not found", ex.getMessage());
    }

    @Test
    void testSearch_NullTopicsAndInterests_HandledGracefully() {
        Student student = new Student();
        student.setInterests(null);

        TraineeshipPosition pos = new TraineeshipPosition();
        pos.setTopics(null);

        when(studentMapper.findByUsername("lucas")).thenReturn(Optional.of(student));
        when(positionMapper.findByIsAssigned(false)).thenReturn(List.of(pos));

        List<TraineeshipPosition> result = searchService.search("lucas");

        assertTrue(result.isEmpty());
    }
}
