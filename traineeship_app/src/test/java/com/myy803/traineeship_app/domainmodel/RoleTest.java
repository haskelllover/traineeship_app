package com.myy803.traineeship_app.domainmodel;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class RoleTest {

    @Test
    void testEnumValues() {
        assertEquals(6, Role.values().length);
        assertEquals(Role.STUDENT, Role.valueOf("STUDENT"));
        assertEquals(Role.COMPANY, Role.valueOf("COMPANY"));
        assertEquals(Role.PROFESSOR, Role.valueOf("PROFESSOR"));
        assertEquals(Role.COMMITTEE_MEMBER, Role.valueOf("COMMITTEE_MEMBER"));
        assertEquals(Role.ADMIN, Role.valueOf("ADMIN"));
        assertEquals(Role.USER, Role.valueOf("USER"));
    }
}