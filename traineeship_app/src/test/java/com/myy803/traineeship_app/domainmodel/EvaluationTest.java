package com.myy803.traineeship_app.domainmodel;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EvaluationTest {
    private Evaluation evaluation;
    private TraineeshipPosition position;

    @BeforeEach
    void setUp() {
        evaluation = new Evaluation();
        position = new TraineeshipPosition();
    }

    @Test
    void testGettersAndSetters() {
        evaluation.setId(1);
        evaluation.setEvaluationType(EvaluationType.COMPANY_EVALUATION);
        evaluation.setStudentMotivation(5);
        evaluation.setStudentEfficiency(4);
        evaluation.setStudentEffectiveness(3);
        evaluation.setCompanyFacilities(2);
        evaluation.setCompanyGuidance(1);
        evaluation.setComments("Good performance");
        evaluation.setTraineeshipPosition(position);

        assertEquals(1, evaluation.getId());
        assertEquals(EvaluationType.COMPANY_EVALUATION, evaluation.getEvaluationType());
        assertEquals(5, evaluation.getStudentMotivation());
        assertEquals(4, evaluation.getStudentEfficiency());
        assertEquals(3, evaluation.getStudentEffectiveness());
        assertEquals(2, evaluation.getCompanyFacilities());
        assertEquals(1, evaluation.getCompanyGuidance());
        assertEquals("Good performance", evaluation.getComments());
        assertEquals(position, evaluation.getTraineeshipPosition());
    }

    @Test
    void testRelationshipWithPosition() {
        evaluation.setTraineeshipPosition(position);
        assertEquals(position, evaluation.getTraineeshipPosition());
    }
}