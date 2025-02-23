package com.vince.echotask.nlp;

import jakarta.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedHashSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CsvReader {

  public static LinkedHashSet<String> vocabularyLinkedHashSet = new LinkedHashSet<>();

  @PostConstruct
  public void readingTrainingData() throws IOException {
    ClassPathResource resource = new ClassPathResource("data/training_data.csv");

    try {
      BufferedReader bufferedReader =
          new BufferedReader(new InputStreamReader(resource.getInputStream()));
      bufferedReader.readLine(); // skip header

      String line;
      while ((line = bufferedReader.readLine()) != null) {
        if (line.isEmpty()) continue;

        String[] tokens = line.split(",");
        String[] splitTokens = tokens[1].split(" ");
        vocabularyLinkedHashSet.addAll(Arrays.asList(splitTokens));
        log.info("vocabularyLinkedHashSet list: {}", vocabularyLinkedHashSet);
      }
    } catch (IOException e) {
      throw new IOException(e);
    }
  }
}
