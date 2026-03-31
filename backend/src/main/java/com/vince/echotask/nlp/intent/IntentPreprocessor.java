package com.vince.echotask.nlp.intent;

import com.vince.echotask.nlp.TokenizerService;
import lombok.extern.slf4j.Slf4j;
import opennlp.tools.lemmatizer.LemmatizerME;
import opennlp.tools.lemmatizer.LemmatizerModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Component
public class IntentPreprocessor {

    private final TokenizerService tokenizerService;
    private final POSTaggerME posTaggerME;
    private final LemmatizerME lemmatizerME;
    private final Set<String> stopWordsSet = new HashSet<>();

    public IntentPreprocessor(TokenizerService tokenizerService) {
        this.tokenizerService = tokenizerService;

        try (InputStream posStream =
                     new ClassPathResource("nlp/opennlp-en-ud-ewt-pos-1.2-2.5.0.bin").getInputStream();
             InputStream lemmatizerStream =
                     new ClassPathResource("nlp/opennlp-en-ud-ewt-lemmas-1.2-2.5.0.bin").getInputStream();
             BufferedReader bufferedReader =
                     new BufferedReader(new InputStreamReader(
                             new ClassPathResource("data/stopwords.txt").getInputStream()))
        ) {
            POSModel posModel = new POSModel(posStream);
            LemmatizerModel lemmatizerModel = new LemmatizerModel(lemmatizerStream);

            this.posTaggerME = new POSTaggerME(posModel);
            this.lemmatizerME = new LemmatizerME(lemmatizerModel);

            String stopWord;
            while ((stopWord = bufferedReader.readLine()) != null) {
                stopWordsSet.add(stopWord);
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize intent preprocessor", e);
        }
    }

    public String[] preprocessForIntent(String transcript) {
        String[] normalizedTokens = tokenizerService.tokenizeAndNormalize(transcript);
        String[] tokensWithoutStopWords = removeStopWords(normalizedTokens);
        String[] posTags = generatePartOfSpeechTags(tokensWithoutStopWords);
        return lemmatizeTokens(tokensWithoutStopWords, posTags);
    }

    private String[] removeStopWords(String[] tokens) {
        return Arrays.stream(tokens)
                .filter(token -> !stopWordsSet.contains(token))
                .toArray(String[]::new);
    }

    private String[] generatePartOfSpeechTags(String[] tokens) {
        return posTaggerME.tag(tokens);
    }

    private String[] lemmatizeTokens(String[] tokens, String[] tags) {
        return lemmatizerME.lemmatize(tokens, tags);
    }
}
