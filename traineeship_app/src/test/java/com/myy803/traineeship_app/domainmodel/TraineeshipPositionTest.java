package com.myy803.traineeship_app.domainmodel;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

class TraineeshipPositionTest {
    private TraineeshipPosition position;
    private Company company;
    private Professor professor;
    private Student student;
    private Evaluation evaluation;

    @BeforeEach
    void setUp() {
        position = new TraineeshipPosition();
        company = new Company();
        professor = new Professor();
        student = new Student();
        evaluation = new Evaluation();
    }

    @Test
    void testGettersAndSetters() {
        position.setId(1);
        position.setTitle("Software Developer Intern");
        position.setDescription("Develop web applications");
        position.setFromDate(LocalDate.of(2023, 6, 1));
        position.setToDate(LocalDate.of(2023, 8, 31));
        position.setTopics("Web Development, REST APIs");
        position.setSkills("Java, Spring Boot");
        position.setIsAssigned(true);
        position.setStudentLogbook("logbook.pdf");
        position.setPassFailGrade(true);
        position.setLogbookEntries("Week 1: Started project");
        position.setHoursCompleted(40);
        position.setWeeklyReport("Completed tasks");
        position.setAchievements("Implemented feature");
        position.setSkillsGained("Spring Boot");

        assertEquals(1, position.getId());
        assertEquals("Software Developer Intern", position.getTitle());
        assertEquals("Develop web applications", position.getDescription());
        assertEquals(LocalDate.of(2023, 6, 1), position.getFromDate());
        assertEquals(LocalDate.of(2023, 8, 31), position.getToDate());
        assertEquals("Web Development, REST APIs", position.getTopics());
        assertEquals("Java, Spring Boot", position.getSkills());
        assertTrue(position.getIsAssigned());
        assertEquals("logbook.pdf", position.getStudentLogbook());
        assertTrue(position.getPassFailGrade());
        assertEquals("Week 1: Started project", position.getLogbookEntries());
        assertEquals(40, position.getHoursCompleted());
        assertEquals("Completed tasks", position.getWeeklyReport());
        assertEquals("Implemented feature", position.getAchievements());
        assertEquals("Spring Boot", position.getSkillsGained());
    }

    @Test
    void testRelationships() {
        position.setCompany(company);
        position.setSupervisor(professor);
        position.setStudent(student);
        
        List<Evaluation> evaluations = new ArrayList<>();
        evaluations.add(evaluation);
        position.setEvaluations(evaluations);

        assertEquals(company, position.getCompany());
        assertEquals(professor, position.getSupervisor());
        assertEquals(student, position.getStudent());
        assertEquals(1, position.getEvaluations().size());
        assertEquals(evaluation, position.getEvaluations().get(0));
    }

    @Test
    void testCompanyNameAndLocation() {
        company.setCompanyName("Test Company");
        company.setCompanyLocation("Athens");
        position.setCompany(company);

        assertEquals("Test Company", position.getCompanyName());
        assertEquals("Athens", position.getCompanyLocation());
    }
}