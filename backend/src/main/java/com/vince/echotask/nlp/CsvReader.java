package com.vince.echotask.nlp;

import jakarta.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CsvReader {

  @Autowired Vectorizer vectorizer;

  public static LinkedHashSet<String> vocabularyLinkedHashSet = new LinkedHashSet<>();
  public static LinkedHashMap<String, Integer> vocabularyLinkedHashMap = new LinkedHashMap<>();

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

        Arrays.asList(splitTokens).forEach(word -> vocabularyLinkedHashMap.put(word, 0));

        log.info("vocabularyLinkedHashSet list: {}", vocabularyLinkedHashSet);
      }
      log.info("vocabularyLinkedHashMap: {}", vocabularyLinkedHashMap);

      String[] tokens = {"add", "new"};
      vectorizer.bagOfWords(tokens);
    } catch (IOException e) {
      throw new IOException(e);
    }
  }
}
