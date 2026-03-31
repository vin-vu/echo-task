package com.vince.echotask.nlp.intent;

import com.vince.echotask.models.Intent;
import com.vince.echotask.models.IntentResolution;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.stream.Collectors;

@Slf4j
@Component
public class IntentService {

    private final Tokenizer tokenizer;
    private final IntentModelService intentModelService;

    public IntentService(Tokenizer tokenizer, IntentModelService intentModelService) {
        this.tokenizer = tokenizer;
        this.intentModelService = intentModelService;
    }

    private String[] tokenizeTranscript(String transcript) {
        return tokenizer.getTranscriptTokens(transcript);
    }

    private SortedMap<Double, Set<String>> categorizeIntent(String[] phraseTokens) {
        return intentModelService.categorizeIntent(phraseTokens);
    }

    private Intent getBestIntent(SortedMap<Double, Set<String>> rankedIntentScores) {
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

    private Map<String, Set<String>> convertRankedIntentScores(SortedMap<Double, Set<String>> intentScores) {
        return intentScores.entrySet().stream().collect(Collectors.toMap(entry ->
                entry.getKey().toString(), Map.Entry::getValue));
    }

    public IntentResolution resolveIntent(String transcript) {
        String[] transcriptTokens = tokenizeTranscript(transcript);
        SortedMap<Double, Set<String>> intentScores = categorizeIntent(transcriptTokens);

        Intent bestIntent = getBestIntent(intentScores);
        Map<String, Set<String>> convertedIntentScores = convertRankedIntentScores(intentScores);

        return new IntentResolution(bestIntent, convertedIntentScores);
    }
}
