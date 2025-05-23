package com.myy803.traineeship_app.domainmodel;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

class CompanyTest {
    private Company company;
    private TraineeshipPosition position1;
    private TraineeshipPosition position2;

    @BeforeEach
    void setUp() {
        company = new Company();
        position1 = new TraineeshipPosition();
        position2 = new TraineeshipPosition();
    }

    @Test
    void testGettersAndSetters() {
        company.setId(1);
        company.setUsername("company1");
        company.setCompanyName("Test Company");
        company.setCompanyLocation("Athens");

        assertEquals(1, company.getId());
        assertEquals("company1", company.getUsername());
        assertEquals("Test Company", company.getCompanyName());
        assertEquals("Athens", company.getCompanyLocation());
    }

    @Test
    void testPositionsRelationship() {
        company.addPosition(position1);
        company.addPosition(position2);

        List<TraineeshipPosition> positions = company.getPositions();
        assertEquals(2, positions.size());
        assertTrue(positions.contains(position1));
        assertTrue(positions.contains(position2));
        assertEquals(company, position1.getCompany());
        assertEquals(company, position2.getCompany());

        company.removePosition(position1);
        assertEquals(1, company.getPositions().size());
        assertNull(position1.getCompany());
    }

    @Test
    void testSetPositions() {
        List<TraineeshipPosition> positions = new ArrayList<>();
        positions.add(position1);
        positions.add(position2);

        company.setPositions(positions);
        assertEquals(2, company.getPositions().size());
    }
}