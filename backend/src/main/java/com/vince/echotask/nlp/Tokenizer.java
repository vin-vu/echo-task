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

    public static Set<String> stopWordsSet = new HashSet<>();

    private static final TokenizerModel tokenizerModel;
    private static final POSModel posModel;
    private static final LemmatizerModel lemmatizerModel;

    static {
        try (InputStream tokenizerStream = new ClassPathResource("nlp/opennlp-en-ud-ewt-tokens-1.2-2.5.0.bin").getInputStream();
             InputStream posStream = new ClassPathResource("nlp/opennlp-en-ud-ewt-pos-1.2-2.5.0.bin").getInputStream();
             InputStream lemmatizerStream = new ClassPathResource("nlp/opennlp-en-ud-ewt-lemmas-1.2-2.5.0.bin").getInputStream();) {

            // load models
            tokenizerModel = new TokenizerModel(tokenizerStream);
            posModel = new POSModel(posStream);
            lemmatizerModel = new LemmatizerModel(lemmatizerStream);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void loadStopWords() throws IOException {
        ClassPathResource stopWordsResource = new ClassPathResource("data/stopwords.txt");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stopWordsResource.getInputStream()));

        String stopWord;
        while ((stopWord = bufferedReader.readLine()) != null) {
            stopWordsSet.add(stopWord);
        }
        log.info("stopWord set: {}", stopWordsSet);
    }

    public String[] tokenizeText(String phrase) throws IOException {
        ClassPathResource modelResource = new ClassPathResource("nlp/opennlp-en-ud-ewt-tokens-1.2-2.5.0.bin");
        InputStream modelInput = modelResource.getInputStream();

        TokenizerModel tokenizerModel = new TokenizerModel(modelInput);
        TokenizerME tokenizer = new TokenizerME(tokenizerModel);

        String[] tokens = tokenizer.tokenize(phrase);

        return normalizeTokens(tokens);
    }

    public String[] normalizeTokens(String[] tokens) throws IOException {

        tokens = Arrays.stream(tokens).map(String::toLowerCase).toArray(String[]::new);
        log.info("tokens: {}", (Object) tokens);

        return removeStopWords(tokens);
    }

    public String[] removeStopWords(String[] tokens) throws IOException {
        tokens = Arrays.stream(tokens)
                .filter(token -> !stopWordsSet.contains(token))
                .toArray(String[]::new);

        log.info("no stop word tokens: {}", (Object) tokens);
        return generatePartOfSpeechTags(tokens);
    }

    public String[] generatePartOfSpeechTags(String[] tokens) throws IOException {
        ClassPathResource modelResource = new ClassPathResource("nlp/opennlp-en-ud-ewt-pos-1.2-2.5.0.bin");
        InputStream modelInput = modelResource.getInputStream();

        POSModel posModel = new POSModel(modelInput);
        POSTaggerME posTagger = new POSTaggerME(posModel);

        String[] tags = posTagger.tag(tokens);
        log.info("tags: {}", (Object) tags);

        return lemmatizeTokens(tokens, tags);
    }

    public String[] lemmatizeTokens(String[] tokens, String[] tags) throws IOException {
        ClassPathResource modelResource = new ClassPathResource("nlp/opennlp-en-ud-ewt-lemmas-1.2-2.5.0.bin");
        InputStream modelInput = modelResource.getInputStream();

        LemmatizerModel lemmatizerModel = new LemmatizerModel(modelInput);
        LemmatizerME lemmatizer = new LemmatizerME(lemmatizerModel);

        String[] lemmas = lemmatizer.lemmatize(tokens, tags);
        log.info("lemmas: {}", (Object) lemmas);
        return lemmas;
    }
}
