package com.myy803.traineeship_app.service;

import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class SupervisorAssignmentFactory {
    private final Map<String, SupervisorAssignmentStrategy> strategies;

    public SupervisorAssignmentFactory(
        AssignmentBasedOnLoad loadBasedStrategy,
        AssignmentBasedOnInterests interestsBasedStrategy
    ) {
        this.strategies = Map.of(
            "LOAD", loadBasedStrategy,
            "INTERESTS", interestsBasedStrategy
        );
    }

    public SupervisorAssignmentStrategy create(String strategy) {
        String key = strategy.toUpperCase();
        if (!strategies.containsKey(key)) {
            throw new IllegalArgumentException(
                "Unknown strategy: " + strategy + 
                ". Available: " + String.join(", ", strategies.keySet())
            );
        }
        return strategies.get(key);
    }
}