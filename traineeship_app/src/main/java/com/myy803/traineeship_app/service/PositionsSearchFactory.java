package com.myy803.traineeship_app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class PositionsSearchFactory {

    private final Map<String, PositionsSearchStrategy> strategies = new HashMap<>();

    @Autowired
    public PositionsSearchFactory(SearchBasedOnInterests searchBasedOnInterests,
                                  SearchBasedOnLocation searchBasedOnLocation,
                                  CombinedSearch combinedSearh) {

        strategies.put("interests", searchBasedOnInterests);
        strategies.put("location", searchBasedOnLocation);
        strategies.put("combined", combinedSearh);
    }

    public PositionsSearchStrategy create(String strategy) {
        return strategies.get(strategy.toLowerCase()); // Optional: make it case-insensitive
    }
}
