package com.vince.echotask.nlp;

import jakarta.annotation.PostConstruct;
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

    @PostConstruct
    public void loadStopWords() throws IOException {
        ClassPathResource stopWordsResource = new ClassPathResource("data/stopwords.txt");
        BufferedReader bufferedReader =
                new BufferedReader(new InputStreamReader(stopWordsResource.getInputStream()));

        String stopWord;
        while ((stopWord = bufferedReader.readLine()) != null) {
            stopWordsSet.add(stopWord);
        }

        log.info("stopWord set: {}", stopWordsSet);
        tokenizeText();
    }

    public void tokenizeText() throws IOException {
        ClassPathResource modelResource =
                new ClassPathResource("nlp/opennlp-en-ud-ewt-tokens-1.2-2.5.0.bin");
        InputStream modelInput = modelResource.getInputStream();

        TokenizerModel tokenizerModel = new TokenizerModel(modelInput);
        TokenizerME tokenizer = new TokenizerME(tokenizerModel);

        String text = "Sample text to be tokenized.";
        String[] tokens = tokenizer.tokenize(text);

        normalizeTokens(tokens);
    }

    public void normalizeTokens(String[] tokens) throws IOException {

        tokens = Arrays.stream(tokens).map(String::toLowerCase).toArray(String[]::new);
        log.info("tokens: {}", (Object) tokens);

        removeStopWords(tokens);
    }

    public void removeStopWords(String[] tokens) throws IOException {
        tokens =
                Arrays.stream(tokens)
                        .filter(token -> !stopWordsSet.contains(token))
                        .toArray(String[]::new);
        log.info("no stop word tokens: {}", (Object) tokens);
        generatePartOfSpeechTags(tokens);
    }

    public void generatePartOfSpeechTags(String[] tokens) throws IOException {
        ClassPathResource modelResource =
                new ClassPathResource("nlp/opennlp-en-ud-ewt-pos-1.2-2.5.0.bin");
        InputStream modelInput = modelResource.getInputStream();

        POSModel posModel = new POSModel(modelInput);
        POSTaggerME posTagger = new POSTaggerME(posModel);

        String[] tags = posTagger.tag(tokens);
        log.info("tags: {}", (Object) tags);

        lemmatizeTokens(tokens, tags);
    }

    public void lemmatizeTokens(String[] tokens, String[] tags) throws IOException {
        ClassPathResource modelResource =
                new ClassPathResource("nlp/opennlp-en-ud-ewt-lemmas-1.2-2.5.0.bin");
        InputStream modelInput = modelResource.getInputStream();

        LemmatizerModel lemmatizerModel = new LemmatizerModel(modelInput);
        LemmatizerME lemmatizer = new LemmatizerME(lemmatizerModel);

        String[] lemmas = lemmatizer.lemmatize(tokens, tags);
        log.info("lemmas: {}", (Object) lemmas);
    }
}
