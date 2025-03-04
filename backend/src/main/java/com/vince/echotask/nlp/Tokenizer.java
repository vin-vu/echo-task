package com.vince.echotask.nlp;

import lombok.extern.slf4j.Slf4j;
import opennlp.tools.lemmatizer.LemmatizerME;
import opennlp.tools.lemmatizer.LemmatizerModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
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
public class Tokenizer {

    private static final TokenizerModel tokenizerModel;
    private static final POSModel posModel;
    private static final LemmatizerModel lemmatizerModel;

    private static final TokenizerME tokenizeME;
    private static final POSTaggerME posTaggerME;
    private static final LemmatizerME lemmatizerME;

    public static Set<String> stopWordsSet = new HashSet<>();

    static {
        try (InputStream tokenizerStream = new ClassPathResource("nlp/opennlp-en-ud-ewt-tokens-1.2-2.5.0.bin").getInputStream();
             InputStream posStream = new ClassPathResource("nlp/opennlp-en-ud-ewt-pos-1.2-2.5.0.bin").getInputStream();
             InputStream lemmatizerStream = new ClassPathResource("nlp/opennlp-en-ud-ewt-lemmas-1.2-2.5.0.bin").getInputStream();
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new ClassPathResource("data/stopwords.txt").getInputStream()))
        ) {

            // load models
            tokenizerModel = new TokenizerModel(tokenizerStream);
            posModel = new POSModel(posStream);
            lemmatizerModel = new LemmatizerModel(lemmatizerStream);

            // instantiate ME models
            tokenizeME = new TokenizerME(tokenizerModel);
            posTaggerME = new POSTaggerME(posModel);
            lemmatizerME = new LemmatizerME(lemmatizerModel);

            // load stopwords
            String stopWord;
            while ((stopWord = bufferedReader.readLine()) != null) {
                stopWordsSet.add(stopWord);
            }
            log.info("stopWord set: {}", stopWordsSet);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String[] getTranscriptTokens(String transcript) {
        String[] transcriptTokens = tokenizeText(transcript);
        String[] normalizedTokens = normalizeTokens(transcriptTokens);
        String[] tokensWithoutStopWords = removeStopWords(normalizedTokens);
        String[] partsOfSpeechTags = generatePartOfSpeechTags(tokensWithoutStopWords);
        return lemmatizeTokens(tokensWithoutStopWords, partsOfSpeechTags);
    }

    private String[] tokenizeText(String phrase) {
        return tokenizeME.tokenize(phrase);
    }

    private String[] normalizeTokens(String[] tokens) {
        return Arrays.stream(tokens).map(String::toLowerCase).toArray(String[]::new);
    }

    private String[] removeStopWords(String[] tokens) {
        tokens = Arrays.stream(tokens)
                .filter(token -> !stopWordsSet.contains(token))
                .toArray(String[]::new);
        return tokens;
    }

    private String[] generatePartOfSpeechTags(String[] tokens) {
        return posTaggerME.tag(tokens);
    }

    private String[] lemmatizeTokens(String[] tokens, String[] tags) {
        return lemmatizerME.lemmatize(tokens, tags);
    }
}
