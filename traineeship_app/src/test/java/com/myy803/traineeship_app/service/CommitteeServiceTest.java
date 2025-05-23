package com.myy803.traineeship_app.service;

import com.myy803.traineeship_app.domainmodel.*;
import com.myy803.traineeship_app.mapper.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommitteeServiceTest {

    @InjectMocks
    private CommitteeServiceImpl committeeService;

    @Mock
    private PositionsSearchFactory positionsSearchFactory;

    @Mock
    private SupervisorAssignmentFactory supervisorAssignmentFactory;

    @Mock
    private StudentMapper studentMapper;

    @Mock
    private ProfessorMapper professorMapper;

    @Mock
    private TraineeshipPositionMapper positionMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAssignPosition_SuccessfulAssignment() {
        Model model = new ConcurrentModel();
        String studentUsername = "john_doe";
        int positionId = 1;

        Student student = new Student();
        student.setUsername(studentUsername);
        student.setId(10);
        student.setLookingForTraineeship(true);

        TraineeshipPosition position = new TraineeshipPosition();
        position.setId(positionId);

        when(positionMapper.findById(positionId)).thenReturn(Optional.of(position));
        when(studentMapper.findByUsername(studentUsername)).thenReturn(Optional.of(student));
        when(positionMapper.findById(student.getId())).thenReturn(Optional.empty());

        String view = committeeService.assignPosition(positionId, studentUsername, model);

        assertEquals("redirect:/committee/applications", view);
        assertEquals("Position assigned successfully", model.getAttribute("successMessage"));
        verify(positionMapper).save(any());
        verify(studentMapper).save(any());
    }

    @Test
    void testAssignPosition_StudentAlreadyAssigned() {
        Model model = new ConcurrentModel();
        String studentUsername = "jane_doe";
        int positionId = 2;

        Student student = new Student();
        student.setUsername(studentUsername);
        student.setId(20);

        TraineeshipPosition newPosition = new TraineeshipPosition();
        newPosition.setId(positionId);

        TraineeshipPosition existingPosition = new TraineeshipPosition();
        existingPosition.setId(99);
        existingPosition.setTitle("Existing Internship");

        when(positionMapper.findById(positionId)).thenReturn(Optional.of(newPosition));
        when(studentMapper.findByUsername(studentUsername)).thenReturn(Optional.of(student));
        when(positionMapper.findById(student.getId())).thenReturn(Optional.of(existingPosition));

        String view = committeeService.assignPosition(positionId, studentUsername, model);

        assertTrue(((String) model.getAttribute("errorMessage")).contains("already assigned"));
        assertTrue(view.contains("redirect:/committee/instructions"));
    }

    @Test
    void testCalculateFinalGrade() {
        Evaluation eval1 = new Evaluation();
        eval1.setStudentMotivation(4);
        eval1.setStudentEfficiency(3);
        eval1.setStudentEffectiveness(5);

        Evaluation eval2 = new Evaluation();
        eval2.setStudentMotivation(2);
        eval2.setStudentEfficiency(4);
        eval2.setStudentEffectiveness(4);

        TraineeshipPosition position = new TraineeshipPosition();
        position.setEvaluations(Arrays.asList(eval1, eval2));

        double grade = committeeService.calculateFinalGrade(position);

        double expected1 = 4 * 0.3 + 3 * 0.4 + 5 * 0.3;
        double expected2 = 2 * 0.3 + 4 * 0.4 + 4 * 0.3;
        double expectedAvg = (expected1 + expected2) / 2;

        assertEquals(expectedAvg, grade, 0.001);
    }

    @Test
    void testCompleteAssignedTraineeships_NoEvaluations() {
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
        Model model = new ConcurrentModel();
        int positionId = 3;

        TraineeshipPosition position = new TraineeshipPosition();
        position.setId(positionId);
        position.setEvaluations(Collections.emptyList());

        when(positionMapper.findById(positionId)).thenReturn(Optional.of(position));

        String view = committeeService.completeAssignedTraineeships(positionId, model, redirectAttributes);

        assertEquals("redirect:/committee/assignees", view);
        verify(redirectAttributes).addFlashAttribute(eq("errorMessage"), contains("No evaluations exist"));
    }

    @Test
    void testCompleteAssignedTraineeships_Success() {
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
        Model model = new ConcurrentModel();
        int positionId = 4;

        Evaluation eval = new Evaluation();
        eval.setStudentMotivation(3);
        eval.setStudentEfficiency(3);
        eval.setStudentEffectiveness(3);

        Student student = new Student();
        student.setId(40);

        TraineeshipPosition position = new TraineeshipPosition();
        position.setId(positionId);
        position.setStudent(student);
        position.setEvaluations(List.of(eval));

        when(positionMapper.findById(positionId)).thenReturn(Optional.of(position));

        String view = committeeService.completeAssignedTraineeships(positionId, model, redirectAttributes);

        assertEquals("redirect:/committee/assignees", view);
        verify(positionMapper).save(any());
        verify(studentMapper).save(any());
        verify(redirectAttributes).addFlashAttribute(eq("successMessage"), contains("Traineeship completed successfully"));
    }
}
