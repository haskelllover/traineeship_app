package com.myy803.traineeship_app.service;

import com.myy803.traineeship_app.domainmodel.Student;
import com.myy803.traineeship_app.domainmodel.TraineeshipPosition;
import com.myy803.traineeship_app.mapper.StudentMapper;
import com.myy803.traineeship_app.mapper.TraineeshipPositionMapper;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.ui.Model;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StudentServiceTest {

    @Mock private StudentMapper studentMapper;
    @Mock private TraineeshipPositionMapper positionMapper;
    @Mock private Model model;

    @InjectMocks private StudentServiceImpl studentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockSecurityContext("testuser");
    }

    void mockSecurityContext(String username) {
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn(username);
        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(context);
    }

    @Test
    void testRetrieveProfile_ReturnsViewAndAddsModelAttributes() {
        Student student = new Student();
        student.setStudentName("Alice");

        when(studentMapper.findByUsername("testuser")).thenReturn(Optional.of(student));

        String view = studentService.retrieveProfile(model);

        assertEquals("student/profile", view);
        verify(model).addAttribute("studentName", "Alice");
        verify(model).addAttribute("student", student);
    }

    @Test
    void testRetrieveProfile_ThrowsIfStudentNotFound() {
        when(studentMapper.findByUsername("testuser")).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> studentService.retrieveProfile(model));
    }

    @Test
    void testSaveProfile_UpdatesAndSavesStudent() {
        Student existing = new Student();
        existing.setStudentName("Old Name");

        Student input = new Student();
        input.setStudentName("New Name");

        when(studentMapper.findByUsername("testuser")).thenReturn(Optional.of(existing));

        String view = studentService.saveProfile(input, model);

        assertEquals("student/profile", view);
        verify(studentMapper).save(existing);
        verify(model).addAttribute("successMessage", "Profile updated successfully!");
        assertEquals("New Name", existing.getStudentName());
    }

    @Test
    void testSaveProfile_ErrorHandledGracefully() {
        when(studentMapper.findByUsername("testuser")).thenThrow(new RuntimeException("DB error"));

        Student student = new Student();
        String view = studentService.saveProfile(student, model);

        assertEquals("student/profile", view);
        verify(model).addAttribute(eq("errorMessage"), contains("Error updating profile"));
    }

    @Test
    void testInitLogbook_WithAssignedPosition() {
        TraineeshipPosition position = new TraineeshipPosition();
        Student student = new Student();
        student.setAssignedTraineeship(position);

        when(studentMapper.findByUsername("testuser")).thenReturn(Optional.of(student));

        String view = studentService.initLogbook(model);

        assertEquals("student/logbook", view);
        verify(model).addAttribute("position", position);
        verify(model).addAttribute("student", student);
    }

    @Test
    void testApplyForTraineeship_AddsStudent() {
        Student student = new Student();
        when(studentMapper.findByUsername("testuser")).thenReturn(Optional.of(student));

        String view = studentService.applyForTraineeship(model);

        assertEquals("student/apply-traineeship", view);
        verify(model).addAttribute("student", student);
    }

    @Test
    void testSubmitApplication_SetsLookingForTraineeship() {
        Student student = new Student();
        when(studentMapper.findByUsername("testuser")).thenReturn(Optional.of(student));

        String view = studentService.submitApplication(model);

        assertEquals("redirect:/student/application-status", view);
        assertTrue(student.isLookingForTraineeship());
        verify(studentMapper).save(student);
        verify(model).addAttribute("successMessage", "Application submitted successfully!");
    }

    @Test
    void testGetApplicationStatus_SetsModelAttributes() {
        Student student = new Student();
        student.setLookingForTraineeship(true);
        student.setAssignedTraineeship(new TraineeshipPosition());

        when(studentMapper.findByUsername("testuser")).thenReturn(Optional.of(student));

        String view = studentService.getApplicationStatus(model);

        assertEquals("student/application-status", view);
        verify(model).addAttribute("isLooking", true);
        verify(model).addAttribute("position", student.getAssignedTraineeship());
        verify(model).addAttribute("student", student);
    }
}
