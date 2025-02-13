package com.vince.echotask.nlp;

import jakarta.annotation.PostConstruct;
import java.io.*;
import lombok.extern.slf4j.Slf4j;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Tokenizer {

  @PostConstruct
  public void readingTrainingData() throws IOException {
    ClassPathResource resource = new ClassPathResource("data/training_data.txt");
    try {
      BufferedReader bufferedReader =
          new BufferedReader(new InputStreamReader(resource.getInputStream()));

      String line;
      while ((line = bufferedReader.readLine()) != null) {
        log.info("line: {}", line);
      }

    } catch (IOException e) {
      throw new IOException(e);
    }
  }

  @PostConstruct
  public void tokenizeSpeech() throws IOException {
    ClassPathResource modelResource =
        new ClassPathResource("nlp/opennlp-en-ud-ewt-tokens-1.2-2.5.0.bin");
    InputStream modelInput = modelResource.getInputStream();

    TokenizerModel tokenizerModel = new TokenizerModel(modelInput);
    TokenizerME tokenizer = new TokenizerME(tokenizerModel);

    String text = "Sample text to be tokenized.";
    String[] tokens = tokenizer.tokenize(text);

    for (String token : tokens) {
      log.info("token: {}", token);
    }
  }
}
