package com.vince.echotask.nlp;

import jakarta.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import lombok.extern.slf4j.Slf4j;
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
}
