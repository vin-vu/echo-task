package com.vince.echotask.nlp;

import lombok.extern.slf4j.Slf4j;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

@Slf4j
@Component
public class TokenizerService {

    private final TokenizerME tokenizeME;

    public TokenizerService() throws IOException {
        try (InputStream tokenizerStream =
                     new ClassPathResource("nlp/opennlp-en-ud-ewt-tokens-1.2-2.5.0.bin").getInputStream()) {

            TokenizerModel tokenizerModel = new TokenizerModel(tokenizerStream);
            tokenizeME = new TokenizerME(tokenizerModel);
        }
    }

    private String[] tokenize(String text) {
        return tokenizeME.tokenize(text);
    }

    private String[] normalize(String[] tokens) {
        return Arrays.stream(tokens)
                .map(String::toLowerCase)
                .toArray(String[]::new);
    }

    public String[] tokenizeAndNormalize(String text) {
        return normalize(tokenize(text));
    }
}
