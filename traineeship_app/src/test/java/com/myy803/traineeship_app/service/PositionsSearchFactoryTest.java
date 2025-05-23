package com.myy803.traineeship_app.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PositionsSearchFactoryTest {

    private PositionsSearchFactory factory;

    private SearchBasedOnInterests mockInterestStrategy;
    private SearchBasedOnLocation mockLocationStrategy;
    private CombinedSearch mockCombinedStrategy;

    @BeforeEach
    void setUp() {
        mockInterestStrategy = mock(SearchBasedOnInterests.class);
        mockLocationStrategy = mock(SearchBasedOnLocation.class);
        mockCombinedStrategy = mock(CombinedSearch.class);

        factory = new PositionsSearchFactory(
            mockInterestStrategy,
            mockLocationStrategy,
            mockCombinedStrategy
        );
    }

    @Test
    void testCreate_ReturnsCorrectStrategy() {
        assertSame(mockInterestStrategy, factory.create("interests"));
        assertSame(mockLocationStrategy, factory.create("location"));
        assertSame(mockCombinedStrategy, factory.create("combined"));
    }

    @Test
    void testCreate_IsCaseInsensitive() {
        assertSame(mockInterestStrategy, factory.create("InTeReStS"));
        assertSame(mockLocationStrategy, factory.create("LOCATION"));
        assertSame(mockCombinedStrategy, factory.create("CoMbInEd"));
    }

    @Test
    void testCreate_ReturnsNullForUnknownStrategy() {
        assertNull(factory.create("unknown"));
    }
}
