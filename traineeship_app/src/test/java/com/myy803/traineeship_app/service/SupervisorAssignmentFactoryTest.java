package com.myy803.traineeship_app.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SupervisorAssignmentFactoryTest {

    private AssignmentBasedOnLoad loadStrategy;
    private AssignmentBasedOnInterests interestsStrategy;
    private SupervisorAssignmentFactory factory;

    @BeforeEach
    void setUp() {
        // Mock both strategies
        loadStrategy = mock(AssignmentBasedOnLoad.class);
        interestsStrategy = mock(AssignmentBasedOnInterests.class);

        // Create factory with mocks
        factory = new SupervisorAssignmentFactory(loadStrategy, interestsStrategy);
    }

    @Test
    void testCreateReturnsLoadStrategy() {
        SupervisorAssignmentStrategy strategy = factory.create("LOAD");
        assertSame(loadStrategy, strategy);
    }

    @Test
    void testCreateReturnsInterestsStrategy() {
        SupervisorAssignmentStrategy strategy = factory.create("INTERESTS");
        assertSame(interestsStrategy, strategy);
    }

    @Test
    void testCreateIsCaseInsensitive() {
        assertSame(loadStrategy, factory.create("load"));
        assertSame(interestsStrategy, factory.create("interests"));
    }

    @Test
    void testCreateThrowsOnUnknownStrategy() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> factory.create("UNKNOWN")
        );
        assertTrue(exception.getMessage().contains("Unknown strategy"));
        assertTrue(exception.getMessage().contains("LOAD"));
        assertTrue(exception.getMessage().contains("INTERESTS"));
    }
}
