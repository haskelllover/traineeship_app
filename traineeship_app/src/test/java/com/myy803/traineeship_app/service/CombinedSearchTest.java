package com.myy803.traineeship_app.service;

import com.myy803.traineeship_app.domainmodel.TraineeshipPosition;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CombinedSearchTest {

    @Mock
    private SearchBasedOnSkills skillsSearch;

    @Mock
    private SearchBasedOnInterests interestsSearch;

    @InjectMocks
    private CombinedSearch combinedSearch;

    @Test
    void search_ShouldCombineAndRemoveDuplicates() {
        // Setup test data
        TraineeshipPosition position1 = new TraineeshipPosition();
        position1.setId(1);
        TraineeshipPosition position2 = new TraineeshipPosition();
        position2.setId(2);
        TraineeshipPosition position3 = new TraineeshipPosition();
        position3.setId(3);

        // Mock dependencies
        when(skillsSearch.search("testUser"))
            .thenReturn(Arrays.asList(position1, position2));
        when(interestsSearch.search("testUser"))
            .thenReturn(Arrays.asList(position2, position3));

        // Execute
        List<TraineeshipPosition> result = combinedSearch.search("testUser");

        // Verify
        assertEquals(3, result.size());
        assertTrue(result.contains(position1));
        assertTrue(result.contains(position2));
        assertTrue(result.contains(position3));
        
        // Verify no duplicates (position2 appears in both lists but should only appear once)
        assertEquals(1, result.stream().filter(p -> p.getId() == 2).count());

        // Verify dependencies were called
        verify(skillsSearch).search("testUser");
        verify(interestsSearch).search("testUser");
    }

    @Test
    void search_WhenOneListEmpty_ShouldReturnOtherList() {
        // Setup test data
        TraineeshipPosition position1 = new TraineeshipPosition();
        position1.setId(1);

        // Mock dependencies
        when(skillsSearch.search("testUser")).thenReturn(Arrays.asList(position1));
        when(interestsSearch.search("testUser")).thenReturn(List.of());

        // Execute
        List<TraineeshipPosition> result = combinedSearch.search("testUser");

        // Verify
        assertEquals(1, result.size());
        assertEquals(position1, result.get(0));
    }

    @Test
    void search_WhenBothListsEmpty_ShouldReturnEmptyList() {
        // Mock dependencies
        when(skillsSearch.search("testUser")).thenReturn(List.of());
        when(interestsSearch.search("testUser")).thenReturn(List.of());

        // Execute
        List<TraineeshipPosition> result = combinedSearch.search("testUser");

        // Verify
        assertTrue(result.isEmpty());
    }

    @Test
    void search_WhenDuplicatePositions_ShouldReturnDistinctPositions() {
        // Setup test data - same position objects
        TraineeshipPosition position1 = new TraineeshipPosition();
        position1.setId(1);
        TraineeshipPosition position2 = new TraineeshipPosition();
        position2.setId(2);

        // Mock dependencies returning same positions
        when(skillsSearch.search("testUser"))
            .thenReturn(Arrays.asList(position1, position2));
        when(interestsSearch.search("testUser"))
            .thenReturn(Arrays.asList(position1, position2));

        // Execute
        List<TraineeshipPosition> result = combinedSearch.search("testUser");

        // Verify
        assertEquals(2, result.size());
        assertTrue(result.contains(position1));
        assertTrue(result.contains(position2));
    }
}