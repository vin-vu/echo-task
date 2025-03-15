package com.vince.echotask.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ParsedIntent {
    private UUID id;
    private Intent intent;
    private String description;
    private boolean completed;
    private Map<String, Set<String>> rankedIntentScores;
}
