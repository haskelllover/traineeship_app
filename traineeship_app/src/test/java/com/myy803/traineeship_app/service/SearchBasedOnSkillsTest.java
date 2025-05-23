package com.myy803.traineeship_app.service;

import com.myy803.traineeship_app.domainmodel.Student;
import com.myy803.traineeship_app.domainmodel.TraineeshipPosition;
import com.myy803.traineeship_app.mapper.StudentMapper;
import com.myy803.traineeship_app.mapper.TraineeshipPositionMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SearchBasedOnSkillsTest {

    @Mock private StudentMapper studentMapper;
    @Mock private TraineeshipPositionMapper positionMapper;

    @InjectMocks private SearchBasedOnSkills searchService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSearch_ReturnsMatchingSkillPositions() {
        Student student = new Student();
        student.setSkills("Java, SQL");

        TraineeshipPosition pos1 = new TraineeshipPosition();
        pos1.setSkills("Java, Spring");

        TraineeshipPosition pos2 = new TraineeshipPosition();
        pos2.setSkills("Python, Django");

        when(studentMapper.findByUsername("alice")).thenReturn(Optional.of(student));
        when(positionMapper.findByIsAssigned(false)).thenReturn(List.of(pos1, pos2));

        List<TraineeshipPosition> result = searchService.search("alice");

        assertEquals(1, result.size());
        assertEquals(pos1, result.get(0));
    }

    @Test
    void testSearch_CaseInsensitiveMatching() {
        Student student = new Student();
        student.setSkills("java");

        TraineeshipPosition pos = new TraineeshipPosition();
        pos.setSkills("JAVA");

        when(studentMapper.findByUsername("bob")).thenReturn(Optional.of(student));
        when(positionMapper.findByIsAssigned(false)).thenReturn(List.of(pos));

        List<TraineeshipPosition> result = searchService.search("bob");

        assertEquals(1, result.size());
    }

    @Test
    void testSearch_NoMatchingSkills_ReturnsEmptyList() {
        Student student = new Student();
        student.setSkills("C++");

        TraineeshipPosition pos = new TraineeshipPosition();
        pos.setSkills("Python");

        when(studentMapper.findByUsername("charlie")).thenReturn(Optional.of(student));
        when(positionMapper.findByIsAssigned(false)).thenReturn(List.of(pos));

        List<TraineeshipPosition> result = searchService.search("charlie");

        assertTrue(result.isEmpty());
    }

    @Test
    void testSearch_NullStudentSkills_ReturnsEmptyList() {
        Student student = new Student();
        student.setSkills(null);

        TraineeshipPosition pos = new TraineeshipPosition();
        pos.setSkills("Java");

        when(studentMapper.findByUsername("dana")).thenReturn(Optional.of(student));
        when(positionMapper.findByIsAssigned(false)).thenReturn(List.of(pos));

        List<TraineeshipPosition> result = searchService.search("dana");

        assertTrue(result.isEmpty());
    }

    @Test
    void testSearch_NullPositionSkills_ReturnsEmptyList() {
        Student student = new Student();
        student.setSkills("Java");

        TraineeshipPosition pos = new TraineeshipPosition();
        pos.setSkills(null);

        when(studentMapper.findByUsername("ed")).thenReturn(Optional.of(student));
        when(positionMapper.findByIsAssigned(false)).thenReturn(List.of(pos));

        List<TraineeshipPosition> result = searchService.search("ed");

        assertTrue(result.isEmpty());
    }

    @Test
    void testSearch_StudentNotFound_ThrowsException() {
        when(studentMapper.findByUsername("notfound")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> searchService.search("notfound"));
    }
}
