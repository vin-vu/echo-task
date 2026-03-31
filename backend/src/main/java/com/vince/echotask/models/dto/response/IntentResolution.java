package com.vince.echotask.models.dto.response;

import com.vince.echotask.models.domain.Intent;

import java.util.Map;
import java.util.Set;

public record IntentResolution(Intent intent,
                               Map<String, Set<String>> rankedScores) {
}
