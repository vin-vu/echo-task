package com.vince.echotask.nlp;

import jakarta.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CsvReader {

  private static Set<String> vocabularySet = new HashSet<>();
  public static ArrayList<String> vocabularyList = new ArrayList<>();

  @PostConstruct
  public void readingTrainingData() throws IOException {
    ClassPathResource resource = new ClassPathResource("data/training_data.csv");

    try {
      BufferedReader bufferedReader =
          new BufferedReader(new InputStreamReader(resource.getInputStream()));

      String line;
      while ((line = bufferedReader.readLine()) != null) {
        if (line.isEmpty()) continue;

        String[] tokens = line.split(",");
        String[] splitPhrase = tokens[1].split(" ");
        for (String word : splitPhrase) {
          if (!vocabularySet.contains(word)) {
            vocabularySet.add(word);
            vocabularyList.add(word);
          }
        }
      }
      log.info("vocab list: {}", vocabularyList);

    } catch (IOException e) {
      throw new IOException(e);
    }
  }
}
