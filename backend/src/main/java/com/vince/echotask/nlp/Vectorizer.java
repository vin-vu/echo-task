package com.vince.echotask.nlp;

import static com.vince.echotask.nlp.CsvReader.vocabularyLinkedHashMap;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Vectorizer {

  public void bagOfWords(String[] tokens) {

    for (String token : tokens) {
      if (vocabularyLinkedHashMap.containsKey(token)) {
        vocabularyLinkedHashMap.put(token, vocabularyLinkedHashMap.get(token) + 1);
      }
    }
    log.info("vocab hashmap: {}", vocabularyLinkedHashMap);
    log.info("vocab hashmap values: {}", vocabularyLinkedHashMap.values());
  }
}
