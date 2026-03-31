package com.vince.echotask.models;

import java.util.Map;
import java.util.Set;

public record IntentResolution(Intent intent,
                               Map<String, Set<String>> rankedScores) {
}
