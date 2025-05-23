package com.myy803.traineeship_app.service;

import com.myy803.traineeship_app.domainmodel.Company;
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

class SearchBasedOnLocationTest {

    @Mock private StudentMapper studentMapper;
    @Mock private TraineeshipPositionMapper positionMapper;

    @InjectMocks private SearchBasedOnLocation searchService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSearch_ReturnsMatchingLocationPositions() {
        Student student = new Student();
        student.setPreferredLocation("Athens");

        Company company1 = new Company();
        company1.setCompanyLocation("Athens");

        Company company2 = new Company();
        company2.setCompanyLocation("Thessaloniki");

        TraineeshipPosition pos1 = new TraineeshipPosition();
        pos1.setCompany(company1);

        TraineeshipPosition pos2 = new TraineeshipPosition();
        pos2.setCompany(company2);

        when(studentMapper.findByUsername("john")).thenReturn(Optional.of(student));
        when(positionMapper.findByIsAssigned(false)).thenReturn(List.of(pos1, pos2));

        List<TraineeshipPosition> result = searchService.search("john");

        assertEquals(1, result.size());
        assertEquals(pos1, result.get(0));
    }

    @Test
    void testSearch_CaseInsensitiveLocationMatch() {
        Student student = new Student();
        student.setPreferredLocation("athens");

        Company company = new Company();
        company.setCompanyLocation("ATHENS");

        TraineeshipPosition pos = new TraineeshipPosition();
        pos.setCompany(company);

        when(studentMapper.findByUsername("john")).thenReturn(Optional.of(student));
        when(positionMapper.findByIsAssigned(false)).thenReturn(List.of(pos));

        List<TraineeshipPosition> result = searchService.search("john");

        assertEquals(1, result.size());
    }

    @Test
    void testSearch_NoPreferredLocation_ThrowsException() {
        Student student = new Student();
        student.setPreferredLocation(null);

        when(studentMapper.findByUsername("john")).thenReturn(Optional.of(student));

        assertThrows(IllegalStateException.class, () -> searchService.search("john"));
    }

    @Test
    void testSearch_StudentNotFound_ThrowsException() {
        when(studentMapper.findByUsername("unknown")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> searchService.search("unknown"));
    }

    @Test
    void testSearch_PositionWithNullCompany_IsIgnored() {
        Student student = new Student();
        student.setPreferredLocation("Athens");

        TraineeshipPosition pos1 = new TraineeshipPosition();
        pos1.setCompany(null); // Should be ignored

        TraineeshipPosition pos2 = new TraineeshipPosition();
        Company company2 = new Company();
        company2.setCompanyLocation("Athens");
        pos2.setCompany(company2);

        when(studentMapper.findByUsername("john")).thenReturn(Optional.of(student));
        when(positionMapper.findByIsAssigned(false)).thenReturn(List.of(pos1, pos2));

        List<TraineeshipPosition> result = searchService.search("john");

        assertEquals(1, result.size());
        assertEquals(pos2, result.get(0));
    }

    @Test
    void testSearch_PositionWithNullCompanyLocation_IsIgnored() {
        Student student = new Student();
        student.setPreferredLocation("Athens");

        Company company1 = new Company();
        company1.setCompanyLocation(null);

        TraineeshipPosition pos1 = new TraineeshipPosition();
        pos1.setCompany(company1);

        when(studentMapper.findByUsername("john")).thenReturn(Optional.of(student));
        when(positionMapper.findByIsAssigned(false)).thenReturn(List.of(pos1));

        List<TraineeshipPosition> result = searchService.search("john");

        assertTrue(result.isEmpty());
    }
}
