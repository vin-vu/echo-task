package com.vince.echotask.nlp.intentcategorizer;

import com.vince.echotask.models.Intent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.stream.Collectors;

@Slf4j
@Component
public class IntentCategorizer {

    private final DoccatModelService doccatModelService;

    public IntentCategorizer(DoccatModelService doccatModelService) {
        this.doccatModelService = doccatModelService;
    }

    public SortedMap<Double, Set<String>> categorizeIntent(String[] phraseTokens) {
        return doccatModelService.categorizeIntent(phraseTokens);
    }

    public Intent getBestIntent(SortedMap<Double, Set<String>> rankedIntentScores) {
        double topScore = rankedIntentScores.lastKey();
        Set<String> bestIntents = rankedIntentScores.get(topScore);

        if (bestIntents.size() > 1) {
            return Intent.UNKNOWN;
        }
        if (topScore < 0.6) {
            return Intent.UNKNOWN;
        }
        return Intent.valueOf(bestIntents.iterator().next());
    }

    public Map<String, Set<String>> convertRankedIntentScores(SortedMap<Double, Set<String>> intentScores) {
        return intentScores.entrySet().stream().collect(Collectors.toMap(entry ->
                entry.getKey().toString(), Map.Entry::getValue));
    }
}
