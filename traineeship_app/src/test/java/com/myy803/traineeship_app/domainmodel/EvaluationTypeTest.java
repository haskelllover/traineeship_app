package com.myy803.traineeship_app.domainmodel;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class EvaluationTypeTest {

    @Test
    void testEnumValues() {
        assertEquals(3, EvaluationType.values().length);
        assertEquals(EvaluationType.COMPANY_EVALUATION, EvaluationType.valueOf("COMPANY_EVALUATION"));
        assertEquals(EvaluationType.PROFESSOR_EVALUATION, EvaluationType.valueOf("PROFESSOR_EVALUATION"));
        assertEquals(EvaluationType.STUDENT_EVALUATION, EvaluationType.valueOf("STUDENT_EVALUATION"));
    }
}