package com.myy803.traineeship_app.service;

import com.myy803.traineeship_app.domainmodel.*;
import com.myy803.traineeship_app.mapper.EvaluationMapper;
import com.myy803.traineeship_app.mapper.ProfessorMapper;
import com.myy803.traineeship_app.mapper.TraineeshipPositionMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProfessorServiceTest {

    @Mock private ProfessorMapper professorMapper;
    @Mock private TraineeshipPositionMapper positionMapper;
    @Mock private EvaluationMapper evaluationMapper;
    @InjectMocks private ProfessorServiceImpl professorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Mock authenticated user
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("testProf");
        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(context);
    }

    @Test
    void testGetProfessorDashboard() {
        assertEquals("professor/dashboard", professorService.getProfessorDashboard());
    }

    @Test
    void testRetrieveProfile_NewProfessorIsCreated() {
        Model model = new ConcurrentModel();
        when(professorMapper.findByUsername("testProf")).thenReturn(Optional.empty());
        when(professorMapper.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        String view = professorService.retrieveProfile(model);

        assertEquals("professor/profile", view);
        assertTrue(model.containsAttribute("professor"));
        assertEquals("testProf", ((Professor) model.getAttribute("professor")).getUsername());
    }

    @Test
    void testSaveProfile_SuccessfulUpdate() {
        Model model = new ConcurrentModel();
        Professor prof = new Professor();
        prof.setUsername("testProf");
        prof.setProfessorName("Updated Name");
        prof.setInterests("ML");

        when(professorMapper.findByUsername("testProf")).thenReturn(Optional.of(new Professor()));

        String result = professorService.saveProfile(prof, model);

        assertEquals("redirect:/professor/profile", result);
        assertEquals("Profile updated successfully!", model.getAttribute("successMessage"));
    }

    @Test
    void testSaveProfile_ProfessorNotFound() {
        Model model = new ConcurrentModel();
        Professor prof = new Professor();
        prof.setUsername("testProf");

        when(professorMapper.findByUsername("testProf")).thenReturn(Optional.empty());

        String result = professorService.saveProfile(prof, model);

        assertEquals("professor/profile", result);
        assertTrue(model.containsAttribute("errorMessage"));
    }

    @Test
    void testListAssigneeTraineeships_LoadsEvaluations() {
        Model model = new ConcurrentModel();
        TraineeshipPosition pos = new TraineeshipPosition();
        pos.setId(1);
        pos.setEvaluations(null);

        when(positionMapper.findByIsAssigned(true)).thenReturn(List.of(pos));
        when(evaluationMapper.findByTraineeshipPositionId(1)).thenReturn(List.of(new Evaluation()));

        String view = professorService.listAssigneeTraineeships(model);

        assertEquals("committee/assignees", view);
        assertTrue(model.containsAttribute("positions"));
    }

    @Test
    void testEvaluateAssignedTraineeship_Success() {
        Model model = new ConcurrentModel();
        TraineeshipPosition pos = new TraineeshipPosition();
        pos.setId(1);

        when(positionMapper.findById(1)).thenReturn(Optional.of(pos));

        String view = professorService.evaluateAssignedTraineeship(1, model);

        assertEquals("professor/evaluation-form", view);
        assertTrue(model.containsAttribute("evaluation"));
        assertTrue(model.containsAttribute("position"));
    }

    @Test
    void testEvaluateAssignedTraineeship_PositionNotFound() {
        Model model = new ConcurrentModel();
        when(positionMapper.findById(1)).thenReturn(Optional.empty());

        String view = professorService.evaluateAssignedTraineeship(1, model);

        assertEquals("redirect:/professor/assignees", view);
        assertTrue(model.containsAttribute("errorMessage"));
    }

    @Test
    void testSaveEvaluation_Success() {
        Model model = new ConcurrentModel();
        Evaluation eval = new Evaluation();
        eval.setStudentMotivation(5);
        eval.setStudentEffectiveness(5);
        eval.setStudentEfficiency(5);
        eval.setCompanyFacilities(5);
        eval.setCompanyGuidance(5);

        TraineeshipPosition pos = new TraineeshipPosition();
        pos.setEvaluations(new ArrayList<>());
        when(positionMapper.findById(1)).thenReturn(Optional.of(pos));

        String view = professorService.saveEvaluation(1, eval, model);

        assertEquals("redirect:/professor/assignees", view);
        assertTrue(model.containsAttribute("successMessage"));
    }

    @Test
    void testSaveEvaluation_InvalidScores() {
        Model model = new ConcurrentModel();
        Evaluation eval = new Evaluation();
        eval.setStudentMotivation(6); // Invalid

        String view = professorService.saveEvaluation(1, eval, model);

        assertEquals("professor/evaluation-form", view);
        assertTrue(model.containsAttribute("errorMessage"));
    }
}
