package com.vince.echotask.service;

import com.vince.echotask.nlp.IntentCategorizer;
import com.vince.echotask.nlp.Tokenizer;
import com.vince.echotask.pojo.IntentRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Set;
import java.util.SortedMap;

@Slf4j
@Service
public class EchoTaskService {

    @Autowired
    Tokenizer tokenizer;

    @Autowired
    IntentCategorizer intentCategorizer;

    public String processIntent(IntentRequest request) throws IOException {

        log.info("process intent: {}", request.toString());

        String[] phraseTokens = tokenizer.tokenizeText(request.getTranscript());
        String[] normalizedTokens = tokenizer.normalizeTokens(phraseTokens);
        String[] tokensWithoutStopWords = tokenizer.removeStopWords(normalizedTokens);
        String[] partsOfSpeechTags = tokenizer.generatePartOfSpeechTags(tokensWithoutStopWords);
        String[] lemmatizedTokens = tokenizer.lemmatizeTokens(tokensWithoutStopWords, partsOfSpeechTags);

        SortedMap<Double, Set<String>> sortedScoreMap = intentCategorizer.categorizeIntent(lemmatizedTokens);
        log.info("sortedScoreMap: {}", sortedScoreMap);

        Double highestScore = sortedScoreMap.lastKey();
        Set<String> bestIntents = sortedScoreMap.get(highestScore); // not handling if more than 1 intent - would share same probability and therefore in same Set

        return bestIntents.iterator().next();
    }
}
