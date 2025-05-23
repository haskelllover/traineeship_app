package com.myy803.traineeship_app.domainmodel;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

class ProfessorTest {
    private Professor professor;
    private TraineeshipPosition position;

    @BeforeEach
    void setUp() {
        professor = new Professor();
        position = new TraineeshipPosition();
    }

    @Test
    void testGettersAndSetters() {
        professor.setId(1);
        professor.setUsername("prof1");
        professor.setProfessorName("John Doe");
        professor.setInterests("AI, Machine Learning");

        assertEquals(1, professor.getId());
        assertEquals("prof1", professor.getUsername());
        assertEquals("John Doe", professor.getProfessorName());
        assertEquals("AI, Machine Learning", professor.getInterests());
    }

    @Test
    void testInterestsList() {
        professor.setInterests("AI, Machine Learning, Data Science");
        List<String> interests = professor.getInterestsList();
        
        assertEquals(3, interests.size());
        assertTrue(interests.contains("AI"));
        assertTrue(interests.contains("Machine Learning"));
        assertTrue(interests.contains("Data Science"));
    }

    @Test
    void testSetInterestsList() {
        List<String> interests = Arrays.asList("AI", "Machine Learning");
        professor.setInterestsList(interests);
        
        assertEquals("AI, Machine Learning", professor.getInterests());
    }

    @Test
    void testSupervisedPositions() {
        professor.setSupervisedPositions(Arrays.asList(position));
        assertEquals(1, professor.getSupervisedPositions().size());
        assertEquals(position, professor.getSupervisedPositions().get(0));
    }
}