package com.myy803.traineeship_app.service;

import com.myy803.traineeship_app.domainmodel.TraineeshipPosition;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class CombinedSearch implements PositionsSearchStrategy {
    private final SearchBasedOnSkills skillsSearch;
    private final SearchBasedOnInterests interestsSearch;

    public CombinedSearch(SearchBasedOnSkills skillsSearch,
                        SearchBasedOnInterests interestsSearch) {
        this.skillsSearch = skillsSearch;
        this.interestsSearch = interestsSearch;
    }

    @Override
    public List<TraineeshipPosition> search(String applicantUsername) {
        List<TraineeshipPosition> bySkills = skillsSearch.search(applicantUsername);
        List<TraineeshipPosition> byInterests = interestsSearch.search(applicantUsername);
        
        // Combine and remove duplicates
        return Stream.concat(bySkills.stream(), byInterests.stream())
            .distinct()
            .collect(Collectors.toList());
    }
}